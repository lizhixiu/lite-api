/*
 * MIT License
 *
 * Copyright (c) 2020 小东
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


package com.xclite.api.context;

import com.xclite.api.servlet.LiteCookie;
import com.xclite.api.servlet.LiteHttpServletRequest;

import java.util.HashMap;

/**
 * Cookie Context 用于脚本中获取cookie信息
 *
 * @author mxd
 * 修改者: zhixiulee
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
