package com.ruoyi.common.utils.webCookie.thailandWebCookie;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SetCookieUtil {

    private static final Logger log = LoggerFactory.getLogger(SetCookieUtil.class);
    
    /**
     * 获取NSE网站cookie
     * @return
     */
    public static String getSETCookie() {
        String cookie = "";

        HttpGet get = new HttpGet("https://www.set.or.th/en/market/product/stock/search?market=set&securityType=Common%20Stocks");
        get.setHeader("accept", "*/*");
        get.setHeader("connection", "Keep-Alive");
        get.setHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        get.setHeader("Referer", "https://www.set.or.th/en/market/product/stock/search?market=set&securityType=Common%20Stocks");
        get.setHeader("Accept-Encoding", "gzip, deflate, br");

        CloseableHttpClient client = HttpClients.createDefault();

        try {
            CloseableHttpResponse response = client.execute(get);
            Header[] headers = response.getHeaders("Set-Cookie");
            for (Header header : headers) {
                String value = header.getValue();
                value = value.substring(0, value.indexOf(";") + 1);
                cookie += value;
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
//        log.info(getSSICookie());


        String cookie = "";

        HttpGet get = new HttpGet("https://iboard-query.ssi.com.vn/v2/stock/exchange/hose");
        get.setHeader("accept", "*/*");
        get.setHeader("connection", "Keep-Alive");
        get.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
        get.setHeader("Referer", "https://iboard.ssi.com.vn/");
//        get.setHeader("Accept-Encoding", "gzip, deflate, br");
        get.setHeader("Origin", "https://iboard.ssi.com.vn");
        get.setHeader("Cookie", "__cf_bm=UmBNJzGJQKMwUXiRGuQc_5aIuJiLQcpztv1RJTxSFeA-1710267048-1.0.1.1-O17g7xUseN46M.kaQ3nTxLvndfYkzJLrMJ4lT5hjP_8cXdzHG9POVYSvZX6cT6HgMzoPWsyhEJ2u4LcqFBo1AQ; _cfuvid=B2uuQ42gvRn5lIz6wtgarhAcxAsZlj0AGVLShC6LIvE-1710264941702-0.0.1.1-604800000");


        CloseableHttpClient client = HttpClients.createDefault();

        try {
            CloseableHttpResponse response = client.execute(get);

            HttpEntity entity = response.getEntity();

//            if ("gzip".equalsIgnoreCase(entity.getContentEncoding().getValue())) {
//                entity = new GzipDecompressingEntity(entity);
//            } else if ("deflate".equalsIgnoreCase(entity.getContentEncoding().getValue())) {
//                entity = new DeflateDecompressingEntity(entity);
//            }


            String jsmc = EntityUtils.toString(entity, "gbk");

            log.info("Response content: " + jsmc);



//            Header[] headers = response.getHeaders("Set-Cookie");
//            for (Header header : headers) {
//                String value = header.getValue();
//                value = value.substring(0, value.indexOf(";") + 1);
//                cookie += value;
//            }



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
    }

}
