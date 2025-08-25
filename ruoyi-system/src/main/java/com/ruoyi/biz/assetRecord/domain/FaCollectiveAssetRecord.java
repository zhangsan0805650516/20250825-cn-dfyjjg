package com.ruoyi.biz.assetRecord.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.biz.collectiveAsset.domain.FaCollectiveAsset;
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
 * 集合资产记录
 *
 * @author ruoyi
 * @date 2025-02-17
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_collective_asset_record")
@ApiModel(value = "集合资产记录")
public class FaCollectiveAssetRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String paymentCode;

    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private String mobile;

    @TableField(exist = false)
    private String weiyima;

    @TableField(exist = false)
    private String assetName;

    /** 用户 */
    @TableField(exist = false)
    private FaMember faMember;

    /** 集合资产 */
    @TableField(exist = false)
    private FaCollectiveAsset faCollectiveAsset;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 用户id */
    @ApiModelProperty(value = "用户id")
    @Excel(name = "用户id")
    @TableField("member_id")
    private Integer memberId;

    /** 资产id */
    @ApiModelProperty(value = "资产id")
    @Excel(name = "资产id")
    @TableField("asset_id")
    private Integer assetId;

    /** 金额 */
    @ApiModelProperty(value = "金额")
    @Excel(name = "金额")
    @TableField("amount")
    private BigDecimal amount;

    /** 类型(1持有 2赎回) */
    @ApiModelProperty(value = "类型(1持有 2赎回)")
    @Excel(name = "类型(1持有 2赎回)")
    @TableField("type")
    private Integer type;

    /** 状态(0待审核 1审核通过 2审核驳回) */
    @ApiModelProperty(value = "状态(0待审核 1审核通过 2审核驳回)")
    @Excel(name = "状态(0待审核 1审核通过 2审核驳回)")
    @TableField("status")
    private Integer status;

    /** 驳回原因 */
    @ApiModelProperty(value = "驳回原因")
    @Excel(name = "驳回原因")
    @TableField("reject_reason")
    private String rejectReason;

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