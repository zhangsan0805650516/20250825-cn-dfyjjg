package com.ruoyi.biz.strategy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.strategy.domain.FaStrategy;

import java.math.BigDecimal;

/**
 * 策略Service接口
 *
 * @author ruoyi
 * @date 2024-01-06
 */
public interface VietnamStrategyService extends IService<FaStrategy>
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
     * 刷新越南市场股票
     * @throws Exception
     */
    void updateVietnamStock() throws Exception;

    /**
     * 刷新越南市场股票 VPS
     * @throws Exception
     */
    void updateVietnamStockVPS() throws Exception;

    /**
     * 刷新大宗价格
     * @throws Exception
     */
    void updateDzPrice() throws Exception;

    /**
     * 获取当前价
     * @param faStrategy
     * @return
     * @throws Exception
     */
    BigDecimal getStockCurrentPrice(FaStrategy faStrategy) throws Exception;

}