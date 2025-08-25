package com.ruoyi.biz.strategy.domain;

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
@ApiModel(value = "策略")
public class StrategyString extends BaseEntity {

    private static final long serialVersionUID=1L;

    private String kind;

    private String themeType;

    private String tradeDate;

    List<Map<String, String>> kline;

    @ApiModelProperty(value = "板块类型(1地区 2板块 3概念)")
    private Integer bkType;

    @ApiModelProperty(value = "排序字段(1现价 2涨跌 3涨跌幅 4成交额 5换手率)")
    private Integer sortBy;

    @ApiModelProperty(value = "顺序(1正序 2倒序)")
    private Integer sort;

    private String interval;

    /** 状态类型 */
    @ApiModelProperty(value = "状态类型")
    private String statusType;

    private String queryString;

    /** ID */
    @ApiModelProperty(value = "ID")
    @Excel(name = "ID")
    private Integer id;

    /** 分类ID */
    @ApiModelProperty(value = "分类ID")
    @Excel(name = "分类ID")
    private Integer classifyId;

    /** 标题 */
    @ApiModelProperty(value = "标题")
    @Excel(name = "标题")
    private String title;

    /** 英文标题 */
    @ApiModelProperty(value = "英文标题")
    @Excel(name = "英文标题")
    private String titleEn;

    /** 编码 */
    @ApiModelProperty(value = "编码")
    @Excel(name = "编码")
    private String code;

    /** 完整编码 */
    @ApiModelProperty(value = "完整编码")
    @Excel(name = "完整编码")
    private String allCode;

    /** 编码前缀 */
    @ApiModelProperty(value = "编码前缀")
    @Excel(name = "编码前缀")
    private String prefixCode;

    /** 条件编码 */
    @ApiModelProperty(value = "条件编码")
    @Excel(name = "条件编码")
    private String conditionCode;

    /** 今开 */
    @ApiModelProperty(value = "今开")
    @Excel(name = "今开")
    private BigDecimal today;

    /** 昨收 */
    @ApiModelProperty(value = "昨收")
    @Excel(name = "昨收")
    private BigDecimal yesterday;

    /** 最高 */
    @ApiModelProperty(value = "最高")
    @Excel(name = "最高")
    private BigDecimal mostHigh;

    /** 最低 */
    @ApiModelProperty(value = "最低")
    @Excel(name = "最低")
    private BigDecimal mostLow;

    /** 股票数 */
    @ApiModelProperty(value = "股票数")
    @Excel(name = "股票数")
    private Integer stockNum;

    /** 当前价 */
    @ApiModelProperty(value = "当前价")
    @Excel(name = "当前价")
    private BigDecimal currentPrice;

    /** 买入价 */
    @ApiModelProperty(value = "买入价")
    @Excel(name = "买入价")
    private BigDecimal buyPrice;

    /** 止盈价 */
    @ApiModelProperty(value = "止盈价")
    @Excel(name = "止盈价")
    private BigDecimal profitPrice;

    /** 涨止盈百分比 */
    @ApiModelProperty(value = "涨止盈百分比")
    @Excel(name = "涨止盈百分比")
    private BigDecimal stopProfitPrice;

    /** 止损价 */
    @ApiModelProperty(value = "止损价")
    @Excel(name = "止损价")
    private BigDecimal losePrice;

    /** 涨止损百分比 */
    @ApiModelProperty(value = "涨止损百分比")
    @Excel(name = "涨止损百分比")
    private BigDecimal stopLosePrice;

    /** 收益率 */
    @ApiModelProperty(value = "收益率")
    @Excel(name = "收益率")
    private BigDecimal yield;

    /** 推荐信用金 */
    @ApiModelProperty(value = "推荐信用金")
    @Excel(name = "推荐信用金")
    private BigDecimal recommendCreditMoney;

    /** 选中信用金 */
    @ApiModelProperty(value = "选中信用金")
    @Excel(name = "选中信用金")
    private BigDecimal selectCreditMoney;

    /** 赔率 */
    @ApiModelProperty(value = "赔率")
    @Excel(name = "赔率")
    private BigDecimal multiplying;

    /** 可买股数描述 */
    @ApiModelProperty(value = "可买股数描述")
    @Excel(name = "可买股数描述")
    private String canBuyDesc;

    /** 自动续期描述 */
    @ApiModelProperty(value = "自动续期描述")
    @Excel(name = "自动续期描述")
    private String autoRenewalDesc;

    /** 综合费用描述 */
    @ApiModelProperty(value = "综合费用描述")
    @Excel(name = "综合费用描述")
    private String allMoneyDesc;

    /** 综合费用 */
    @ApiModelProperty(value = "综合费用")
    @Excel(name = "综合费用")
    private BigDecimal allMoney;

    /** 可买股数 */
    @ApiModelProperty(value = "可买股数")
    @Excel(name = "可买股数")
    private Integer canBuy;

    /** 增发数量 */
    @ApiModelProperty(value = "增发数量")
    @Excel(name = "增发数量")
    private Integer makeBargainNum;

    /** 成交额单位亿 */
    @ApiModelProperty(value = "成交额单位亿")
    @Excel(name = "成交额单位亿")
    private BigDecimal makeBargainMoney;

    /** 市盈率 */
    @ApiModelProperty(value = "市盈率")
    @Excel(name = "市盈率")
    private BigDecimal cityProfit;

    /** 市净率 */
    @ApiModelProperty(value = "市净率")
    @Excel(name = "市净率")
    private BigDecimal cityClean;

    /** 换手率 */
    @ApiModelProperty(value = "换手率")
    @Excel(name = "换手率")
    private BigDecimal changeHands;

    /** 最低单位亿 */
    @ApiModelProperty(value = "最低单位亿")
    @Excel(name = "最低单位亿")
    private BigDecimal minValue;

    /** 自动续费设置:0=否,1=是 */
    @ApiModelProperty(value = "自动续费设置:0=否,1=是")
    @Excel(name = "自动续费设置:0=否,1=是")
    private Integer isAutoMoney;

    /** 市值 */
    @ApiModelProperty(value = "市值")
    @Excel(name = "市值")
    private BigDecimal cityValue;

    /** 删除时间 */
    @ApiModelProperty(value = "删除时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date deleteTime;

    /** 1 涨 2跌 */
    @ApiModelProperty(value = "1 涨 2跌")
    @Excel(name = "1 涨 2跌")
    private Integer isRise;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-当前价格")
    @Excel(name = "采集-当前价格")
    private String caiTrade;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-涨跌")
    @Excel(name = "采集-涨跌")
    private String caiPricechange;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-涨跌幅")
    @Excel(name = "采集-涨跌幅")
    private String caiChangepercent;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-买入价")
    @Excel(name = "采集-买入价")
    private String caiBuy;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-卖出价")
    @Excel(name = "采集-卖出价")
    private String caiSell;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-昨收价")
    @Excel(name = "采集-昨收价")
    private String caiSettlement;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-今开价")
    @Excel(name = "采集-今开价")
    private String caiOpen;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-最高价")
    @Excel(name = "采集-最高价")
    private String caiHigh;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-最低价")
    @Excel(name = "采集-最低价")
    private String caiLow;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-成交量(手)")
    @Excel(name = "采集-成交量(手)")
    private String caiVolume;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-成交额(万)")
    @Excel(name = "采集-成交额(万)")
    private String caiAmount;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-换手率")
    @Excel(name = "采集-换手率")
    private String caiChangeHands;

    /** 采集-均价 */
    @ApiModelProperty(value = "采集-均价")
    @Excel(name = "采集-均价")
    private String caiAverage;

    /** 采集-涨停价 */
    @ApiModelProperty(value = "采集-涨停价")
    @Excel(name = "采集-涨停价")
    private String caiLimitUpPrice;

    /** 采集-跌停价 */
    @ApiModelProperty(value = "采集-跌停价")
    @Excel(name = "采集-跌停价")
    private String caiLimitDownPrice;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-数据时间")
    @Excel(name = "采集-数据时间")
    private Date caiTicktime;

    /** 0开启1关闭 */
    @ApiModelProperty(value = "0开启1关闭")
    @Excel(name = "0开启1关闭")
    private Integer status;

    /** 0开启1关闭 */
    @ApiModelProperty(value = "0开启1关闭")
    @Excel(name = "0开启1关闭")
    private Integer lockStatus;

    /** 1:沪2深3创业4北交5科创6基金 */
    @ApiModelProperty(value = "1:沪2深3创业4北交5科创6基金")
    @Excel(name = "1:沪2深3创业4北交5科创6基金")
    private Integer type;

    /** 当天平仓 */
    @ApiModelProperty(value = "当天平仓")
    @Excel(name = "当天平仓")
    private Integer currentStatus;

    /** 抢筹状态 */
    @ApiModelProperty(value = "抢筹状态")
    @Excel(name = "抢筹状态")
    private Integer qcStatus;

    /** vip抢筹 */
    @ApiModelProperty(value = "vip抢筹")
    @Excel(name = "vip抢筹")
    private Integer vipQcStatus;

    /** 置顶 */
    @ApiModelProperty(value = "置顶")
    @Excel(name = "置顶")
    private Integer zhiding;

    /** 0:非指数 1指数 */
    @ApiModelProperty(value = "0:非指数 1指数")
    @Excel(name = "0:非指数 1指数")
    private Integer zsType;

    /** 排序字段 */
    @ApiModelProperty(value = "排序字段")
    @Excel(name = "排序字段")
    private Integer orderFlag;

    /** 是否热门 */
    @ApiModelProperty(value = "是否热门")
    @Excel(name = "是否热门")
    private Integer isHot;

    /** 开启指数交易 0关闭 1开启 */
    @ApiModelProperty(value = "开启指数交易 0关闭 1开启")
    @Excel(name = "开启指数交易 0关闭 1开启")
    private Integer isZs;

    /** 开启配资交易 0关闭 1开启 */
    @ApiModelProperty(value = "开启配资交易 0关闭 1开启")
    @Excel(name = "开启配资交易 0关闭 1开启")
    private Integer isPz;

    /** 开启大宗交易 0关闭 1开启 */
    @ApiModelProperty(value = "开启大宗交易 0关闭 1开启")
    @Excel(name = "开启大宗交易 0关闭 1开启")
    private Integer isDz;

    /** 开启增发交易 0关闭 1开启 */
    @ApiModelProperty(value = "开启增发交易 0关闭 1开启")
    @Excel(name = "开启增发交易 0关闭 1开启")
    private Integer isZfa;

    /** 增发平仓 */
    @ApiModelProperty(value = "增发平仓")
    @Excel(name = "增发平仓")
    private Integer pingDay;

    /** 增发数量万起 */
    @ApiModelProperty(value = "增发数量万起")
    @Excel(name = "增发数量万起")
    private Integer zfaNum;

    /** 增发价格 */
    @ApiModelProperty(value = "增发价格")
    @Excel(name = "增发价格")
    private BigDecimal zfaPrice;

    /** 下单总股数 */
    @ApiModelProperty(value = "下单总股数")
    @Excel(name = "下单总股数")
    private Integer totalZfaNum;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    private Integer deleteFlag;

    /** 是否隐藏 */
    @ApiModelProperty(value = "是否隐藏")
    @Excel(name = "是否隐藏")
    private Integer isHide;

    /** vip抢筹价格 */
    @ApiModelProperty(value = "vip抢筹价格")
    @Excel(name = "vip抢筹价格")
    private BigDecimal vipQcPrice;

    /** vip抢筹密钥 */
    @ApiModelProperty(value = "vip抢筹密钥")
    @Excel(name = "vip抢筹密钥")
    private BigDecimal vipQcConditionCode;

    /** vip抢筹总额 */
    @ApiModelProperty(value = "vip抢筹总额")
    @Excel(name = "vip抢筹总额")
    private Integer totalQcNum;

    /** 剩余抢筹数量 */
    @ApiModelProperty(value = "剩余抢筹数量")
    @Excel(name = "剩余抢筹数量")
    private Integer leftQcNum;

    /**
     * 买1
     */
    private BigDecimal buy1;

    /**
     * 买1量
     */
    private BigDecimal buy1Num;

    /**
     * 买2
     */
    private BigDecimal buy2;

    /**
     * 买2量
     */
    private BigDecimal buy2Num;

    /**
     * 买3
     */
    private BigDecimal buy3;

    /**
     * 买3量
     */
    private BigDecimal buy3Num;

    /**
     * 买4
     */
    private BigDecimal buy4;

    /**
     * 买4量
     */
    private BigDecimal buy4Num;

    /**
     * 买5
     */
    private BigDecimal buy5;

    /**
     * 买5量
     */
    private BigDecimal buy5Num;

    /**
     * 买总量
     */
    private BigDecimal buyTotalNum;

    /**
     * 卖1
     */
    private BigDecimal sell1;

    /**
     * 卖1量
     */
    private BigDecimal sell1Num;

    /**
     * 卖2
     */
    private BigDecimal sell2;

    /**
     * 卖2量
     */
    private BigDecimal sell2Num;

    /**
     * 卖3
     */
    private BigDecimal sell3;

    /**
     * 卖3量
     */
    private BigDecimal sell3Num;

    /**
     * 卖4
     */
    private BigDecimal sell4;

    /**
     * 卖4量
     */
    private BigDecimal sell4Num;

    /**
     * 卖5
     */
    private BigDecimal sell5;

    /**
     * 卖5量
     */
    private BigDecimal sell5Num;

    /**
     * 卖总量
     */
    private BigDecimal sellTotalNum;

    /** 上次浏览时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastViewTime;

    /** 开启融券交易 0关闭 1开启 */
    @ApiModelProperty(value = "开启融券交易 0关闭 1开启")
    @Excel(name = "开启融券交易 0关闭 1开启")
    private Integer isRq;

    /** 可借总量 */
    @ApiModelProperty(value = "可借总量")
    @Excel(name = "可借总量")
    private Integer borrowNum;

    /** 可借剩余量 */
    @ApiModelProperty(value = "可借剩余量")
    @Excel(name = "可借剩余量")
    private Integer borrowNumLeft;

    /** 可借天数 */
    @ApiModelProperty(value = "可借天数")
    @Excel(name = "可借天数")
    private Integer borrowDays;

}