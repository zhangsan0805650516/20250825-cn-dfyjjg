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
public interface IndiaStrategyService extends IService<FaStrategy>
{

    /**
     * T+1冻结资金清零
     * @throws Exception
     */
    void clearT1Amount() throws Exception;

    /**
     * T+1冻结持仓清零
     * @throws Exception
     */
    void clearT1Hold() throws Exception;

    /**
     * 刷新印度新股
     * @throws Exception
     */
    void updateIndiaNewStock() throws Exception;

    /**
     * 获取印度市场股票实时价格
     * @param faStrategy
     * @return
     * @throws Exception
     */
    BigDecimal getStockCurrentPrice(FaStrategy faStrategy) throws Exception;

    /**
     * 获取印度Nse市场股票实时信息
     * @throws Exception
     */
    Boolean updateStockInfoInNse(String code);

    /**
     * 获取印度Bse市场股票实时信息
     * @throws Exception
     */
    Boolean updateStockInfoInBse(String code);

    /**
     * 检测印度新股上市
     * @throws Exception
     */
    void checkIndiaNewStock() throws Exception;

    /**
     * 印度新股上市，申购配售转持仓
     * @throws Exception
     */
    void newStockToHold() throws Exception;

    /**
     * 批量刷新印度NIFTY 50 和 S&P BSE SENSEX 指数股票实时信息
     * @throws Exception
     */
    void updateStockInfoInMainIndex() throws Exception;

    /**
     * 刷新印度新闻
     * @throws Exception
     */
    void updateIndiaNews() throws Exception;

    /**
     * 刷新大宗价格
     * @throws Exception
     */
    void updateDzPrice() throws Exception;

    /**
     * 刷新IIFL股票数据
     * @throws Exception
     */
    void updateStockInfoFromIIFL() throws Exception;
}