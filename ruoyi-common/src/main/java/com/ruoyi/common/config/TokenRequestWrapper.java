package com.ruoyi.common.config;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.rsa.RSAUtil;
import com.ruoyi.common.utils.url.UrlUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;

/**
 * @author Administrator
 */
@Slf4j
public class TokenRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    public TokenRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        BufferedReader reader = request.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        String json = stringBuilder.toString();
        if (StringUtils.isNotEmpty(json)) {
            JSONObject jsonObject = JSONObject.parseObject(json);
            try {
                String queryInfo = UrlUtil.getQueryInfo();

                String query = jsonObject.getString(queryInfo);
                if (StringUtils.isNotEmpty(query)) {
                    query = RSAUtil.decryptByPrivateStr(UrlUtil.RSA_PRIVATE_KEY, jsonObject.getString(queryInfo));
                }
                if (StringUtils.isNotEmpty(query)) {
                    query = URLDecoder.decode(query);
                }
                if ("undefined".equals(query) || StringUtils.isEmpty(query)) {
                    query = "{}";
                }
                body = query.getBytes();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    /**
     * @param
     * @description 在使用@RequestBody注解的时候，其实框架是调用了getInputStream()方法，所以我们要重写这个方法
     * @date 2020/6/16 14:45
     */
    @Override
    public ServletInputStream getInputStream() {

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

}