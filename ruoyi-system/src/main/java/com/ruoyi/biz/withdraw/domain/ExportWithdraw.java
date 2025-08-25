package com.ruoyi.biz.withdraw.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.entity.FaMember;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 提现
 *
 * @author ruoyi
 * @date 2024-01-07
 */
@Getter
@Setter
@Accessors(chain = true)
public class ExportWithdraw implements Serializable {

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

    /** 提现金额 */
    @Excel(name = "提现金额")
    private BigDecimal money;

    /** 收款人信息 */
    @Excels({
            @Excel(name = "收款银行", targetAttr = "depositBank", type = Excel.Type.EXPORT),
            @Excel(name = "收款人姓名", targetAttr = "accountName", type = Excel.Type.EXPORT),
            @Excel(name = "收款人账号", targetAttr = "account", type = Excel.Type.EXPORT)
    })
    private FaMember faMember;

    /** 打款状态:0=待打款,1=已打款,2=驳回 */
    @Excel(name = "打款状态", readConverterExp = "0=未付款,1=已付款,2=驳回")
    private Integer isPay;

    /** 驳回原因 */
    @Excel(name = "驳回原因")
    private String reject;

    /** 提现时间 */
    @Excel(name = "提现时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}