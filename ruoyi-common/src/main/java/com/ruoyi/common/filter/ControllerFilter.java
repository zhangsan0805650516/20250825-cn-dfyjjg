package com.ruoyi.common.filter;

import com.ruoyi.common.config.TokenRequestWrapper;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName ControllerFileter
 * @Description 介绍
 * @Author hh
 * @Date 2019/11/13 0013 14:50
 * @Version 1.0
 **/
@Component
public class ControllerFilter extends OncePerRequestFilter {

    private String[] excludedUris;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String param = "/api/common/upload,/api/rechargeNotify/rechargeNotify";
        if (StringUtils.isNotEmpty(param)) {
            this.excludedUris = param.split(",");
        }

        boolean flag = false;
        for (String uri : excludedUris) {
            if (!request.getRequestURI().contains("/api/")) {
                flag = true;
            }
            if (request.getRequestURI().contains("/api/") && request.getRequestURI().contains(uri)) {
                flag = true;
            }
        }
        if (!flag) {
            request = new TokenRequestWrapper(request);
        }
        filterChain.doFilter(request, response);
    }
}