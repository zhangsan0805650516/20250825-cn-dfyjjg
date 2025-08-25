package com.ruoyi.quartz.task;

import com.ruoyi.biz.strategy.service.ThailandStrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Component("thailandTask")
public class ThailandTask
{

    private static final Logger log = LoggerFactory.getLogger(ThailandTask.class);
    
    @Autowired
    private ThailandStrategyService thailandStrategyService;

    // T+1冻结资金清零
    public void clearT1Amount()
    {
        log.info("T+1冻结资金清零--开始");
        try {
            if (checkProd()) {
                thailandStrategyService.clearT1Amount();
            }
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
            if (checkProd()) {
                thailandStrategyService.clearT1Hold();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("T+1冻结持仓清零--结束");
    }

    // 刷新泰国股票
    public void updateThailandStock() {
        log.info("刷新泰国股票--开始");
        try {
            if (checkProd()) {
                thailandStrategyService.updateThailandStock();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("刷新泰国股票--结束");
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
            if (checkProd()) {
                // 刷新大宗价格
                thailandStrategyService.updateDzPrice();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("刷新大宗价格--结束");
    }

    /**
     * 刷新抢筹价
     * 把未开启抢筹的股票的采集价更新到抢筹价格
     * 已开启抢筹的股票 抢筹价格保持不变
     */
    public void updateVipQcPrice() {
        log.info("刷新抢筹价--开始");
        try {
            if (checkProd()) {
                thailandStrategyService.updateVipQcPrice();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("刷新抢筹价--结束");
    }

//    // 刷新新股
//    public void updateNewStock()
//    {
//        log.info("刷新新股--开始");
//        try {
//            if (checkProd()) {
//                chinaStrategyService.updateNewStock();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        log.info("刷新新股--结束");
//    }
//
//    // 检测新股上市
//    public void checkNewStockList()
//    {
//        log.info("检测新股上市--开始");
//        try {
//            if (checkProd()) {
//                chinaStrategyService.checkNewStockList();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        log.info("检测新股上市--结束");
//    }

    /**
     * 检查是否prod环境
     * @return
     */
    private boolean checkProd() {
        String filePath = "/home/admin/prod.txt";
        File file = new File(filePath);
        if (file.exists()) {
            log.info("文件存在，prod环境");
        } else {
            log.info("文件不存在，模拟环境");
        }
        return file.exists();
    }

}
