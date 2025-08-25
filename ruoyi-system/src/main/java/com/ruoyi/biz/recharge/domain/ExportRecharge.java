package com.ruoyi.biz.recharge.domain;

import com.ruoyi.biz.sysbank.domain.FaSysbank;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 充值
 *
 * @author ruoyi
 * @date 2024-01-07
 */
@Getter
@Setter
@Accessors(chain = true)
public class ExportRecharge implements Serializable {

    private static final long serialVersionUID=1L;

    /** ID */
    @Excel(name = "ID")
    private Integer id;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

//    /** 手机号 */
//    @Excel(name = "手机号")
//    private String mobile;

    /** 上级姓名 */
    @Excel(name = "上级姓名")
    private String superiorName;

    /** 上级机构码 */
    @Excel(name = "上级机构码")
    private String superiorCode;

    /** 充值金额 */
    @Excel(name = "充值金额")
    private BigDecimal money;

    /** 是否付款:0=否,1=是,2=驳回 */
    @Excel(name = "付款状态", readConverterExp = "0=未付款,1=已付款,2=驳回")
    private Integer isPay;

    /** 驳回原因 */
    @Excel(name = "驳回原因")
    private String reject;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

    /** 充值时间 */
    @Excel(name = "充值时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Excel(name = "订单号")
    private String orderId;

    /** 通道信息 */
    @Excels({
            @Excel(name = "通道名称", targetAttr = "tdName", type = Excel.Type.EXPORT),
            @Excel(name = "通道别名", targetAttr = "tdAlias", type = Excel.Type.EXPORT),
    })
    private FaSysbank faSysbank;

}