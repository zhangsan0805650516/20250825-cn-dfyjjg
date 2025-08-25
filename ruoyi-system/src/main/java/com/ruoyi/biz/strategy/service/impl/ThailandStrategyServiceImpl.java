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
import com.ruoyi.biz.strategy.service.ThailandStrategyService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.ThailandHttpUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 策略Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-06
 */
@Service
public class ThailandStrategyServiceImpl extends ServiceImpl<FaStrategyMapper, FaStrategy> implements ThailandStrategyService
{

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Autowired
    private IFaStockTradingService iFaStockTradingService;

    @Autowired
    private IFaMemberService iFaMemberService;

    @Autowired
    private IFaStockHoldDetailService iFaStockHoldDetailService;

    @Autowired
    private FaStrategyMapper faStrategyMapper;

    @Autowired
    private RedisCache redisCache;

    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

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
                    BigDecimal amount = faStockTrading.getTradingAmount().subtract(faStockTrading.getStampDuty()).subtract(faStockTrading.getTradingPoundage());
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
     * 刷新泰国股票
     * @throws Exception
     */
    @Override
    public void updateThailandStock() throws Exception {
        String cookie = redisCache.getSETToken();
        String[] setUrls = new String[] {
                "https://www.set.or.th/api/set/index/AGRO/composition",
                "https://www.set.or.th/api/set/index/CONSUMP/composition",
                "https://www.set.or.th/api/set/index/FINCIAL/composition",
                "https://www.set.or.th/api/set/index/INDUS/composition",
                "https://www.set.or.th/api/set/index/PROPCON/composition",
                "https://www.set.or.th/api/set/index/RESOURC/composition",
                "https://www.set.or.th/api/set/index/SERVICE/composition",
                "https://www.set.or.th/api/set/index/TECH/composition"
        };

        for (String url : setUrls) {
            String result = ThailandHttpUtils.sendGetForSet(url, cookie);
            updateThailandStockSet(result);
        }

        String[] maiUrls = new String[] {
                "https://www.set.or.th/api/set/index/AGRO-m/composition",
                "https://www.set.or.th/api/set/index/CONSUMP-m/composition",
                "https://www.set.or.th/api/set/index/FINCIAL-m/composition",
                "https://www.set.or.th/api/set/index/INDUS-m/composition",
                "https://www.set.or.th/api/set/index/PROPCON-m/composition",
                "https://www.set.or.th/api/set/index/RESOURC-m/composition",
                "https://www.set.or.th/api/set/index/SERVICE-m/composition",
                "https://www.set.or.th/api/set/index/TECH-m/composition"
        };

        for (String url : maiUrls) {
            String result = ThailandHttpUtils.sendGetForSet(url, cookie);
            updateThailandStockMai(result);
        }
    }

    private void updateThailandStockSet(String result) throws Exception {
        if (StringUtils.isNotEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("composition")) {
                jsonObject = jsonObject.getJSONObject("composition");
                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("subIndices")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("subIndices");
                    if (null != jsonArray && !jsonArray.isEmpty()) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            if (ObjectUtils.isNotEmpty(object) && object.containsKey("stockInfos")) {
                                JSONArray stockInfos = object.getJSONArray("stockInfos");
                                if (null != stockInfos && !stockInfos.isEmpty()) {
                                    for (int j = 0; j < stockInfos.size(); j++) {
                                        JSONObject stockInfo = stockInfos.getJSONObject(j);
                                        if (ObjectUtils.isNotEmpty(stockInfo)) {
                                            updateThailandStock(stockInfo);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateThailandStock(JSONObject stockInfo) throws Exception {
        LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaStrategy::getCode, stockInfo.getString("symbol"));

        lambdaUpdateWrapper.set(FaStrategy::getCaiTrade, stockInfo.getBigDecimal("last"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiPricechange, stockInfo.getBigDecimal("change"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiChangepercent, stockInfo.getBigDecimal("percentChange"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiBuy, getCaiBuy(stockInfo.getJSONArray("bids")));
        lambdaUpdateWrapper.set(FaStrategy::getCaiSell, getCaiSell(stockInfo.getJSONArray("offers")));
        lambdaUpdateWrapper.set(FaStrategy::getCaiSettlement, stockInfo.getBigDecimal("prior"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiOpen, stockInfo.getBigDecimal("open"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiHigh, stockInfo.getBigDecimal("high"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiLow, stockInfo.getBigDecimal("low"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiVolume, stockInfo.getBigDecimal("totalVolume"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiAmount, stockInfo.getBigDecimal("totalValue"));
//                                            lambdaUpdateWrapper.set(FaStrategy::getCaiChangeHands, new BigDecimal(rows.get(4).text()));
        lambdaUpdateWrapper.set(FaStrategy::getCaiAverage, stockInfo.getBigDecimal("average"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiLimitUpPrice, stockInfo.getBigDecimal("ceiling"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiLimitDownPrice, stockInfo.getBigDecimal("floor"));
//                                            lambdaUpdateWrapper.set(FaStrategy::getCaiTicktime, new BigDecimal(rows.get(4).text()));
        lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    private void updateThailandStockMai(String result) throws Exception {
        if (StringUtils.isNotEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("composition")) {
                jsonObject = jsonObject.getJSONObject("composition");
                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("stockInfos")) {
                    JSONArray stockInfos = jsonObject.getJSONArray("stockInfos");
                    if (null != stockInfos && !stockInfos.isEmpty()) {
                        for (int j = 0; j < stockInfos.size(); j++) {
                            JSONObject stockInfo = stockInfos.getJSONObject(j);
                            if (ObjectUtils.isNotEmpty(stockInfo)) {
                                updateThailandStock(stockInfo);
                            }
                        }
                    }
                }
            }
        }
    }

    private BigDecimal getCaiBuy(JSONArray bids) throws Exception {
        BigDecimal buy = BigDecimal.ZERO;
        if (!bids.isEmpty()) {
            JSONObject jsonObject = bids.getJSONObject(0);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("price")) {
                buy = jsonObject.getBigDecimal("price");
            }
        }
        return buy;
    }

    private BigDecimal getCaiSell(JSONArray offers) throws Exception {
        BigDecimal sell = BigDecimal.ZERO;
        if (!offers.isEmpty()) {
            JSONObject jsonObject = offers.getJSONObject(0);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("price")) {
                sell = jsonObject.getBigDecimal("price");
            }
        }
        return sell;
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
     * 刷新抢筹价
     * @throws Exception
     */
    @Override
    public void updateVipQcPrice() throws Exception {
        faStrategyMapper.updateVipQcPrice();
    }

    /**
     * 获取实时价格
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getStockCurrentPrice(FaStrategy faStrategy) throws Exception {
        BigDecimal currentPrice = BigDecimal.ZERO;
        // 更新实时信息到数据库
        try {
            currentPrice = updateCurrentStockInfo(faStrategy);
        } catch (Exception e) {
            // 更新出问题，取数据库价格
            currentPrice = faStrategyMapper.getStockCurrentPrice(faStrategy);
        }
        return currentPrice;
    }

    /**
     * 更新实时信息到数据库
     * @param faStrategy
     * @throws Exception
     */
    private BigDecimal updateCurrentStockInfo(FaStrategy faStrategy) throws Exception {
        BigDecimal currentPrice = BigDecimal.ZERO;

        Document doc = Jsoup.connect("https://www.set.or.th/en/market/product/stock/quote/" + faStrategy.getCode() + "/price")
                //设置爬取超时时间
                .timeout(10000)
                //get请求
                .get();

        Elements tbody = doc.getElementsByClass("card-minimal-body");
        Elements rows = tbody.select("span");

        LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaStrategy::getId, faStrategy.getId());

        // 股票是否开盘
        if ("-".equals(rows.get(4).text())) {
            throw new ServiceException(MessageUtils.message("stock closed"), HttpStatus.ERROR);
        }
        currentPrice = new BigDecimal(rows.get(4).text());

        lambdaUpdateWrapper.set(FaStrategy::getCaiTrade, currentPrice);
        lambdaUpdateWrapper.set(FaStrategy::getCaiPricechange, getCaiPricechange(rows.get(5).text()));
        lambdaUpdateWrapper.set(FaStrategy::getCaiChangepercent, getCaiChangepercent(rows.get(5).text()));
        lambdaUpdateWrapper.set(FaStrategy::getCaiBuy, getCaiBuy(rows.get(16).text()));
        lambdaUpdateWrapper.set(FaStrategy::getCaiSell, getCaiSell(rows.get(17).text()));
        lambdaUpdateWrapper.set(FaStrategy::getCaiSettlement, new BigDecimal(rows.get(8).text()));
        lambdaUpdateWrapper.set(FaStrategy::getCaiOpen, new BigDecimal(rows.get(9).text()));
        lambdaUpdateWrapper.set(FaStrategy::getCaiHigh, new BigDecimal(rows.get(0).text()));
        lambdaUpdateWrapper.set(FaStrategy::getCaiLow, new BigDecimal(rows.get(1).text()));
        lambdaUpdateWrapper.set(FaStrategy::getCaiVolume, new BigDecimal(rows.get(10).text().replace(",", "")));
        lambdaUpdateWrapper.set(FaStrategy::getCaiAmount, new BigDecimal(rows.get(11).text().replace(",", "")));
//        lambdaUpdateWrapper.set(FaStrategy::getCaiChangeHands, new BigDecimal(rows.get(4).text()));
        lambdaUpdateWrapper.set(FaStrategy::getCaiAverage, new BigDecimal(rows.get(12).text()));
        lambdaUpdateWrapper.set(FaStrategy::getCaiLimitUpPrice, new BigDecimal(rows.get(15).text()));
        lambdaUpdateWrapper.set(FaStrategy::getCaiLimitDownPrice, new BigDecimal(rows.get(14).text()));
//        lambdaUpdateWrapper.set(FaStrategy::getCaiTicktime, new BigDecimal(rows.get(4).text()));

        // 抢筹未开，更新抢筹价格
        if (0 == faStrategy.getVipQcStatus()) {
            lambdaUpdateWrapper.set(FaStrategy::getVipQcPrice, currentPrice);
        }

        lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);

        return currentPrice;
    }

    private BigDecimal getCaiPricechange(String content) throws Exception {
        content = content.substring(0, content.indexOf(" ")).trim();
        return new BigDecimal(content);
    }

    private BigDecimal getCaiChangepercent(String content) throws Exception {
        content = content.substring(content.indexOf("(") + 1, content.indexOf("%")).trim();
        return new BigDecimal(content);
    }

    private BigDecimal getCaiBuy(String content) throws Exception {
        content = content.substring(0, content.indexOf(" ")).trim();
        return new BigDecimal(content);
    }

    private BigDecimal getCaiSell(String content) throws Exception {
        content = content.substring(0, content.indexOf(" ")).trim();
        return new BigDecimal(content);
    }

}