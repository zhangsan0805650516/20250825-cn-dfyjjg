package com.ruoyi.biz.memberSysNotice.domain;

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
 * 用户公告
 *
 * @author ruoyi
 * @date 2024-08-20
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_member_sys_notice")
@ApiModel(value = "用户公告")
public class FaMemberSysNotice extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 会员id */
    @ApiModelProperty(value = "会员id")
    @Excel(name = "会员id")
    @TableField("member_id")
    private Integer memberId;

    /** 公告id */
    @ApiModelProperty(value = "公告id")
    @Excel(name = "公告id")
    @TableField("notice_id")
    private Integer noticeId;

    /** 状态 */
    @ApiModelProperty(value = "状态")
    @Excel(name = "状态")
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