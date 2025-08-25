package com.ruoyi.quartz.task;

import com.ruoyi.biz.strategy.service.VietnamStrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Component("vietnamTask")
public class VietnamTask
{

    private static final Logger log = LoggerFactory.getLogger(VietnamTask.class);
    
    @Autowired
    private VietnamStrategyService vietnamStrategyService;

    // T+1冻结资金清零
    public void clearT1Amount()
    {
        log.info("T+1冻结资金清零--开始");
        try {
            vietnamStrategyService.clearT1Amount();
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
            vietnamStrategyService.clearT1Hold();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("T+1冻结持仓清零--结束");
    }

    // 刷新越南市场新股

    // 检测印度新股上市

    // 印度新股上市，申购配售转持仓

    // 刷新越南市场股票
    public void updateVietnamStock() {
        log.info("刷新越南市场股票--开始");
        try {
            // SSI
//            vietnamStrategyService.updateVietnamStock();
            // VPS
            vietnamStrategyService.updateVietnamStockVPS();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("刷新越南市场股票--结束");
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
            vietnamStrategyService.updateDzPrice();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("刷新大宗价格--结束");
    }

}
