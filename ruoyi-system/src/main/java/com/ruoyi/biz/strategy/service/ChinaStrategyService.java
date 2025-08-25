package com.ruoyi.biz.strategy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.strategy.domain.FaStrategy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 策略Service接口
 *
 * @author ruoyi
 * @date 2024-01-06
 */
public interface ChinaStrategyService extends IService<FaStrategy>
{

    /**
     * T+1冻结资金清零
     */
    void clearT1Amount() throws Exception;

    /**
     * T+1冻结持仓清零
     */
    void clearT1Hold() throws Exception;

    /**
     * 刷新大宗价格
     * @throws Exception
     */
    void updateDzPrice() throws Exception;

    /**
     * 刷新A股股票
     * @throws Exception
     */
    void updateChinaStock() throws Exception;

    /**
     * 刷新持仓股票价格
     * @throws Exception
     */
    void updateHoldingStock() throws Exception;

    /**
     * 获取实时价格
     * @param faStrategy
     * @return
     * @throws Exception
     */
    BigDecimal getStockCurrentPrice(FaStrategy faStrategy) throws Exception;

    /**
     * 刷新新股
     * @throws Exception
     */
    void updateNewStock() throws Exception;

    /**
     * 检测新股上市
     * @throws Exception
     */
    void checkNewStockList() throws Exception;

    /**
     * 刷新抢筹价
     * @throws Exception
     */
    void updateVipQcPrice() throws Exception;

    /**
     * 上证指数k线
     * @param faStrategy
     * @return
     * @throws Exception
     */
    List<Map<String, String>> getSHKline(FaStrategy faStrategy) throws Exception;

    /**
     * 刷新选定股票价格
     * @throws Exception
     */
    void updateChooseStock() throws Exception;
}