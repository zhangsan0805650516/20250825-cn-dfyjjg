package com.ruoyi.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
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
@TableName("fa_member")
@ApiModel(value = "会员管理")
public class FaMember extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 资金池转账时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private Date createTimeFundPool;

    @TableField(exist = false)
    private BigDecimal money;

    @TableField(exist = false)
    private Integer mobileDecrypt;

    /** 资金池余额 */
    @TableField(exist = false)
    private BigDecimal zjcBalance;

    @TableField(exist = false)
    private Integer sysbankId;

    @TableField(exist = false)
    private Integer payType;

    /** 总资产 */
    @TableField(exist = false)
    private BigDecimal total;

    /** 占用资金 */
    @TableField(exist = false)
    private BigDecimal takeUp;

    /** 新股冻结 */
    @TableField(exist = false)
    private BigDecimal newStockFreeze;

    /** 总盈亏 */
    @TableField(exist = false)
    private BigDecimal totalProfit;

    @TableField(exist = false)
    private String kfUrl;

    @TableField(exist = false)
    private Integer[] ids;

    @TableField(exist = false)
    private String confirmPassword;

    @TableField(exist = false)
    private Integer direct;

    @TableField(exist = false)
    private Integer rechargeType;

    @TableField(exist = false)
    private BigDecimal amount;

    @TableField(exist = false)
    private String queryString;

    /** 状态类型 */
    @ApiModelProperty(value = "状态类型")
    @TableField(exist = false)
    private String statusType;

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

    /** 密码 */
    @ApiModelProperty(value = "密码")
    @Excel(name = "密码")
    @TableField("password")
    private String password;

    /** 密码盐 */
    @ApiModelProperty(value = "密码盐")
    @Excel(name = "密码盐")
    @TableField("salt")
    private String salt;

    /** 手机号 */
    @ApiModelProperty(value = "手机号")
    @Excel(name = "手机号")
    @TableField("mobile")
    private String mobile;

    /** 头像 */
    @ApiModelProperty(value = "头像")
    @Excel(name = "头像")
    @TableField("avatar")
    private String avatar;

    /** 连续登录天数 */
    @ApiModelProperty(value = "连续登录天数")
    @Excel(name = "连续登录天数")
    @TableField("successions")
    private Integer successions;

    /** 最大连续登录天数 */
    @ApiModelProperty(value = "最大连续登录天数")
    @Excel(name = "最大连续登录天数")
    @TableField("max_successions")
    private Integer maxSuccessions;

    /** 上次登录时间 */
    @ApiModelProperty(value = "上次登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "上次登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("prev_time")
    private Date prevTime;

    /** 登录时间 */
    @ApiModelProperty(value = "登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("login_time")
    private Date loginTime;

    /** 登录IP */
    @ApiModelProperty(value = "登录IP")
    @Excel(name = "登录IP")
    @TableField("login_ip")
    private String loginIp;

    /** 支付密码 */
    @ApiModelProperty(value = "支付密码")
    @Excel(name = "支付密码")
    @TableField("payment_code")
    private String paymentCode;

    /** 加入IP */
    @ApiModelProperty(value = "加入IP")
    @Excel(name = "加入IP")
    @TableField("join_ip")
    private String joinIp;

    /** 加入时间 */
    @ApiModelProperty(value = "加入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "加入时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("join_time")
    private Date joinTime;

    /** 状态 */
    @ApiModelProperty(value = "状态")
    @Excel(name = "状态")
    @TableField("status")
    private Integer status;

    /** 可用余额 */
    @ApiModelProperty(value = "可用余额")
    @Excel(name = "可用余额")
    @TableField("balance")
    private BigDecimal balance;

    /** 上级姓名 */
    @ApiModelProperty(value = "上级姓名")
    @Excel(name = "上级姓名")
    @TableField("superior_name")
    private String superiorName;

    /** 上级邀请码 */
    @ApiModelProperty(value = "上级邀请码")
    @Excel(name = "上级邀请码")
    @TableField("superior_code")
    private String superiorCode;

    /** 上级id */
    @ApiModelProperty(value = "上级id")
    @Excel(name = "上级id")
    @TableField("superior_id")
    private Integer superiorId;

    /** 机构号 */
    @ApiModelProperty(value = "机构号")
    @Excel(name = "机构号")
    @TableField("institution_number")
    private String institutionNumber;

    /** 代理管理员ID */
    @ApiModelProperty(value = "代理管理员ID")
    @Excel(name = "代理管理员ID")
    @TableField("daili_id")
    private Long dailiId;

    /** 禁止交易 */
    @ApiModelProperty(value = "禁止交易")
    @Excel(name = "禁止交易")
    @TableField("jingzhijiaoyi")
    private Integer jingzhijiaoyi;

    /** 开启配资 */
    @ApiModelProperty(value = "开启配资")
    @Excel(name = "开启配资")
    @TableField("is_pz")
    private Integer isPz;

    /** 开启大宗 */
    @ApiModelProperty(value = "开启大宗")
    @Excel(name = "开启大宗")
    @TableField("is_dz")
    private Integer isDz;

    /** 开启配售 */
    @ApiModelProperty(value = "开启配售")
    @Excel(name = "开启配售")
    @TableField("is_ps")
    private Integer isPs;

    /** 开启申购 */
    @ApiModelProperty(value = "开启申购")
    @Excel(name = "开启申购")
    @TableField("is_sg")
    private Integer isSg;

    /** 开启指数 */
    @ApiModelProperty(value = "开启指数")
    @Excel(name = "开启指数")
    @TableField("is_zs")
    private Integer isZs;

    /** 开启抢筹 */
    @ApiModelProperty(value = "开启抢筹")
    @Excel(name = "开启抢筹")
    @TableField("is_qc")
    private Integer isQc;

    /** 开启期货 */
    @ApiModelProperty(value = "开启期货")
    @Excel(name = "开启期货")
    @TableField("is_qh")
    private Integer isQh;

    /** 开启VIP调研 */
    @ApiModelProperty(value = "开启VIP调研")
    @Excel(name = "开启VIP调研")
    @TableField("is_vip")
    private Integer isVip;

    /** 是否开启消息通知 */
    @ApiModelProperty(value = "是否开启消息通知")
    @Excel(name = "是否开启消息通知")
    @TableField("is_inform")
    private Integer isInform;

    /** 收益冻结 */
    @ApiModelProperty(value = "收益冻结")
    @Excel(name = "收益冻结")
    @TableField("freeze_profit")
    private BigDecimal freezeProfit;

    /** 唯一码 */
    @ApiModelProperty(value = "唯一码")
    @Excel(name = "唯一码")
    @TableField("weiyima")
    private String weiyima;

    /** 认证姓名 */
    @ApiModelProperty(value = "认证姓名")
    @Excel(name = "认证姓名")
    @TableField("name")
    private String name;

    /** 身份证号码 */
    @ApiModelProperty(value = "身份证号码")
    @Excel(name = "身份证号码")
    @TableField("id_card")
    private String idCard;

    /** 身份证正面照片 */
    @ApiModelProperty(value = "身份证正面照片")
    @Excel(name = "身份证正面照片")
    @TableField("id_card_front_image")
    private String idCardFrontImage;

    /** 身份证反面照片 */
    @ApiModelProperty(value = "身份证反面照片")
    @Excel(name = "身份证反面照片")
    @TableField("id_card_back_image")
    private String idCardBackImage;

    /** 提交认证时间 */
    @ApiModelProperty(value = "提交认证时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "提交认证时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("submit_auth_time")
    private Date submitAuthTime;

    /** 认证时间 */
    @ApiModelProperty(value = "认证时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "认证时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("auth_time")
    private Date authTime;

    /** 是否实名(0未实名1审核中2审核通过3审核驳回) */
    @ApiModelProperty(value = "是否实名(0未实名1审核中2审核通过3审核驳回)")
    @Excel(name = "是否实名(0未实名1审核中2审核通过3审核驳回)")
    @TableField("is_auth")
    private Integer isAuth;

    /** 驳回原因 */
    @ApiModelProperty(value = "驳回原因")
    @Excel(name = "驳回原因")
    @TableField("auth_reject_reason")
    private String authRejectReason;

    /** 驳回时间 */
    @ApiModelProperty(value = "驳回时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "驳回时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("auth_reject_time")
    private Date authRejectTime;

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

    /** 银行卡图片 */
    @ApiModelProperty(value = "银行卡图片")
    @Excel(name = "银行卡图片")
    @TableField("card_image")
    private String cardImage;

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

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

    /** 银行卡审核(0未审核1审核中2审核通过3审核驳回) */
    @ApiModelProperty(value = "银行卡审核(0未审核1审核中2审核通过3审核驳回)")
    @Excel(name = "银行卡审核(0未审核1审核中2审核通过3审核驳回)")
    @TableField("bank_card_auth")
    private Integer bankCardAuth;

    /** 登录域名 */
    @ApiModelProperty(value = "登录域名")
    @Excel(name = "登录域名")
    @TableField("login_domain")
    private String loginDomain;

    /** 提现开关(0关 1开) */
    @ApiModelProperty(value = "提现开关(0关 1开)")
    @Excel(name = "提现开关(0关 1开)")
    @TableField("withdraw_switch")
    private Integer withdrawSwitch;

    /** 手机号 */
    @ApiModelProperty(value = "手机号")
    @Excel(name = "手机号")
    @TableField(select = false)
    private String phoneNo;
}