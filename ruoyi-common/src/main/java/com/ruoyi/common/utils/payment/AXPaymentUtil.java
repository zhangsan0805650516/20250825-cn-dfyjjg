package com.ruoyi.common.utils.payment;

import cn.hutool.json.JSONUtil;
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
import java.math.RoundingMode;
import java.util.*;


/**
 * 支付工具
 */
public class AXPaymentUtil {

    private static final Logger log = LoggerFactory.getLogger(AXPaymentUtil.class);

    private static final String GATEWAY = "https://baibaitue-api.meisuobudamiya.com/api/order";
    private static final String APP_ID = "bb6b0b41e88b7380bea17697";
    private static final Integer product_id = 3;
    private static final String secret = "ce34Ff7ee23Fa33f48c76E4fB6158DB1872554F8";

    public static String getPaymentUrl(String gateway, String appId, Integer product_id, String secret, String orderId, long applyDate, BigDecimal amount, String notifyUrl) throws Exception {
        SortedMap<String, Object> params = new TreeMap<>();
        params.put("app_id", appId);
        params.put("product_id", product_id);
        params.put("out_trade_no", orderId);
//        params.put("notify_url", "http://" + ip + "/api/member/rechargeNotifyAXpay");
        params.put("notify_url", notifyUrl);
        params.put("amount", amount.setScale(2, RoundingMode.HALF_UP).toString());
        params.put("time", applyDate);

        // 排序参数字符串
        String stringSignTemp = getSignTemp(params);
        // 拼接商户密钥
        stringSignTemp = stringSignTemp + "&key=" + secret;
        // 加密串
        String sign = getSign(stringSignTemp);

        params.put("sign", sign);

        log.error(JSONUtil.toJsonStr(params));

        String result = HttpUtils.sendPostForPayment(gateway, JSONUtil.toJsonStr(params));
        log.error("getPaymentUrl=" + result);
        if (StringUtils.isNotEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("code") && jsonObject.getInteger("code") == 200) {
                jsonObject = jsonObject.getJSONObject("data");
                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("url")) {
                    return jsonObject.getString("url");
                }
            } else if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("message")) {
                throw new ServiceException(jsonObject.getString("message"), HttpStatus.ERROR);
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
        getPaymentUrl(GATEWAY, APP_ID, product_id, secret, "RE" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue(), new Date().getTime() / 1000, new BigDecimal("300.00"), "");
    }

}

