package com.ruoyi.biz.exchangeConfig.domain;

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
 * 交易所配置
 *
 * @author ruoyi
 * @date 2024-03-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_exchange_config")
@ApiModel(value = "交易所配置")
public class FaExchangeConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 交易所类型 */
    @ApiModelProperty(value = "交易所类型")
    @Excel(name = "交易所类型")
    @TableField("exchange_type")
    private Integer exchangeType;

    /** 上午交易时间 */
    @ApiModelProperty(value = "上午交易时间")
    @Excel(name = "上午交易时间")
    @TableField("trading_time_morning")
    private String tradingTimeMorning;

    /** 下午交易时间 */
    @ApiModelProperty(value = "下午交易时间")
    @Excel(name = "下午交易时间")
    @TableField("trading_time_afternoon")
    private String tradingTimeAfternoon;

    /** 上午申购时间 */
    @ApiModelProperty(value = "上午申购时间")
    @Excel(name = "上午申购时间")
    @TableField("subscribe_time_morning")
    private String subscribeTimeMorning;

    /** 下午申购时间 */
    @ApiModelProperty(value = "下午申购时间")
    @Excel(name = "下午申购时间")
    @TableField("subscribe_time_afternoon")
    private String subscribeTimeAfternoon;

    /** 上午配售时间 */
    @ApiModelProperty(value = "上午配售时间")
    @Excel(name = "上午配售时间")
    @TableField("placement_time_morning")
    private String placementTimeMorning;

    /** 下午配售时间 */
    @ApiModelProperty(value = "下午配售时间")
    @Excel(name = "下午配售时间")
    @TableField("placement_time_afternoon")
    private String placementTimeAfternoon;

    /** 上午大宗交易时间 */
    @ApiModelProperty(value = "上午大宗交易时间")
    @Excel(name = "上午大宗交易时间")
    @TableField("block_time_morning")
    private String blockTimeMorning;

    /** 下午大宗交易时间 */
    @ApiModelProperty(value = "下午大宗交易时间")
    @Excel(name = "下午大宗交易时间")
    @TableField("block_time_afternoon")
    private String blockTimeAfternoon;

    /** 涨停百分比 */
    @ApiModelProperty(value = "涨停百分比")
    @Excel(name = "涨停百分比")
    @TableField("limit_up")
    private BigDecimal limitUp;

    /** 跌停百分比 */
    @ApiModelProperty(value = "跌停百分比")
    @Excel(name = "跌停百分比")
    @TableField("limit_down")
    private BigDecimal limitDown;

    /** 状态 */
    @ApiModelProperty(value = "状态")
    @Excel(name = "状态")
    @TableField("status")
    private Integer status;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

}