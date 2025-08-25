package com.ruoyi.biz.tradeApprove.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 交易审核
 *
 * @author ruoyi
 * @date 2024-05-31
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_trade_approve")
@ApiModel(value = "交易审核")
public class FaTradeApprove extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
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

    /** 交易类型(1大宗 2VIP调研 3申购 4配售 ) */
    @ApiModelProperty(value = "交易类型(1大宗 2VIP调研 3申购 4配售 )")
    @Excel(name = "交易类型(1大宗 2VIP调研 3申购 4配售 )")
    @TableField("trade_type")
    private Integer tradeType;

    /** 关联id */
    @ApiModelProperty(value = "关联id")
    @Excel(name = "关联id")
    @TableField("trade_id")
    private Integer tradeId;

    /** 关联流水id */
    @ApiModelProperty(value = "关联流水id")
    @Excel(name = "关联流水id")
    @TableField("capital_id")
    private Integer capitalId;

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

    /** 成交手数(1手=100股) */
    @ApiModelProperty(value = "成交手数(1手=100股)")
    @Excel(name = "成交手数(1手=100股)")
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

    /** 交易手续费 */
    @ApiModelProperty(value = "交易手续费")
    @Excel(name = "交易手续费")
    @TableField("trading_poundage")
    private BigDecimal tradingPoundage;

    /** 应缴金额 */
    @ApiModelProperty(value = "应缴金额")
    @Excel(name = "应缴金额")
    @TableField("should_pay_amount")
    private BigDecimal shouldPayAmount;

    /** 实缴金额 */
    @ApiModelProperty(value = "实缴金额")
    @Excel(name = "实缴金额")
    @TableField("paid_amount")
    private BigDecimal paidAmount;

    /** 配售状态 */
    @ApiModelProperty(value = "配售状态")
    @Excel(name = "配售状态")
    @TableField("placing_status")
    private Integer placingStatus;

    /** 补缴状态(0无需补缴 1需要补缴 2补缴完成 3废弃) */
    @ApiModelProperty(value = "补缴状态(0无需补缴 1需要补缴 2补缴完成 3废弃)")
    @Excel(name = "补缴状态(0无需补缴 1需要补缴 2补缴完成 3废弃)")
    @TableField("repay_status")
    private Integer repayStatus;

    /** 补缴说明 */
    @ApiModelProperty(value = "补缴说明")
    @Excel(name = "补缴说明")
    @TableField("repay_description")
    private String repayDescription;

    /** 扣款方式 */
    @ApiModelProperty(value = "扣款方式")
    @Excel(name = "扣款方式")
    @TableField("cost_type")
    private Integer costType;

    /** 扣款时刻 */
    @ApiModelProperty(value = "扣款时刻")
    @Excel(name = "扣款时刻")
    @TableField("cost_time")
    private Integer costTime;

    /** 状态(0待审核 1审核通过 2审核驳回) */
    @ApiModelProperty(value = "状态(0待审核 1审核通过 2审核驳回)")
    @Excel(name = "状态(0待审核 1审核通过 2审核驳回)")
    @TableField("status")
    private Integer status;

    /** 驳回原因 */
    @ApiModelProperty(value = "驳回原因")
    @Excel(name = "驳回原因")
    @TableField("reject_reason")
    private String rejectReason;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

}