package com.ruoyi.biz.capitalLog.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.biz.stockHoldDetail.domain.FaStockHoldDetail;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.FaMember;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 资金记录
 *
 * @author ruoyi
 * @date 2024-01-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_capital_log")
@ApiModel(value = "资金记录")
public class FaCapitalLog extends BaseEntity {

    private static final long serialVersionUID=1L;

    /** 用户 */
    @TableField(exist = false)
    private FaStockHoldDetail faStockHoldDetail;

    @TableField(exist = false)
    private String salt;

    /** 用户 */
    @TableField(exist = false)
    private FaMember faMember;

    @TableField(exist = false)
    private String weiyima;

    @TableField(exist = false)
    private String description;

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

    /** 操作的内容 */
    @ApiModelProperty(value = "操作的内容")
    @Excel(name = "操作的内容")
    @TableField("content")
    private String content;

    /** 操作之前金额多少 */
    @ApiModelProperty(value = "操作之前金额多少")
    @Excel(name = "操作之前金额多少")
    @TableField("before_money")
    private BigDecimal beforeMoney;

    /** 操作之后金额多少 */
    @ApiModelProperty(value = "操作之后金额多少")
    @Excel(name = "操作之后金额多少")
    @TableField("later_money")
    private BigDecimal laterMoney;

    /** 操作的金额 */
    @ApiModelProperty(value = "操作的金额")
    @Excel(name = "操作的金额")
    @TableField("money")
    private BigDecimal money;

    @ApiModelProperty(value = "类型(0充值 1提现 " +
            "2普通下单 3普通下单手续费 4印花税 5平仓收益 " +
            "6中签认缴 7提现退回 " +
            "8大宗下单 9大宗下单手续费 10大宗平仓收益 11大宗印花税 " +
            "12配售冻结 13未中签退回 14普通平仓手续费 15大宗卖出手续费 " +
            "16VIP调研下单 17VIP调研下单手续费 18VIP调研平仓收益 19VIP调研印花税 20VIP调研卖出手续费 " +
            "21货币兑换出 22货币兑换入 " +
            "23大宗冻结 24大宗退回 " +
            "25融券下单 26融券下单手续费 27融券平仓收益 28融券印花税 29融券平仓手续费" +
            "30投票冻结 301投票解冻" +
            "31重组下单 32重组下单手续费 33重组平仓收益 34重组印花税 35重组平仓手续费" +
            "40买入集合资产 41赎回集合资产 42赎回退回" +
            "1002资金池增加 1003资金池转出 1004转入资金池 1005资金池转出 1006转入资金池)")
    @Excel(name = "类型()")
    @TableField("type")
    private Integer type;

    /** 方向(0赠 1减) */
    @ApiModelProperty(value = "方向(0赠 1减)")
    @Excel(name = "方向(0赠 1减)")
    @TableField("direct")
    private Integer direct;

    /** 持仓id */
    @ApiModelProperty(value = "持仓id")
    @Excel(name = "持仓id")
    @TableField("order_id")
    private Integer orderId;

    /** 股票id */
    @ApiModelProperty(value = "股票id")
    @Excel(name = "股票id")
    @TableField("stock_id")
    private Integer stockId;

    /** 股票名称 */
    @ApiModelProperty(value = "股票名称")
    @Excel(name = "股票名称")
    @TableField("stock_name")
    private String stockName;

    /** 股票代码 */
    @ApiModelProperty(value = "股票代码")
    @Excel(name = "股票代码")
    @TableField("stock_symbol")
    private String stockSymbol;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

}