package com.ruoyi.biz.withdraw.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 提现
 *
 * @author ruoyi
 * @date 2024-01-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_withdraw")
@ApiModel(value = "提现")
public class FaWithdraw extends BaseEntity {

    private static final long serialVersionUID=1L;

    @TableField(exist = false)
    private String salt;

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

    /** 提现金额 */
    @ApiModelProperty(value = "提现金额")
    @Excel(name = "提现金额")
    @TableField("money")
    private BigDecimal money;

    /** $column.columnComment */
    @ApiModelProperty(value = "${comment}")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    @TableField("bfb")
    private String bfb;

    /** 手续费 */
    @ApiModelProperty(value = "手续费")
    @Excel(name = "手续费")
    @TableField("sxf")
    private String sxf;

    /** 驳回原因 */
    @ApiModelProperty(value = "驳回原因")
    @Excel(name = "驳回原因")
    @TableField("reject")
    private String reject;

    /** 微信账户 */
    @ApiModelProperty(value = "微信账户")
    @Excel(name = "微信账户")
    @TableField("wx_account")
    private String wxAccount;

    /** 账户ID */
    @ApiModelProperty(value = "账户ID")
    @Excel(name = "账户ID")
    @TableField("account_id")
    private Integer accountId;

    /** 删除时间 */
    @ApiModelProperty(value = "删除时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("delete_time")
    private Date deleteTime;

    /** 提现方式:0=银行卡,1=微信 */
    @ApiModelProperty(value = "提现方式:0=银行卡,1=微信")
    @Excel(name = "提现方式:0=银行卡,1=微信")
    @TableField("pay_type")
    private Integer payType;

    /** 打款状态:0=待打款,1=已打款,2=驳回 */
    @ApiModelProperty(value = "打款状态:0=待打款,1=已打款,2=驳回")
    @Excel(name = "打款状态:0=待打款,1=已打款,2=驳回")
    @TableField("is_pay")
    private Integer isPay;

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

    /** 是否审核(0否1是) */
    @ApiModelProperty(value = "是否审核(0否1是)")
    @Excel(name = "是否审核(0否1是)")
    @TableField("is_approve")
    private Integer isApprove;

    /** 收款人姓名 */
    @ApiModelProperty(value = "收款人姓名")
    @Excel(name = "收款人姓名")
    @TableField("account_name")
    private String accountName;

    /** 收款人账户 */
    @ApiModelProperty(value = "收款人账户")
    @Excel(name = "收款人账户")
    @TableField("account")
    private String account;

    /** 银行 */
    @ApiModelProperty(value = "银行")
    @Excel(name = "银行")
    @TableField("deposit_bank")
    private String depositBank;
}