package com.ruoyi.biz.recharge.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.biz.sysbank.domain.FaSysbank;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.FaMember;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 充值
 *
 * @author ruoyi
 * @date 2024-01-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_recharge")
@ApiModel(value = "充值")
public class FaRecharge extends BaseEntity {

    private static final long serialVersionUID=1L;

    @TableField(exist = false)
    private String salt;

    /** 通道 */
    @TableField(exist = false)
    private FaSysbank faSysbank;

    @TableField(exist = false)
    private String weiyima;

    @TableField(exist = false)
    private Integer dailiId;

    /** 用户 */
    @TableField(exist = false)
    private FaMember faMember;

    /** ID */
    @ApiModelProperty(value = "ID")
    @Excel(name = "ID")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 用户ID */
    @ApiModelProperty(value = "用户ID")
    @Excel(name = "用户ID")
    @TableField("user_id")
    private Integer userId;

    /** 手机号 */
    @ApiModelProperty(value = "手机号")
    @Excel(name = "手机号")
    @TableField("mobile")
    private String mobile;

    /** 姓名 */
    @ApiModelProperty(value = "姓名")
    @Excel(name = "姓名")
    @TableField("name")
    private String name;

    /** 上级id */
    @ApiModelProperty(value = "上级id")
    @Excel(name = "上级id")
    @TableField("superior_id")
    private Integer superiorId;

    /** 上级机构码 */
    @ApiModelProperty(value = "上级机构码")
    @Excel(name = "上级机构码")
    @TableField("superior_code")
    private String superiorCode;

    /** 上级姓名 */
    @ApiModelProperty(value = "上级姓名")
    @Excel(name = "上级姓名")
    @TableField("superior_name")
    private String superiorName;

    /** 充值金额 */
    @ApiModelProperty(value = "充值金额")
    @Excel(name = "充值金额")
    @TableField("money")
    private BigDecimal money;

    /** 删除时间 */
    @ApiModelProperty(value = "删除时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("delete_time")
    private Date deleteTime;

    /** 付款时间 */
    @ApiModelProperty(value = "付款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "付款时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("pay_time")
    private Date payTime;

    /** 支付方式:0手动,1支付宝,2对公转账,3现金转账,4三方存管 */
    @ApiModelProperty(value = "支付方式:0手动,1支付宝,2对公转账,3现金转账,4三方存管")
    @Excel(name = "支付方式:0手动,1支付宝,2对公转账,3现金转账,4三方存管")
    @TableField("pay_type")
    private Integer payType;

    /** 是否付款:0=否,1=是,2=驳回 */
    @ApiModelProperty(value = "是否付款:0=否,1=是,2=驳回")
    @Excel(name = "是否付款:0=否,1=是,2=驳回")
    @TableField("is_pay")
    private Integer isPay;

    /** 默认0未打款，1代表已打款, 2驳回 */
    @ApiModelProperty(value = "默认0未打款，1代表已打款, 2驳回")
    @Excel(name = "默认0未打款，1代表已打款, 2驳回")
    @TableField("is_remit")
    private Integer isRemit;

    /** 驳回原因 */
    @ApiModelProperty(value = "驳回原因")
    @Excel(name = "驳回原因")
    @TableField("reject")
    private String reject;

    /** $column.columnComment */
    @ApiModelProperty(value = "${comment}")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    @TableField("is_simulation")
    private String isSimulation;

    /** $column.columnComment */
    @ApiModelProperty(value = "${comment}")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    @TableField("sysbankid")
    private Integer sysbankid;

    /** 消息开关(0关 1开) */
    @ApiModelProperty(value = "消息开关(0关 1开)")
    @Excel(name = "消息开关(0关 1开)")
    @TableField("is_qx")
    private Integer isQx;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

    /** 订单号 */
    @ApiModelProperty(value = "订单号")
    @Excel(name = "订单号")
    @TableField("order_id")
    private String orderId;

    /** 平台订单号 */
    @ApiModelProperty(value = "平台订单号")
    @Excel(name = "平台订单号")
    @TableField("transaction_id")
    private String transactionId;

    /** 是否审核(0否1是) */
    @ApiModelProperty(value = "是否审核(0否1是)")
    @Excel(name = "是否审核(0否1是)")
    @TableField("is_approve")
    private Integer isApprove;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @Excel(name = "备注")
    @TableField("remarks")
    private String remarks;

}