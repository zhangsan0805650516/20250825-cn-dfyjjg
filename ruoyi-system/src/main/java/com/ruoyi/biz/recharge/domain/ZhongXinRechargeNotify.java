package com.ruoyi.biz.recharge.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 充值
 *
 * @author ruoyi
 * @date 2024-01-07
 */
@Data
public class ZhongXinRechargeNotify implements Serializable {

    private static final long serialVersionUID = 1L;

    // zhongxin
    private String orderNo;
    private String payNo;
    private String status;

}