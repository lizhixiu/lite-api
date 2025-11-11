package com.xclite.boot.admin.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class WebUtils {

    /**
     * 获取当前的HttpServletRequest对象。
     *
     * @return HttpServletRequest对象，如果获取失败则返回null。
     */
    public static HttpServletRequest getHttpServletRequest() {
        return null;
    }

    /**
     * 获取当前请求的上下文URL。
     *
     * @return 上下文URL，如果获取失败则返回空字符串。
     */
    public static String getContextUrl() {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return "";
        }
        try {
            StringBuffer url = request.getRequestURL();
            return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getServletContext().getContextPath()).append("/").toString();
        } catch (Exception e) {
            log.error("Failed to get context URL.", e);
            return "";
        }
    }

}
