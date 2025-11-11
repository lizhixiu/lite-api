/*
 * Copyright 2025 xclite.com and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
