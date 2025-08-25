package com.ruoyi.biz.sgList.domain;

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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户申购
 *
 * @author ruoyi
 * @date 2024-01-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_stock_sg")
@ApiModel(value = "用户申购")
public class FaSgList extends BaseEntity {

    private static final long serialVersionUID=1L;

    @TableField(exist = false)
    private String salt;

    @TableField(exist = false)
    private String weiyima;

    @TableField(exist = false)
    private BigDecimal fxRate;

    @TableField(exist = false)
    private Integer dailiId;

    @TableField(exist = false)
    private String memberName;

    @TableField(exist = false)
    private String mobile;

    /** 用户 */
    @TableField(exist = false)
    private FaMember faMember;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 申购用户ID */
    @ApiModelProperty(value = "申购用户ID")
    @Excel(name = "申购用户ID")
    @TableField("user_id")
    private Integer userId;

    /** 新股ID */
    @ApiModelProperty(value = "新股ID")
    @Excel(name = "新股ID")
    @TableField("shengou_id")
    private Integer shengouId;

    /** 代码 */
    @ApiModelProperty(value = "代码")
    @Excel(name = "代码")
    @TableField("code")
    private String code;

    /** 完整编码 */
    @ApiModelProperty(value = "完整编码")
    @Excel(name = "完整编码")
    @TableField("all_code")
    private String allCode;

    /** 股票名称 */
    @ApiModelProperty(value = "股票名称")
    @Excel(name = "股票名称")
    @TableField("name")
    private String name;

    /** 申购价 */
    @ApiModelProperty(value = "申购价")
    @Excel(name = "申购价")
    @TableField("sg_fx_price")
    private BigDecimal sgFxPrice;

    /** 市盈率 */
    @ApiModelProperty(value = "市盈率")
    @Excel(name = "市盈率")
    @TableField("sg_hy_rate")
    private BigDecimal sgHyRate;

    /** 申购数量(手) */
    @ApiModelProperty(value = "申购数量(手)")
    @Excel(name = "申购数量(手)")
    @TableField("sg_num")
    private Integer sgNum;

    /** 申购数量(股) */
    @ApiModelProperty(value = "申购数量(股)")
    @Excel(name = "申购数量(股)")
    @TableField("sg_nums")
    private Integer sgNums;

    /** 最大申购 */
    @ApiModelProperty(value = "最大申购")
    @Excel(name = "最大申购")
    @TableField("max_sg")
    private Integer maxSg;

    /** 状态:0=申购中,1=中签,2=未中签,3=弃购 */
    @ApiModelProperty(value = "状态:0=申购中,1=中签,2=未中签,3=弃购")
    @Excel(name = "状态:0=申购中,1=中签,2=未中签,3=弃购")
    @TableField("status")
    private Integer status;

    /** 保证金 */
    @ApiModelProperty(value = "保证金")
    @Excel(name = "保证金")
    @TableField("money")
    private BigDecimal money;

    /** 中签数(股) */
    @ApiModelProperty(value = "中签数(股)")
    @Excel(name = "中签数(股)")
    @TableField("zq_num")
    private Integer zqNum;

    /** 冻结资金 */
    @ApiModelProperty(value = "冻结资金")
    @Excel(name = "冻结资金")
    @TableField("dj_money")
    private BigDecimal djMoney;

    /**  */
    @ApiModelProperty(value = "申购日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "申购日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("sg_sg_date")
    private Date sgSgDate;

    /**  */
    @ApiModelProperty(value = "中签缴款日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "中签缴款日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("sg_zq_jk_date")
    private Date sgZqJkDate;

    /**  */
    @ApiModelProperty(value = "上市日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "上市日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("sg_ss_date")
    private Date sgSsDate;

    /** 认缴 */
    @ApiModelProperty(value = "认缴")
    @Excel(name = "认缴")
    @TableField("renjiao")
    private Integer renjiao;

    /** 认缴时间 */
    @ApiModelProperty(value = "认缴时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "认缴时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("renjiao_time")
    private Date renjiaoTime;

    /** 是否转入持仓0:未转入;1:已转入;2:弃 */
    @ApiModelProperty(value = "是否转入持仓0:未转入;1:已转入;2:弃")
    @Excel(name = "是否转入持仓0:未转入;1:已转入;2:弃")
    @TableField("is_cc")
    private Integer isCc;

    /** 转入持仓时间 */
    @ApiModelProperty(value = "转入持仓时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "转入持仓时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("is_cc_time")
    private Date isCcTime;

    /** 中签后价格 */
    @ApiModelProperty(value = "中签后价格")
    @Excel(name = "中签后价格")
    @TableField("zq_money")
    private BigDecimal zqMoney;

    /** 签或者手个数 签下 主板1签1000 其他一签500  一手100 */
    @ApiModelProperty(value = "签或者手个数 签下 主板1签1000 其他一签500  一手100")
    @Excel(name = "签或者手个数 签下 主板1签1000 其他一签500  一手100")
    @TableField("zq_nums")
    private Integer zqNums;

    /**  */
    @ApiModelProperty(value = "")
    @Excel(name = "")
    @TableField("freeze_profit")
    private BigDecimal freezeProfit;

    /**  */
    @ApiModelProperty(value = "")
    @Excel(name = "")
    @TableField("balance")
    private BigDecimal balance;

    /**  */
    @ApiModelProperty(value = "")
    @Excel(name = "")
    @TableField("old_sg_num")
    private Integer oldSgNum;

    /**  */
    @ApiModelProperty(value = "")
    @Excel(name = "")
    @TableField("old_sg_nums")
    private Integer oldSgNums;

    /** 1沪2深3创5科4京 */
    @ApiModelProperty(value = "1沪2深3创5科4京")
    @Excel(name = "1沪2深3创5科4京")
    @TableField("sg_type")
    private Integer sgType;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

}