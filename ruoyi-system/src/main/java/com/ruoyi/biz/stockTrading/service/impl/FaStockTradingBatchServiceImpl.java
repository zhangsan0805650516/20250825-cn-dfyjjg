package com.ruoyi.biz.stockTrading.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.stockHoldDetail.domain.FaStockHoldDetail;
import com.ruoyi.biz.stockHoldDetail.service.IFaStockHoldDetailService;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;
import com.ruoyi.biz.stockTrading.mapper.FaStockTradingMapper;
import com.ruoyi.biz.stockTrading.service.IFaStockTradingBatchService;
import com.ruoyi.biz.stockTrading.service.IFaStockTradingService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 成交记录Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-23
 */
@Service
public class FaStockTradingBatchServiceImpl extends ServiceImpl<FaStockTradingMapper, FaStockTrading> implements IFaStockTradingBatchService
{

    @Autowired
    private IFaStockTradingService faStockTradingService;

    @Autowired
    private IFaStockHoldDetailService faStockHoldDetailService;

    /**
     * 卖出股票（拆分持仓）
     * @param holdParam
     * @param stockHoldDetail
     * @throws Exception
     */
    @Override
    @Transactional
    public void sellStockBatch(FaStockHoldDetail holdParam, FaStockHoldDetail stockHoldDetail) throws Exception {
        FaStockHoldDetail sellHold = new FaStockHoldDetail();
        // 判断交易数量
        // 没传交易数量参数
        if (null == holdParam.getSellNumber()) {
            sellHold = stockHoldDetail;
        } else {
            // 超出持仓数量
            if (holdParam.getSellNumber() > stockHoldDetail.getHoldNumber()) {
                throw new ServiceException(MessageUtils.message("beyond.member.hold"), HttpStatus.ERROR);
            }
            // 等于持仓数量,正常卖出
            else if (holdParam.getSellNumber().equals(stockHoldDetail.getHoldNumber())) {
                sellHold = stockHoldDetail;
            }
            // 小于持仓数量,拆分持仓
            else if (holdParam.getSellNumber() < stockHoldDetail.getHoldNumber()) {
                sellHold = createNewSellHold(holdParam, stockHoldDetail);
            }
        }

        //新股交易，走普通逻辑
        if (0 == sellHold.getHoldType()) {
            faStockTradingService.sellStock(sellHold);
        }
        //普通交易
        else if (1 == sellHold.getHoldType()) {
            faStockTradingService.sellStock(sellHold);
        }
        // 大宗交易
        else if (2 == sellHold.getHoldType()) {
            faStockTradingService.sellStockDz(sellHold);
        }
        // VIP调研交易
        else if (3 == sellHold.getHoldType()) {
            faStockTradingService.sellStockVip(sellHold);
        }
        // 预约重组交易
        else if (9 == sellHold.getHoldType()) {
            faStockTradingService.sellStockYycz(sellHold);
        }
    }

    /**
     * 拆分持仓,生成新持仓,更新旧持仓
     * @param holdParam
     * @param stockHoldDetail
     * @return
     * @throws Exception
     */
    private FaStockHoldDetail createNewSellHold(FaStockHoldDetail holdParam, FaStockHoldDetail stockHoldDetail) throws Exception {
        // 新持仓
        FaStockHoldDetail newStockHoldDetail = new FaStockHoldDetail();
        // 复制属性
        BeanUtils.copyBeanProp(newStockHoldDetail, stockHoldDetail);

        // 去掉id
        newStockHoldDetail.setId(null);
        // 原持仓id
        newStockHoldDetail.setHoldId(stockHoldDetail.getId());
        // 计算持仓数量,金额
        newStockHoldDetail.setHoldHand(holdParam.getSellNumber() / 100);
        newStockHoldDetail.setHoldNumber(holdParam.getSellNumber());
        newStockHoldDetail.setTradingHand(newStockHoldDetail.getHoldHand());
        newStockHoldDetail.setTradingNumber(newStockHoldDetail.getHoldNumber());

        // 保存新持仓
        faStockHoldDetailService.save(newStockHoldDetail);

        // 更新旧持仓
        LambdaUpdateWrapper<FaStockHoldDetail> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaStockHoldDetail::getId, stockHoldDetail.getId());
        lambdaUpdateWrapper.set(FaStockHoldDetail::getHoldHand, (stockHoldDetail.getHoldNumber() - holdParam.getSellNumber()) / 100);
        lambdaUpdateWrapper.set(FaStockHoldDetail::getHoldNumber, stockHoldDetail.getHoldNumber() - holdParam.getSellNumber());
        lambdaUpdateWrapper.set(FaStockHoldDetail::getTradingHand, (stockHoldDetail.getHoldNumber() - holdParam.getSellNumber()) / 100);
        lambdaUpdateWrapper.set(FaStockHoldDetail::getTradingNumber, stockHoldDetail.getHoldNumber() - holdParam.getSellNumber());
        faStockHoldDetailService.update(lambdaUpdateWrapper);

        return newStockHoldDetail;
    }
}