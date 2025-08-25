package com.ruoyi.biz.newsCatalog.domain;

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
 * 新闻栏目
 *
 * @author ruoyi
 * @date 2024-01-09
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_news_catalog")
@ApiModel(value = "新闻栏目")
public class FaNewsCatalog extends BaseEntity {

private static final long serialVersionUID=1L;

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 栏目 */
    @ApiModelProperty(value = "栏目")
    @Excel(name = "栏目")
    @TableField("title")
    private String title;

    /** 状态:0=隐藏,1=正常 */
    @ApiModelProperty(value = "状态:0=隐藏,1=正常")
    @Excel(name = "状态:0=隐藏,1=正常")
    @TableField("status")
    private Integer status;

    /** 排序 */
    @ApiModelProperty(value = "排序")
    @Excel(name = "排序")
    @TableField("sort")
    private Integer sort;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

}