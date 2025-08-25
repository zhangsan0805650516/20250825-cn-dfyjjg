package com.ruoyi.biz.attention.domain;

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
@TableName("fa_attention")
@ApiModel(value = "广告图")
public class FaAttention extends BaseEntity {

private static final long serialVersionUID=1L;

    /** ID */
    @ApiModelProperty(value = "ID")
    @Excel(name = "ID")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 用户ID */
    @ApiModelProperty(value = "用户ID")
    @Excel(name = "用户ID")
    @TableField("user_id")
    private Integer userId;

    /** 达人用户ID */
    @ApiModelProperty(value = "达人用户ID")
    @Excel(name = "达人用户ID")
    @TableField("expert_user_id")
    private Integer expertUserId;

    /** 订阅:0=否,1=是 */
    @ApiModelProperty(value = "订阅:0=否,1=是")
    @Excel(name = "订阅:0=否,1=是")
    @TableField("is_attention")
    private Integer isAttention;

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