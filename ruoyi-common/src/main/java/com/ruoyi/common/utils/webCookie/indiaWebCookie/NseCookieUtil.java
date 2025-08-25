package com.ruoyi.common.utils.webCookie.indiaWebCookie;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class NseCookieUtil {

    private static final Logger log = LoggerFactory.getLogger(NseCookieUtil.class);
    
    /**
     * 获取NSE网站cookie
     * @return
     */
    public static String getNseCookie() {
        String cookie = "";

        HttpGet get = new HttpGet("https://www.nseindia.com/");
        get.setHeader("accept", "*/*");
        get.setHeader("connection", "Keep-Alive");
        get.setHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        get.setHeader("Referer", "https://www.nseindia.com/");
        get.setHeader("Accept-Encoding", "gzip, deflate, br");

        CloseableHttpClient client = HttpClients.createDefault();

        try {
            CloseableHttpResponse response = client.execute(get);
            Header[] headers = response.getHeaders("Set-Cookie");
            for (Header header : headers) {
                if (header.getValue().contains("nsit=") || header.getValue().contains("nseappid=")) {
                    String value = header.getValue();
                    value = value.substring(0, value.indexOf(";") + 1);
                    cookie += value;
                }
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return cookie;
    }

    public static void main(String[] args) {
        log.info(getNseCookie());
    }

}
