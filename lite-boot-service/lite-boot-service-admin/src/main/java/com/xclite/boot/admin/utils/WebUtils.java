/*
 * MIT License
 *
 * Copyright (c) 2022 吕金泽
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
