package com.ruoyi.biz.holidayConfig.domain;

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

import java.util.Date;

/**
 * 节假日配置
 *
 * @author ruoyi
 * @date 2024-03-12
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_holiday_config")
@ApiModel(value = "节假日配置")
public class FaHolidayConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 节日名称 */
    @ApiModelProperty(value = "节日名称")
    @Excel(name = "节日名称")
    @TableField("holiday_name")
    private String holidayName;

    /** 节日开始日期 */
    @ApiModelProperty(value = "节日开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "节日开始日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("holiday_start")
    private Date holidayStart;

    /** 节日结束日期 */
    @ApiModelProperty(value = "节日结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "节日结束日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("holiday_end")
    private Date holidayEnd;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

}