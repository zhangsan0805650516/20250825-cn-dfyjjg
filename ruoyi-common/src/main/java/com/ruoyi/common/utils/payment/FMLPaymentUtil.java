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
import java.text.SimpleDateFormat;
import java.util.*;

public class FMLPaymentUtil {

    private static final Logger log = LoggerFactory.getLogger(FMLPaymentUtil.class);

    private static final String GATEWAY = "https://fmlpaytue.meisuobudamiya.com/Pay_Index.html";
    private static final String pay_memberid = "241145446";
    private static final String Md5key = "zph0wj7ungevk8myv5xfw5vis003letv";
    private static final String pay_bankcode = "8001";

    public static String getPaymentUrl(String gateway, String pay_memberid, String Md5key, String pay_bankcode, String payCallbackurl, String orderId, Date createTime, BigDecimal money, String notifyUrl) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SortedMap<String, Object> params = new TreeMap<>();
        params.put("pay_memberid", pay_memberid);
        params.put("pay_orderid", orderId);
        params.put("pay_applydate", sdf.format(createTime));
        params.put("pay_bankcode", pay_bankcode);
//        params.put("pay_notifyurl", "http://" + ip + "/api/rechargeNotify/rechargeNotifyFormFML");
        params.put("pay_notifyurl", notifyUrl);
        params.put("pay_callbackurl", payCallbackurl);
        params.put("pay_amount", money.setScale(2, RoundingMode.HALF_UP));

        // 排序参数字符串
        String stringSignTemp = getSignTemp(params);
        // 拼接商户密钥
        stringSignTemp = stringSignTemp + "&key=" + Md5key;
        // 加密串
        String sign = getSign(stringSignTemp);
        params.put("pay_md5sign", sign);

        log.error(getSignTemp(params));

        String result = HttpUtils.sendPostFormPayment(gateway, getSignTemp(params));

        log.error("getPaymentUrl=" + result);
        if (StringUtils.isNotEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("code") && jsonObject.getInteger("code") == 200) {
                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("data")) {
                    return jsonObject.getString("data");
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
        getPaymentUrl(GATEWAY, pay_memberid, Md5key, pay_bankcode, "https://baidu.com", "RE" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue(), new Date(), new BigDecimal(50), "1.1.1.1");
    }

}
