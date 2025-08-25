package com.ruoyi.biz.collectiveAsset.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.biz.assetRecord.domain.FaCollectiveAssetRecord;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * 集合资产
 *
 * @author ruoyi
 * @date 2025-02-17
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_collective_asset")
@ApiModel(value = "集合资产")
public class FaCollectiveAsset extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private Integer memberId;

    /** 个人资产可赎回总额 */
    @TableField(exist = false)
    private BigDecimal assetRedeemAmountPerson;

    /** 个人资产正在赎回总额 */
    @TableField(exist = false)
    private BigDecimal redeemTotalAmountPerson;

    /** 资产买入记录 */
    @TableField(exist = false)
    private List<FaCollectiveAssetRecord> collectiveAssetRecords;

    /** 排序(0时间 1金额) */
    @TableField(exist = false)
    private Integer orderBy;

    /** 是否需要输入密码 0否 1是 */
    @TableField(exist = false)
    private Integer needSecret;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 资产名称 */
    @ApiModelProperty(value = "资产名称")
    @Excel(name = "资产名称")
    @TableField("asset_name")
    private String assetName;

    /** 资产编码 */
    @ApiModelProperty(value = "资产编码")
    @Excel(name = "资产编码")
    @TableField("asset_code")
    private String assetCode;

    /** 资产总额 */
    @ApiModelProperty(value = "资产总额")
    @Excel(name = "资产总额")
    @TableField("asset_total_amount")
    private BigDecimal assetTotalAmount;

    /** 售出总额 */
    @ApiModelProperty(value = "售出总额")
    @Excel(name = "售出总额")
    @TableField("sell_total_amount")
    private BigDecimal sellTotalAmount;

    /** 资产可赎回总额 */
    @ApiModelProperty(value = "资产可赎回总额")
    @Excel(name = "资产可赎回总额")
    @TableField("asset_redeem_amount")
    private BigDecimal assetRedeemAmount;

    /** 赎回总额 */
    @ApiModelProperty(value = "赎回总额")
    @Excel(name = "赎回总额")
    @TableField("redeem_total_amount")
    private BigDecimal redeemTotalAmount;

    /** 资产密钥 */
    @ApiModelProperty(value = "资产密钥")
    @Excel(name = "资产密钥")
    @TableField("asset_secret")
    private String assetSecret;

    /** 收益率 */
    @ApiModelProperty(value = "收益率")
    @Excel(name = "收益率")
    @TableField("earning_rate")
    private BigDecimal earningRate;

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