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


package com.xclite.api.context;

import com.xclite.api.servlet.LiteCookie;
import com.xclite.api.servlet.LiteHttpServletRequest;

import java.util.HashMap;

/**
 * Cookie Context 用于脚本中获取cookie信息
 *
 * @author zhixiulee
 */
public class CookieContext extends HashMap<String, String> {

    private final LiteCookie[] cookies;

    public CookieContext(LiteHttpServletRequest request) {
        this.cookies = request.getCookies();
    }

    @Override
    public String get(Object key) {
        if (cookies != null) {
            for (LiteCookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase("" + key)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
