package com.ruoyi.biz.capitalLog.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.entity.FaMember;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金记录
 *
 * @author ruoyi
 * @date 2024-01-07
 */
@Data
public class ExportCapitalLog implements Serializable
{

    private static final long serialVersionUID=1L;

    /** 用户信息 */
    @Excels({
            @Excel(name = "用户姓名", targetAttr = "name", type = Excel.Type.EXPORT),
            @Excel(name = "手机号", targetAttr = "mobile", type = Excel.Type.EXPORT),
            @Excel(name = "唯一码", targetAttr = "weiyima", type = Excel.Type.EXPORT),
    })
    private FaMember faMember;

    /** 上级机构码 */
    @Excel(name = "上级机构码")
    private String superiorCode;

    /** 上级姓名 */
    @Excel(name = "上级姓名")
    private String superiorName;

    /** 股票名称 */
    @Excel(name = "股票名称")
    private String stockName;

    /** 股票代码 */
    @Excel(name = "股票代码")
    private String stockSymbol;

    /** 类型 */
    @Excel(name = "类型")
    private String content;

    /** 原金额 */
    @Excel(name = "原金额")
    private BigDecimal beforeMoney;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal money;

    /** 变动后金额 */
    @Excel(name = "变动后金额")
    private BigDecimal laterMoney;

    /** 操作时间 */
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}