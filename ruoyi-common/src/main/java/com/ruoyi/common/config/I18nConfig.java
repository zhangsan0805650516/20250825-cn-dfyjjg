package com.ruoyi.common.config;

import com.ruoyi.common.filter.MyI18nInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@Slf4j
public class I18nConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        MyI18nInterceptor myHandlerInterceptor = new MyI18nInterceptor();
        InterceptorRegistration loginRegistry = registry.addInterceptor(myHandlerInterceptor);
        // 拦截路径
        loginRegistry.addPathPatterns("/**");
    }

}
