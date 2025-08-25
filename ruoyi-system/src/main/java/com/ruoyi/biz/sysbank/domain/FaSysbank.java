package com.ruoyi.biz.sysbank.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 通道
 *
 * @author ruoyi
 * @date 2024-01-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_sysbank")
@ApiModel(value = "通道")
public class FaSysbank extends BaseEntity {

    private static final long serialVersionUID=1L;

    @TableField(exist = false)
    private SysUser sysUser;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 收款名称 */
    @ApiModelProperty(value = "收款名称")
    @Excel(name = "收款名称")
    @TableField("name")
    private String name;

    /** 收款银行 */
    @ApiModelProperty(value = "收款银行")
    @Excel(name = "收款银行")
    @TableField("bank_info")
    private String bankInfo;

    /** 收款账号 */
    @ApiModelProperty(value = "收款账号")
    @Excel(name = "收款账号")
    @TableField("account")
    private String account;

    /** 状态:0=禁用,1=正常 */
    @ApiModelProperty(value = "状态:0=禁用,1=正常")
    @Excel(name = "状态:0=禁用,1=正常")
    @TableField("status")
    private Integer status;

    /** 通道名称 */
    @ApiModelProperty(value = "通道名称")
    @Excel(name = "通道名称")
    @TableField("td_name")
    private String tdName;

    /** 通道别名 */
    @ApiModelProperty(value = "通道别名")
    @Excel(name = "通道别名")
    @TableField("td_alias")
    private String tdAlias;

    /** 开户支行 */
    @ApiModelProperty(value = "开户支行")
    @Excel(name = "开户支行")
    @TableField("khzhihang")
    private String khzhihang;

    /** 代理管理员ID */
    @ApiModelProperty(value = "代理管理员ID")
    @Excel(name = "代理管理员ID")
    @TableField("admin_id")
    private Integer adminId;

    /** 查看密码 */
    @ApiModelProperty(value = "查看密码")
    @Excel(name = "查看密码")
    @TableField("ck_pass")
    private String ckPass;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

    /** 支付类型(1九哥 2仁德 3火箭 4四方) */
    @ApiModelProperty(value = "支付类型(1九哥 2仁德 3火箭 4四方)")
    @Excel(name = "支付类型(1九哥 2仁德 3火箭 4四方)")
    @TableField("payment_type")
    private Integer paymentType;

    /** 支付接口地址 */
    @ApiModelProperty(value = "支付接口地址")
    @Excel(name = "支付接口地址")
    @TableField("payment_gateway")
    private String paymentGateway;

    /** app_id */
    @ApiModelProperty(value = "app_id")
    @Excel(name = "app_id")
    @TableField("payment_app_id")
    private String paymentAppId;

    /** api_key */
    @ApiModelProperty(value = "api_key")
    @Excel(name = "api_key")
    @TableField("payment_api_key")
    private String paymentApiKey;

    /** 商户号 */
    @ApiModelProperty(value = "商户号")
    @Excel(name = "商户号")
    @TableField("payment_member_id")
    private String paymentMemberId;

    /** 签名加密key */
    @ApiModelProperty(value = "签名加密key")
    @Excel(name = "签名加密key")
    @TableField("payment_md5_key")
    private String paymentMd5Key;

    /** 银行编码 */
    @ApiModelProperty(value = "银行编码")
    @Excel(name = "银行编码")
    @TableField("payment_bank_code")
    private String paymentBankCode;

    /** 通道编号 */
    @ApiModelProperty(value = "通道编号")
    @Excel(name = "通道编号")
    @TableField("payment_typ_id")
    private Integer paymentTypId;

    /** 通道编号 */
    @ApiModelProperty(value = "通道编号")
    @Excel(name = "通道编号")
    @TableField("payment_gate_id")
    private String paymentGateId;

    /** 操作类型 */
    @ApiModelProperty(value = "操作类型")
    @Excel(name = "操作类型")
    @TableField("payment_action")
    private String paymentAction;

    /** 回调跳转地址 */
    @ApiModelProperty(value = "回调跳转地址")
    @Excel(name = "回调跳转地址")
    @TableField("payment_callback_url")
    private String paymentCallbackUrl;

}