package com.ruoyi.biz.strategy.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.shengou.domain.FaNewStock;
import com.ruoyi.biz.shengou.service.IFaShengouService;
import com.ruoyi.biz.stockHoldDetail.service.IFaStockHoldDetailService;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;
import com.ruoyi.biz.stockTrading.service.IFaStockTradingService;
import com.ruoyi.biz.strategy.domain.FaStrategy;
import com.ruoyi.biz.strategy.mapper.FaStrategyMapper;
import com.ruoyi.biz.strategy.service.ChinaStrategyService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.common.utils.stockUtils.StockUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * 策略Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-06
 */
@Service
public class ChinaStrategyServiceImpl extends ServiceImpl<FaStrategyMapper, FaStrategy> implements ChinaStrategyService
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
    private IFaShengouService iFaShengouService;

    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd 09:10:00");

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
     * 刷新大宗价格
     * @throws Exception
     */
    @Override
    public void updateDzPrice() throws Exception {
        faStrategyMapper.updateDzPrice();
    }

    /**
     * 刷新A股股票
     * @throws Exception
     */
    @Override
    public void updateChinaStock() throws Exception {
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

                    Thread thread = new Thread(getChinaStock(codes));
                    thread.start();
                }
            }
        }
    }

    /**
     * 刷新持仓股票价格
     * @throws Exception
     */
    @Override
    public void updateHoldingStock() throws Exception {
        // 取出持仓股票allCode集合
        String[] codeList = faStrategyMapper.getHoldingStockCodeList();
        if (codeList.length > 0) {
            String codes = Arrays.toString(codeList);
            codes = codes.substring(1);
            codes = codes.substring(0, codes.length() - 1);
            codes = codes.replace(", ", ",");

            Thread thread = new Thread(getChinaStock(codes));
            thread.start();
        }
    }

    /**
     * 获取股票实时信息
     * @param codes
     * @return
     */
    public static TimerTask getChinaStock(String codes) {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 数据集合
                String result = StockUtils.getCurrentInfo(codes);
                String[] infos = result.split(";");
                for (String info : infos) {
                    String[] detailInfo = info.split("~");
                    if (detailInfo.length > 4) {
                        // 更新实时信息到数据库
                        try {
                            SpringUtils.getBean(ChinaStrategyServiceImpl.class).updateChinaStock(detailInfo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
    }

    /**
     * 更新实时信息到数据库
     * @param info
     * @throws Exception
     */
    private void updateChinaStock(String[] info) throws Exception {
        if (info.length > 37) {
            LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();

            String allCode = info[0].substring(info[0].indexOf("_") + 1, info[0].indexOf("="));

            lambdaUpdateWrapper.eq(FaStrategy::getCode, info[2]);
            lambdaUpdateWrapper.eq(FaStrategy::getAllCode, allCode);

            // 更新股票名称
            if (StringUtils.isNotEmpty(info[1])) {
                lambdaUpdateWrapper.set(FaStrategy::getTitle, info[1].trim());
            }
            // 采集-当前价格
            if (StringUtils.isNotEmpty(info[3])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiTrade, info[3]);
            }
            // 采集-涨跌
            if (StringUtils.isNotEmpty(info[31])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiPricechange, info[31]);
            }
            // 采集-涨跌幅
            if (StringUtils.isNotEmpty(info[32])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiChangepercent, info[32]);
            }
            // 采集-买入价
            if (StringUtils.isNotEmpty(info[9])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiBuy, info[9]);
            }
            // 采集-卖出价
            if (StringUtils.isNotEmpty(info[19])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiSell, info[19]);
            }
            // 采集-昨收价
            if (StringUtils.isNotEmpty(info[4])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiSettlement, info[4]);
            }
            // 采集-今开价
            if (StringUtils.isNotEmpty(info[5])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiOpen, info[5]);
            }
            // 采集-最高价
            if (StringUtils.isNotEmpty(info[33])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiHigh, info[33]);
            }
            // 采集-最低价
            if (StringUtils.isNotEmpty(info[34])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiLow, info[34]);
            }
            // 采集-成交量(手)
            if (StringUtils.isNotEmpty(info[36])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiVolume, info[36]);
            }
            // 采集-成交量(手)
            if (StringUtils.isNotEmpty(info[37])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiAmount, info[37]);
            }
            // 采集-换手率
            if (StringUtils.isNotEmpty(info[38])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiChangeHands, info[38]);
            }
            // 采集-均价
            if (StringUtils.isNotEmpty(info[51])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiAverage, info[51]);
            }
            // 采集-涨停价
            if (StringUtils.isNotEmpty(info[47])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiLimitUpPrice, info[47]);
            }
            // 采集-跌停价
            if (StringUtils.isNotEmpty(info[48])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiLimitDownPrice, info[48]);
            }
            // 采集-数据时间
            if (StringUtils.isNotEmpty(info[30])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiTicktime, getDate(info[30]));
            }

            // 买5卖5
            BigDecimal buy1 = BigDecimal.ZERO;
            BigDecimal buy1Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[9])) {
                buy1 = new BigDecimal(info[9]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy1, buy1);
            }
            if (StringUtils.isNotEmpty(info[10])) {
                buy1Num = new BigDecimal(info[10]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy1Num, buy1Num);
            }
            BigDecimal buy2 = BigDecimal.ZERO;
            BigDecimal buy2Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[11])) {
                buy2 = new BigDecimal(info[11]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy2, buy2);
            }
            if (StringUtils.isNotEmpty(info[12])) {
                buy2Num = new BigDecimal(info[12]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy2Num, buy2Num);
            }
            BigDecimal buy3 = BigDecimal.ZERO;
            BigDecimal buy3Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[13])) {
                buy3 = new BigDecimal(info[13]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy3, buy3);
            }
            if (StringUtils.isNotEmpty(info[14])) {
                buy3Num = new BigDecimal(info[14]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy3Num, buy3Num);
            }
            BigDecimal buy4 = BigDecimal.ZERO;
            BigDecimal buy4Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[15])) {
                buy4 = new BigDecimal(info[15]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy4, buy4);
            }
            if (StringUtils.isNotEmpty(info[16])) {
                buy4Num = new BigDecimal(info[16]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy4Num, buy4Num);
            }
            BigDecimal buy5 = BigDecimal.ZERO;
            BigDecimal buy5Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[17])) {
                buy5 = new BigDecimal(info[17]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy5, buy5);
            }
            if (StringUtils.isNotEmpty(info[18])) {
                buy5Num = new BigDecimal(info[18]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy5Num, buy5Num);
            }
            lambdaUpdateWrapper.set(FaStrategy::getBuyTotalNum, buy1Num.add(buy2Num).add(buy3Num).add(buy4Num).add(buy5Num));

            BigDecimal sell1 = BigDecimal.ZERO;
            BigDecimal sell1Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[19])) {
                sell1 = new BigDecimal(info[19]);
                lambdaUpdateWrapper.set(FaStrategy::getSell1, sell1);
            }
            if (StringUtils.isNotEmpty(info[20])) {
                sell1Num = new BigDecimal(info[20]);
                lambdaUpdateWrapper.set(FaStrategy::getSell1Num, sell1Num);
            }
            BigDecimal sell2 = BigDecimal.ZERO;
            BigDecimal sell2Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[21])) {
                sell2 = new BigDecimal(info[21]);
                lambdaUpdateWrapper.set(FaStrategy::getSell2, sell2);
            }
            if (StringUtils.isNotEmpty(info[22])) {
                sell2Num = new BigDecimal(info[22]);
                lambdaUpdateWrapper.set(FaStrategy::getSell2Num, sell2Num);
            }
            BigDecimal sell3 = BigDecimal.ZERO;
            BigDecimal sell3Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[23])) {
                sell3 = new BigDecimal(info[23]);
                lambdaUpdateWrapper.set(FaStrategy::getSell3, sell3);
            }
            if (StringUtils.isNotEmpty(info[24])) {
                sell3Num = new BigDecimal(info[24]);
                lambdaUpdateWrapper.set(FaStrategy::getSell3Num, sell3Num);
            }
            BigDecimal sell4 = BigDecimal.ZERO;
            BigDecimal sell4Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[25])) {
                sell4 = new BigDecimal(info[25]);
                lambdaUpdateWrapper.set(FaStrategy::getSell4, sell4);
            }
            if (StringUtils.isNotEmpty(info[26])) {
                sell4Num = new BigDecimal(info[26]);
                lambdaUpdateWrapper.set(FaStrategy::getSell4Num, sell4Num);
            }
            BigDecimal sell5 = BigDecimal.ZERO;
            BigDecimal sell5Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[27])) {
                sell5 = new BigDecimal(info[27]);
                lambdaUpdateWrapper.set(FaStrategy::getSell5, sell5);
            }
            if (StringUtils.isNotEmpty(info[28])) {
                sell5Num = new BigDecimal(info[28]);
                lambdaUpdateWrapper.set(FaStrategy::getSell5Num, sell5Num);
            }
            lambdaUpdateWrapper.set(FaStrategy::getSellTotalNum, sell1Num.add(sell2Num).add(sell3Num).add(sell4Num).add(sell5Num));

            lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
            this.update(lambdaUpdateWrapper);
        }
    }

    /**
     * 时间格式转换
     * @param dateStr
     * @return
     */
    private Date getDate(String dateStr) throws Exception {
        if (StringUtils.isNotEmpty(dateStr) && dateStr.length() == 14) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
            return sdf1.parse(dateStr);
        }
        return new Date();
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
        String currentInfo = StockUtils.getCurrentInfo(faStrategy.getAllCode());
        if (StringUtils.isNotEmpty(currentInfo)) {
            String[] info = currentInfo.split("~");
            if (info.length > 4) {
                currentPrice = new BigDecimal(info[3]);
                // 更新实时信息到数据库
                try {
                    updateCurrentStockInfo(info, faStrategy);
                } catch (Exception e) {
                    // 更新出问题，取数据库价格
                    currentPrice = faStrategyMapper.getStockCurrentPrice(faStrategy);
                }
            }
        }
        return currentPrice;
    }

    /**
     * 更新实时信息到数据库
     * @param info
     * @throws Exception
     */
    private void updateCurrentStockInfo(String[] info, FaStrategy faStrategy) throws Exception {
        if (info.length > 37) {
            LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(FaStrategy::getId, faStrategy.getId());

            // 采集-当前价格
            if (StringUtils.isNotEmpty(info[3])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiTrade, info[3]);
            }
            // 采集-涨跌
            if (StringUtils.isNotEmpty(info[31])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiPricechange, info[31]);
            }
            // 采集-涨跌幅
            if (StringUtils.isNotEmpty(info[32])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiChangepercent, info[32]);
            }
            // 采集-买入价
            if (StringUtils.isNotEmpty(info[9])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiBuy, info[9]);
            }
            // 采集-卖出价
            if (StringUtils.isNotEmpty(info[19])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiSell, info[19]);
            }
            // 采集-昨收价
            if (StringUtils.isNotEmpty(info[4])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiSettlement, info[4]);
            }
            // 采集-今开价
            if (StringUtils.isNotEmpty(info[5])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiOpen, info[5]);
            }
            // 采集-最高价
            if (StringUtils.isNotEmpty(info[33])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiHigh, info[33]);
            }
            // 采集-最低价
            if (StringUtils.isNotEmpty(info[34])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiLow, info[34]);
            }
            // 采集-成交量(手)
            if (StringUtils.isNotEmpty(info[36])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiVolume, info[36]);
            }
            // 采集-成交量(手)
            if (StringUtils.isNotEmpty(info[37])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiAmount, info[37]);
            }
            // 采集-换手率
            if (StringUtils.isNotEmpty(info[38])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiChangeHands, info[38]);
            }
            // 采集-均价
            if (StringUtils.isNotEmpty(info[51])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiAverage, info[51]);
            }
            // 采集-涨停价
            if (StringUtils.isNotEmpty(info[47])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiLimitUpPrice, info[47]);
            }
            // 采集-跌停价
            if (StringUtils.isNotEmpty(info[48])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiLimitDownPrice, info[48]);
            }
            // 采集-数据时间
            if (StringUtils.isNotEmpty(info[30])) {
                lambdaUpdateWrapper.set(FaStrategy::getCaiTicktime, getDate(info[30]));
            }

            // 买5卖5
            BigDecimal buy1 = BigDecimal.ZERO;
            BigDecimal buy1Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[9])) {
                buy1 = new BigDecimal(info[9]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy1, buy1);
            }
            if (StringUtils.isNotEmpty(info[10])) {
                buy1Num = new BigDecimal(info[10]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy1Num, buy1Num);
            }
            BigDecimal buy2 = BigDecimal.ZERO;
            BigDecimal buy2Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[11])) {
                buy2 = new BigDecimal(info[11]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy2, buy2);
            }
            if (StringUtils.isNotEmpty(info[12])) {
                buy2Num = new BigDecimal(info[12]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy2Num, buy2Num);
            }
            BigDecimal buy3 = BigDecimal.ZERO;
            BigDecimal buy3Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[13])) {
                buy3 = new BigDecimal(info[13]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy3, buy3);
            }
            if (StringUtils.isNotEmpty(info[14])) {
                buy3Num = new BigDecimal(info[14]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy3Num, buy3Num);
            }
            BigDecimal buy4 = BigDecimal.ZERO;
            BigDecimal buy4Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[15])) {
                buy4 = new BigDecimal(info[15]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy4, buy4);
            }
            if (StringUtils.isNotEmpty(info[16])) {
                buy4Num = new BigDecimal(info[16]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy4Num, buy4Num);
            }
            BigDecimal buy5 = BigDecimal.ZERO;
            BigDecimal buy5Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[17])) {
                buy5 = new BigDecimal(info[17]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy5, buy5);
            }
            if (StringUtils.isNotEmpty(info[18])) {
                buy5Num = new BigDecimal(info[18]);
                lambdaUpdateWrapper.set(FaStrategy::getBuy5Num, buy5Num);
            }
            lambdaUpdateWrapper.set(FaStrategy::getBuyTotalNum, buy1Num.add(buy2Num).add(buy3Num).add(buy4Num).add(buy5Num));

            BigDecimal sell1 = BigDecimal.ZERO;
            BigDecimal sell1Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[19])) {
                sell1 = new BigDecimal(info[19]);
                lambdaUpdateWrapper.set(FaStrategy::getSell1, sell1);
            }
            if (StringUtils.isNotEmpty(info[20])) {
                sell1Num = new BigDecimal(info[20]);
                lambdaUpdateWrapper.set(FaStrategy::getSell1Num, sell1Num);
            }
            BigDecimal sell2 = BigDecimal.ZERO;
            BigDecimal sell2Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[21])) {
                sell2 = new BigDecimal(info[21]);
                lambdaUpdateWrapper.set(FaStrategy::getSell2, sell2);
            }
            if (StringUtils.isNotEmpty(info[22])) {
                sell2Num = new BigDecimal(info[22]);
                lambdaUpdateWrapper.set(FaStrategy::getSell2Num, sell2Num);
            }
            BigDecimal sell3 = BigDecimal.ZERO;
            BigDecimal sell3Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[23])) {
                sell3 = new BigDecimal(info[23]);
                lambdaUpdateWrapper.set(FaStrategy::getSell3, sell3);
            }
            if (StringUtils.isNotEmpty(info[24])) {
                sell3Num = new BigDecimal(info[24]);
                lambdaUpdateWrapper.set(FaStrategy::getSell3Num, sell3Num);
            }
            BigDecimal sell4 = BigDecimal.ZERO;
            BigDecimal sell4Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[25])) {
                sell4 = new BigDecimal(info[25]);
                lambdaUpdateWrapper.set(FaStrategy::getSell4, sell4);
            }
            if (StringUtils.isNotEmpty(info[26])) {
                sell4Num = new BigDecimal(info[26]);
                lambdaUpdateWrapper.set(FaStrategy::getSell4Num, sell4Num);
            }
            BigDecimal sell5 = BigDecimal.ZERO;
            BigDecimal sell5Num = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(info[27])) {
                sell5 = new BigDecimal(info[27]);
                lambdaUpdateWrapper.set(FaStrategy::getSell5, sell5);
            }
            if (StringUtils.isNotEmpty(info[28])) {
                sell5Num = new BigDecimal(info[28]);
                lambdaUpdateWrapper.set(FaStrategy::getSell5Num, sell5Num);
            }
            lambdaUpdateWrapper.set(FaStrategy::getSellTotalNum, sell1Num.add(sell2Num).add(sell3Num).add(sell4Num).add(sell5Num));

            lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());

            // 抢筹未开，更新抢筹价格
            if (0 == faStrategy.getVipQcStatus() && StringUtils.isNotEmpty(info[3])) {
                lambdaUpdateWrapper.set(FaStrategy::getVipQcPrice, info[3]);
            }

            this.update(lambdaUpdateWrapper);
        }
    }

    /**
     * 刷新新股
     * @throws Exception
     */
    @Override
    public void updateNewStock() throws Exception {
        Document doc = Jsoup.connect("http://data.10jqka.com.cn/ipo/xgsgyzq/")
                //设置爬取超时时间
                .timeout(10000)
                //get请求
                .get();

        Elements tbody = doc.getElementsByClass("m_tbd");
        Elements rows = tbody.select("tr");
        List<FaNewStock> list = new ArrayList<>();
        FaNewStock faNewStock;
        for(int i = 0; i < rows.size(); i++) {
            faNewStock = new FaNewStock();
            Element tr = rows.get(i);//获取表头
            Elements tds = tr.select("td");

            faNewStock.setCode(dataFormat(tds.get(0).text()));
            faNewStock.setName(dataFormat(tds.get(1).text()));
            faNewStock.setSgCode(dataFormat(tds.get(2).text()));
            faNewStock.setFxNum(dataFormat(tds.get(3).text()));
            faNewStock.setWsfxNum(dataFormat(tds.get(4).text()));
            faNewStock.setSgLimit(dataFormat(tds.get(5).text()));
            faNewStock.setDgLimit(dataFormat(tds.get(6).text()));
            faNewStock.setFxPrice(dataFormat(tds.get(7).text()));
            faNewStock.setFxRate(dataFormat(tds.get(8).text()));
            faNewStock.setHyRate(dataFormat(tds.get(9).text()));
            // 申购日期
            faNewStock.setSgDate(dateFormat(tds.get(10).text()));
            faNewStock.setZqRate(dataFormat(tds.get(11).text()));
            // 中签缴款日期
            faNewStock.setZqJkDate(dateFormat(faNewStock.getSgDate(), tds.get(13).text()));
            // 上市日期
            faNewStock.setSsDate(dateFormat(faNewStock.getSgDate(), tds.get(14).text()));
            faNewStock.setSgNums(1000000);
            faNewStock.setXxNums(1000000);
            // 发行规模/发行数量
            faNewStock.setIssueSize(dataFormat(tds.get(3).text()));
            // 发行数量/网上发行数量
            faNewStock.setNoshIssued(dataFormat(tds.get(4).text()));
            // 刷新新股时 根据股票代码判断所属市场
            faNewStock.setSgType(typeFormat(tds.get(0).text()));
            // 根据类型获取allCode
            faNewStock.setAllCode(getAllCode(faNewStock));
            list.add(faNewStock);
        }
        updateNewStock(list);
    }

    /**
     * 通过code获取allCode
     * @param faNewStock
     * @return
     * @throws Exception
     */
    private String getAllCode(FaNewStock faNewStock) throws Exception {
        if (1 == faNewStock.getSgType()) {
            return "sh" + faNewStock.getCode();
        } else if (2 == faNewStock.getSgType()) {
            return "sz" + faNewStock.getCode();
        } else if (3 == faNewStock.getSgType()) {
            return "sz" + faNewStock.getCode();
        } else if (4 == faNewStock.getSgType()) {
            return "bj" + faNewStock.getCode();
        } else if (5 == faNewStock.getSgType()) {
            return "sh" + faNewStock.getCode();
        }
        return faNewStock.getCode();
    }

    /**
     * 更新新股
     * @param list
     * @throws Exception
     */
    private void updateNewStock(List<FaNewStock> list) throws Exception {
        if (!list.isEmpty()) {
            // 倒序
            Collections.reverse(list);
            for (FaNewStock faNewStock : list) {
                LambdaQueryWrapper<FaNewStock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(FaNewStock::getCode, faNewStock.getCode());
                FaNewStock check = iFaShengouService.getOne(lambdaQueryWrapper);
                if (ObjectUtils.isEmpty(check)) {
                    faNewStock.setCreateTime(new Date());
                    iFaShengouService.save(faNewStock);
                } else {
                    if (0 == check.getIsList()) {
                        LambdaUpdateWrapper<FaNewStock> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                        lambdaUpdateWrapper.eq(FaNewStock::getId, check.getId());
                        lambdaUpdateWrapper.set(FaNewStock::getUpdateTime, new Date());
                        if (StringUtils.isEmpty(check.getCode())) {
                            lambdaUpdateWrapper.set(FaNewStock::getCode, faNewStock.getCode());
                        }
                        if (StringUtils.isEmpty(check.getAllCode())) {
                            lambdaUpdateWrapper.set(FaNewStock::getAllCode, faNewStock.getAllCode());
                        }
                        // 刷新新股时 根据股票代码判断所属市场
                        if (null == check.getSgType()) {
                            lambdaUpdateWrapper.set(FaNewStock::getSgType, faNewStock.getSgType());
                        }
//                        if (StringUtils.isEmpty(check.getName())) {
                            lambdaUpdateWrapper.set(FaNewStock::getName, faNewStock.getName());
//                        }
                        if (StringUtils.isEmpty(check.getSgCode())) {
                            lambdaUpdateWrapper.set(FaNewStock::getSgCode, faNewStock.getSgCode());
                        }
                        if (StringUtils.isEmpty(check.getFxNum())) {
                            lambdaUpdateWrapper.set(FaNewStock::getFxNum, faNewStock.getFxNum());
                        }
                        if (StringUtils.isEmpty(check.getWsfxNum())) {
                            lambdaUpdateWrapper.set(FaNewStock::getWsfxNum, faNewStock.getWsfxNum());
                        }
                        if (StringUtils.isEmpty(check.getSgLimit())) {
                            lambdaUpdateWrapper.set(FaNewStock::getSgLimit, faNewStock.getSgLimit());
                        }
                        if (StringUtils.isEmpty(check.getDgLimit())) {
                            lambdaUpdateWrapper.set(FaNewStock::getDgLimit, faNewStock.getDgLimit());
                        }
                        if (StringUtils.isEmpty(check.getFxPrice())) {
                            lambdaUpdateWrapper.set(FaNewStock::getFxPrice, faNewStock.getFxPrice());
                        }
                        if (StringUtils.isEmpty(check.getFxRate())) {
                            lambdaUpdateWrapper.set(FaNewStock::getFxRate, faNewStock.getFxRate());
                        }
                        if (StringUtils.isEmpty(check.getHyRate())) {
                            lambdaUpdateWrapper.set(FaNewStock::getHyRate, faNewStock.getHyRate());
                        }
                        // 申购日期
                        if (null == check.getSgDate()) {
                            lambdaUpdateWrapper.set(FaNewStock::getSgDate, faNewStock.getSgDate());
                        }
                        if (StringUtils.isEmpty(check.getZqRate())) {
                            lambdaUpdateWrapper.set(FaNewStock::getZqRate, faNewStock.getZqRate());
                        }
                        // 中签缴款日期
                        if (null == check.getZqJkDate()) {
                            lambdaUpdateWrapper.set(FaNewStock::getZqJkDate, faNewStock.getZqJkDate());
                        }
                        // 上市日期
                        if (null == check.getSsDate()) {
                            lambdaUpdateWrapper.set(FaNewStock::getSsDate, faNewStock.getSsDate());
                        }
                        if (null == check.getSgNums()) {
                            lambdaUpdateWrapper.set(FaNewStock::getSgNums, faNewStock.getSgNums());
                        }
                        if (null == check.getXxNums()) {
                            lambdaUpdateWrapper.set(FaNewStock::getXxNums, faNewStock.getXxNums());
                        }
                        // 发行规模/发行数量
                        if (StringUtils.isEmpty(check.getIssueSize())) {
                            lambdaUpdateWrapper.set(FaNewStock::getIssueSize, faNewStock.getIssueSize());
                        }
                        // 发行数量/网上发行数量
                        if (StringUtils.isEmpty(check.getNoshIssued())) {
                            lambdaUpdateWrapper.set(FaNewStock::getNoshIssued, faNewStock.getNoshIssued());
                        }
                        iFaShengouService.update(lambdaUpdateWrapper);
                    }
                }
            }
        }
    }

    /**
     * 数据格式
     * @param text
     * @return
     * @throws Exception
     */
    private String dataFormat(String text) throws Exception {
        if ("-".equals(text)) {
            return null;
        }
        return text;
    }

    /**
     * 日期格式
     * @param text
     * @return
     */
    private Date dateFormat(String text) throws Exception {
        if ("-".equals(text)) {
            return null;
        }
        LocalDate localDate = LocalDate.now();
        String year = String.valueOf(localDate.getYear());
        text = text.split(" ")[0];
        if (text.length() == 5) {
            text = year + "-" + text;
        }
        Date date = sdf1.parse(text);
        return date;
    }

    /**
     * 日期格式
     * @param sgDate
     * @param text
     * @return
     * @throws Exception
     */
    private Date dateFormat(Date sgDate, String text) throws Exception {
        if ("-".equals(text)) {
            return null;
        }
        Date date = dateFormat(text);
        // 如果相差大于1年，时间减1年
        if (date.getTime() - sgDate.getTime() > 365 * 24 * 60 * 60 * 1000) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, -1);
            date = calendar.getTime();
        }
        return date;
    }

    /**
     * 判断股票所属市场
     * @param text
     * @return
     * @throws Exception
     */
    private Integer typeFormat(String text) throws Exception {
        // 沪 60
        if (text.matches("60.*")) {
            return 1;
        }
        // 深 00
        if (text.matches("00.*")) {
            return 2;
        }
        // 创业板 30
        if (text.matches("30.*")) {
            return 3;
        }
        // 北交所 82、83、87、88、92
        if (text.matches("82.*") || text.matches("83.*") || text.matches("87.*") || text.matches("88.*") || text.matches("92.*")) {
            return 4;
        }
        // 科创版 688
        if (text.matches("688.*")) {
            return 5;
        }
        return 0;
    }

    /**
     * 检测新股上市
     * @throws Exception
     */
    @Override
    public void checkNewStockList() throws Exception {
        // 今天
        String today = sdf1.format(new Date());
        Date now = new Date();
        // 取出未上市的股票
        LambdaQueryWrapper<FaNewStock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaNewStock::getIsList, 0);
        List<FaNewStock> list = iFaShengouService.list(lambdaQueryWrapper);
        if (!list.isEmpty()) {
            for (FaNewStock faNewStock : list) {
                // 判断上市时间是否今天
                if (null != faNewStock.getSsDate() && today.equals(sdf1.format(faNewStock.getSsDate()))) {
                    // 转入股票列表
                    FaStrategy faStrategy = addFromNewStock(faNewStock);
                    // 申购配售转持仓
//                    iFaStockHoldDetailService.addFromNewStock(faNewStock, faStrategy);
                    // 更新新股状态
                    faNewStock.setIsList(1);
                    faNewStock.setUpdateTime(new Date());
                    iFaShengouService.updateById(faNewStock);
                }
                // 今天之前的上市股票
                else if (null != faNewStock.getSsDate() && faNewStock.getSsDate().before(new Date())) {
                    // 转入股票列表
                    FaStrategy faStrategy = addFromNewStock(faNewStock);
                    // 更新新股状态
                    faNewStock.setIsList(1);
                    faNewStock.setUpdateTime(new Date());
                    iFaShengouService.updateById(faNewStock);
                }

                // 申购日期是今天且时间超过9点10
                if (null != faNewStock.getSgDate() && today.equals(sdf1.format(faNewStock.getSgDate())) && now.after(sdf2.parse(sdf3.format(new Date())))) {
                    LambdaUpdateWrapper<FaNewStock> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaNewStock::getId, faNewStock.getId());
                    lambdaUpdateWrapper.isNull(FaNewStock::getSgSwitchTime);
                    lambdaUpdateWrapper.set(FaNewStock::getSgSwitch, 1);
                    lambdaUpdateWrapper.set(FaNewStock::getSgSwitchTime, new Date());
                    iFaShengouService.update(lambdaUpdateWrapper);
                }
            }
        }
    }

    /**
     * 新股转入股票列表
     * @param faNewStock
     * @return
     * @throws Exception
     */
    private FaStrategy addFromNewStock(FaNewStock faNewStock) throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStrategy::getCode, faNewStock.getCode());
        FaStrategy faStrategy = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faStrategy)) {
            faStrategy = new FaStrategy();
            faStrategy.setCode(faNewStock.getCode());
            faStrategy.setAllCode(faNewStock.getAllCode());
            faStrategy.setTitle(faNewStock.getName());
            faStrategy.setType(faNewStock.getSgType());
            faStrategy.setDeleteFlag(0);
            faStrategy.setCreateTime(DateUtils.getNowDate());
            this.save(faStrategy);
        }
        return faStrategy;
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
     * 上证指数k线
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, String>> getSHKline(FaStrategy faStrategy) throws Exception {
        faStrategy.setAllCode("sh000001");

        List<Map<String, String>> list = new ArrayList<>();
        String result = HttpUtils.sendGet("https://web.ifzq.gtimg.cn/appstock/app/minute/query?_var=min_data_" + faStrategy.getAllCode() + "&code=" + faStrategy.getAllCode() + "&r=" + System.currentTimeMillis());
        if (StringUtils.isNotEmpty(result)) {
            result = result.substring(result.indexOf("=") + 1);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.containsKey("data")) {
                jsonObject = jsonObject.getJSONObject("data");
                if (jsonObject.containsKey(faStrategy.getAllCode())) {
                    jsonObject = jsonObject.getJSONObject(faStrategy.getAllCode());
                    if (jsonObject.containsKey("data")) {
                        jsonObject = jsonObject.getJSONObject("data");
                        if (jsonObject.containsKey("data")) {
                            String date = jsonObject.getString("date");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (!jsonArray.isEmpty()) {
                                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd HHmmss");
                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    String info = jsonArray.getString(i);
                                    String[] infos = info.split(" ");
                                    Map<String, String> map = new HashMap<>();
                                    map.put("close", infos[1]);
                                    map.put("datetime", sdf2.format(sdf1.parse(date + " " + infos[0] + "00")));
                                    map.put("high", infos[1]);
                                    map.put("low", infos[1]);
                                    map.put("open", infos[1]);
                                    map.put("timestamp", sdf2.format(sdf1.parse(date + " " + infos[0] + "00")));
                                    map.put("volume", infos[2]);
                                    list.add(map);
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * 刷新选定股票价格
     * @throws Exception
     */
    @Override
    public void updateChooseStock() throws Exception {
        // 取出指定股票allCode集合
        String[] codeList = faStrategyMapper.getChooseStockCodeList();
        if (codeList.length > 0) {
            String codes = Arrays.toString(codeList);
            codes = codes.substring(1);
            codes = codes.substring(0, codes.length() - 1);
            codes = codes.replace(", ", ",");

            Thread thread = new Thread(getChinaStock(codes));
            thread.start();
        }
    }

}