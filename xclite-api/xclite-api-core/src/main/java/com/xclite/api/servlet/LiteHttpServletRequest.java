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


package com.xclite.api.servlet;

import com.jfinal.upload.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Enumeration;

public interface LiteHttpServletRequest {

    String getHeader(String name);

    Enumeration<String> getHeaders(String name);

    String getRequestURI();

    String getMethod();

    void setAttribute(String key, Object value);

    String[] getParameterValues(String name);

    Object getAttribute(String name);

    HttpServletRequest getHttpInputMessage();

    String getContentType();

    LiteHttpSession getSession();

    LiteCookie[] getCookies();

    InputStream getInputStream() throws IOException;

    boolean isMultipart();

    String getRemoteAddr();

    MultipartRequest resolveMultipart();

    Principal getUserPrincipal();

    <T> T getRequest();
}


