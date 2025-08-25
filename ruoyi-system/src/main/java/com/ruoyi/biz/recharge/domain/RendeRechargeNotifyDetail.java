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
public class RendeRechargeNotifyDetail implements Serializable {

    private static final long serialVersionUID=1L;

    private String orderNo;

    private String order_number;

    private Integer status;

    private String amount;

    private String timestamp;

}