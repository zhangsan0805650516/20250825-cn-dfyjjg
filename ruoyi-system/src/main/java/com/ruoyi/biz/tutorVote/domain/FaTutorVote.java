package com.ruoyi.biz.tutorVote.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.biz.tutorList.domain.FaTutorList;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.FaMember;
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
@TableName("fa_tutor_vote")
@ApiModel(value = "分类")
public class FaTutorVote extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String salt;

    @TableField(exist = false)
    private String mobile;

    @TableField(exist = false)
    private Integer[] ids;

    @TableField(exist = false)
    private String weiyima;

    @TableField(exist = false)
    private Integer voteNums;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String userMobile;

    @TableField(exist = false)
    private String tutorName;

    @TableField(exist = false)
    private FaMember faMember;

    @TableField(exist = false)
    private FaTutorList faTutorList;

    /** $column.columnComment */
    @ApiModelProperty(value = "${comment}")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 用户ID */
    @ApiModelProperty(value = "用户ID")
    @Excel(name = "用户ID")
    @TableField("user_id")
    private Integer userId;

    /** 导师ID */
    @ApiModelProperty(value = "导师ID")
    @Excel(name = "导师ID")
    @TableField("tutor_id")
    private Integer tutorId;

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

    /** 状态(0投票冻结 1已解冻) */
    @ApiModelProperty(value = "状态(0投票冻结 1已解冻)")
    @Excel(name = "状态(0投票冻结 1已解冻)")
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

    /** 类型(0用户投票 1后台加票) */
    @ApiModelProperty(value = "类型(0用户投票 1后台加票)")
    @Excel(name = "类型(0用户投票 1后台加票)")
    @TableField("vote_type")
    private Integer voteType;

}