package com.ruoyi.biz.memberImage.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 会员图片
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_member_image")
@ApiModel(value = "会员图片")
public class FaMemberImage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** ID */
    @ApiModelProperty(value = "ID")
    @Excel(name = "ID")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 用户id */
    @ApiModelProperty(value = "用户id")
    @Excel(name = "用户id")
    @TableField("member_id")
    private Integer memberId;

    /** 头像 */
    @ApiModelProperty(value = "头像")
    @Excel(name = "头像")
    @TableField("avatar")
    private String avatar;

    /** 身份证正面照片 */
    @ApiModelProperty(value = "身份证正面照片")
    @Excel(name = "身份证正面照片")
    @TableField("id_card_front_image")
    private String idCardFrontImage;

    /** 身份证反面照片 */
    @ApiModelProperty(value = "身份证反面照片")
    @Excel(name = "身份证反面照片")
    @TableField("id_card_back_image")
    private String idCardBackImage;

    /** 银行卡图片 */
    @ApiModelProperty(value = "银行卡图片")
    @Excel(name = "银行卡图片")
    @TableField("card_image")
    private String cardImage;

    /** 头像地址 */
    @ApiModelProperty(value = "头像地址")
    @Excel(name = "头像地址")
    @TableField("avatar_url")
    private String avatarUrl;

    /** 身份证正面照片地址 */
    @ApiModelProperty(value = "身份证正面照片地址")
    @Excel(name = "身份证正面照片地址")
    @TableField("id_card_front_image_url")
    private String idCardFrontImageUrl;

    /** 身份证反面照片地址 */
    @ApiModelProperty(value = "身份证反面照片地址")
    @Excel(name = "身份证反面照片地址")
    @TableField("id_card_back_image_url")
    private String idCardBackImageUrl;

    /** 银行卡图片地址 */
    @ApiModelProperty(value = "银行卡图片地址")
    @Excel(name = "银行卡图片地址")
    @TableField("card_image_url")
    private String cardImageUrl;

    /** 状态(0未开始 1已开始 2已结束) */
    @ApiModelProperty(value = "状态(0未开始 1已开始 2已结束)")
    @Excel(name = "状态(0未开始 1已开始 2已结束)")
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