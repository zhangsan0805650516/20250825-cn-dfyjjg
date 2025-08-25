package com.ruoyi.biz.news.domain;

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
 * 新闻
 *
 * @author ruoyi
 * @date 2024-01-16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_news")
@ApiModel(value = "新闻")
public class FaNews extends BaseEntity {

private static final long serialVersionUID=1L;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 分类id */
    @ApiModelProperty(value = "分类id")
    @Excel(name = "分类id")
    @TableField("catalog_id")
    private Integer catalogId;

    /** 展示方式
mode = 1 垂直排列，略缩图显示大图
mode = 2 垂直排列，图文混排
mode = 3 垂直排列，略缩图显示多图
mode = 4 水平排列，左图右文
mode = 5 水平排列，右图左文
mode = 6 垂直排列，无略缩图，主标题+副标题显示 */
    @ApiModelProperty(value = "展示方式 mode = 1 垂直排列，略缩图显示大图 mode = 2 垂直排列，图文混排 mode = 3 垂直排列，略缩图显示多图 mode = 4 水平排列，左图右文 mode = 5 水平排列，右图左文 mode = 6 垂直排列，无略缩图，主标题+副标题显示")
    @Excel(name = "展示方式 mode = 1 垂直排列，略缩图显示大图 mode = 2 垂直排列，图文混排 mode = 3 垂直排列，略缩图显示多图 mode = 4 水平排列，左图右文 mode = 5 水平排列，右图左文 mode = 6 垂直排列，无略缩图，主标题+副标题显示")
    @TableField("show_mode")
    private Integer showMode;

    /** 新闻id */
    @ApiModelProperty(value = "新闻id")
    @Excel(name = "新闻id")
    @TableField("news_id")
    private String newsId;

    /** 新闻标题 */
    @ApiModelProperty(value = "新闻标题")
    @Excel(name = "新闻标题")
    @TableField("news_title")
    private String newsTitle;

    /** 新闻摘要 */
    @ApiModelProperty(value = "新闻摘要")
    @Excel(name = "新闻摘要")
    @TableField("news_abstract")
    private String newsAbstract;

    /** 新闻内容 */
    @ApiModelProperty(value = "新闻内容")
    @Excel(name = "新闻内容")
    @TableField("news_content")
    private String newsContent;

    /** 新闻图片 */
    @ApiModelProperty(value = "新闻图片")
    @Excel(name = "新闻图片")
    @TableField("news_image")
    private String newsImage;

    /** 发布时间 */
    @ApiModelProperty(value = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("news_time")
    private Date newsTime;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

    /** 是否完成 */
    @ApiModelProperty(value = "是否完成")
    @Excel(name = "是否完成")
    @TableField("is_done")
    private Integer isDone;

}