package com.ruoyi.biz.stockHoldDetail.domain;

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
 * 持仓明细
 *
 * @author ruoyi
 * @date 2024-01-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_stock_hold_detail")
@ApiModel(value = "持仓明细")
public class FaStockHoldDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String salt;

    @TableField(exist = false)
    private String weiyima;

    /** 卖出数量(股) */
    @ApiModelProperty(value = "卖出数量(股)")
    @Excel(name = "卖出数量(股)")
    @TableField(exist = false)
    private Integer sellNumber;

    @TableField(exist = false)
    private BigDecimal principal;

    @TableField(exist = false)
    private BigDecimal buyFee;

    @TableField(exist = false)
    private BigDecimal sellFee;

    @TableField(exist = false)
    private BigDecimal stampDuty;

    @TableField(exist = false)
    private BigDecimal dzMarketValue;

    @TableField(exist = false)
    private BigDecimal dzProfitLose;

    @TableField(exist = false)
    private BigDecimal fee;

    @TableField(exist = false)
    private BigDecimal currentPrice;

    @TableField(exist = false)
    private BigDecimal profitRate;

    @TableField(exist = false)
    private String forceClosePassword;

    @TableField(exist = false)
    private Integer dailiId;

    @TableField(exist = false)
    private String memberName;

    @TableField(exist = false)
    private String mobile;

    /** 用户 */
    @TableField(exist = false)
    private FaMember faMember;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 申购配售类型(0申购 1配售) */
    @ApiModelProperty(value = "申购配售类型(0申购 1配售)")
    @Excel(name = "申购配售类型(0申购 1配售)")
    @TableField("hold_id")
    private Integer holdId;

    /** 用户id */
    @ApiModelProperty(value = "用户id")
    @Excel(name = "用户id")
    @TableField("member_id")
    private Integer memberId;

    /** 上级id */
    @ApiModelProperty(value = "上级id")
    @Excel(name = "上级id")
    @TableField("pid")
    private Integer pid;

    /** 上级code */
    @ApiModelProperty(value = "上级code")
    @Excel(name = "上级code")
    @TableField("pid_code")
    private String pidCode;

    /** 上级姓名 */
    @ApiModelProperty(value = "上级姓名")
    @Excel(name = "上级姓名")
    @TableField("pid_name")
    private String pidName;

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

    /** 完整编码 */
    @ApiModelProperty(value = "完整编码")
    @Excel(name = "完整编码")
    @TableField("all_code")
    private String allCode;

    /** 1:沪2深3创业4北交5科创6基金 */
    @ApiModelProperty(value = "1:沪2深3创业4北交5科创6基金")
    @Excel(name = "1:沪2深3创业4北交5科创6基金")
    @TableField("stock_type")
    private Integer stockType;

    /** 持有手数(1手=1000股) */
    @ApiModelProperty(value = "持有手数(1手=100股)")
    @Excel(name = "持有手数(1手=100股)")
    @TableField("hold_hand")
    private Integer holdHand;

    /** 持有数量 */
    @ApiModelProperty(value = "持有数量")
    @Excel(name = "持有数量")
    @TableField("hold_number")
    private Integer holdNumber;

    /** 持仓类型(1普通交易 2大宗交易 3配资交易 4指数交易 5期货交易 6基金 7增发) */
    @ApiModelProperty(value = "持仓类型(1普通交易 2大宗交易 3配资交易 4指数交易 5期货交易 6基金 7增发 8融券 9预约重组)")
    @Excel(name = "持仓类型(1普通交易 2大宗交易 3配资交易 4指数交易 5期货交易 6基金 7增发 8融券 9预约重组)")
    @TableField("hold_type")
    private Integer holdType;

    /** 均价 */
    @ApiModelProperty(value = "均价")
    @Excel(name = "均价")
    @TableField("average")
    private BigDecimal average;

    /** 盈亏 */
    @ApiModelProperty(value = "盈亏")
    @Excel(name = "盈亏")
    @TableField("profit_lose")
    private BigDecimal profitLose;

    /** 持仓来源(0普通交易 1新股转持仓) */
    @ApiModelProperty(value = "持仓来源(0普通交易 1新股转持仓)")
    @Excel(name = "持仓来源(0普通交易 1新股转持仓)")
    @TableField("resource_type")
    private Integer resourceType;

    /** 状态（0持有 1清空） */
    @ApiModelProperty(value = "状态")
    @Excel(name = "状态", readConverterExp = "0=持有,1=清空")
    @TableField("status")
    private Integer status;

    /** 删除标记(0否1是) */
    @ApiModelProperty(value = "删除标记(0否1是)")
    @Excel(name = "删除标记(0否1是)")
    @TableField("delete_flag")
    private Integer deleteFlag;

    /** T+N冻结数量 */
    @ApiModelProperty(value = "T+N冻结数量")
    @Excel(name = "T+N冻结数量")
    @TableField("freeze_number")
    private Integer freezeNumber;

    /** T+N剩余冻结时间 */
    @ApiModelProperty(value = "T+N剩余冻结时间")
    @Excel(name = "T+N剩余冻结时间")
    @TableField("freeze_days_left")
    private Integer freezeDaysLeft;

    /** T+N状态(0冻结中 1解冻) */
    @ApiModelProperty(value = "T+N状态(0冻结中 1解冻)")
    @Excel(name = "T+N状态(0冻结中 1解冻)")
    @TableField("freeze_status")
    private Integer freezeStatus;

    /** 申购配售id */
    @ApiModelProperty(value = "申购配售id")
    @Excel(name = "申购配售id")
    @TableField("new_stock_id")
    private Integer newStockId;

    /** 是否上市(0否1是) */
    @ApiModelProperty(value = "是否上市(0否1是)")
    @Excel(name = "是否上市(0否1是)")
    @TableField("is_list")
    private Integer isList;

    /** 是否锁仓(0否1是) */
    @ApiModelProperty(value = "是否锁仓(0否1是)")
    @Excel(name = "是否锁仓(0否1是)")
    @TableField("is_lock")
    private Integer isLock;

    /** 买入价 */
    @ApiModelProperty(value = "买入价")
    @Excel(name = "买入价")
    @TableField("buy_price")
    private BigDecimal buyPrice;

    /** 买入手续费 */
    @ApiModelProperty(value = "买入手续费")
    @Excel(name = "买入手续费")
    @TableField("buy_poundage")
    private BigDecimal buyPoundage;

    @ApiModelProperty(value = "买入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("buy_time")
    private Date buyTime;

    /** 卖出价 */
    @ApiModelProperty(value = "卖出价")
    @Excel(name = "卖出价")
    @TableField("sell_price")
    private BigDecimal sellPrice;

    /** 卖出手续费 */
    @ApiModelProperty(value = "卖出手续费")
    @Excel(name = "卖出手续费")
    @TableField("sell_poundage")
    private BigDecimal sellPoundage;

    /** 卖出印花税 */
    @ApiModelProperty(value = "卖出印花税")
    @Excel(name = "卖出印花税")
    @TableField("sell_stamp_duty")
    private BigDecimal sellStampDuty;

    @ApiModelProperty(value = "卖出时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("sell_time")
    private Date sellTime;

    /** 交易手数(1手=100股) */
    @ApiModelProperty(value = "交易手数(1手=100股)")
    @Excel(name = "交易手数(1手=100股)")
    @TableField("trading_hand")
    private Integer tradingHand;

    /** 交易股数 */
    @ApiModelProperty(value = "交易股数")
    @Excel(name = "交易股数")
    @TableField("trading_number")
    private Integer tradingNumber;

    /** 方向(1买涨 2买跌) */
    @ApiModelProperty(value = "方向(1买涨 2买跌)")
    @Excel(name = "方向(1买涨 2买跌)")
    @TableField("trade_direct")
    private Integer tradeDirect;

    /** 杠杆 */
    @ApiModelProperty(value = "杠杆")
    @Excel(name = "杠杆")
    @TableField("leverage")
    private Integer leverage;

    /** 保证金 */
    @ApiModelProperty(value = "保证金")
    @Excel(name = "保证金")
    @TableField("guarantee_amount")
    private BigDecimal guaranteeAmount;

}