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
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class DaiFuPaymentUtil {

    private static final Logger log = LoggerFactory.getLogger(DaiFuPaymentUtil.class);

    private static final String GATEWAY = "https://a.f-dsn.com/api/personnelfiles/preorder/addorder";
    private static final String pay_memberid = "bagey";
    private static final String Md5key = "VD00XLDF8XN40ZBB60F20TD026H6Z8";

    public static String getPaymentUrl(String gateway, String userName, String Md5key, String orderId, BigDecimal amount, String notifyUrl, String frontReturnUrl) throws Exception {
        SortedMap<String, String> params = new TreeMap<>();
        params.put("userName", userName);
        params.put("amount", amount.setScale(2, RoundingMode.HALF_UP).toString());
        params.put("outOrderId", orderId);

//        String returnUrl = "http://" + ip + "/api/rechargeNotify/rechargeNotifyJsonDaiFu";
        String returnUrl = notifyUrl;
        params.put("returnUrl", returnUrl);

//        String frontReturnUrl = "OK";
        params.put("frontReturnUrl", frontReturnUrl);

        // 参数字符串
        String stringSignTemp = "userName=" + userName + "&amount=" + amount.setScale(2, RoundingMode.HALF_UP).toString() + "&outOrderId=" + orderId + "&returnUrl=" + returnUrl + "&frontReturnUrl=" + frontReturnUrl;

        // 拼接商户密钥
        stringSignTemp = stringSignTemp + "&access_token=" + Md5key;

        // 加密串
        String sign = getSign(stringSignTemp);

        params.put("sign", sign);

        log.error(JSONUtil.toJsonStr(params));

        String result = HttpUtils.sendPostForPayment(gateway, JSONUtil.toJsonStr(params));
        log.error("getPaymentUrl=" + result);
        if (StringUtils.isNotEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("code") && jsonObject.getInteger("code") == 1) {
                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("msg")) {
                    return jsonObject.getString("msg");
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
    private static String getSignTemp(Map<String, String> params) throws Exception {
        String prestr = "";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            prestr = prestr + entry + "&";
        }
        prestr = prestr.substring(0, prestr.lastIndexOf("&"));
        return prestr;
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

    public static void main(String[] args) throws Exception {
        getPaymentUrl(GATEWAY, pay_memberid, Md5key, "RE" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue(), new BigDecimal("1000.00"), "http://47.239.80.39/api/rechargeNotify/rechargeNotify/3", "https://193.112.199.69/paySuccess.html");
    }

}
