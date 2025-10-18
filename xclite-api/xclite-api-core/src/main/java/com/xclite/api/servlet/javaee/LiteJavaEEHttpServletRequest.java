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

import com.jfinal.upload.MultipartRequest;
import com.xclite.api.servlet.LiteCookie;
import com.xclite.api.servlet.LiteHttpServletRequest;
import com.xclite.api.servlet.LiteHttpSession;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Enumeration;

/**
 * 不依赖Spring的HttpServletRequest实现
 */
public class LiteJavaEEHttpServletRequest implements LiteHttpServletRequest {

    private final HttpServletRequest request;

    public LiteJavaEEHttpServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String getHeader(String name) {
        return request.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return request.getHeaders(name);
    }

    @Override
    public String getRequestURI() {
        return request.getRequestURI();
    }

    @Override
    public String getMethod() {
        return request.getMethod();
    }

    @Override
    public void setAttribute(String key, Object value) {
        request.setAttribute(key, value);
    }

    @Override
    public String[] getParameterValues(String name) {
        return request.getParameterValues(name);
    }

    @Override
    public Object getAttribute(String name) {
        return request.getAttribute(name);
    }

    @Override
    public HttpServletRequest getHttpInputMessage() {
        return request;
    }

    @Override
    public String getContentType() {
        return request.getContentType();
    }

    @Override
    public LiteHttpSession getSession() {
        return new LiteJavaEEHttpSession(request.getSession());
    }

    @Override
    public LiteCookie[] getCookies() {
        javax.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return new LiteCookie[0];
        }

        LiteCookie[] liteCookies = new LiteCookie[cookies.length];
        for (int i = 0; i < cookies.length; i++) {
            liteCookies[i] = new LiteJavaEECookie(cookies[i]);
        }
        return liteCookies;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return request.getInputStream();
    }

    @Override
    public boolean isMultipart() {
        return request.getContentType() != null && request.getContentType().startsWith("multipart/");
    }

    @Override
    public String getRemoteAddr() {
        return request.getRemoteAddr();
    }

    @Override
    public MultipartRequest resolveMultipart() {
        // 这里需要根据实际情况实现
        return new MultipartRequest(request);
    }

    @Override
    public Principal getUserPrincipal() {
        return request.getUserPrincipal();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getRequest() {
        return (T) request;
    }
}