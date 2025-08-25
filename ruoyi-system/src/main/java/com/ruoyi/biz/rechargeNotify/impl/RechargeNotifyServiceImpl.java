package com.ruoyi.biz.rechargeNotify.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.recharge.domain.*;
import com.ruoyi.biz.recharge.service.IFaRechargeService;
import com.ruoyi.biz.rechargeNotify.RechargeNotifyService;
import com.ruoyi.biz.sysbank.domain.FaSysbank;
import com.ruoyi.biz.sysbank.service.IFaSysbankService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class RechargeNotifyServiceImpl implements RechargeNotifyService
{

    private static final Logger log = LoggerFactory.getLogger(RechargeNotifyServiceImpl.class);

    @Autowired
    private IFaRechargeService iFaRechargeService;

    @Autowired
    private IFaCapitalLogService iFaCapitalLogService;

    @Autowired
    private IFaSysbankService iFaSysbankService;

    /**
     * 充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    @Override
    public void nineBrotherRechargeNotify(RechargeNotify rechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, rechargeNotify.getPayOrderId());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 支付成功
            if ("2".equals(rechargeNotify.getState())) {
                // 未付款
                if (0 == faRecharge.getIsPay()) {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getOrderId, rechargeNotify.getPayOrderId());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                    iFaRechargeService.update(lambdaUpdateWrapper);

                    // 资金流水
                    iFaCapitalLogService.save(faRecharge);
                }
            }
            // 支付失败
            else {
                LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                // 平台订单id
                lambdaUpdateWrapper.set(FaRecharge::getOrderId, rechargeNotify.getPayOrderId());
                lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                iFaRechargeService.update(lambdaUpdateWrapper);
            }
        }
    }

    /**
     * 充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    @Override
    public void rendeRechargeNotify(RechargeNotify rechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, rechargeNotify.getData().getOrder_number());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 接口成功
            if (0 == rechargeNotify.getCode()) {
                // 支付完成
                if (2 == rechargeNotify.getData().getStatus()) {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getData().getOrderNo());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                    iFaRechargeService.update(lambdaUpdateWrapper);

                    // 资金流水
                    iFaCapitalLogService.save(faRecharge);
                }
                // 支付失败
                else {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getData().getOrderNo());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    iFaRechargeService.update(lambdaUpdateWrapper);
                }
            }
        }
    }

    /**
     * 充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    @Override
    public void huojianRechargeNotify(RechargeNotify rechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 商户订单号
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, rechargeNotify.getOrder_no());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 支付成功
            if (2 == rechargeNotify.getStatus()) {
                // 未付款
                if (0 == faRecharge.getIsPay()) {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getUuid());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                    iFaRechargeService.update(lambdaUpdateWrapper);

                    // 资金流水
                    iFaCapitalLogService.save(faRecharge);
                }
            }
            // 支付失败
            else {
                LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                // 平台订单id
                lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getUuid());
                lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                iFaRechargeService.update(lambdaUpdateWrapper);
            }
        }
    }

    @Override
    public void sifangRechargeNotify(RechargeNotify rechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 商户订单号
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, rechargeNotify.getOrderid());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 支付成功
            if ("00".equals(rechargeNotify.getReturncode())) {
                // 未付款
                if (0 == faRecharge.getIsPay()) {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getTransaction_id());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                    iFaRechargeService.update(lambdaUpdateWrapper);

                    // 资金流水
                    iFaCapitalLogService.save(faRecharge);
                }
            }
            // 支付失败
            else {
                LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                // 平台订单id
                lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getTransaction_id());
                lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                iFaRechargeService.update(lambdaUpdateWrapper);
            }
        }
    }

    /**
     * 充值回调接口json代付
     * @param rechargeNotify
     * @throws Exception
     */
    @Override
    public void daiFuRechargeNotify(RechargeNotify rechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 商户订单号
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, rechargeNotify.getOutOrderId());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 支付成功
            if (1 == rechargeNotify.getStatus()) {
                // 未付款
                if (0 == faRecharge.getIsPay()) {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                    iFaRechargeService.update(lambdaUpdateWrapper);

                    // 资金流水
                    iFaCapitalLogService.save(faRecharge);
                }
            }
            // 支付失败
            else {
                LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                lambdaUpdateWrapper.set(FaRecharge::getReject, rechargeNotify.getOrderMsg());
                lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                iFaRechargeService.update(lambdaUpdateWrapper);
            }
        }
    }

    @Override
    public void FMLRechargeNotify(RechargeNotify rechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 商户订单号
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, rechargeNotify.getOrderid());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 支付成功
            if ("00".equals(rechargeNotify.getReturncode())) {
                // 未付款
                if (0 == faRecharge.getIsPay()) {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getTransaction_id());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                    iFaRechargeService.update(lambdaUpdateWrapper);

                    // 资金流水
                    iFaCapitalLogService.save(faRecharge);
                }
            }
            // 支付失败
            else {
                LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                // 平台订单id
                lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getTransaction_id());
                lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                iFaRechargeService.update(lambdaUpdateWrapper);
            }
        }
    }

    @Override
    public void hengTongRechargeNotify(RechargeNotify rechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 商户订单号
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, rechargeNotify.getMchOrderNo());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 支付成功
            if ("2".equals(rechargeNotify.getState())) {
                // 未付款
                if (0 == faRecharge.getIsPay()) {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getPayOrderId());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                    iFaRechargeService.update(lambdaUpdateWrapper);

                    // 资金流水
                    iFaCapitalLogService.save(faRecharge);
                }
            }
            // 支付失败
            else {
                LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                // 平台订单id
                lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getPayOrderId());
                lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                iFaRechargeService.update(lambdaUpdateWrapper);
            }
        }
    }

    @Override
    public void shanDeBaoRechargeNotify(RechargeNotify rechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 商户订单号
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, rechargeNotify.getOrder_no());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 支付成功
            if (1 == rechargeNotify.getPay_state()) {
                // 未付款
                if (0 == faRecharge.getIsPay()) {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getPayOrderId());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                    iFaRechargeService.update(lambdaUpdateWrapper);

                    // 资金流水
                    iFaCapitalLogService.save(faRecharge);
                }
            }
            // 支付失败
            else {
                LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                // 平台订单id
                lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getPayOrderId());
                lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                iFaRechargeService.update(lambdaUpdateWrapper);
            }
        }
    }

    @Override
    public void xlRechargeNotify(RechargeNotify rechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 商户订单号
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, rechargeNotify.getMchOrderNo());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 支付成功
            if ("2".equals(rechargeNotify.getState())) {
                // 未付款
                if (0 == faRecharge.getIsPay()) {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getPayOrderId());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                    iFaRechargeService.update(lambdaUpdateWrapper);

                    // 资金流水
                    iFaCapitalLogService.save(faRecharge);
                }
            }
            // 支付失败
            else {
                LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                // 平台订单id
                lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getPayOrderId());
                lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                iFaRechargeService.update(lambdaUpdateWrapper);
            }
        }
    }

    @Override
    public void shaYuRechargeNotify(ShaYuRechargeNotify rechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 商户订单号
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, rechargeNotify.getOrderId());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 支付成功
            if ("ok".equals(rechargeNotify.getStatus())) {
                // 未付款
                if (0 == faRecharge.getIsPay()) {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getTransaction_id());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                    iFaRechargeService.update(lambdaUpdateWrapper);

                    // 资金流水
                    iFaCapitalLogService.save(faRecharge);
                }
            }
            // 支付失败
            else {
                LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                // 平台订单id
                lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getTransaction_id());
                lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                iFaRechargeService.update(lambdaUpdateWrapper);
            }
        }
    }

    /**
     * 充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    @Override
    public void axRechargeNotify(RechargeNotify rechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 商户订单号
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, rechargeNotify.getOut_trade_no());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 支付成功
            if (1 == rechargeNotify.getTrade_status()) {
                // 未付款
                if (0 == faRecharge.getIsPay()) {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getTrade_no());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                    iFaRechargeService.update(lambdaUpdateWrapper);

                    // 资金流水
                    iFaCapitalLogService.save(faRecharge);
                }
            }
            // 支付失败
            else {
                LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                // 平台订单id
                lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getTrade_no());
                lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                iFaRechargeService.update(lambdaUpdateWrapper);
            }
        }
    }

    @Override
    public void haiZeiRechargeNotify(RechargeNotify rechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 商户订单号
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, rechargeNotify.getOrderid());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 支付成功
            if ("00".equals(rechargeNotify.getReturncode())) {
                // 未付款
                if (0 == faRecharge.getIsPay()) {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getTransaction_id());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                    iFaRechargeService.update(lambdaUpdateWrapper);

                    // 资金流水
                    iFaCapitalLogService.save(faRecharge);
                }
            }
            // 支付失败
            else {
                LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                // 平台订单id
                lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getTransaction_id());
                lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                iFaRechargeService.update(lambdaUpdateWrapper);
            }
        }
    }

    @Override
    public void hongYunRechargeNotify(HongYunRechargeNotify hongYunRechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 商户订单号
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, hongYunRechargeNotify.getOut_trade_no());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 支付成功
            if ("success".equals(hongYunRechargeNotify.getStatus())) {
                // 未付款
                if (0 == faRecharge.getIsPay()) {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getTransactionId, hongYunRechargeNotify.getTrade_no());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                    iFaRechargeService.update(lambdaUpdateWrapper);

                    // 资金流水
                    iFaCapitalLogService.save(faRecharge);
                }
            }
            // 支付失败
            else {
                LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                // 平台订单id
                lambdaUpdateWrapper.set(FaRecharge::getTransactionId, hongYunRechargeNotify.getTrade_no());
                lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                iFaRechargeService.update(lambdaUpdateWrapper);
            }
        }
    }

    @Override
    public void zhongXinRechargeNotify(ZhongXinRechargeNotify rechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 商户订单号
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, rechargeNotify.getOrderNo());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 支付成功
            if ("S".equalsIgnoreCase(rechargeNotify.getStatus())) {
                // 未付款
                if (0 == faRecharge.getIsPay()) {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getPayNo());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                    iFaRechargeService.update(lambdaUpdateWrapper);

                    // 资金流水
                    iFaCapitalLogService.save(faRecharge);
                }
            }
            // 支付失败
            else {
                LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                // 平台订单id
                lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getPayNo());
                lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                iFaRechargeService.update(lambdaUpdateWrapper);
            }
        }
    }

    @Override
    public void rechargeNotifyFormWeiHan(RechargeNotify rechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 商户订单号
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, rechargeNotify.getMchOrderNo());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 支付成功
            if (2 == rechargeNotify.getStatus()) {
                // 未付款
                if (0 == faRecharge.getIsPay()) {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getPayOrderId());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                    iFaRechargeService.update(lambdaUpdateWrapper);

                    // 资金流水
                    iFaCapitalLogService.save(faRecharge);
                }
            }
            // 支付失败
            else {
                LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                // 平台订单id
                lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getPayOrderId());
                lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                iFaRechargeService.update(lambdaUpdateWrapper);
            }
        }
    }

    @Override
    public void feiYunRechargeNotify(FeiYunRechargeNotify feiYunRechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 商户订单号
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, feiYunRechargeNotify.getOrderid());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 未付款
            if (0 == faRecharge.getIsPay()) {
                LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                // 平台订单id
//                lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getPayOrderId());
                lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                iFaRechargeService.update(lambdaUpdateWrapper);

                // 资金流水
                iFaCapitalLogService.save(faRecharge);
            }
        }
    }

    @Override
    public String rechargeNotify(String json, Integer id) throws Exception {
        FaSysbank faSysbank = iFaSysbankService.getById(id);
        if (ObjectUtils.isEmpty(faSysbank)) {
            throw new ServiceException("payment config error", HttpStatus.ERROR);
        }

        // 取出配置
        String config = faSysbank.getPaymentGateway();
        if (StringUtils.isNotEmpty(config)) {
            JSONObject jsonObject = JSONObject.parseObject(config);
            if (ObjectUtils.isNotEmpty(jsonObject)) {
                JSONObject notifyObject = JSONObject.parseObject(json);

                // 回调配置
                jsonObject = jsonObject.getJSONObject("notifyInfo");
                // 商户订单号
                String orderNoOut = jsonObject.getString("orderNoOut");
                orderNoOut = notifyObject.getString(orderNoOut);
                LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(FaRecharge::getOrderId, orderNoOut);
                FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
                if (ObjectUtils.isNotEmpty(faRecharge)) {
                    String orderNoIn = "";
                    if (jsonObject.containsKey("orderNoIn")) {
                        orderNoIn = jsonObject.getString("orderNoIn");
                        orderNoIn = notifyObject.getString(orderNoIn);
                    }

                    String statusKey = jsonObject.getString("statusKey");
                    String statusValue = jsonObject.getString("statusValue");
                    String value = notifyObject.getString(statusKey);
                    if (StringUtils.equalsIgnoreCase(statusValue, value)) {
                        // 未付款
                        if (0 == faRecharge.getIsPay()) {
                            LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                            lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                            lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                            // 平台订单id
                            lambdaUpdateWrapper.set(FaRecharge::getTransactionId, orderNoIn);
                            lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                            lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                            lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                            iFaRechargeService.update(lambdaUpdateWrapper);

                            // 资金流水
                            iFaCapitalLogService.save(faRecharge);
                        }
                        return jsonObject.getString("responseWord");
                    }
                    // 支付失败
                    else {
                        // 未付款
                        if (0 == faRecharge.getIsPay()) {
                            LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                            lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                            lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                            // 平台订单id
                            lambdaUpdateWrapper.set(FaRecharge::getTransactionId, orderNoIn);
                            lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                            iFaRechargeService.update(lambdaUpdateWrapper);
                        }
                    }
                }
            }
        }
        return "";
    }

    @Override
    public String rechargeNotify(HttpServletRequest request, Integer id) throws Exception {
        FaSysbank faSysbank = iFaSysbankService.getById(id);
        if (ObjectUtils.isEmpty(faSysbank)) {
            throw new ServiceException("payment config error", HttpStatus.ERROR);
        }

        // 取出配置
        String config = faSysbank.getPaymentGateway();
        if (StringUtils.isNotEmpty(config)) {
            JSONObject jsonObject = JSONObject.parseObject(config);
            if (ObjectUtils.isNotEmpty(jsonObject)) {
                // 回调配置
                jsonObject = jsonObject.getJSONObject("notifyInfo");
                // 商户订单号
                String orderNoOut = jsonObject.getString("orderNoOut");
                orderNoOut = request.getParameter(orderNoOut);
                LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(FaRecharge::getOrderId, orderNoOut);
                FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
                if (ObjectUtils.isNotEmpty(faRecharge)) {
                    String orderNoIn = "";
                    if (jsonObject.containsKey("orderNoIn")) {
                        orderNoIn = jsonObject.getString("orderNoIn");
                        orderNoIn = request.getParameter(orderNoIn);
                    }

                    String statusKey = jsonObject.getString("statusKey");
                    String statusValue = jsonObject.getString("statusValue");
                    String value = request.getParameter(statusKey);
                    if (StringUtils.equalsIgnoreCase(statusValue, value)) {
                        // 未付款
                        if (0 == faRecharge.getIsPay()) {
                            LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                            lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                            lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                            // 平台订单id
                            lambdaUpdateWrapper.set(FaRecharge::getTransactionId, orderNoIn);
                            lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                            lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                            lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                            iFaRechargeService.update(lambdaUpdateWrapper);

                            // 资金流水
                            iFaCapitalLogService.save(faRecharge);
                        }
                        return jsonObject.getString("responseWord");
                    }
                    // 支付失败
                    else {
                        // 未付款
                        if (0 == faRecharge.getIsPay()) {
                            LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                            lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                            lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                            // 平台订单id
                            lambdaUpdateWrapper.set(FaRecharge::getTransactionId, orderNoIn);
                            lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                            iFaRechargeService.update(lambdaUpdateWrapper);
                        }
                    }
                }
            }
        }
        return "";
    }

}
