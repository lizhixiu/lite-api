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

import com.xclite.api.servlet.LiteHttpSession;

import javax.servlet.http.HttpSession;

/**
 * 不依赖Spring的HttpSession实现
 */
public class LiteJavaEEHttpSession implements LiteHttpSession {

    private final HttpSession session;

    public LiteJavaEEHttpSession(HttpSession session) {
        this.session = session;
    }

    @Override
    public Object getAttribute(String key) {
        return session.getAttribute(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        session.setAttribute(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getSession() {
        return (T) session;
    }


}