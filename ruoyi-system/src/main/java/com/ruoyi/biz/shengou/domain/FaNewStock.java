package com.ruoyi.biz.shengou.domain;

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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 新股列表
 *
 * @author ruoyi
 * @date 2024-01-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_new_stock")
@ApiModel(value = "新股列表")
public class FaNewStock extends BaseEntity {

    private static final long serialVersionUID=1L;

    /** 是否申购 0否1是 */
    @TableField(exist = false)
    private Integer isSg;

    /** 0申购 1配售 */
    @TableField(exist = false)
    private Integer sgOrPs;

    @TableField(exist = false)
    private String queryString;

    /** 申购手数 */
    @TableField(exist = false)
    private Integer sgNum;

    /** 用户id */
    @TableField(exist = false)
    private Integer memberId;

    /** 开关状态 */
    @TableField(exist = false)
    private Integer switchStatus;

    /** 开关类型 */
    @TableField(exist = false)
    private String switchType;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id", readConverterExp = "$column.readConverterExp()")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 股票代码 */
    @ApiModelProperty(value = "股票代码")
    @Excel(name = "股票代码")
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

    /** 申购代码 */
    @ApiModelProperty(value = "申购代码")
    @Excel(name = "申购代码")
    @TableField("sg_code")
    private String sgCode;

    /** 发行总数 */
    @ApiModelProperty(value = "发行总数")
    @Excel(name = "发行总数")
    @TableField("fx_num")
    private String fxNum;

    /** 网上发行 */
    @ApiModelProperty(value = "网上发行")
    @Excel(name = "网上发行")
    @TableField("wsfx_num")
    private String wsfxNum;

    /** 申购上限 */
    @ApiModelProperty(value = "申购上限")
    @Excel(name = "申购上限")
    @TableField("sg_limit")
    private String sgLimit;

    /** 订单上限 */
    @ApiModelProperty(value = "订单上限")
    @Excel(name = "订单上限")
    @TableField("dd_limit")
    private String ddLimit;

    /** 顶格申购需配市值 */
    @ApiModelProperty(value = "顶格申购需配市值")
    @Excel(name = "顶格申购需配市值")
    @TableField("dg_limit")
    private String dgLimit;

    /** 发行价格 */
    @ApiModelProperty(value = "发行价格")
    @Excel(name = "发行价格")
    @TableField("fx_price")
    private String fxPrice;

    /** 发行市盈率 */
    @ApiModelProperty(value = "发行市盈率")
    @Excel(name = "发行市盈率")
    @TableField("fx_rate")
    private String fxRate;

    /** 行业市盈率 */
    @ApiModelProperty(value = "行业市盈率")
    @Excel(name = "行业市盈率")
    @TableField("hy_rate")
    private String hyRate;

    /** $column.columnComment */
    @ApiModelProperty(value = "${comment}")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    @TableField("sg_date_int")
    private Integer sgDateInt;

    /** 申购日期 */
    @ApiModelProperty(value = "申购日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "申购日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("sg_date")
    private Date sgDate;

    /** 申购截至日期 */
    @ApiModelProperty(value = "申购截至日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "申购截至日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("sg_end_date")
    private Date sgEndDate;

    /** 申购星期 */
    @ApiModelProperty(value = "申购星期")
    @Excel(name = "申购星期")
    @TableField("sg_date_xq")
    private String sgDateXq;

    /**  中签率(%) */
    @ApiModelProperty(value = " 中签率(%)")
    @Excel(name = " 中签率(%)")
    @TableField("zq_rate")
    private String zqRate;

    /** 中签号 */
    @ApiModelProperty(value = "中签号")
    @Excel(name = "中签号")
    @TableField("zq_no")
    private String zqNo;

    /** 中签缴款日期 */
    @ApiModelProperty(value = "中签缴款日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "中签缴款日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("zq_jk_date")
    private Date zqJkDate;

    /** $column.columnComment */
    @ApiModelProperty(value = "${comment}")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    @TableField("ss_date_int")
    private Integer ssDateInt;

    /** 上市日期 */
    @ApiModelProperty(value = "上市日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "上市日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("ss_date")
    private Date ssDate;

    /** 打新收益(元) */
    @ApiModelProperty(value = "打新收益(元)")
    @Excel(name = "打新收益(元)")
    @TableField("dx_value")
    private String dxValue;

    /** 涨停数量 */
    @ApiModelProperty(value = "涨停数量")
    @Excel(name = "涨停数量")
    @TableField("zt_num")
    private String ztNum;

    /** 状态:0=隐藏,1=正常 */
    @ApiModelProperty(value = "状态:0=隐藏,1=正常")
    @Excel(name = "状态:0=隐藏,1=正常")
    @TableField("status")
    private Integer status;

    /** 打开申购 */
    @ApiModelProperty(value = "打开申购")
    @Excel(name = "打开申购")
    @TableField("sg_switch")
    private Integer sgSwitch;

    /** 申购交易所 */
    @ApiModelProperty(value = "申购交易所")
    @Excel(name = "申购交易所")
    @TableField("sg_exchange")
    private Integer sgExchange;

    /** 线下配售状态 */
    @ApiModelProperty(value = "线下配售状态")
    @Excel(name = "线下配售状态")
    @TableField("xx_switch")
    private Integer xxSwitch;

    /** 配售交易所 */
    @ApiModelProperty(value = "配售交易所")
    @Excel(name = "配售交易所")
    @TableField("xx_exchange")
    private Integer xxExchange;

    /** $column.columnComment */
    @ApiModelProperty(value = "${comment}")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    @TableField("sg_switch_time")
    private Date sgSwitchTime;

    /** $column.columnComment */
    @ApiModelProperty(value = "${comment}")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    @TableField("xx_switch_time")
    private Date xxSwitchTime;

    /** $column.columnComment */
    @ApiModelProperty(value = "${comment}")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    @TableField("content")
    private String content;

    /** $column.columnComment */
    @ApiModelProperty(value = "${comment}")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    @TableField("sg_nums")
    private Integer sgNums;

    /** $column.columnComment */
    @ApiModelProperty(value = "${comment}")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    @TableField("xx_nums")
    private Integer xxNums;

    /** 1沪2深3创5科4京 */
    @ApiModelProperty(value = "1沪2深3创5科4京")
    @Excel(name = "1沪2深3创5科4京")
    @TableField("sg_type")
    private Integer sgType;

    /** $column.columnComment */
    @ApiModelProperty(value = "${comment}")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    @TableField("tn")
    private Integer tn;

    /** 手动设置申购日期 */
    @ApiModelProperty(value = "手动设置申购日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "手动设置申购日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("sg_date_n")
    private Date sgDateN;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

    /** 是否上市 */
    @ApiModelProperty(value = "是否上市")
    @Excel(name = "是否上市")
    @TableField("is_list")
    private Integer isList;

    /** 发行规模 */
    @ApiModelProperty(value = "发行规模")
    @Excel(name = "发行规模")
    @TableField("issue_size")
    private String issueSize;

    /** 发行规模 */
    @ApiModelProperty(value = "发行规模")
    @Excel(name = "发行规模")
    @TableField("nosh_issued")
    private String noshIssued;

    /** 上市交易所(1NSE 2BSE 3NSE+BSE) */
    @ApiModelProperty(value = "上市交易所(1NSE 2BSE 3NSE+BSE)")
    @Excel(name = "上市交易所(1NSE 2BSE 3NSE+BSE)")
    @TableField("listing_exchange")
    private Integer listingExchange;

    /** 配售价格 */
    @ApiModelProperty(value = "配售价格")
    @Excel(name = "配售价格")
    @TableField("ps_price")
    private BigDecimal psPrice;

}