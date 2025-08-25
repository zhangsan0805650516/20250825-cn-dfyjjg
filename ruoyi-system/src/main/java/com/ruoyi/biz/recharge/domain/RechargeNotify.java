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
public class RechargeNotify implements Serializable {

    private static final long serialVersionUID=1L;

    // 九哥
    /** 商户编号 */
    private String mchNo;

    /** 订单号 */
    private String payOrderId;

    /** 订单金额 */
    private String payAmt;

    /** 应用appId */
    private String appId;

    /** 货币 */
    private String currency;

    /** 支付状态: 0-订单生成, 1-支付中, 2-支付成功, 3-支付失败, 4-已撤销, 5-已退款, 6-订单关闭 */
    private String state;

    /** 请求时间 */
    private String reqTime;

    /** MD5签名 */
    private String sign;


    // 仁德
    private Integer code;

    private RendeRechargeNotifyDetail data;

    private String msg;

    // 火箭
    /** 2为成功可以用来判断.虽然只有成功才回调 */
    private Integer status;

    /** 平台订单号 */
    private String uuid;

    /** 商户订单号 */
    private String order_no;

    /** 实际支付金额 */
    private String amount;

    // 四方
    private String memberid;
    private String orderid;
    private String true_amount;
    private String datetime;
    private String transaction_id;
    private String returncode;

    // 代付
//    private String code;
    private String outOrderId;
    //    private String amount;
//    /** 1成功 2失败 */
//    private String status;
    private String createdTime;
    //    private String sign;
    private String orderMsg;

    // 亨通
    private String mchOrderNo;

    // 衫德宝
    private Integer pay_state;

    // AX
    private String trade_no;
    private String out_trade_no;
    private Integer trade_status;

}