package com.ruoyi.common.utils.payment;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * 支付工具
 */
public class RendePaymentUtil {

    private static final Logger log = LoggerFactory.getLogger(RendePaymentUtil.class);

//    private static final String GATEWAY = "https://pay.rende.one/api/v1/order";
//    private static final String API_KEY = "znqlH9AU-4vCQjSPF-OaWhwKf4-fRRlhOwX";
//    private static final String API_SECRET = "j3HUHqSc2L";
//    private static final String GATE_ID = "10045";
//    private static final String ACTION = "order";

    public static String getPaymentUrl(String gateway, String apiKey, String apiSecret, String gateId, String action, String orderId, Date applyDate, BigDecimal amount, String notifyUrl) throws Exception {
        String timestamp = String.valueOf(applyDate.getTime() / 1000);

        SortedMap<String, Object> params = new TreeMap<>();
        params.put("api_key", apiKey);
        params.put("action", action);
        params.put("timestamp", timestamp);
        params.put("sign", getSign(apiKey, apiSecret, action, timestamp));
        params.put("data", getData(gateId, orderId, amount, notifyUrl));

        log.error(JSONUtil.toJsonStr(params));

        String result = HttpUtils.sendPostForPayment(gateway, JSONUtil.toJsonStr(params));
        log.error("getPaymentUrl=" + result);
        if (StringUtils.isNotEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("code") && jsonObject.getInteger("code") == 0) {
                jsonObject = jsonObject.getJSONObject("data");
                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("url")) {
                    return jsonObject.getString("url");
                }
            } else if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("msg")) {
                throw new ServiceException(jsonObject.getString("msg"), HttpStatus.ERROR);
            }
        }
        return null;
    }

    /**
     * 加密
     * @param timestamp
     * @return
     * @throws Exception
     */
    private static String getSign(String apiKey, String apiSecret, String action, String timestamp) throws Exception {
        String data = apiKey.toUpperCase() + apiSecret.toUpperCase() + action.toUpperCase() + timestamp.toUpperCase();

        try {
            // Create HMAC-SHA256 key from the given secret
            SecretKeySpec secretKeySpec = new SecretKeySpec(apiSecret.getBytes(), "HmacSHA256");

            // Get an instance of Mac object implementing HMAC-SHA256
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);

            // Calculate the HMAC value
            byte[] hmacBytes = mac.doFinal(data.getBytes());

            // Convert result into a hexadecimal string
            StringBuilder sb = new StringBuilder(hmacBytes.length * 2);
            for (byte b : hmacBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to calculate HMAC-SHA256", e);
        }
    }

    /**
     * 参数组装
     * @param orderId
     * @param amount
     * @return
     * @throws Exception
     */
    private static SortedMap<String, Object> getData(String gateId, String orderId, BigDecimal amount, String notifyUrl) throws Exception {
        SortedMap<String, Object> data = new TreeMap<>();
        data.put("gate_id", gateId);
        data.put("order_number", orderId);
        data.put("amount", amount.setScale(2, RoundingMode.HALF_UP).toString());
//        data.put("notify_url", "http://" + ip + "/api/rechargeNotify/rechargeNotifyJsonRende");
        data.put("notify_url", notifyUrl);

        return data;
    }

//    public static void main(String[] args) throws Exception {
//        getPaymentUrl("RE" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue(), new Date(), new BigDecimal(300));
//    }

}

