package com.ruoyi.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员管理
 *
 * @author ruoyi
 * @date 2024-01-04
 */
@Data
public class ExportMemberWithMobile implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Excel(name = "ID")
    private Integer id;

    /** 实名姓名 */
    @Excel(name = "实名姓名")
    private String name;

    /** 手机号 */
    @Excel(name = "手机号")
    private String mobile;

    /** 唯一码 */
    @Excel(name = "唯一码")
    private String weiyima;

    /** 是否实名(0未实名1审核中2审核通过3审核驳回) */
    @Excel(name = "是否实名", readConverterExp = "0=未实名,1=审核中,2=已实名,3=审核驳回")
    private Integer isAuth;

    /** 身份证号码 */
    @Excel(name = "身份证号码")
    private String idCard;

    /** 身份证正面 */
    @Excel(name = "身份证正面")
    private String idCardFrontImage;

    /** 身份证反面 */
    @Excel(name = "身份证反面")
    private String idCardBackImage;

    /** 上级机构码 */
    @Excel(name = "上级机构码")
    private String superiorCode;

    /** 上级姓名 */
    @Excel(name = "上级姓名")
    private String superiorName;

    /** 总资产 */
    @Excel(name = "总资产")
    private BigDecimal total;

    /** 可用资金 */
    @Excel(name = "可用资金")
    private BigDecimal balance;

    /** 占用资金 */
    @Excel(name = "占用资金")
    private BigDecimal takeUp;

    /** 新股冻结 */
    @Excel(name = "新股冻结")
    private BigDecimal newStockFreeze;

    /** 总盈亏 */
    @Excel(name = "总盈亏")
    private BigDecimal totalProfit;

    /** T+1锁定 */
    @Excel(name = "T+1锁定")
    private BigDecimal freezeProfit;

    /** 银行 */
    @Excel(name = "银行")
    private String depositBank;

    /** 收款人姓名 */
    @Excel(name = "收款人姓名")
    private String accountName;

    /** 收款人账户 */
    @Excel(name = "收款人账户")
    private String account;

    /** 登录IP */
    @Excel(name = "登录IP")
    private String loginIp;

    /** 登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    /** 注册时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "注册时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}