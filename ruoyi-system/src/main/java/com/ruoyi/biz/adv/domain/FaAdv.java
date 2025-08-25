package com.ruoyi.biz.adv.domain;

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
 * 广告图
 *
 * @author ruoyi
 * @date 2024-01-09
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_adv")
@ApiModel(value = "广告图")
public class FaAdv extends BaseEntity {

private static final long serialVersionUID=1L;

    /** ID */
    @ApiModelProperty(value = "ID")
    @Excel(name = "ID")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 标题 */
    @ApiModelProperty(value = "标题")
    @Excel(name = "标题")
    @TableField("title")
    private String title;

    /** 图片 */
    @ApiModelProperty(value = "图片")
    @Excel(name = "图片")
    @TableField("image")
    private String image;

    /** 描述 */
    @ApiModelProperty(value = "描述")
    @Excel(name = "描述")
    @TableField("description")
    private String description;

    /** 链接 */
    @ApiModelProperty(value = "链接")
    @Excel(name = "链接")
    @TableField("link")
    private String link;

    /** 排序 */
    @ApiModelProperty(value = "排序")
    @Excel(name = "排序")
    @TableField("sort")
    private Integer sort;

    /** 删除时间 */
    @ApiModelProperty(value = "删除时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("delete_time")
    private Date deleteTime;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

}