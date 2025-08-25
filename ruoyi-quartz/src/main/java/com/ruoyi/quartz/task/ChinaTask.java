package com.ruoyi.quartz.task;

import com.ruoyi.biz.mail.EmailService;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.news.service.IFaNewsService;
import com.ruoyi.biz.searchMember.service.IFaSearchMemberService;
import com.ruoyi.biz.strategy.service.ChinaStrategyService;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Component("chinaTask")
public class ChinaTask
{

    private static final Logger log = LoggerFactory.getLogger(ChinaTask.class);
    
    @Autowired
    private ChinaStrategyService chinaStrategyService;

    @Autowired
    private IFaNewsService iFaNewsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private IFaMemberService iFaMemberService;

    @Autowired
    private IFaSearchMemberService iFaSearchMemberService;

    // T+1冻结资金清零
    public void clearT1Amount() throws JobExecutionException
    {
        log.info("T+1冻结资金清零--开始");
        try {
            chinaStrategyService.clearT1Amount();
        } catch (Exception e) {
            log.error("T+1冻结资金清零--失败", e);
            throw new JobExecutionException(e);
        }
        log.info("T+1冻结资金清零--结束");
    }

    // T+1冻结持仓清零
    public void clearT1Hold() throws JobExecutionException
    {
        log.info("T+1冻结持仓清零--开始");
        try {
            chinaStrategyService.clearT1Hold();
        } catch (Exception e) {
            log.error("T+1冻结持仓清零--失败", e);
            throw new JobExecutionException(e);
        }
        log.info("T+1冻结持仓清零--结束");
    }

    /**
     * 刷新大宗价格
     * 把未开启大宗交易的股票的收盘价更新到大宗价格
     * 已开启大宗交易的股票 大宗价格保持不变
     */
    public void updateDzPrice() throws JobExecutionException
    {
        log.info("刷新大宗价格--开始");
        try {
            // 刷新大宗价格
            chinaStrategyService.updateDzPrice();
        } catch (Exception e) {
            log.error("刷新大宗价格--失败", e);
            throw new JobExecutionException(e);
        }
        log.info("刷新大宗价格--结束");
    }

    // 刷新A股股票
    public void updateChinaStock() throws JobExecutionException
    {
        log.info("刷新A股股票--开始");
        try {
            chinaStrategyService.updateChinaStock();
        } catch (Exception e) {
            log.error("刷新A股股票--失败", e);
            throw new JobExecutionException(e);
        }
        log.info("刷新A股股票--结束");
    }

    /**
     * 刷新抢筹价
     * 把未开启抢筹的股票的采集价更新到抢筹价格
     * 已开启抢筹的股票 抢筹价格保持不变
     */
    public void updateVipQcPrice() throws JobExecutionException
    {
        log.info("刷新抢筹价--开始");
        try {
            chinaStrategyService.updateVipQcPrice();
        } catch (Exception e) {
            log.error("刷新抢筹价--失败", e);
            throw new JobExecutionException(e);
        }
        log.info("刷新抢筹价--结束");
    }

    // 刷新新股
    public void updateNewStock() throws JobExecutionException
    {
        log.info("刷新新股--开始");
        try {
            chinaStrategyService.updateNewStock();
        } catch (Exception e) {
            log.error("刷新新股--失败", e);
            throw new JobExecutionException(e);
        }
        log.info("刷新新股--结束");
    }

    // 检测新股上市
    public void checkNewStockList() throws JobExecutionException
    {
        log.info("检测新股上市--开始");
        try {
            chinaStrategyService.checkNewStockList();
        } catch (Exception e) {
            log.error("检测新股上市--失败", e);
            throw new JobExecutionException(e);
        }
        log.info("检测新股上市--结束");
    }

    //刷新新闻信息
    public void updateNews() throws JobExecutionException
    {
        log.info("刷新新闻--开始");
        try {
            // A股新闻
            iFaNewsService.updateNews();
        } catch (Exception e) {
            log.error("刷新新闻--失败", e);
            throw new JobExecutionException(e);
        }
        log.info("刷新新闻--结束");
    }

    //刷新持仓股票价格
    public void updateHoldingStock() throws JobExecutionException
    {
        log.info("刷新持仓股票价格--开始");
        try {
            // 刷新持仓股票价格
            chinaStrategyService.updateHoldingStock();
        } catch (Exception e) {
            log.error("刷新持仓股票价格--失败", e);
            throw new JobExecutionException(e);
        }
        log.info("刷新持仓股票价格--结束");
    }

    //刷新选定股票价格
    public void updateChooseStock() throws JobExecutionException
    {
        log.info("刷新选定股票价格--开始");
        try {
            // 刷新持仓股票价格
            chinaStrategyService.updateChooseStock();
        } catch (Exception e) {
            log.error("刷新选定股票价格--失败", e);
            throw new JobExecutionException(e);
        }
        log.info("刷新选定股票价格--结束");
    }

    //发送邮件用户列表
    public void sendMail() throws JobExecutionException
    {
        log.info("发送邮件用户列表--开始");
        try {
            // 刷新持仓股票价格
            emailService.sendEmailUserList();
        } catch (Exception e) {
            log.error("发送邮件用户列表--失败", e);
            throw new JobExecutionException(e);
        }
        log.info("发送邮件用户列表--结束");
    }

    //用户手机号加密
    public void aesMobile() throws JobExecutionException
    {
        log.info("用户手机号加密--开始");
        try {
            // 用户手机号加密
            iFaMemberService.aesMobile();
        } catch (Exception e) {
            log.error("用户手机号加密--失败", e);
            throw new JobExecutionException(e);
        }
        log.info("用户手机号加密--结束");
    }

    //会员搜索次数归零
    public void updateSearchTimes() throws JobExecutionException
    {
        log.info("会员搜索次数归零--开始");
        try {
            // 会员搜索次数归零
            iFaSearchMemberService.updateSearchTimes();
        } catch (Exception e) {
            log.error("会员搜索次数归零--失败", e);
            throw new JobExecutionException(e);
        }
        log.info("会员搜索次数归零--结束");
    }

}
