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
import java.math.RoundingMode;
import java.util.*;


/**
 * 支付工具
 */
public class FeiYunPaymentUtil {

    private static final Logger log = LoggerFactory.getLogger(FeiYunPaymentUtil.class);

    private static final String GATEWAY = "https://10690.cfeiyun.com/v2/payment/create";
    private static final String key = "F292BF12-4018-498F-A1B9-8E8A1E3B6181";
    private static final String subtype = "bindcard";
    private static final String format = "json";
    private static final String url = "https://baidu.com";

    public static String getPaymentUrl(String gateway, String key, String subtype, String format, String url, String orderId, BigDecimal money) throws Exception {
        SortedMap<String, Object> params = new TreeMap<>();
        params.put("orderid", orderId);
        params.put("price", money.setScale(2, RoundingMode.HALF_UP).toString());
        params.put("subtype", subtype);
        params.put("format", format);
        params.put("url", url);

        // 加密串
        String sign = getSign(key + orderId);

        params.put("sign", sign);

        log.error(getSignTemp(params));

        String result = HttpUtils.sendPostFormPayment(gateway, getSignTemp(params));
        log.error("getPaymentUrl=" + result);
        if (StringUtils.isNotEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("Result") && jsonObject.getBoolean("Result")) {
                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("URL")) {
                    return jsonObject.getString("URL");
                }
            } else if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("Description")) {
                throw new ServiceException(jsonObject.getString("Description"), HttpStatus.ERROR);
            }
        }
        return null;
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

    public static void main(String[] args) throws Exception {
        getPaymentUrl(GATEWAY, key, subtype, format, url, "RE" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue(), new BigDecimal(1000));
    }

}

