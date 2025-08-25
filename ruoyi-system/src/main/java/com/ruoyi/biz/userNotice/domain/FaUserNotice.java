package com.ruoyi.biz.userNotice.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.FaMember;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户消息
 *
 * @author ruoyi
 * @date 2024-01-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_user_notice")
@ApiModel(value = "用户消息")
public class FaUserNotice extends BaseEntity {

    private static final long serialVersionUID=1L;

    @TableField(exist = false)
    private String salt;

    @TableField(exist = false)
    private String weiyima;

    @TableField(exist = false)
    private Integer dailiId;

    @TableField(exist = false)
    private String memberName;

    @TableField(exist = false)
    private String mobile;

    @TableField(exist = false)
    private FaMember faMember;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 用户id */
    @ApiModelProperty(value = "用户id")
    @Excel(name = "用户id")
    @TableField("user_id")
    private Integer userId;

    /** 标题 */
    @ApiModelProperty(value = "标题")
    @Excel(name = "标题")
    @TableField("title")
    private String title;

    /** 内容 */
    @ApiModelProperty(value = "内容")
    @Excel(name = "内容")
    @TableField("content")
    private String content;

    /** 消息类型(0普通 1中签认缴) */
    @ApiModelProperty(value = "消息类型(0普通 1中签 2认缴)")
    @Excel(name = "消息类型(0普通 1中签 2认缴)")
    @TableField("notice_type")
    private Integer noticeType;

    /** 跳转地址 */
    @ApiModelProperty(value = "跳转地址")
    @Excel(name = "跳转地址")
    @TableField("link")
    private String link;

    /** 阅读时间 */
    @ApiModelProperty(value = "阅读时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "阅读时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("read_time")
    private Date readTime;

    /** 是否已读 */
    @ApiModelProperty(value = "是否已读")
    @Excel(name = "是否已读")
    @TableField("is_read")
    private Integer isRead;

    /** 0单个用户 1所有用户 */
    @ApiModelProperty(value = "0单个用户 1所有用户")
    @Excel(name = "0单个用户 1所有用户")
    @TableField("is_all")
    private Integer isAll;

    /** 是否弹窗 */
    @ApiModelProperty(value = "是否弹窗")
    @Excel(name = "是否弹窗")
    @TableField("is_dialog")
    private Integer isDialog;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

}