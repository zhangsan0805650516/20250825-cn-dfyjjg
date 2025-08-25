package com.ruoyi.biz.contractList.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.biz.contractTemplate.domain.FaContractTemplate;
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
 * 用户合同
 *
 * @author ruoyi
 * @date 2024-01-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_contract_list")
@ApiModel(value = "用户合同")
public class FaContractList extends BaseEntity {

private static final long serialVersionUID=1L;

    @TableField(exist = false)
    private String salt;

    @TableField(exist = false)
    private String mobile;

    @TableField(exist = false)
    private String weiyima;

    @TableField(exist = false)
    private String contractUrl;

    @TableField(exist = false)
    private FaMember faMember;

    @TableField(exist = false)
    private FaContractTemplate faContractTemplate;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 合同模板ID */
    @ApiModelProperty(value = "合同模板ID")
    @Excel(name = "合同模板ID")
    @TableField("template_id")
    private Integer templateId;

    /** 用户ID */
    @ApiModelProperty(value = "用户ID")
    @Excel(name = "用户ID")
    @TableField("user_id")
    private Long userId;

    /** 姓名 */
    @ApiModelProperty(value = "姓名")
    @Excel(name = "姓名")
    @TableField("name")
    private String name;

    /** 身份证号 */
    @ApiModelProperty(value = "身份证号")
    @Excel(name = "身份证号")
    @TableField("id_card")
    private String idCard;

    /** 地址 */
    @ApiModelProperty(value = "地址")
    @Excel(name = "地址")
    @TableField("address")
    private String address;

    /** 内容 */
    @ApiModelProperty(value = "内容")
    @Excel(name = "内容")
    @TableField("main_content")
    private String mainContent;

    /** 签字图片 */
    @ApiModelProperty(value = "签字图片")
    @Excel(name = "签字图片")
    @TableField("image")
    private String image;

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

    /** 状态(0未签约 1已签约) */
    @ApiModelProperty(value = "状态(0未签约 1已签约)")
    @Excel(name = "状态(0未签约 1已签约)")
    @TableField("sign_status")
    private Integer signStatus;

    /** 签约时间 */
    @ApiModelProperty(value = "签约时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "签约时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("sign_date")
    private Date signDate;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

}