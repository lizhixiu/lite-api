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