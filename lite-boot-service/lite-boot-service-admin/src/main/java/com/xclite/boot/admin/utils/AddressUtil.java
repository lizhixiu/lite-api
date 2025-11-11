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

import cn.hutool.core.net.Ipv4Util;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddressUtil {

    public static String getAddress(String ip) {
        // 输入验证
        if (ip == null || ip.trim().isEmpty()) {
            log.error("Input IP address is null or empty.");
            return "未知";
        }
        try {
            if (Ipv4Util.isInnerIP(ip)) {
                return "内网IP";
            }
            String url = "https://whois.pconline.com.cn/ipJson.jsp?json=true&ip=" + ip;
            String response = HttpUtil.get(url);
            return JSONUtil.parseObj(response).getStr("addr");
        } catch (IllegalArgumentException e) {
            log.error("Invalid IP address format: {}", ip, e);
            return "内网IP";
        } catch (Exception e) {
            log.error("Failed to get address information for IP: {}", ip, e);
            return "未知";
        }
    }
}