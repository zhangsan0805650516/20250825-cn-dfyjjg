package com.ruoyi.framework.interceptor;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ase.AESUtil;
import com.ruoyi.common.utils.url.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class EncryptResponseAdvice implements ResponseBodyAdvice<Object> {

    private String[] excludedUris;

    private static final Logger log = LoggerFactory.getLogger(EncryptResponseAdvice.class);

    /**
     * 响应匹配
     * @param returnType
     * @param converterType
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (encryptCheck(request)) {
            try {
                return encrypt(body);
            } catch (Exception e) {
                log.error("响应加密失败");
                log.error(e.toString());
                return body;
            }
        }
        return body;
    }

    private boolean encryptCheck(ServerHttpRequest request) {
        String param = "/api/rechargeNotify/";

        String requestURI = request.getURI().getPath();
        requestURI = UrlUtil.modifyPath(requestURI);

        if (StringUtils.isNotEmpty(param)) {
            this.excludedUris = param.split(",");
        }

        boolean encrypt = false;
        for (String uri : excludedUris) {
            if (requestURI.contains("/api/")) {
                encrypt = true;
            }
            if (requestURI.contains(uri)) {
                encrypt = false;
            }
        }
        return encrypt;
    }

    private Object encrypt(Object body) throws Exception {
        String code = AESUtil.encrypt(JSON.toJSONString(body));
        return code;
    }

}
