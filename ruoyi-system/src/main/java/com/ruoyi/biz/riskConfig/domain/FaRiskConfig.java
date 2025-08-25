package com.ruoyi.biz.riskConfig.domain;

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

import java.util.List;

/**
 * 风控设置
 *
 * @author ruoyi
 * @date 2024-01-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_risk_config")
@ApiModel(value = "风控设置")
public class FaRiskConfig extends BaseEntity {

private static final long serialVersionUID=1L;

    /** value键值对 */
    @TableField(exist = false)
    List<String[]> valueList;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 变量名 */
    @ApiModelProperty(value = "变量名")
    @Excel(name = "变量名")
    @TableField("name")
    private String name;

    /** 分组 */
    @ApiModelProperty(value = "分组")
    @Excel(name = "分组")
    @TableField("config_group")
    private String configGroup;

    /** 变量标题 */
    @ApiModelProperty(value = "变量标题")
    @Excel(name = "变量标题")
    @TableField("title")
    private String title;

    /** 变量描述 */
    @ApiModelProperty(value = "变量描述")
    @Excel(name = "变量描述")
    @TableField("tip")
    private String tip;

    /** 类型:string,text,int,bool,array,datetime,date,file */
    @ApiModelProperty(value = "类型:string,text,int,bool,array,datetime,date,file")
    @Excel(name = "类型:string,text,int,bool,array,datetime,date,file")
    @TableField("type")
    private String type;

    /** 变量值 */
    @ApiModelProperty(value = "变量值")
    @Excel(name = "变量值")
    @TableField("value")
    private String value;

    /** 变量字典数据 */
    @ApiModelProperty(value = "变量字典数据")
    @Excel(name = "变量字典数据")
    @TableField("content")
    private String content;

    /** 验证规则 */
    @ApiModelProperty(value = "验证规则")
    @Excel(name = "验证规则")
    @TableField("rule")
    private String rule;

    /** 扩展属性 */
    @ApiModelProperty(value = "扩展属性")
    @Excel(name = "扩展属性")
    @TableField("extend")
    private String extend;

    /** 配置 */
    @ApiModelProperty(value = "配置")
    @Excel(name = "配置")
    @TableField("setting")
    private String setting;

    /** 是否可见 */
    @ApiModelProperty(value = "是否可见")
    @Excel(name = "是否可见")
    @TableField("visible")
    private String visible;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

}