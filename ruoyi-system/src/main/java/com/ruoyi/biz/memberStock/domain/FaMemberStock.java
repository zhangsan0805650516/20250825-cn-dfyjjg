package com.ruoyi.biz.memberStock.domain;

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

/**
 * 自选股票
 *
 * @author ruoyi
 * @date 2024-01-19
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_member_stock")
@ApiModel(value = "自选股票")
public class FaMemberStock extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 交易所(1:NSE 2:BSE) */
    @TableField(exist = false)
    private Integer type;

    /** ID */
    @ApiModelProperty(value = "ID")
    @Excel(name = "ID")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 用户ID */
    @ApiModelProperty(value = "用户ID")
    @Excel(name = "用户ID")
    @TableField("member_id")
    private Integer memberId;

    /** 股票ID */
    @ApiModelProperty(value = "股票ID")
    @Excel(name = "股票ID")
    @TableField("stock_id")
    private Integer stockId;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

}