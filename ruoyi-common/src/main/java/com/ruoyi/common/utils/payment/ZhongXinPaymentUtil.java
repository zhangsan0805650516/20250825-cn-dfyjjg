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

public class ZhongXinPaymentUtil {

    private static final Logger log = LoggerFactory.getLogger(ZhongXinPaymentUtil.class);

    private static final String GATEWAY = "http://47.99.129.234:8096/sfang-api/pay/createOrder";
    private static final long merchantId = 14;
    private static final Integer productId = 8001;
    private static final String key = "3c0p21FhfHjkcoecLUQNd7FZPwaxO8BhgjkxguK7x9qj2jU3THNuOHzMGPi28pPPOk5VToxfx6c5zydmBHCk5tefShPoxhJytHp9Oa5SHKVhnAuMHuB1KJuc4EI6uKvO";

    public static String getPaymentUrl(String gateway, long merchantId, Integer productId, String key, String orderId, BigDecimal money, String clientIP, String callbackUrl) throws Exception {
        SortedMap<String, Object> params = new TreeMap<>();
        params.put("merchantId", merchantId);
        params.put("productId", productId);
        params.put("orderNo", orderId);
        params.put("currency", "cny");
        // 支付金额,单位分
        params.put("amount", money.setScale(2, RoundingMode.HALF_UP).toString());
        params.put("clientIP", clientIP);
        params.put("callbackUrl", callbackUrl);
        params.put("commodity", "充值");
        params.put("productDesc", "充值");

        // 排序参数字符串
        String stringSignTemp = getSignTemp(params);
        // 拼接商户密钥
        stringSignTemp = stringSignTemp + "&key=" + key;
        // 加密串
        String sign = getSign(stringSignTemp);
        params.put("sign", sign);

        log.error(JSONUtil.toJsonStr(params));

        String result = HttpUtils.sendPostForPayment(gateway, JSONUtil.toJsonStr(params));

        log.error("getPaymentUrl=" + result);
        if (StringUtils.isNotEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("code") && jsonObject.getInteger("code") == 0) {
                jsonObject = jsonObject.getJSONObject("data");
                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("payParams")) {
                    return jsonObject.getString("payParams");
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
        String sign = DigestUtils.md5DigestAsHex(stringSignTemp.getBytes()).toUpperCase();
        return sign;
    }

    private static String convertToChinese(String unicodeString) {
        int len = unicodeString.length();
        StringBuffer sb = new StringBuffer();
        for (int i = 2; i < len; i += 6) {
            String unicodeChar = unicodeString.substring(i, i + 4);
            char ch = (char) Integer.parseInt(unicodeChar, 16);
            sb.append(ch);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        getPaymentUrl(GATEWAY, merchantId, productId, key, "RE" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue(), new BigDecimal("300.12"), "1.1.1.1", "https://baidu.com");
    }

}
