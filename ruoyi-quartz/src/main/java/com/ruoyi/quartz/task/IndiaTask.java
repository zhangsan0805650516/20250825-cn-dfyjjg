package com.ruoyi.quartz.task;

import com.ruoyi.biz.news.service.impl.FaNewsServiceImpl;
import com.ruoyi.biz.strategy.service.IndiaStrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Component("indiaTask")
public class IndiaTask
{

    private static final Logger log = LoggerFactory.getLogger(IndiaTask.class);
    
    @Autowired
    private IndiaStrategyService indiaStrategyService;

    // T+1冻结资金清零
    public void clearT1Amount()
    {
        log.info("T+1冻结资金清零--开始");
        try {
            indiaStrategyService.clearT1Amount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("T+1冻结资金清零--结束");
    }

    // T+1冻结持仓清零
    public void clearT1Hold()
    {
        log.info("T+1冻结持仓清零--开始");
        try {
            indiaStrategyService.clearT1Hold();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("T+1冻结持仓清零--结束");
    }

    // 刷新印度新股
    public void updateIndiaNewStock()
    {
        log.info("刷新印度新股--开始");
        try {
            indiaStrategyService.updateIndiaNewStock();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("刷新印度新股--结束");
    }

    // 检测印度新股上市
    public void checkIndiaNewStock()
    {
        log.info("检测印度新股上市--开始");
        try {
            indiaStrategyService.checkIndiaNewStock();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("检测印度新股上市--结束");
    }

    // 印度新股上市，申购配售转持仓
    public void newStockToHold()
    {
        log.info("印度新股上市，申购配售转持仓--开始");
        try {
            indiaStrategyService.newStockToHold();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("印度新股上市，申购配售转持仓--结束");
    }

    // 刷新印度新闻
    public void updateNews()
    {
        log.info("刷新印度新闻--开始");
        try {
            // 印度新闻
            indiaStrategyService.updateIndiaNews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("刷新印度新闻--结束");
    }

    /**
     * 刷新大宗价格
     * 把未开启大宗交易的股票的收盘价更新到大宗价格
     * 已开启大宗交易的股票 大宗价格保持不变
     */
    public void updateDzPrice()
    {
        log.info("刷新大宗价格--开始");
        try {
            // 刷新大宗价格
            indiaStrategyService.updateDzPrice();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("刷新大宗价格--结束");
    }

    // 刷新IIFL股票数据
    public void updateStockInfoFromIIFL()
    {
        log.info("刷新IIFL股票数据--开始");
        try {
            // 刷新IIFL股票数据
            indiaStrategyService.updateStockInfoFromIIFL();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("刷新IIFL股票数据--结束");
    }

//    // 批量刷新印度NIFTY 50 和 S&P BSE SENSEX 指数股票实时信息
//    public void updateStockInfoInMainIndex()
//    {
//        log.info("批量刷新印度NIFTY 50 和 S&P BSE SENSEX 指数股票实时信息--开始");
//        try {
//            indiaStrategyService.updateStockInfoInMainIndex();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        log.info("批量刷新印度NIFTY 50 和 S&P BSE SENSEX 指数股票实时信息--结束");
//    }

}
