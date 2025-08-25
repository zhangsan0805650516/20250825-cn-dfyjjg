package com.ruoyi.biz.strategy.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.news.domain.FaNews;
import com.ruoyi.biz.news.service.IFaNewsService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.shengou.domain.FaNewStock;
import com.ruoyi.biz.shengou.service.IFaShengouService;
import com.ruoyi.biz.stockHoldDetail.service.IFaStockHoldDetailService;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;
import com.ruoyi.biz.stockTrading.service.IFaStockTradingService;
import com.ruoyi.biz.strategy.domain.FaStrategy;
import com.ruoyi.biz.strategy.mapper.FaStrategyMapper;
import com.ruoyi.biz.strategy.service.IFaStrategyService;
import com.ruoyi.biz.strategy.service.IndiaStrategyService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.http.IndiaHttpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 策略Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-06
 */
@Service
public class IndiaStrategyServiceImpl extends ServiceImpl<FaStrategyMapper, FaStrategy> implements IndiaStrategyService
{

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IFaShengouService iFaShengouService;

    @Autowired
    private IFaStrategyService iFaStrategyService;

    @Autowired
    private IFaMemberService iFaMemberService;

    @Autowired
    private IFaStockHoldDetailService iFaStockHoldDetailService;

    @Autowired
    private IFaNewsService iFaNewsService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Autowired
    private IFaStockTradingService iFaStockTradingService;

    @Autowired
    private FaStrategyMapper faStrategyMapper;

    private static final String API_KEY = "55add7694cb14a9ba10296e6c1956f8a";

    /**
     * T+N冻结资金清零
     * @throws Exception
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
     * @throws Exception
     */
    @Override
    public void clearT1Hold() throws Exception {
        // 持仓明细剩余冻结天数-1
        iFaStockHoldDetailService.updateFreezeDaysLeft();
        // 持仓明细冻结状态判断
        iFaStockHoldDetailService.updateFreezeStatus();
    }

    /**
     * 刷新印度新股
     * @throws Exception
     */
    @Override
    public void updateIndiaNewStock() throws Exception {
        // iifl网站刷新股
        updateIndiaNewStockFromIifl();

        // chittorgarh网站刷新股 todo
//        updateIndiaNewStockFromChittorgarh();
    }

    /**
     * chittorgarh网站刷新股
     * @throws Exception
     */
//    private void updateIndiaNewStockFromChittorgarh() throws Exception {
//        Document doc = Jsoup.connect("https://www.chittorgarh.com/report/ipo-in-india-list-main-board-sme/82/#")
//                //设置爬取超时时间
//                .timeout(10000)
//                //get请求
//                .get();
//
//        Elements tbody = doc.getElementsByClass("tbody");
//        Elements rows = tbody.select("tr");
//        List<FaNewStock> list = new ArrayList<>();
//        FaNewStock faNewStock;
//        for(int i = 0; i < rows.size(); i++) {
//            faNewStock = new FaNewStock();
//            Element tr = rows.get(i);//获取表头
//            Elements tds = tr.select("td");
//
//            faNewStock.setCode(dataFormat(tds.get(0).text()));
//            faNewStock.setName(dataFormat(tds.get(1).text()));
//            faNewStock.setSgCode(dataFormat(tds.get(2).text()));
//            faNewStock.setFxNum(dataFormat(tds.get(3).text()));
//            faNewStock.setWsfxNum(dataFormat(tds.get(4).text()));
//            faNewStock.setSgLimit(dataFormat(tds.get(5).text()));
//            faNewStock.setDgLimit(dataFormat(tds.get(6).text()));
//            faNewStock.setFxPrice(dataFormat(tds.get(7).text()));
//            faNewStock.setFxRate(dataFormat(tds.get(8).text()));
//            faNewStock.setHyRate(dataFormat(tds.get(9).text()));
//            // 申购日期
//            faNewStock.setSgDate(dateFormat(tds.get(10).text()));
//            faNewStock.setZqRate(dataFormat(tds.get(11).text()));
//            // 中签缴款日期
//            faNewStock.setZqJkDate(dateFormat(faNewStock.getSgDate(), tds.get(13).text()));
//            // 上市日期
//            faNewStock.setSsDate(dateFormat(faNewStock.getSgDate(), tds.get(14).text()));
//            faNewStock.setSgNums(1000000);
//            faNewStock.setXxNums(1000000);
//            // 刷新新股时 根据股票代码判断所属市场
//            faNewStock.setSgType(typeFormat(tds.get(0).text()));
//            list.add(faNewStock);
//        }
//        updateNewStock(list);
//    }

    /**
     * iifl网站刷新股
     * @throws Exception
     */
    private void updateIndiaNewStockFromIifl() throws Exception {
        String token = redisCache.getIiflToken();
        // 开始申购的新股，能获取到交易所类型
        String open = HttpUtils.sendGetForIifl("https://api.indiainfoline.com/api/cmots/v1/ipo-fpo/open-issues/10", token);
        if (StringUtils.isNotEmpty(open)) {
            JSONObject jsonObject = JSONObject.parseObject(open);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("statusCode") && jsonObject.getInteger("statusCode") == 200) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (!jsonArray.isEmpty()) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        FaNewStock faNewStock = new FaNewStock();
                        faNewStock.setSgCode(jsonArray.getJSONObject(i).getString("co_code"));
                        faNewStock.setName(jsonArray.getJSONObject(i).getString("lname"));
                        faNewStock.setFxPrice(jsonArray.getJSONObject(i).getString("issueprice"));
                        faNewStock.setFxRate(jsonArray.getJSONObject(i).getString("volsrno"));
                        faNewStock.setSgDate(changeDate(jsonArray.getJSONObject(i).getString("opendate")));
                        faNewStock.setSgEndDate(changeDate(jsonArray.getJSONObject(i).getString("closdate")));
                        faNewStock.setIssueSize(jsonArray.getJSONObject(i).getString("issuesize"));
                        faNewStock.setNoshIssued(jsonArray.getJSONObject(i).getString("noshissued"));
                        faNewStock.setListingExchange(getListingExchange(jsonArray.getJSONObject(i).getString("listing")));
                        faNewStock.setCreateTime(new Date());
                        LambdaQueryWrapper<FaNewStock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                        lambdaQueryWrapper.eq(FaNewStock::getSgCode, faNewStock.getSgCode());
                        lambdaQueryWrapper.eq(FaNewStock::getDeleteFlag, 0);
                        FaNewStock checkOne = iFaShengouService.getOne(lambdaQueryWrapper);
                        // 已存在，更新信息
                        if (ObjectUtils.isNotEmpty(checkOne)) {
                            LambdaUpdateWrapper<FaNewStock> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                            lambdaUpdateWrapper.eq(FaNewStock::getId, checkOne.getId());
                            lambdaUpdateWrapper.set(FaNewStock::getName, faNewStock.getName());
                            lambdaUpdateWrapper.set(FaNewStock::getFxPrice, faNewStock.getFxPrice());
                            lambdaUpdateWrapper.set(FaNewStock::getFxRate, faNewStock.getFxRate());
                            lambdaUpdateWrapper.set(FaNewStock::getSgDate, faNewStock.getSgDate());
                            lambdaUpdateWrapper.set(FaNewStock::getSgEndDate, faNewStock.getSgEndDate());
                            lambdaUpdateWrapper.set(FaNewStock::getIssueSize, faNewStock.getIssueSize());
                            lambdaUpdateWrapper.set(FaNewStock::getNoshIssued, faNewStock.getNoshIssued());
                            lambdaUpdateWrapper.set(FaNewStock::getListingExchange, faNewStock.getListingExchange());
                            lambdaUpdateWrapper.set(FaNewStock::getUpdateTime, new Date());
                            iFaShengouService.update(lambdaUpdateWrapper);
                        }
                        // 不存在，按交易所分类保存
                        else {
                            if (jsonArray.getJSONObject(i).containsKey("listing") && StringUtils.isNotEmpty(jsonArray.getJSONObject(i).getString("listing"))) {
                                String listing = jsonArray.getJSONObject(i).getString("listing");
                                faNewStock.setSgType(0);
                                // 默认打开申购
                                faNewStock.setSgSwitch(1);
                                iFaShengouService.save(faNewStock);

//                                // 上NSE交易所
//                                if (listing.indexOf("NSE") > 0) {
//                                    FaNewStock newStock = new FaNewStock();
//                                    BeanUtils.copyBeanProp(newStock, faNewStock);
//                                    newStock.setSgType(1);
//                                    iFaShengouService.save(newStock);
//                                }
//                                // 上BSE交易所
//                                if (listing.indexOf("BSE") > 0) {
//                                    FaNewStock newStock = new FaNewStock();
//                                    BeanUtils.copyBeanProp(newStock, faNewStock);
//                                    newStock.setSgType(2);
//                                    iFaShengouService.save(newStock);
//                                }
                            }
                        }
                    }
                }
            }
        }

        // 关闭申购的新股，能获取到上市日期
        String closed = HttpUtils.sendGetForIifl("https://api.indiainfoline.com/api/cmots/v1/ipo-fpo/closed-issues/10", token);
        if (StringUtils.isNotEmpty(closed)) {
            JSONObject jsonObject = JSONObject.parseObject(closed);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("statusCode") && jsonObject.getInteger("statusCode") == 200) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (!jsonArray.isEmpty()) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        LambdaUpdateWrapper<FaNewStock> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                        lambdaUpdateWrapper.eq(FaNewStock::getSgCode, jsonArray.getJSONObject(i).getString("co_code"));
                        lambdaUpdateWrapper.eq(FaNewStock::getName, jsonArray.getJSONObject(i).getString("lname"));
                        lambdaUpdateWrapper.set(FaNewStock::getSsDate, changeDate(jsonArray.getJSONObject(i).getString("listdate")));
                        lambdaUpdateWrapper.set(FaNewStock::getUpdateTime, new Date());
                        iFaShengouService.update(lambdaUpdateWrapper);
                    }
                }
            }
        }
    }

    /**
     * 上市交易所(1NSE 2BSE 3NSE+BSE)
     * @param listing
     * @return
     * @throws Exception
     */
    private Integer getListingExchange(String listing) throws Exception {
        int nseFlag = 0;
        int bseFlag = 0;
        if (listing.contains("nse") || listing.contains("NSE")) {
            nseFlag = 1;
        }
        if (listing.contains("bse") || listing.contains("BSE")) {
            bseFlag = 2;
        }
        return nseFlag + bseFlag;
    }

    /**
     * 印度时间格式转换
     * @param date
     * @return
     * @throws Exception
     */
    private Date changeDate(String date) throws Exception {
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy K:mm:ss aa", Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = sdf2.format(sdf1.parse(date));
        return sdf2.parse(date);
    }

    /**
     * 获取印度市场股票实时价格 官网和接口
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
        boolean office = false;

        // 官网失败，再从接口取最新价，更新到数据库
        if (!office) {
            updateStockCurrentPriceByInterface(faStrategy);
        }
    }

    /**
     * 从接口取最新价
     * @param faStrategy
     * @throws Exception
     */
    private void updateStockCurrentPriceByInterface(FaStrategy faStrategy) throws Exception {
        String result = IndiaHttpUtils.sendGetForRapid("https://indian-stock-exchange-api1.p.rapidapi.com/stock_price/?symbol=", faStrategy.getCode());
        if (StringUtils.isNotEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("last_trading_price")) {
                LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaStrategy::getId, faStrategy.getId());
                lambdaUpdateWrapper.set(FaStrategy::getCaiTrade, jsonObject.getBigDecimal("last_trading_price"));
                // 大宗关闭时，刷新价格，开启时不刷   不实时刷新大宗价格，一天一次定时任务，将收盘价刷到大宗价格
//                if (faStrategy.getIsDz() == 0) {
//                    lambdaUpdateWrapper.set(FaStrategy::getZfaPrice, jsonObject.getBigDecimal("last_trading_price"));
//                }
                lambdaUpdateWrapper.set(FaStrategy::getCaiPricechange, jsonObject.getBigDecimal("day_change"));
                lambdaUpdateWrapper.set(FaStrategy::getCaiChangepercent, jsonObject.getBigDecimal("day_change_percent"));
//                lambdaUpdateWrapper.set(FaStrategy::getCaiBuy, info[9]);
//                lambdaUpdateWrapper.set(FaStrategy::getCaiSell, info[19]);
                lambdaUpdateWrapper.set(FaStrategy::getCaiSettlement, jsonObject.getBigDecimal("previous_close"));
                lambdaUpdateWrapper.set(FaStrategy::getCaiOpen, jsonObject.getBigDecimal("open"));
                lambdaUpdateWrapper.set(FaStrategy::getCaiHigh, jsonObject.getBigDecimal("day_high"));
                lambdaUpdateWrapper.set(FaStrategy::getCaiLow, jsonObject.getBigDecimal("day_low"));
                lambdaUpdateWrapper.set(FaStrategy::getCaiVolume, jsonObject.getBigDecimal("volume"));
//                lambdaUpdateWrapper.set(FaStrategy::getCaiAmount, info[37]);
//                lambdaUpdateWrapper.set(FaStrategy::getCaiTicktime, getDate(jsonObject.getString("timestamp")));
                lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
                this.update(lambdaUpdateWrapper);
            } else {
                throw new ServiceException(MessageUtils.message("rapidapi.error"), HttpStatus.ERROR);
            }
        }
    }

    /**
     * 先从官网取最新价
     * @param faStrategy
     * @return
     */
    private boolean updateStockCurrentPriceByOffice(FaStrategy faStrategy) {
        boolean flag = false;
        // NSE
        if (1 == faStrategy.getType()) {
            flag = updateStockInfoInNse(faStrategy.getCode());
        }
        // BSE
        else if (2 == faStrategy.getType()) {
            flag = updateStockInfoInBse(faStrategy.getCode());
        }
        return flag;
    }

    /**
     * 获取印度市场股票实时价格 接口
     * @throws Exception
     */
    private BigDecimal getStockCurrentPrice(String codes, String exchange) throws Exception {
        String result = HttpUtils.sendGet("https://api.twelvedata.com/price?symbol=" + codes + "&exchange=" + exchange + "&apikey=" + API_KEY);
        if (StringUtils.isNotEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("price")) {
                return new BigDecimal(jsonObject.getString("price"));
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * 获取印度市场股票实时价格 官网
     * @throws Exception
     */
    @Override
    public Boolean updateStockInfoInNse(String code) {
        try {
            String result = HttpUtils.sendGetForNse("https://www.nseindia.com/api/quote-equity?symbol=" + code, redisCache.getNseCookie());
            if (StringUtils.isNotEmpty(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("priceInfo")) {
                    JSONObject priceInfo = jsonObject.getJSONObject("priceInfo");
                    LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaStrategy::getCode, code);
                    lambdaUpdateWrapper.eq(FaStrategy::getType, 1);
                    lambdaUpdateWrapper.set(FaStrategy::getCaiTrade, priceInfo.getString("lastPrice"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiPricechange, priceInfo.getString("change"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiChangepercent, priceInfo.getString("pChange"));
//                lambdaUpdateWrapper.set(FaStrategy::getCaiBuy, info[9]);
//                lambdaUpdateWrapper.set(FaStrategy::getCaiSell, info[19]);
                    lambdaUpdateWrapper.set(FaStrategy::getCaiSettlement, priceInfo.getString("basePrice"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiOpen, priceInfo.getString("open"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiHigh, priceInfo.getJSONObject("intraDayHighLow").getString("max"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiLow, priceInfo.getJSONObject("intraDayHighLow").getString("min"));
//                lambdaUpdateWrapper.set(FaStrategy::getCaiVolume, header.getString("volume"));
//                lambdaUpdateWrapper.set(FaStrategy::getCaiAmount, info[37]);
//                lambdaUpdateWrapper.set(FaStrategy::getCaiTicktime, getDate(jsonObject.getString("timestamp")));
                    lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
                    this.update(lambdaUpdateWrapper);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 获取印度市场股票实时价格 官网
     * @throws Exception
     */
    @Override
    public Boolean updateStockInfoInBse(String code) {
        try {
            String result = HttpUtils.sendGetForBse("https://api.bseindia.com/BseIndiaAPI/api/getScripHeaderData/w?Debtflag=&scripcode=" + code + "&seriesid=");
            if (StringUtils.isNotEmpty(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("CurrRate") && jsonObject.containsKey("Header")) {
                    JSONObject currRate = jsonObject.getJSONObject("CurrRate");
                    JSONObject header = jsonObject.getJSONObject("Header");
                    LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaStrategy::getCode, code);
                    lambdaUpdateWrapper.eq(FaStrategy::getType, 2);
                    lambdaUpdateWrapper.set(FaStrategy::getCaiTrade, currRate.getString("LTP"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiPricechange, currRate.getString("Chg"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiChangepercent, currRate.getString("PcChg"));
//                lambdaUpdateWrapper.set(FaStrategy::getCaiBuy, info[9]);
//                lambdaUpdateWrapper.set(FaStrategy::getCaiSell, info[19]);
                    lambdaUpdateWrapper.set(FaStrategy::getCaiSettlement, header.getString("PrevClose"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiOpen, header.getString("Open"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiHigh, header.getString("High"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiLow, header.getString("Low"));
//                lambdaUpdateWrapper.set(FaStrategy::getCaiVolume, header.getString("volume"));
//                lambdaUpdateWrapper.set(FaStrategy::getCaiAmount, info[37]);
//                lambdaUpdateWrapper.set(FaStrategy::getCaiTicktime, getDate(jsonObject.getString("timestamp")));
                    lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
                    this.update(lambdaUpdateWrapper);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 检测印度新股上市
     * @throws Exception
     */
    @Override
    public void checkIndiaNewStock() throws Exception {
        String token = redisCache.getIiflToken();
        // NSE上市新股
        String nseNewListing = HttpUtils.sendGetForIifl("https://api.indiainfoline.com/api/cmots/v1/ipo-fpo/new-listings/nse/10", token);
        if (StringUtils.isNotEmpty(nseNewListing)) {
            JSONObject jsonObject = JSONObject.parseObject(nseNewListing);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("statusCode") && jsonObject.getInteger("statusCode") == 200) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (!jsonArray.isEmpty()) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        // 股票表内是否存在
                        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                        lambdaQueryWrapper.eq(FaStrategy::getCode, jsonArray.getJSONObject(i).getString("code"));
                        lambdaQueryWrapper.eq(FaStrategy::getType, 1);
                        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
                        int count = (int) iFaStrategyService.count(lambdaQueryWrapper);
                        // 不存在，新增NSE股票
                        if (count == 0) {
                            FaStrategy faStrategy = new FaStrategy();
                            faStrategy.setTitle(jsonArray.getJSONObject(i).getString("co_name"));
                            faStrategy.setCode(jsonArray.getJSONObject(i).getString("code"));
                            faStrategy.setAllCode(jsonArray.getJSONObject(i).getString("code"));
                            faStrategy.setType(1);
                            faStrategy.setCreateTime(new Date());
                            iFaStrategyService.save(faStrategy);
                        }
                        // 更新新股表内状态
                        LambdaUpdateWrapper<FaNewStock> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                        lambdaUpdateWrapper.eq(FaNewStock::getSgCode, jsonArray.getJSONObject(i).getString("co_code"));
//                        lambdaUpdateWrapper.eq(FaNewStock::getSgType, 1);
                        lambdaUpdateWrapper.eq(FaNewStock::getIsList, 0);
                        lambdaUpdateWrapper.set(FaNewStock::getIsList, 1);
                        lambdaUpdateWrapper.set(FaNewStock::getUpdateTime, new Date());
                        lambdaUpdateWrapper.set(FaNewStock::getAllCode, jsonArray.getJSONObject(i).getString("code"));
                        iFaShengouService.update(lambdaUpdateWrapper);
                    }
                }
            }
        }

        // BSE上市新股
        String bseNewListing = HttpUtils.sendGetForIifl("https://api.indiainfoline.com/api/cmots/v1/ipo-fpo/new-listings/bse/10", token);
        if (StringUtils.isNotEmpty(bseNewListing)) {
            JSONObject jsonObject = JSONObject.parseObject(bseNewListing);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("statusCode") && jsonObject.getInteger("statusCode") == 200) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (!jsonArray.isEmpty()) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        // 股票表内是否存在
                        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                        lambdaQueryWrapper.eq(FaStrategy::getCode, jsonArray.getJSONObject(i).getString("code"));
                        lambdaQueryWrapper.eq(FaStrategy::getType, 2);
                        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
                        int count = (int) iFaStrategyService.count(lambdaQueryWrapper);
                        // 不存在，新增BSE股票
                        if (count == 0) {
                            FaStrategy faStrategy = new FaStrategy();
                            faStrategy.setTitle(jsonArray.getJSONObject(i).getString("co_name"));
                            faStrategy.setCode(jsonArray.getJSONObject(i).getString("code"));
//                            faStrategy.setAllCode(jsonArray.getJSONObject(i).getString("code"));
                            faStrategy.setType(2);
                            faStrategy.setCreateTime(new Date());

                            // 查询nse里面的allCode
                            LambdaQueryWrapper<FaStrategy> l2 = new LambdaQueryWrapper<>();
                            l2.eq(FaStrategy::getTitle, faStrategy.getTitle());
                            l2.eq(FaStrategy::getType, 1);
                            l2.eq(FaStrategy::getDeleteFlag, 0);
                            FaStrategy fa = iFaStrategyService.getOne(l2);
                            if (ObjectUtils.isNotEmpty(fa)) {
                                faStrategy.setAllCode(fa.getAllCode());
                            }

                            iFaStrategyService.insertFaStrategy(faStrategy);
                        }
                        // 更新新股表内状态
                        LambdaUpdateWrapper<FaNewStock> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                        lambdaUpdateWrapper.eq(FaNewStock::getSgCode, jsonArray.getJSONObject(i).getString("co_code"));
//                        lambdaUpdateWrapper.eq(FaNewStock::getSgType, 2);
                        lambdaUpdateWrapper.eq(FaNewStock::getIsList, 0);
                        lambdaUpdateWrapper.set(FaNewStock::getIsList, 1);
                        lambdaUpdateWrapper.set(FaNewStock::getUpdateTime, new Date());
                        lambdaUpdateWrapper.set(FaNewStock::getCode, jsonArray.getJSONObject(i).getString("code"));
                        iFaShengouService.update(lambdaUpdateWrapper);
                    }
                }
            }
        }
    }

    /**
     * 印度新股上市，申购配售转持仓
     * @throws Exception
     */
    @Override
    public void newStockToHold() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

        // 今天上市的新股
        LambdaQueryWrapper<FaNewStock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaNewStock::getIsList, 1);
        lambdaQueryWrapper.ge(FaNewStock::getSsDate, sdf.format(new Date()));
        lambdaQueryWrapper.ge(FaNewStock::getUpdateTime, sdf.format(new Date()));
        lambdaQueryWrapper.eq(FaNewStock::getDeleteFlag, 0);
        List<FaNewStock> newStockList = iFaShengouService.list(lambdaQueryWrapper);
        if (!newStockList.isEmpty()) {
            for (FaNewStock newStock : newStockList) {
                String code = "";
                if (StringUtils.isNotEmpty(newStock.getAllCode())) {
                    code = newStock.getAllCode();
                }
                if (StringUtils.isNotEmpty(newStock.getCode())) {
                    code = newStock.getCode();
                }
                if (StringUtils.isNotEmpty(code)) {
                    LambdaQueryWrapper<FaStrategy> strategyLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    strategyLambdaQueryWrapper.eq(FaStrategy::getCode, code);
                    strategyLambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
                    FaStrategy faStrategy = iFaStrategyService.getOne(strategyLambdaQueryWrapper);
                    // 申购配售转持仓
                    iFaStockHoldDetailService.addFromNewStock(newStock, faStrategy);
                }
            }
        }
    }

    /**
     * 批量刷新印度NIFTY 50 和 S&P BSE SENSEX 指数股票实时信息
     * @throws Exception
     */
    @Override
    public void updateStockInfoInMainIndex() throws Exception {
        // NSE NIFTY 50 指数股
        JSONArray jsonArray1 = this.getNseStockInfoByBlock("NIFTY 50", redisCache.getNseCookie());
        if (!jsonArray1.isEmpty()) {
            // 更新数据库
            this.updateStockInfoInNseBatch(jsonArray1);
        }

        // NSE NIFTY 50 指数股
        JSONArray jsonArray2 = this.getBseStockInfoByBlock("S&P BSE SENSEX");
        if (!jsonArray2.isEmpty()) {
            // 更新数据库
            this.updateStockInfoInBseBatch(jsonArray2);
        }
    }

    /**
     * 根据板块分类获取股票信息
     * @param block
     * @return
     * @throws Exception
     */
    public JSONArray getNseStockInfoByBlock(String block, String cookie) throws Exception {
        JSONArray jsonArray = new JSONArray();
        block = URLEncoder.encode(block);
        String result = HttpUtils.sendGetForNse("https://www.nseindia.com/api/equity-stockIndices?index=" + block, cookie);
        if (StringUtils.isNotEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("data")) {
                jsonArray = jsonObject.getJSONArray("data");
            }
        }
        return jsonArray;
    }

    /**
     * 更新NSE数据到数据库
     * @param jsonArray
     * @throws Exception
     */
    public List<FaStrategy> updateStockInfoInNseBatch(JSONArray jsonArray) throws Exception {
        List<FaStrategy> faStrategyList = new ArrayList<>();
        List<String> codeList = new ArrayList<>();
        if (null != jsonArray && !jsonArray.isEmpty()) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper;
                if (ObjectUtils.isNotEmpty(jsonObject)) {
                    lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaStrategy::getType, 1);
                    lambdaUpdateWrapper.eq(FaStrategy::getCode, jsonObject.getString("symbol"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiTrade, jsonObject.getString("lastPrice"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiPricechange, jsonObject.getString("change"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiChangepercent, jsonObject.getString("pChange"));
//                            lambdaUpdateWrapper.set(FaStrategy::getCaiBuy, info[9]);
//                            lambdaUpdateWrapper.set(FaStrategy::getCaiSell, info[19]);
                    lambdaUpdateWrapper.set(FaStrategy::getCaiSettlement, jsonObject.getString("previousClose"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiOpen, jsonObject.getString("open"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiHigh, jsonObject.getString("dayHigh"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiLow, jsonObject.getString("dayLow"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiVolume, jsonObject.getString("totalTradedVolume"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiAmount, jsonObject.getString("totalTradedValue"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiTicktime, changeNseDate(jsonObject.getString("lastUpdateTime")));
                    lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
                    this.update(lambdaUpdateWrapper);

                    if (i != 0 && i < 11) {
                        codeList.add(jsonObject.getString("symbol"));
                    }
                }
            }
        }
        if (!codeList.isEmpty()) {
            LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaStrategy::getType, 1);
            lambdaQueryWrapper.in(FaStrategy::getCode, codeList);
            faStrategyList = this.list(lambdaQueryWrapper);
        }
        return faStrategyList;
    }

    /**
     * 时间格式转换
     * @param lastUpdateTime
     * @return
     * @throws Exception
     */
    private String changeNseDate(String lastUpdateTime) throws Exception {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNotEmpty(lastUpdateTime)) {
            Date date = sdf1.parse(lastUpdateTime);
            return sdf2.format(date);
        }
        return sdf2.format(new Date());
    }

    /**
     * 根据板块分类获取股票信息
     * @param block
     * @return
     * @throws Exception
     */
    public JSONArray getBseStockInfoByBlock(String block) throws Exception {
        JSONArray jsonArray = new JSONArray();
        JSONArray array = new JSONArray();
        block = URLEncoder.encode(block);
        int pgN = 1;
        do {
            String result = HttpUtils.sendGetForBse("https://api.bseindia.com/BseIndiaAPI/api/GetStkCurrMain_new/w?flag=Equity&ddlVal1=Index&ddlVal2=" + block + "&m=0&pgN=" + pgN + "&srts=D&srtb=6");
            if (StringUtils.isNotEmpty(result)) {
                array = JSONArray.parseArray(result);
                jsonArray.addAll(array);
            }
            pgN ++;
        } while (!array.isEmpty());
        return jsonArray;
    }

    public List<FaStrategy> updateStockInfoInBseBatch(JSONArray jsonArray) throws Exception {
        List<FaStrategy> faStrategyList = new ArrayList<>();
        List<String> codeList = new ArrayList<>();
        if (null != jsonArray && !jsonArray.isEmpty()) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper;
                if (ObjectUtils.isNotEmpty(jsonObject)) {
                    lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaStrategy::getType, 2);
                    lambdaUpdateWrapper.eq(FaStrategy::getCode, jsonObject.getString("Symbol"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiTrade, jsonObject.getString("Price"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiPricechange, jsonObject.getString("Change"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiChangepercent, jsonObject.getString("PercentChange"));
//                            lambdaUpdateWrapper.set(FaStrategy::getCaiBuy, info[9]);
//                            lambdaUpdateWrapper.set(FaStrategy::getCaiSell, info[19]);
                    lambdaUpdateWrapper.set(FaStrategy::getCaiSettlement, jsonObject.getString("PreCloseRate"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiOpen, jsonObject.getString("Open"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiHigh, jsonObject.getString("High"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiLow, jsonObject.getString("Low"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiVolume, jsonObject.getString("Volume"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiAmount, jsonObject.getString("TurnOver"));
                    lambdaUpdateWrapper.set(FaStrategy::getCaiTicktime, changeBseDate(jsonObject.getString("LastTrdTime")));
                    lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
                    this.update(lambdaUpdateWrapper);

                    if (i < 10) {
                        codeList.add(jsonObject.getString("Symbol"));
                    }
                }
            }
        }
        if (!codeList.isEmpty()) {
            LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaStrategy::getType, 2);
            lambdaQueryWrapper.in(FaStrategy::getCode, codeList);
            faStrategyList = this.list(lambdaQueryWrapper);
        }
        return faStrategyList;
    }

    private String changeBseDate(String lastUpdateTime) throws Exception {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNotEmpty(lastUpdateTime)) {
            Date date = sdf1.parse(lastUpdateTime);
            return sdf2.format(date);
        }
        return sdf2.format(new Date());
    }

    /**
     * 刷新印度新闻
     * @throws Exception
     */
    @Override
    public void updateIndiaNews() throws Exception {
        // 新闻缩略列表
        Document doc = Jsoup.connect("https://www.moneycontrol.com/rss/latestnews.xml")
                //设置爬取超时时间
                .timeout(10000)
                //get请求
                .get();

        Elements newsList = doc.getElementsByTag("item");
        for(int i = 0; i < newsList.size(); i++) {
            // 新闻链接
            String link = newsList.get(i).getElementsByTag("link").text();
            Document newsContent = Jsoup.connect(link)
                    //设置爬取超时时间
                    .timeout(10000)
                    //get请求
                    .get();

            if (null != newsContent.getElementById("contentdata")) {
                FaNews faNews = new FaNews();
                faNews.setNewsTitle(newsList.get(i).getElementsByTag("title").text());
                faNews.setNewsTime(new Date(newsList.get(i).getElementsByTag("pubDate").text()));

                // 判断是否已存在
                LambdaQueryWrapper<FaNews> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(FaNews::getNewsTitle, faNews.getNewsTitle());
                int count = (int) iFaNewsService.count(lambdaQueryWrapper);
                if (count > 0) {
                    continue;
                }

                // 摘要
                if (null != newsContent.getElementsByClass("article_desc")) {
                    faNews.setNewsAbstract(newsContent.getElementsByClass("article_desc").text());
                }

                // 正文
                Element news = newsContent.getElementById("contentdata");
                if (null != news.getElementsByClass("social_icons_wrapper")) {
                    news.getElementsByClass("social_icons_wrapper").remove();
                }
                if (null != news.getElementsByClass("hide-moblie mid-arti-ad")) {
                    news.getElementsByClass("hide-moblie mid-arti-ad").remove();
                }
                if (null != news.getElementById("taboola-mid-article-thumbnails")) {
                    news.getElementById("taboola-mid-article-thumbnails").remove();
                }
                if (null != news.getElementsByTag("script")) {
                    news.getElementsByTag("script").remove();
                }
                if (null != news.getElementsByClass("related_stories_left_block")) {
                    news.getElementsByClass("related_stories_left_block").remove();
                }
                if (null != news.getElementsByClass("show-moblie mid-arti-ad")) {
                    news.getElementsByClass("show-moblie mid-arti-ad").remove();
                }

                String img = news.getElementsByClass("article_image").select("img").attr("data-src");
                news.getElementsByClass("article_image").select("img").attr("src", img);
                faNews.setNewsContent(String.valueOf(news));

                faNews.setCreateTime(new Date());
                iFaNewsService.save(faNews);
            }
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
     * 刷新IIFL股票数据
     * @throws Exception
     */
    @Override
    public void updateStockInfoFromIIFL() throws Exception {
        String token = redisCache.getIiflToken();

        // 更新三大指数
        Thread thread1 = new Thread(updateIndex(token));
        thread1.start();
        // 更新NSE板块
        Thread thread2 = new Thread(updateNse(token));
        thread2.start();
        // 更新BSE板块
        Thread thread3 = new Thread(updateBse(token));
        thread3.start();
    }

    public static TimerTask updateIndex(String token) {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // Nifty 50
                String nifty = HttpUtils.sendGetForIifl("https://api.indiainfoline.com/api/cmots/v1/markets/index-high-low/nse/20559/days", token);
                if (StringUtils.isNotEmpty(nifty)) {
                    JSONObject jsonObject = JSONObject.parseObject(nifty);
                    if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("statusCode") && jsonObject.getInteger("statusCode") == 200) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (!jsonArray.isEmpty()) {
                            try {
                                SpringUtils.getBean(IndiaStrategyServiceImpl.class).updateIndex(jsonArray.getJSONObject(0), 6957);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                // S&P BSE SENSEX
                String sensex = HttpUtils.sendGetForIifl("https://api.indiainfoline.com/api/cmots/v1/markets/index-high-low/bse/20558/days", token);
                if (StringUtils.isNotEmpty(sensex)) {
                    JSONObject jsonObject = JSONObject.parseObject(sensex);
                    if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("statusCode") && jsonObject.getInteger("statusCode") == 200) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (!jsonArray.isEmpty()) {
                            try {
                                SpringUtils.getBean(IndiaStrategyServiceImpl.class).updateIndex(jsonArray.getJSONObject(0), 6958);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                // S&P BSE LargeCap
                String largeCap = HttpUtils.sendGetForIifl("https://api.indiainfoline.com/api/cmots/v1/markets/index-high-low/bse/65961/days", token);
                if (StringUtils.isNotEmpty(largeCap)) {
                    JSONObject jsonObject = JSONObject.parseObject(largeCap);
                    if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("statusCode") && jsonObject.getInteger("statusCode") == 200) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (!jsonArray.isEmpty()) {
                            try {
                                SpringUtils.getBean(IndiaStrategyServiceImpl.class).updateIndex(jsonArray.getJSONObject(0), 6959);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        };
    }

    // 更新指数 6957:Nifty 50; 6958:S&P BSE SENSEX; 6959:S&P BSE LargeCap
    public void updateIndex(JSONObject jsonObject, int stockId) {
        LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaStrategy::getId, stockId);
        lambdaUpdateWrapper.set(FaStrategy::getCaiTrade, jsonObject.getBigDecimal("currprice"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiPricechange, jsonObject.getBigDecimal("pricediff"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiChangepercent, jsonObject.getBigDecimal("change"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiSettlement, jsonObject.getBigDecimal("prev_close"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiOpen, jsonObject.getBigDecimal("open"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiHigh, jsonObject.getBigDecimal("high"));
        lambdaUpdateWrapper.set(FaStrategy::getCaiLow, jsonObject.getBigDecimal("low"));
        lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
        iFaStrategyService.update(lambdaUpdateWrapper);
    }

    public static TimerTask updateNse(String token) {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // NSE 板块
                String block = HttpUtils.sendGetForIifl("https://api.indiainfoline.com/api/cmots/v1/markets/index-dropdown/nse", token);
                if (StringUtils.isNotEmpty(block)) {
                    JSONObject jsonObject = JSONObject.parseObject(block);
                    if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("statusCode") && jsonObject.getInteger("statusCode") == 200) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (!jsonArray.isEmpty()) {
                            for (int i = 0; i < jsonArray.size(); i++) {
                                String indexcode = jsonArray.getJSONObject(i).getString("indexcode");
                                if (StringUtils.isNotEmpty(indexcode)) {
                                    String result = HttpUtils.sendGetForIifl("https://api.indiainfoline.com/api/cmots/v1/markets/Index-Component-Prices/nse/" + indexcode, token);
                                    if (StringUtils.isNotEmpty(result)) {
                                        JSONObject stockData = JSONObject.parseObject(result);
                                        if (ObjectUtils.isNotEmpty(stockData) && stockData.containsKey("statusCode") && stockData.getInteger("statusCode") == 200) {
                                            JSONArray stockInfoList = stockData.getJSONArray("data");
                                            try {
                                                SpringUtils.getBean(IndiaStrategyServiceImpl.class).updateStock(stockInfoList, 1);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    public static TimerTask updateBse(String token) {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // BSE 板块
                String block = HttpUtils.sendGetForIifl("https://api.indiainfoline.com/api/cmots/v1/markets/index-dropdown/bse", token);
                if (StringUtils.isNotEmpty(block)) {
                    JSONObject jsonObject = JSONObject.parseObject(block);
                    if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("statusCode") && jsonObject.getInteger("statusCode") == 200) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (!jsonArray.isEmpty()) {
                            for (int i = 0; i < jsonArray.size(); i++) {
                                String indexcode = jsonArray.getJSONObject(i).getString("indexcode");
                                if (StringUtils.isNotEmpty(indexcode)) {
                                    String result = HttpUtils.sendGetForIifl("https://api.indiainfoline.com/api/cmots/v1/markets/Index-Component-Prices/bse/" + indexcode, token);
                                    if (StringUtils.isNotEmpty(result)) {
                                        JSONObject stockData = JSONObject.parseObject(result);
                                        if (ObjectUtils.isNotEmpty(stockData) && stockData.containsKey("statusCode") && stockData.getInteger("statusCode") == 200) {
                                            JSONArray stockInfoList = stockData.getJSONArray("data");
                                            try {
                                                SpringUtils.getBean(IndiaStrategyServiceImpl.class).updateStock(stockInfoList, 2);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    public void updateStock(JSONArray jsonArray, int exchangeType) throws Exception {
        if (!jsonArray.isEmpty()) {
            for (int i = 0; i < jsonArray.size(); i++) {
                LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaStrategy::getAllCode, jsonArray.getJSONObject(i).getString("symbol"));
                lambdaUpdateWrapper.eq(FaStrategy::getType, exchangeType);
                lambdaUpdateWrapper.set(FaStrategy::getCaiTrade, jsonArray.getJSONObject(i).getBigDecimal("ltp"));
                lambdaUpdateWrapper.set(FaStrategy::getCaiPricechange, jsonArray.getJSONObject(i).getBigDecimal("dayopen").subtract(jsonArray.getJSONObject(i).getBigDecimal("ltp")));
                lambdaUpdateWrapper.set(FaStrategy::getCaiChangepercent, jsonArray.getJSONObject(i).getBigDecimal("perchange"));
//                lambdaUpdateWrapper.set(FaStrategy::getCaiSettlement, jsonObject.getBigDecimal("prev_close"));
                lambdaUpdateWrapper.set(FaStrategy::getCaiOpen, jsonArray.getJSONObject(i).getBigDecimal("dayopen"));
                lambdaUpdateWrapper.set(FaStrategy::getCaiHigh, jsonArray.getJSONObject(i).getBigDecimal("dayhigh"));
                lambdaUpdateWrapper.set(FaStrategy::getCaiLow, jsonArray.getJSONObject(i).getBigDecimal("daylow"));
                lambdaUpdateWrapper.set(FaStrategy::getCaiVolume, jsonArray.getJSONObject(i).getBigDecimal("volume"));
                lambdaUpdateWrapper.set(FaStrategy::getCaiAmount, jsonArray.getJSONObject(i).getBigDecimal("tradedvalue"));
                lambdaUpdateWrapper.set(FaStrategy::getCaiTicktime, changeDate(jsonArray.getJSONObject(i).getString("tradedate")));
                lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
                iFaStrategyService.update(lambdaUpdateWrapper);
            }
        }
    }

}