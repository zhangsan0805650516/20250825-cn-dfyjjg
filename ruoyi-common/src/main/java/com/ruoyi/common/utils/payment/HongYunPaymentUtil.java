package com.ruoyi.common.utils.payment;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.OrderUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.util.*;


/**
 * 支付工具
 */
public class HongYunPaymentUtil {

    private static final Logger log = LoggerFactory.getLogger(HongYunPaymentUtil.class);

    private static final String GATEWAY = "http://47.236.82.250:405/gateway/index/checkpoint";
    private static final String APIKEY = "FF6AECCC0C6CCD";
    private static final String ACCOUNT_ID = "10049";
    private static final String CONTENT_TYPE = "json";
    private static final String THOROUGHFARE = "1000";
    private static final String CALLBACK_URL = "1000";

    public static String getPaymentUrl(String gateway, String APIKEY, String account_id, String content_type, String thoroughfare, String out_trade_no, BigDecimal amount, String callback_url, long timestamp, String ip, String deviceos, Integer payer_ip) throws Exception {
        SortedMap<String, Object> params = new TreeMap<>();
        params.put("account_id", account_id);
        params.put("content_type", content_type);
        params.put("thoroughfare", thoroughfare);
        params.put("out_trade_no", out_trade_no);
        params.put("amount", amount);
        params.put("callback_url", callback_url);
        params.put("timestamp", timestamp);
        params.put("ip", ip);
        params.put("deviceos", deviceos);
        params.put("payer_ip", payer_ip);

        // 排序参数字符串
        String stringSignTemp = getSignTemp(params);
        // 拼接商户密钥
        stringSignTemp = stringSignTemp + "&key=" + APIKEY;
        // 加密串
        String sign = getSign(stringSignTemp);

        params.put("sign", sign);

        log.error(getSignTemp(params));

        String result = HttpUtils.sendPostFormPayment(gateway, getSignTemp(params));
        log.error("getPaymentUrl=" + result);
        if (StringUtils.isNotEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("code") && jsonObject.getInteger("code") == 200) {
                jsonObject = jsonObject.getJSONObject("data");
                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("pay_url")) {
                    return jsonObject.getString("pay_url");
                }
            } else if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("msg")) {
                throw new ServiceException(jsonObject.getString("msg"), HttpStatus.ERROR);
            }
        }
        return null;
    }

    /**
     * 参数排序
     * @param params
     * @return
     * @throws Exception
     */
    private static String getSignTemp(Map<String, Object> params) throws Exception {
        Set<Map.Entry<String, Object>> entries = params.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        List<String> values = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String k = String.valueOf(entry.getKey());
            String v = String.valueOf(entry.getValue());
            if (StringUtils.isNotBlank(k) && StringUtils.isNotBlank(v)) {
                values.add(k + "=" + v);
            }
        }
        String result = StringUtils.join(values, "&");
        return result;
    }

    /**
     * 加密
     * @param stringSignTemp
     * @return
     * @throws Exception
     */
    private static String getSign(String stringSignTemp) throws Exception {
        String sign = DigestUtils.md5DigestAsHex(stringSignTemp.getBytes()).toLowerCase();
        return sign;
    }

    public static void main(String[] args) throws Exception {
        getPaymentUrl(GATEWAY, APIKEY, ACCOUNT_ID, CONTENT_TYPE, THOROUGHFARE, "RE" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue(), new BigDecimal("300.00"), CALLBACK_URL, new Date().getTime(), "1.1.1.1", "other", 1);
    }

}

