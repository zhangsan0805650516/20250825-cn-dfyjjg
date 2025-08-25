package com.ruoyi.biz.contractTemplate.domain;

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
 * 合同模板
 *
 * @author ruoyi
 * @date 2024-01-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_contract_template")
@ApiModel(value = "合同模板")
public class FaContractTemplate extends BaseEntity {

private static final long serialVersionUID=1L;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 父ID */
    @ApiModelProperty(value = "父ID")
    @Excel(name = "父ID")
    @TableField("pid")
    private Integer pid;

    /** 合同名称 */
    @ApiModelProperty(value = "合同名称")
    @Excel(name = "合同名称")
    @TableField("name")
    private String name;

    /** 图片 */
    @ApiModelProperty(value = "图片")
    @Excel(name = "图片")
    @TableField("image")
    private String image;

    /** 关键字 */
    @ApiModelProperty(value = "关键字")
    @Excel(name = "关键字")
    @TableField("keywords")
    private String keywords;

    /** 描述 */
    @ApiModelProperty(value = "描述")
    @Excel(name = "描述")
    @TableField("description")
    private String description;

    /** 自定义名称 */
    @ApiModelProperty(value = "自定义名称")
    @Excel(name = "自定义名称")
    @TableField("diy_name")
    private String diyName;

    /** 删除时间 */
    @ApiModelProperty(value = "删除时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("delete_time")
    private Date deleteTime;

    /** 权重 */
    @ApiModelProperty(value = "权重")
    @Excel(name = "权重")
    @TableField("weigh")
    private Integer weigh;

    /** 状态(0正常 1隐藏) */
    @ApiModelProperty(value = "状态(0正常 1隐藏)")
    @Excel(name = "状态(0正常 1隐藏)")
    @TableField("status")
    private Integer status;

    /** 内容 */
    @ApiModelProperty(value = "内容")
    @Excel(name = "内容")
    @TableField("main_content")
    private String mainContent;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

}