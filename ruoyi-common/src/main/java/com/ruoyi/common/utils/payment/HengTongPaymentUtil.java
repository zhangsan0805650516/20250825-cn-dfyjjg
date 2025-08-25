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
import java.text.SimpleDateFormat;
import java.util.*;

public class HengTongPaymentUtil {

    private static final Logger log = LoggerFactory.getLogger(HengTongPaymentUtil.class);

    private static final String GATEWAY = "http://pay.hengtongtopay.com/api/pay/unifiedOrder";
    private static final String pay_memberid = "M1731306983";
    private static final String APP_ID = "6731a5e7e4b0387dd533fa98";
    private static final String Md5key = "dpy1yu20y3lk2xb67v8g3no2hi67yvupfa00gwpl611x5b7bbcbg4p67mwvujv7co5utkka61bz7mzfd1teu0jm93x3lwt37k6i3ccld03x43bhuqc655aa3bwe2idlp";
    private static final String pay_bankcode = "ITPAY";
    private static final String payCallbackurl = "https://baidu.com";

    public static String getPaymentUrl(String gateway, String pay_memberid, String APP_ID, String Md5key, String pay_bankcode, String payCallbackurl, String orderId, Date createTime, BigDecimal money, String notifyUrl) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SortedMap<String, Object> params = new TreeMap<>();
        params.put("mchNo", pay_memberid);
        params.put("appId", APP_ID);
        params.put("mchOrderNo", orderId);
        params.put("wayCode", pay_bankcode);
        // 支付金额,单位分
        params.put("amount", money.setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).intValue());
        params.put("currency", "cny");
        params.put("subject", "充值");
        params.put("body", "充值");
        params.put("reqTime", createTime.getTime());
        params.put("version", "1.0");
//        params.put("notifyUrl", "http://" + ip + "/api/rechargeNotify/rechargeNotifyFormHengTong");
        params.put("notifyUrl", notifyUrl);
        params.put("returnUrl", payCallbackurl);
        params.put("signType", "MD5");

        // 排序参数字符串
        String stringSignTemp = getSignTemp(params);
        // 拼接商户密钥
        stringSignTemp = stringSignTemp + "&key=" + Md5key;
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
                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("payData")) {
                    return jsonObject.getString("payData");
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
        getPaymentUrl(GATEWAY, pay_memberid, APP_ID, Md5key, pay_bankcode, payCallbackurl, "RE" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue(), new Date(), new BigDecimal(50), "1.1.1.1");
    }

}
