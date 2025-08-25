package com.ruoyi.common.filter;

import com.ruoyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Slf4j
public class MyI18nInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String key = "language";
        String language = request.getHeader(key);
        // 前端传递的language必须是zh_CN格式的，中间的_必须要完整，不能只传递zh或en
        log.info("当前语言={}",language);
        // 默认ch_CN
        if (StringUtils.isEmpty(language)) {
            language = "zh_CN";
        }
        Locale locale = new Locale(language.split("_")[0],language.split("_")[1]);
        // 这样赋值以后，MessageUtils.message方法就不用修改了
        LocaleContextHolder.setLocale(locale);
        return true;
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }

}