package com.ruoyi.biz.nav.domain;

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
 * 导航图标
 *
 * @author ruoyi
 * @date 2024-01-09
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_nav")
@ApiModel(value = "导航图标")
public class FaNav extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 开关状态 */
    @TableField(exist = false)
    private Integer switchStatus;

    /** 开关类型 */
    @TableField(exist = false)
    private String switchType;

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

    /** 链接 */
    @ApiModelProperty(value = "链接")
    @Excel(name = "链接")
    @TableField("link")
    private String link;

    /** 删除时间 */
    @ApiModelProperty(value = "删除时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("delete_time")
    private Date deleteTime;

    /** 1首页菜单 2个人中心 */
    @ApiModelProperty(value = "1首页菜单 2个人中心")
    @Excel(name = "1首页菜单 2个人中心")
    @TableField("type")
    private Integer type;

    /** 跳转类型(0普通页面1权限页面2跳转浏览器) */
    @ApiModelProperty(value = "跳转类型(0普通页面1权限页面2跳转浏览器)")
    @Excel(name = "跳转类型(0普通页面1权限页面2跳转浏览器)")
    @TableField("link_type")
    private Integer linkType;

    /** $column.columnComment */
    @ApiModelProperty(value = "0 显示 1隐藏")
    @Excel(name = "0 显示 1隐藏")
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