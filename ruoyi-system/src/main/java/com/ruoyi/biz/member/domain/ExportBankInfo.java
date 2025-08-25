package com.ruoyi.biz.member.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员管理
 *
 * @author ruoyi
 * @date 2024-01-04
 */
@Getter
@Setter
@Accessors(chain = true)
public class ExportBankInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID */
    @ApiModelProperty(value = "ID")
    @Excel(name = "ID")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 用户名 */
    @ApiModelProperty(value = "用户名")
    @Excel(name = "用户名")
    @TableField("username")
    private String username;

    /** 昵称 */
    @ApiModelProperty(value = "昵称")
    @Excel(name = "昵称")
    @TableField("nickname")
    private String nickname;

    /** 手机号 */
    @ApiModelProperty(value = "手机号")
    @Excel(name = "手机号")
    @TableField("mobile")
    private String mobile;

    /** 认证姓名 */
    @ApiModelProperty(value = "认证姓名")
    @Excel(name = "认证姓名")
    @TableField("name")
    private String name;

    /** 银行 */
    @ApiModelProperty(value = "银行")
    @Excel(name = "银行")
    @TableField("deposit_bank")
    private String depositBank;

    /** 开户支行 */
    @ApiModelProperty(value = "开户支行")
    @Excel(name = "开户支行")
    @TableField("khzhihang")
    private String khzhihang;

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

    /** 绑定时间 */
    @ApiModelProperty(value = "绑定时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "绑定时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("binding_time")
    private Date bindingTime;

    /** 解绑时间 */
    @ApiModelProperty(value = "解绑时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "解绑时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("unbind_time")
    private Date unbindTime;

}