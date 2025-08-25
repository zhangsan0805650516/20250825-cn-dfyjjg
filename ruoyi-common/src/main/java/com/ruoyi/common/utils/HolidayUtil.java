package com.ruoyi.common.utils;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.ObjectUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 判断日期类型（工作日、周末、节日）
 */
public class HolidayUtil {

    public static boolean isHoliday(String date) {
        // 获取节假日信息
        String result = getHolidayInfo(date);
        if (StringUtils.isNotEmpty(result)) {
            JSONObject obj = JSONObject.parseObject(result);
            JSONObject type = obj.getJSONObject("type");
            if (ObjectUtils.isNotEmpty(type) && type.containsKey("type") && type.getIntValue("type") == 0) {
                return false;
            }
        }
        return true;
    }

    private static String getHolidayInfo(String date) {
        // 获取节假日信息
        String result = getHoliday(date);
        JSONObject obj = JSONObject.parseObject(result);
        if(obj == null || !obj.containsKey("code") || obj.getIntValue("code") != 0) {
            result = getHoliday(date);
            obj = JSONObject.parseObject(result);
            if(obj == null || !obj.containsKey("code") || obj.getIntValue("code") != 0) {
                result = getHoliday(date);
                obj = JSONObject.parseObject(result);
                if(obj == null || !obj.containsKey("code") || obj.getIntValue("code") != 0) {
                    return null;
                }
            }
        }
        return result;
    }

    private static String getHoliday(String date) {
        String httpUrl = "https://timor.tech/api/holiday/info/";
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + date;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

