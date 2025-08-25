package com.ruoyi.biz.stockTrading.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.stockHoldDetail.domain.FaStockHoldDetail;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;

/**
 * 成交记录Service接口
 *
 * @author ruoyi
 * @date 2024-01-23
 */
public interface IFaStockTradingBatchService extends IService<FaStockTrading>
{

    /**
     * 卖出股票（拆分持仓）
     * @param holdParam
     * @param stockHoldDetail
     * @throws Exception
     */
    void sellStockBatch(FaStockHoldDetail holdParam, FaStockHoldDetail stockHoldDetail) throws Exception;

}