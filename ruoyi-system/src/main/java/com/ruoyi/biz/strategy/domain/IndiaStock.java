package com.ruoyi.biz.strategy.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class IndiaStock implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 股票代码
     */
    private String symbol;

    /**
     * 股票名称
     */
    private String name;

    /**
     * 货币
     */
    private String currency;

    /**
     * 市场 1 NSE  2 BSE
     */
    private String exchange;

    /**
     * ISO股票代码
     */
    private String mic_code;

    /**
     * 国家
     */
    private String country;

    /**
     * 股票类型
     */
    private String type;

}
