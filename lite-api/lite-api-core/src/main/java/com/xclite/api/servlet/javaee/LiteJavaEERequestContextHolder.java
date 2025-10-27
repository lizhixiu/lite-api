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


package com.xclite.api.servlet.javaee;

import com.xclite.api.servlet.LiteHttpServletRequest;
import com.xclite.api.servlet.LiteHttpServletResponse;
import com.xclite.api.servlet.LiteRequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 不依赖Spring的请求上下文持有者实现
 */
public class LiteJavaEERequestContextHolder implements LiteRequestContextHolder {

    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();
    private static final ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<>();

    /**
     * 设置当前线程的请求和响应对象
     */
    public static void setRequestContext(HttpServletRequest request, HttpServletResponse response) {
        requestHolder.set(request);
        responseHolder.set(response);
    }

    /**
     * 清理当前线程的请求和响应对象
     */
    public static void clearRequestContext() {
        requestHolder.remove();
        responseHolder.remove();
    }

    /**
     * 获取当前线程的请求对象
     */
    public static HttpServletRequest getCurrentRequest() {
        return requestHolder.get();
    }

    /**
     * 获取当前线程的响应对象
     */
    public static HttpServletResponse getCurrentResponse() {
        return responseHolder.get();
    }

    // 注意：这里我们不实现convert方法，因为接口签名不匹配
    // 原接口使用的是ServletRequestAttributes，而我们使用的是LiteHttpServletRequest

    @Override
    public LiteHttpServletRequest getRequest() {
        HttpServletRequest request = requestHolder.get();
        if (request != null) {
            return new LiteJavaEEHttpServletRequest(request);
        }
        return null;
    }

    @Override
    public LiteHttpServletResponse getResponse() {
        HttpServletResponse response = responseHolder.get();
        if (response != null) {
            return new LiteJavaEEHttpServletResponse(response);
        }
        return null;
    }
}