package com.ruoyi.common.utils.stockUtils;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取股票信息类
 * 
 * @author ruoyi
 */
public class StockUtils
{
    private static final Logger log = LoggerFactory.getLogger(StockUtils.class);

    // 股票实时信息地址
    public static final String STOCK_CURRENT_INFO_URL = "https://qt.gtimg.cn/q=";

    /**
     * 获取股票实时信息
     * @param stockSymbol
     * @return
     */
    public static String getCurrentInfo(String stockSymbol) {
        return HttpUtils.sendGet(STOCK_CURRENT_INFO_URL + stockSymbol, null, Constants.GBK);
    }

}
