package com.ruoyi.common.utils.payment;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.OrderUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class HuoJianPaymentUtil {

    private static final Logger log = LoggerFactory.getLogger(HuoJianPaymentUtil.class);

    private static final String GATEWAY = "https://api.huojian68.com/v1/payment";
    private static final String APP_ID = "pingan888";
    private static final String API_KEY = "66c4044d-3612-461d-a902-bdbe30b1c365";
    private static final Integer TYP_ID = 10;

    public static String getPaymentUrl(String gateway, String appId, String apiKey, Integer typId, String orderId, Date applyDate, BigDecimal amount, String notifyUrl) throws Exception {
        SortedMap<String, Object> params = new TreeMap<>();
        params.put("app_id", appId);
        params.put("amount", amount.setScale(2, RoundingMode.HALF_UP).toString());
        params.put("order_no", orderId);
        params.put("ts", applyDate.getTime() / 1000);
        params.put("typ_id", typId);
//        params.put("notify", "http://" + ip + "/api/rechargeNotify/rechargeNotifyHuojian");
        params.put("notify", notifyUrl);

        // 排序参数字符串
        String stringSignTemp = getSignTemp(params);
        // 拼接商户密钥
        stringSignTemp = stringSignTemp + "&key=" + apiKey;
        // 加密串
        String sign = getSign(stringSignTemp);

        params.put("sign", sign);

        log.error(JSONUtil.toJsonStr(params));

        String result = HttpUtils.sendPostForPayment(gateway, JSONUtil.toJsonStr(params));
        log.error("getPaymentUrl=" + result);
        if (StringUtils.isNotEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("code") && jsonObject.getInteger("code") == 0) {
//                    jsonObject = jsonObject.getJSONObject("data");
                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("payload")) {
                    return jsonObject.getString("payload");
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
        getPaymentUrl(GATEWAY, APP_ID, API_KEY, TYP_ID, "RE" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue(), new Date(), new BigDecimal("2000.00"), "1.1.1.1");
    }

}
