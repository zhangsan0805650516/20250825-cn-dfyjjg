package com.ruoyi.biz.strategy.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.stockHoldDetail.service.IFaStockHoldDetailService;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;
import com.ruoyi.biz.stockTrading.service.IFaStockTradingService;
import com.ruoyi.biz.strategy.domain.FaStrategy;
import com.ruoyi.biz.strategy.mapper.FaStrategyMapper;
import com.ruoyi.biz.strategy.service.IFaStrategyService;
import com.ruoyi.biz.strategy.service.VietnamStrategyService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.VietnamHttpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 策略Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-06
 */
@Service
public class VietnameStrategyServiceImpl extends ServiceImpl<FaStrategyMapper, FaStrategy> implements VietnamStrategyService
{

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Autowired
    private IFaStockTradingService iFaStockTradingService;

    @Autowired
    private IFaMemberService iFaMemberService;

    @Autowired
    private IFaStockHoldDetailService iFaStockHoldDetailService;

    @Autowired
    private IFaStrategyService iFaStrategyService;

    @Autowired
    private FaStrategyMapper faStrategyMapper;

    private static final String INDEX_URL = "https://iboard-query.ssi.com.vn/exchange-index/multiple";
    private static final String HOSE_STOCK_URL = "https://iboard-query.ssi.com.vn/v2/stock/exchange/hose";
    private static final String HNX_STOCK_URL = "https://iboard-query.ssi.com.vn/v2/stock/exchange/hnx";
    private static final String UPCOM_STOCK_URL = "https://iboard-query.ssi.com.vn/v2/stock/exchange/upcom";

    /**
     * T+1冻结资金清零
     */
    @Override
    public void clearT1Amount() throws Exception {
        // 判断卖出资金T+N
        int tn = Integer.parseInt(iFaRiskConfigService.getConfigValue("kq_dj", "1"));
        // 查询卖出订单，解冻金额
        if (tn > 0) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, tn * -1);
            Date date = calendar.getTime();
            // 起始日期
            String startDate = sdf1.format(date);
            // 截至日期
            String endDate = sdf2.format(date);

            LambdaQueryWrapper<FaStockTrading> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaStockTrading::getTradingType, 2);
            lambdaQueryWrapper.eq(FaStockTrading::getDeleteFlag, 0);
            lambdaQueryWrapper.between(FaStockTrading::getCreateTime, startDate, endDate);
            List<FaStockTrading> faStockTradingList = iFaStockTradingService.list(lambdaQueryWrapper);
            if (!faStockTradingList.isEmpty()) {
                // 遍历交易记录
                for (FaStockTrading faStockTrading : faStockTradingList) {
                    BigDecimal amount = faStockTrading.getTradingAmount();
                    iFaMemberService.updateFaMemberFreezeProfit(faStockTrading.getMemberId(), amount, 1);
                }
            }
        }
    }

    /**
     * T+1冻结持仓清零
     */
    @Override
    public void clearT1Hold() throws Exception {
        // 持仓明细剩余冻结天数-1
        iFaStockHoldDetailService.updateFreezeDaysLeft();
        // 持仓明细冻结状态判断
        iFaStockHoldDetailService.updateFreezeStatus();
    }

    /**
     * 刷新越南市场股票
     * @throws Exception
     */
    @Override
    public void updateVietnamStock() throws Exception {
        String cookie = redisCache.getSSIToken();

        // 刷新指数
        updateVietnamIndex(cookie);

        // 刷新HOSE交易所股票
        updateVietnamStock(HOSE_STOCK_URL, 1, cookie);

        // 刷新HNX交易所股票
        updateVietnamStock(HNX_STOCK_URL, 2, cookie);

        // 刷新UPCOM交易所股票
        updateVietnamStock(UPCOM_STOCK_URL, 3, cookie);
    }

    /**
     * 刷新越南市场股票 VPS
     * @throws Exception
     */
    @Override
    public void updateVietnamStockVPS() throws Exception {
        // 刷新指数
        updateVietnamIndexVPS("https://bgapidatafeed.vps.com.vn/getlistindexdetail/02,03,10,11");

        // 刷新股票
        updateVietnamStockVPS("https://bgapidatafeed.vps.com.vn/getliststockdata/");
    }

    /**
     * 刷新4大指数 02:HNX 03:UPCOM 10:VNINDEX 11:HNX30
     * @param url
     * @throws Exception
     */
    private void updateVietnamIndexVPS(String url) throws Exception {
        String result = VietnamHttpUtils.sendGet(url);
        if (StringUtils.isNotEmpty(result)) {
            JSONArray jsonArray = JSONArray.parseArray(result);
            if (!jsonArray.isEmpty()) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    if (jsonArray.getJSONObject(i).containsKey("mc")) {
                        LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                        lambdaUpdateWrapper.eq(FaStrategy::getPrefixCode, jsonArray.getJSONObject(i).getString("mc"));
                        BigDecimal caiTrade = jsonArray.getJSONObject(i).getBigDecimal("cIndex");
                        lambdaUpdateWrapper.set(FaStrategy::getCaiTrade, caiTrade);
                        BigDecimal caiOpen = jsonArray.getJSONObject(i).getBigDecimal("oIndex");
                        lambdaUpdateWrapper.set(FaStrategy::getCaiOpen, caiOpen);
                        String ot = jsonArray.getJSONObject(i).getString("ot");
                        String[] ots = ot.split("\\|");
                        if (ots.length > 2) {
                            BigDecimal caiPricechange = new BigDecimal(ots[0]);
                            BigDecimal caiChangepercent = new BigDecimal(ots[1].substring(0, ots[1].length() - 1));
                            if (caiTrade.compareTo(caiOpen) >= 0) {
                                lambdaUpdateWrapper.set(FaStrategy::getCaiPricechange, caiPricechange);
                                lambdaUpdateWrapper.set(FaStrategy::getCaiChangepercent, caiChangepercent);
                            } else {
                                lambdaUpdateWrapper.set(FaStrategy::getCaiPricechange, caiPricechange.multiply(new BigDecimal(-1)));
                                lambdaUpdateWrapper.set(FaStrategy::getCaiChangepercent, caiChangepercent.multiply(new BigDecimal(-1)));
                            }
                        }
                        lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
                        this.update(lambdaUpdateWrapper);
                    }
                }
            }
        }
    }

    /**
     * 批量刷新股票信息
     * @param url
     * @throws Exception
     */
    private void updateVietnamStockVPS(String url) throws Exception {
        // 总数
        int count = (int) this.count();
        if (count > 0) {
            // 200一组
            int times = count / 200 + 1;
            for (int i = 0; i < times; i++) {
                // 股票代码集合
                String[] codeList = faStrategyMapper.getCodeList(i * 200, 200);
                if (codeList.length > 0) {
                    String codes = Arrays.toString(codeList);
                    codes = codes.substring(1);
                    codes = codes.substring(0, codes.length() - 1);
                    codes = codes.replace(", ", ",");

                    Thread thread = new Thread(updateVietnamStockVPS(url, codes));
                    thread.start();
                }
            }
        }
    }

    /**
     * 批量刷新股票信息
     * @param url
     * @throws Exception
     */
    private TimerTask updateVietnamStockVPS(String url, String codes) throws Exception {
        return new TimerTask() {
            @Override
            public void run() {
                String result = VietnamHttpUtils.sendGet(url + codes);
                if (StringUtils.isNotEmpty(result)) {
                    JSONArray jsonArray = JSONArray.parseArray(result);
                    if (!jsonArray.isEmpty()) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject.containsKey("sym")) {
                                SpringUtils.getBean(VietnameStrategyServiceImpl.class).updateVietnamStockVPS(jsonObject);
                            }
                        }
                    }
                }
            }
        };
    }

    private void updateVietnamStockVPS(JSONObject jsonObject) {
        LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaStrategy::getCode, jsonObject.getString("sym"));

        BigDecimal caiTrade = jsonObject.getBigDecimal("lastPrice");
        BigDecimal caiSettlement = jsonObject.getBigDecimal("r");
        BigDecimal caiPricechange = jsonObject.getBigDecimal("ot");
        BigDecimal caiChangepercent = jsonObject.getBigDecimal("changePc");

        if (caiTrade.compareTo(BigDecimal.ZERO) <= 0) {
            caiTrade = caiSettlement;
        }
        lambdaUpdateWrapper.set(FaStrategy::getCaiTrade, caiTrade);
        lambdaUpdateWrapper.set(FaStrategy::getCaiSettlement, caiSettlement);

        if (caiTrade.compareTo(caiSettlement) >= 0) {
            lambdaUpdateWrapper.set(FaStrategy::getCaiPricechange, caiPricechange);
            lambdaUpdateWrapper.set(FaStrategy::getCaiChangepercent, caiChangepercent);
        } else {
            lambdaUpdateWrapper.set(FaStrategy::getCaiPricechange, caiPricechange.multiply(new BigDecimal(-1)));
            lambdaUpdateWrapper.set(FaStrategy::getCaiChangepercent, caiChangepercent.multiply(new BigDecimal(-1)));
        }

        lambdaUpdateWrapper.set(FaStrategy::getCaiHigh, jsonObject.getBigDecimal("highPrice"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiLow, jsonObject.getBigDecimal("lowPrice"));
        lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 刷新越南市场指数
     * @param cookie
     * @throws Exception
     */
    private void updateVietnamIndex(String cookie) throws Exception {
        String result = VietnamHttpUtils.sendPostForSSI(VietnameStrategyServiceImpl.INDEX_URL, cookie, "{\"indexIds\":[\"VNINDEX\",\"VN30\",\"HNX30\",\"VNXALL\",\"HNXIndex\",\"HNXUpcomIndex\"]}");
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("code") && "SUCCESS".equalsIgnoreCase(jsonObject.getString("code"))) {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            if (!jsonArray.isEmpty()) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    // 第一次保存
//                    saveVietnamIndex(jsonArray.getJSONObject(i));
                    // 更新
                    updateVietnamIndex(jsonArray.getJSONObject(i));
                }
            }
        }
    }

    /**
     * 保存越南市场指数
     * @param jsonObject
     * @throws Exception
     */
    private void saveVietnamIndex(JSONObject jsonObject) throws Exception {
        // 第一次保存
        FaStrategy faStrategy = new FaStrategy();
        faStrategy.setTitle(jsonObject.getString("label"));
        faStrategy.setTitleEn(jsonObject.getString("label"));
        faStrategy.setCode(jsonObject.getString("label"));
        faStrategy.setAllCode(jsonObject.getString("label"));
        faStrategy.setType(0);
        faStrategy.setCreateTime(new Date());

        faStrategy.setCaiTrade(jsonObject.getBigDecimal("indexValue"));
        faStrategy.setCaiPricechange(jsonObject.getBigDecimal("change"));
        faStrategy.setCaiChangepercent(jsonObject.getBigDecimal("changePercent"));
        faStrategy.setCaiSettlement(jsonObject.getBigDecimal("prevIndexValue"));
        faStrategy.setCaiOpen(jsonObject.getBigDecimal("chartOpen"));
        faStrategy.setCaiHigh(jsonObject.getBigDecimal("chartHigh"));
        faStrategy.setCaiLow(jsonObject.getBigDecimal("chartLow"));
        faStrategy.setCaiVolume(jsonObject.getBigDecimal("totalQtty").divide(new BigDecimal(1000000), 2, RoundingMode.HALF_UP));
        faStrategy.setCaiAmount(jsonObject.getBigDecimal("totalValue").divide(new BigDecimal(1000000000), 2, RoundingMode.HALF_UP));
        faStrategy.setCaiTicktime(changeDate(jsonObject.getLong("timeMaker")));
        baseMapper.insert(faStrategy);
    }

    /**
     * 刷新越南市场指数
     * @param jsonObject
     * @throws Exception
     */
    private void updateVietnamIndex(JSONObject jsonObject) throws Exception {
        LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaStrategy::getCode, jsonObject.getString("label"));

        lambdaUpdateWrapper.set(FaStrategy::getCaiTrade, jsonObject.getBigDecimal("indexValue"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiPricechange, jsonObject.getBigDecimal("change"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiChangepercent, jsonObject.getBigDecimal("changePercent"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiSettlement, jsonObject.getBigDecimal("prevIndexValue"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiOpen, jsonObject.getBigDecimal("chartOpen"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiHigh, jsonObject.getBigDecimal("chartHigh"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiLow, jsonObject.getBigDecimal("chartLow"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiVolume, jsonObject.getBigDecimal("totalQtty").divide(new BigDecimal(1000000), 2, RoundingMode.HALF_UP));
        lambdaUpdateWrapper.set(FaStrategy::getCaiAmount, jsonObject.getBigDecimal("totalValue").divide(new BigDecimal(1000000000), 2, RoundingMode.HALF_UP));
        lambdaUpdateWrapper.set(FaStrategy::getCaiTicktime, changeDate(jsonObject.getLong("timeMaker")));
        lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    private Date changeDate(long timeMaker) throws Exception {
        Date date = new Date(timeMaker);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date);
        return sdf.parse(formattedDate);
    }

    /**
     * 刷新越南市场股票
     * @param stockUrl 地址
     * @param type 交易所类型
     * @throws Exception
     */
    private void updateVietnamStock(String stockUrl, int type, String cookie) throws Exception {
        String result = VietnamHttpUtils.sendGetForSSI(stockUrl, cookie);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("code") && "SUCCESS".equalsIgnoreCase(jsonObject.getString("code"))) {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            if (!jsonArray.isEmpty()) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    updateVietnamStock(jsonArray.getJSONObject(i), type);
                }
            }
        }
    }

    /**
     * 刷新越南市场股票
     * @param jsonObject 股票对象
     * @param type 交易所类型
     * @throws Exception
     */
    private void updateVietnamStock(JSONObject jsonObject, int type) throws Exception {
        // 第一次保存全部股票信息
//        saveStock(jsonObject, type);
        // 刷新越南市场股票
        updateStock(jsonObject);
    }

    /**
     * 更新股票信息
     * @param jsonObject
     * @throws Exception
     */
    private void updateStock(JSONObject jsonObject) throws Exception {
        if (ObjectUtils.isNotEmpty(jsonObject)) {
            LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(FaStrategy::getCode, jsonObject.getString("ss"));
            if (jsonObject.containsKey("mp")) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiTrade, new BigDecimal(jsonObject.getIntValue("mp")).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
            }
            if (jsonObject.containsKey("pc")) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiPricechange, new BigDecimal(jsonObject.getIntValue("pc")).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
            }
            if (jsonObject.containsKey("cp")) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiChangepercent, jsonObject.getBigDecimal("cp"));
            }
            if (jsonObject.containsKey("b1")) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiBuy, new BigDecimal(jsonObject.getIntValue("b1")).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
            }
            if (jsonObject.containsKey("o1")) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiSell, new BigDecimal(jsonObject.getIntValue("o1")).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
            }
            if (jsonObject.containsKey("r")) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiSettlement, jsonObject.getBigDecimal("r").divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
            }
            if (jsonObject.containsKey("o")) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiOpen, new BigDecimal(jsonObject.getIntValue("o")).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
            }
            if (jsonObject.containsKey("h")) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiHigh, new BigDecimal(jsonObject.getIntValue("h")).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
            }
            if (jsonObject.containsKey("l")) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiLow, new BigDecimal(jsonObject.getIntValue("l")).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
            }
            if (jsonObject.containsKey("mtq")) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiVolume, jsonObject.getBigDecimal("mtq"));
            }
            if (jsonObject.containsKey("rfq")) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiAmount, jsonObject.getBigDecimal("rfq"));
            }
            if (jsonObject.containsKey("timeMaker")) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiTicktime, changeDate(jsonObject.getLong("timeMaker")));
            }
            lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
            this.update(lambdaUpdateWrapper);
        }
    }

    /**
     * 第一次保存全部股票信息
     * @param jsonObject
     * @param type
     * @throws Exception
     */
    private void saveStock(JSONObject jsonObject, int type) throws Exception {
        if (ObjectUtils.isNotEmpty(jsonObject)) {
            FaStrategy faStrategy = new FaStrategy();
            faStrategy.setType(type);

            faStrategy.setTitle(jsonObject.getString("cv"));
            faStrategy.setTitleEn(jsonObject.getString("ce"));
            faStrategy.setCode(jsonObject.getString("ss"));
            faStrategy.setAllCode(jsonObject.getString("ss"));

            if (jsonObject.containsKey("mp")) {
                faStrategy.setCaiTrade(jsonObject.getBigDecimal("mp").divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
            }
            if (jsonObject.containsKey("pc")) {
                faStrategy.setCaiPricechange(jsonObject.getBigDecimal("pc").divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
            }
            if (jsonObject.containsKey("cp")) {
                faStrategy.setCaiChangepercent(jsonObject.getBigDecimal("cp"));
            }
            if (jsonObject.containsKey("b1")) {
                faStrategy.setCaiBuy(jsonObject.getBigDecimal("b1").divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
            }
            if (jsonObject.containsKey("o1")) {
                faStrategy.setCaiSell(jsonObject.getBigDecimal("o1").divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
            }
            if (jsonObject.containsKey("r")) {
                faStrategy.setCaiSettlement(jsonObject.getBigDecimal("r").divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
            }
            if (jsonObject.containsKey("o")) {
                faStrategy.setCaiOpen(jsonObject.getBigDecimal("o").divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
            }
            if (jsonObject.containsKey("h")) {
                faStrategy.setCaiHigh(jsonObject.getBigDecimal("h").divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
            }
            if (jsonObject.containsKey("l")) {
                faStrategy.setCaiLow(jsonObject.getBigDecimal("l").divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
            }
            if (jsonObject.containsKey("mtq")) {
                faStrategy.setCaiVolume(new BigDecimal(jsonObject.getIntValue("mtq")));
            }
            if (jsonObject.containsKey("rfq")) {
                faStrategy.setCaiAmount(new BigDecimal(jsonObject.getIntValue("rfq")));
            }
            faStrategy.setCreateTime(new Date());
            baseMapper.insert(faStrategy);
        }
    }

    /**
     * 刷新大宗价格
     * @throws Exception
     */
    @Override
    public void updateDzPrice() throws Exception {
        faStrategyMapper.updateDzPrice();
    }

    /**
     * 获取当前价
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getStockCurrentPrice(FaStrategy faStrategy) throws Exception {

        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStrategy::getCode, faStrategy.getCode());
        faStrategy = iFaStrategyService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faStrategy)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        // 先更新最新价到数据库
        updateStockCurrentPrice(faStrategy);

        // 从数据库取最新价
        BigDecimal currentPrice = faStrategyMapper.getStockCurrentPrice(faStrategy);
        return currentPrice;
    }

    /**
     * 先更新最新价到数据库
     * @param faStrategy
     * @throws Exception
     */
    private void updateStockCurrentPrice(FaStrategy faStrategy) throws Exception {
        // 先从官网取最新价，更新到数据库
//        boolean office = updateStockCurrentPriceByOffice(faStrategy);
//        boolean office = false;

        // 官网失败，再从接口取最新价，更新到数据库
//        if (!office) {
//            updateStockCurrentPriceByInterface(faStrategy);
//        }

        // 从VPS获取最新价
        updateStockCurrentPriceByVPS(faStrategy);
    }

    /**
     * VPS获取最新价
     * @param faStrategy
     * @throws Exception
     */
    private void updateStockCurrentPriceByVPS(FaStrategy faStrategy) throws Exception {
        String result = VietnamHttpUtils.sendGet("https://bgapidatafeed.vps.com.vn/getliststockdata/" + faStrategy.getCode());
        if (StringUtils.isNotEmpty(result)) {
            JSONArray jsonArray = JSONArray.parseArray(result);
            if (!jsonArray.isEmpty()) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                if (jsonObject.containsKey("sym")) {
                    updateVietnamStockVPS(jsonObject);
                }
            }
        }
    }

    private boolean updateStockCurrentPriceByOffice(FaStrategy faStrategy) throws Exception {
        String cookie = redisCache.getSSIToken();
        String result = VietnamHttpUtils.sendGetForSSI("https://iboard-query.ssi.com.vn/v2/stock/" + faStrategy.getCode(), cookie);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("code") && "SUCCESS".equalsIgnoreCase(jsonObject.getString("code"))) {
            jsonObject = jsonObject.getJSONObject("data");
            if (ObjectUtils.isNotEmpty(jsonObject)) {
                LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaStrategy::getId, faStrategy.getId());
                if (jsonObject.containsKey("mp")) {
                    lambdaUpdateWrapper.set(FaStrategy::getCaiTrade, new BigDecimal(jsonObject.getIntValue("mp")).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
                }
                if (jsonObject.containsKey("pc")) {
                    lambdaUpdateWrapper.set(FaStrategy::getCaiPricechange, new BigDecimal(jsonObject.getIntValue("pc")).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
                }
                if (jsonObject.containsKey("cp")) {
                    lambdaUpdateWrapper.set(FaStrategy::getCaiChangepercent, jsonObject.getBigDecimal("cp"));
                }
                if (jsonObject.containsKey("b1")) {
                    lambdaUpdateWrapper.set(FaStrategy::getCaiBuy, new BigDecimal(jsonObject.getIntValue("b1")).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
                }
                if (jsonObject.containsKey("o1")) {
                    lambdaUpdateWrapper.set(FaStrategy::getCaiSell, new BigDecimal(jsonObject.getIntValue("o1")).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
                }
                if (jsonObject.containsKey("r")) {
                    lambdaUpdateWrapper.set(FaStrategy::getCaiSettlement, jsonObject.getBigDecimal("r").divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
                }
                if (jsonObject.containsKey("o")) {
                    lambdaUpdateWrapper.set(FaStrategy::getCaiOpen, new BigDecimal(jsonObject.getIntValue("o")).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
                }
                if (jsonObject.containsKey("h")) {
                    lambdaUpdateWrapper.set(FaStrategy::getCaiHigh, new BigDecimal(jsonObject.getIntValue("h")).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
                }
                if (jsonObject.containsKey("l")) {
                    lambdaUpdateWrapper.set(FaStrategy::getCaiLow, new BigDecimal(jsonObject.getIntValue("l")).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP));
                }
                if (jsonObject.containsKey("mtq")) {
                    lambdaUpdateWrapper.set(FaStrategy::getCaiVolume, jsonObject.getBigDecimal("mtq"));
                }
                if (jsonObject.containsKey("rfq")) {
                    lambdaUpdateWrapper.set(FaStrategy::getCaiAmount, jsonObject.getBigDecimal("rfq"));
                }
                if (jsonObject.containsKey("timeMaker")) {
                    lambdaUpdateWrapper.set(FaStrategy::getCaiTicktime, changeDate(jsonObject.getLong("timeMaker")));
                }
                lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
                this.update(lambdaUpdateWrapper);
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        try {
            VietnameStrategyServiceImpl vietnameStrategyService = new VietnameStrategyServiceImpl();
            vietnameStrategyService.updateVietnamStock("https://iboard-query.ssi.com.vn/v2/stock/exchange/hose", 1, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}