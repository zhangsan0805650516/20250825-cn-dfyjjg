package com.ruoyi.biz.strategy.domain;

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
import java.util.List;
import java.util.Map;

/**
 * 策略
 *
 * @author ruoyi
 * @date 2024-01-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_strategy")
@ApiModel(value = "策略")
public class FaStrategy extends BaseEntity {

    private static final long serialVersionUID=1L;

    @TableField(exist = false)
    private String kind;

    @TableField(exist = false)
    private String themeType;

    @TableField(exist = false)
    private String tradeDate;

    @TableField(exist = false)
    List<Map<String, String>> kline;

    @ApiModelProperty(value = "板块类型(1地区 2板块 3概念)")
    @TableField(exist = false)
    private Integer bkType;

    @ApiModelProperty(value = "排序字段(1现价 2涨跌 3涨跌幅 4成交额 5换手率)")
    @TableField(exist = false)
    private Integer sortBy;

    @ApiModelProperty(value = "顺序(1正序 2倒序)")
    @TableField(exist = false)
    private Integer sort;

    @TableField(exist = false)
    private String interval;

    /** 状态类型 */
    @ApiModelProperty(value = "状态类型")
    @TableField(exist = false)
    private String statusType;

    @TableField(exist = false)
    private String queryString;

    /** ID */
    @ApiModelProperty(value = "ID")
    @Excel(name = "ID")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 分类ID */
    @ApiModelProperty(value = "分类ID")
    @Excel(name = "分类ID")
    @TableField("classify_id")
    private Integer classifyId;

    /** 标题 */
    @ApiModelProperty(value = "标题")
    @Excel(name = "标题")
    @TableField("title")
    private String title;

    /** 英文标题 */
    @ApiModelProperty(value = "英文标题")
    @Excel(name = "英文标题")
    @TableField("title_en")
    private String titleEn;

    /** 编码 */
    @ApiModelProperty(value = "编码")
    @Excel(name = "编码")
    @TableField("code")
    private String code;

    /** 完整编码 */
    @ApiModelProperty(value = "完整编码")
    @Excel(name = "完整编码")
    @TableField("all_code")
    private String allCode;

    /** 编码前缀 */
    @ApiModelProperty(value = "编码前缀")
    @Excel(name = "编码前缀")
    @TableField("prefix_code")
    private String prefixCode;

    /** 条件编码 */
    @ApiModelProperty(value = "条件编码")
    @Excel(name = "条件编码")
    @TableField("condition_code")
    private String conditionCode;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-当前价格")
    @Excel(name = "采集-当前价格")
    @TableField("cai_trade")
    private BigDecimal caiTrade;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-涨跌")
    @Excel(name = "采集-涨跌")
    @TableField("cai_pricechange")
    private BigDecimal caiPricechange;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-涨跌幅")
    @Excel(name = "采集-涨跌幅")
    @TableField("cai_changepercent")
    private BigDecimal caiChangepercent;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-买入价")
    @Excel(name = "采集-买入价")
    @TableField("cai_buy")
    private BigDecimal caiBuy;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-卖出价")
    @Excel(name = "采集-卖出价")
    @TableField("cai_sell")
    private BigDecimal caiSell;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-昨收价")
    @Excel(name = "采集-昨收价")
    @TableField("cai_settlement")
    private BigDecimal caiSettlement;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-今开价")
    @Excel(name = "采集-今开价")
    @TableField("cai_open")
    private BigDecimal caiOpen;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-最高价")
    @Excel(name = "采集-最高价")
    @TableField("cai_high")
    private BigDecimal caiHigh;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-最低价")
    @Excel(name = "采集-最低价")
    @TableField("cai_low")
    private BigDecimal caiLow;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-成交量(手)")
    @Excel(name = "采集-成交量(手)")
    @TableField("cai_volume")
    private BigDecimal caiVolume;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-成交额(万)")
    @Excel(name = "采集-成交额(万)")
    @TableField("cai_amount")
    private BigDecimal caiAmount;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-换手率")
    @Excel(name = "采集-换手率")
    @TableField("cai_change_hands")
    private BigDecimal caiChangeHands;

    /** 采集-均价 */
    @ApiModelProperty(value = "采集-均价")
    @Excel(name = "采集-均价")
    @TableField("cai_average")
    private BigDecimal caiAverage;

    /** 采集-涨停价 */
    @ApiModelProperty(value = "采集-涨停价")
    @Excel(name = "采集-涨停价")
    @TableField("cai_limit_up_price")
    private BigDecimal caiLimitUpPrice;

    /** 采集-跌停价 */
    @ApiModelProperty(value = "采集-跌停价")
    @Excel(name = "采集-跌停价")
    @TableField("cai_limit_down_price")
    private BigDecimal caiLimitDownPrice;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-数据时间")
    @Excel(name = "采集-数据时间")
    @TableField("cai_ticktime")
    private Date caiTicktime;

    /** 0开启1关闭 */
    @ApiModelProperty(value = "0开启1关闭")
    @Excel(name = "0开启1关闭")
    @TableField("status")
    private Integer status;

    /** 1:沪2深3创业4北交5科创6基金 */
    @ApiModelProperty(value = "1:沪2深3创业4北交5科创6基金")
    @Excel(name = "1:沪2深3创业4北交5科创6基金")
    @TableField("type")
    private Integer type;

    /** 当天平仓 */
    @ApiModelProperty(value = "当天平仓")
    @Excel(name = "当天平仓")
    @TableField("current_status")
    private Integer currentStatus;

    /** 抢筹状态 */
    @ApiModelProperty(value = "抢筹状态")
    @Excel(name = "抢筹状态")
    @TableField("qc_status")
    private Integer qcStatus;

    /** vip抢筹 */
    @ApiModelProperty(value = "vip抢筹")
    @Excel(name = "vip抢筹")
    @TableField("vip_qc_status")
    private Integer vipQcStatus;

    /** 置顶 */
    @ApiModelProperty(value = "置顶")
    @Excel(name = "置顶")
    @TableField("zhiding")
    private Integer zhiding;

    /** 开启指数交易 0关闭 1开启 */
    @ApiModelProperty(value = "开启指数交易 0关闭 1开启")
    @Excel(name = "开启指数交易 0关闭 1开启")
    @TableField("is_zs")
    private Integer isZs;

    /** 开启配资交易 0关闭 1开启 */
    @ApiModelProperty(value = "开启配资交易 0关闭 1开启")
    @Excel(name = "开启配资交易 0关闭 1开启")
    @TableField("is_pz")
    private Integer isPz;

    /** 开启大宗交易 0关闭 1开启 */
    @ApiModelProperty(value = "开启大宗交易 0关闭 1开启")
    @Excel(name = "开启大宗交易 0关闭 1开启")
    @TableField("is_dz")
    private Integer isDz;

    /** 开启增发交易 0关闭 1开启 */
    @ApiModelProperty(value = "开启增发交易 0关闭 1开启")
    @Excel(name = "开启增发交易 0关闭 1开启")
    @TableField("is_zfa")
    private Integer isZfa;

    /** 增发平仓 */
    @ApiModelProperty(value = "增发平仓")
    @Excel(name = "增发平仓")
    @TableField("ping_day")
    private Integer pingDay;

    /** 增发数量万起 */
    @ApiModelProperty(value = "增发数量万起")
    @Excel(name = "增发数量万起")
    @TableField("zfa_num")
    private Integer zfaNum;

    /** 增发价格 */
    @ApiModelProperty(value = "增发价格")
    @Excel(name = "增发价格")
    @TableField("zfa_price")
    private BigDecimal zfaPrice;

    /** 下单总股数 */
    @ApiModelProperty(value = "下单总股数")
    @Excel(name = "下单总股数")
    @TableField("total_zfa_num")
    private Integer totalZfaNum;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

    /** 是否隐藏 */
    @ApiModelProperty(value = "是否隐藏")
    @Excel(name = "是否隐藏")
    @TableField("is_hide")
    private Integer isHide;

    /** vip抢筹价格 */
    @ApiModelProperty(value = "vip抢筹价格")
    @Excel(name = "vip抢筹价格")
    @TableField("vip_qc_price")
    private BigDecimal vipQcPrice;

    /** vip抢筹密钥 */
    @ApiModelProperty(value = "vip抢筹密钥")
    @Excel(name = "vip抢筹密钥")
    @TableField("vip_qc_condition_code")
    private BigDecimal vipQcConditionCode;

    /** vip抢筹总额 */
    @ApiModelProperty(value = "vip抢筹总额")
    @Excel(name = "vip抢筹总额")
    @TableField("total_qc_num")
    private Integer totalQcNum;

    /** 剩余抢筹数量 */
    @ApiModelProperty(value = "剩余抢筹数量")
    @Excel(name = "剩余抢筹数量")
    @TableField("left_qc_num")
    private Integer leftQcNum;

    /**
     * 买1
     */
    @TableField("buy1")
    private BigDecimal buy1;

    /**
     * 买1量
     */
    @TableField("buy1_num")
    private BigDecimal buy1Num;

    /**
     * 买2
     */
    @TableField("buy2")
    private BigDecimal buy2;

    /**
     * 买2量
     */
    @TableField("buy2_num")
    private BigDecimal buy2Num;

    /**
     * 买3
     */
    @TableField("buy3")
    private BigDecimal buy3;

    /**
     * 买3量
     */
    @TableField("buy3_num")
    private BigDecimal buy3Num;

    /**
     * 买4
     */
    @TableField("buy4")
    private BigDecimal buy4;

    /**
     * 买4量
     */
    @TableField("buy4_num")
    private BigDecimal buy4Num;

    /**
     * 买5
     */
    @TableField("buy5")
    private BigDecimal buy5;

    /**
     * 买5量
     */
    @TableField("buy5_num")
    private BigDecimal buy5Num;

    /**
     * 买总量
     */
    @TableField("buy_total_num")
    private BigDecimal buyTotalNum;

    /**
     * 卖1
     */
    @TableField("sell1")
    private BigDecimal sell1;

    /**
     * 卖1量
     */
    @TableField("sell1_num")
    private BigDecimal sell1Num;

    /**
     * 卖2
     */
    @TableField("sell2")
    private BigDecimal sell2;

    /**
     * 卖2量
     */
    @TableField("sell2_num")
    private BigDecimal sell2Num;

    /**
     * 卖3
     */
    @TableField("sell3")
    private BigDecimal sell3;

    /**
     * 卖3量
     */
    @TableField("sell3_num")
    private BigDecimal sell3Num;

    /**
     * 卖4
     */
    @TableField("sell4")
    private BigDecimal sell4;

    /**
     * 卖4量
     */
    @TableField("sell4_num")
    private BigDecimal sell4Num;

    /**
     * 卖5
     */
    @TableField("sell5")
    private BigDecimal sell5;

    /**
     * 卖5量
     */
    @TableField("sell5_num")
    private BigDecimal sell5Num;

    /**
     * 卖总量
     */
    @TableField("sell_total_num")
    private BigDecimal sellTotalNum;

    /** 上次浏览时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("last_view_time")
    private Date lastViewTime;

    /** 开启融券交易 0关闭 1开启 */
    @ApiModelProperty(value = "开启融券交易 0关闭 1开启")
    @Excel(name = "开启融券交易 0关闭 1开启")
    @TableField("is_rq")
    private Integer isRq;

    /** 可借总量 */
    @ApiModelProperty(value = "可借总量")
    @Excel(name = "可借总量")
    @TableField("borrow_num")
    private Integer borrowNum;

    /** 可借剩余量 */
    @ApiModelProperty(value = "可借剩余量")
    @Excel(name = "可借剩余量")
    @TableField("borrow_num_left")
    private Integer borrowNumLeft;

    /** 可借天数 */
    @ApiModelProperty(value = "可借天数")
    @Excel(name = "可借天数")
    @TableField("borrow_days")
    private Integer borrowDays;

    /** 融券密钥 */
    @ApiModelProperty(value = "融券密钥")
    @Excel(name = "融券密钥")
    @TableField("borrow_code")
    private String borrowCode;

    /** 预约重组开关(0关闭 1开启) */
    @ApiModelProperty(value = "预约重组开关(0关闭 1开启)")
    @Excel(name = "预约重组开关(0关闭 1开启)")
    @TableField("yycz_switch")
    private Integer yyczSwitch;

    /** 预约重组公告 */
    @ApiModelProperty(value = "预约重组公告")
    @Excel(name = "预约重组公告")
    @TableField("yycz_notice")
    private String yyczNotice;

    /** 预约重组总数量 */
    @ApiModelProperty(value = "预约重组总数量")
    @Excel(name = "预约重组总数量")
    @TableField("yycz_total_num")
    private Integer yyczTotalNum;

    /** 预约重组剩余数量 */
    @ApiModelProperty(value = "预约重组剩余数量")
    @Excel(name = "预约重组剩余数量")
    @TableField("yycz_left_num")
    private Integer yyczLeftNum;

    /** 预约重组总名额 */
    @ApiModelProperty(value = "预约重组总名额")
    @Excel(name = "预约重组总名额")
    @TableField("yycz_total_quota")
    private Integer yyczTotalQuota;

    /** 预约重组剩余名额 */
    @ApiModelProperty(value = "预约重组剩余名额")
    @Excel(name = "预约重组剩余名额")
    @TableField("yycz_left_quota")
    private Integer yyczLeftQuota;

}