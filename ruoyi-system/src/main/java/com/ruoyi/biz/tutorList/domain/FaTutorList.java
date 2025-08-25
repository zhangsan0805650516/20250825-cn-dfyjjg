package com.ruoyi.biz.tutorList.domain;

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
 * 分类
 *
 * @author ruoyi
 * @date 2024-10-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_tutor_list")
@ApiModel(value = "分类")
public class FaTutorList extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String queryString;

    @TableField(exist = false)
    private Integer userId;

    @TableField(exist = false)
    private Integer isVote;

    /** $column.columnComment */
    @ApiModelProperty(value = "${comment}")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 导师头像 */
    @ApiModelProperty(value = "导师头像")
    @Excel(name = "导师头像")
    @TableField("tutor_image")
    private String tutorImage;

    /** 导师姓名 */
    @ApiModelProperty(value = "导师姓名")
    @Excel(name = "导师姓名")
    @TableField("tutor_name")
    private String tutorName;

    /** 导师身份证号 */
    @ApiModelProperty(value = "导师身份证号")
    @Excel(name = "导师身份证号")
    @TableField("tutor_id_num")
    private String tutorIdNum;

    /** 导师简介 */
    @ApiModelProperty(value = "导师简介")
    @Excel(name = "导师简介")
    @TableField("tutor_intro")
    private String tutorIntro;

    /** 投票次数 */
    @ApiModelProperty(value = "投票次数")
    @Excel(name = "投票次数")
    @TableField("vote_nums")
    private Integer voteNums;

    /** 金额 */
    @ApiModelProperty(value = "金额")
    @Excel(name = "金额")
    @TableField("money")
    private BigDecimal money;

    /** 权重 */
    @ApiModelProperty(value = "权重")
    @Excel(name = "权重")
    @TableField("weigh")
    private Integer weigh;

    /** 状态(0未签约 1已签约) */
    @ApiModelProperty(value = "状态(0未签约 1已签约)")
    @Excel(name = "状态(0未签约 1已签约)")
    @TableField("status")
    private Integer status;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @Excel(name = "备注")
    @TableField("remarks")
    private String remarks;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

}