package com.ruoyi.biz.stockTrading.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.biz.strategy.domain.FaStrategy;
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
 * 成交记录
 *
 * @author ruoyi
 * @date 2024-01-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_stock_trading")
@ApiModel(value = "成交记录")
public class FaStockTrading extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String salt;

    @TableField(exist = false)
    private String weiyima;

    @TableField(exist = false)
    private BigDecimal profitLose;

    @TableField(exist = false)
    private String conditionCode;

    @TableField(exist = false)
    private Integer isSimulation;

    /** 持仓类型(0新股 1普通交易 2大宗交易 3VIP调研 4指数交易 5期货交易 6基金 7增发 8融券 9预约重组) */
    @TableField(exist = false)
    private Integer holdType;

    @TableField(exist = false)
    private Integer dailiId;

    @TableField(exist = false)
    private String memberName;

    @TableField(exist = false)
    private String mobile;

    @TableField(exist = false)
    private FaMember faMember;

    @TableField(exist = false)
    private FaStrategy faStrategy;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 用户id */
    @ApiModelProperty(value = "用户id")
    @Excel(name = "用户id")
    @TableField("member_id")
    private Integer memberId;

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

    /** 持仓id */
    @ApiModelProperty(value = "持仓id")
    @Excel(name = "持仓id")
    @TableField("hold_id")
    private Integer holdId;

    /** 日期 */
    @ApiModelProperty(value = "日期")
    @Excel(name = "日期")
    @TableField("date")
    private Integer date;

    /** 时间 */
    @ApiModelProperty(value = "时间")
    @Excel(name = "时间")
    @TableField("time")
    private Integer time;

    /** 成交手数(手=100股) */
    @ApiModelProperty(value = "成交手数(手=100股)")
    @Excel(name = "成交手数(手=100股)")
    @TableField("trading_hand")
    private Integer tradingHand;

    /** 成交数量 */
    @ApiModelProperty(value = "成交数量")
    @Excel(name = "成交数量")
    @TableField("trading_number")
    private Integer tradingNumber;

    /** 成交价格 */
    @ApiModelProperty(value = "成交价格")
    @Excel(name = "成交价格")
    @TableField("trading_price")
    private BigDecimal tradingPrice;

    /** 成交金额 */
    @ApiModelProperty(value = "成交金额")
    @Excel(name = "成交金额")
    @TableField("trading_amount")
    private BigDecimal tradingAmount;

    /** 买卖(1买 2卖) */
    @ApiModelProperty(value = "买卖(1买 2卖)")
    @Excel(name = "买卖(1买 2卖)")
    @TableField("trading_type")
    private Integer tradingType;

    /** 手续费 */
    @ApiModelProperty(value = "手续费")
    @Excel(name = "手续费")
    @TableField("trading_poundage")
    private BigDecimal tradingPoundage;

    /** 印花税 */
    @ApiModelProperty(value = "印花税")
    @Excel(name = "印花税")
    @TableField("stamp_duty")
    private BigDecimal stampDuty;

    /** 状态 */
    @ApiModelProperty(value = "状态")
    @Excel(name = "状态")
    @TableField("status")
    private Integer status;

    /** 删除标记(0否1是) */
    @ApiModelProperty(value = "删除标记(0否1是)")
    @Excel(name = "删除标记(0否1是)")
    @TableField("delete_flag")
    private Integer deleteFlag;

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