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


package com.xclite.api.utils;

import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * IP地址工具类，用于获取真实IP地址
 */
public class IpUtils {

    private static final String[] DEFAULT_IP_HEADER = new String[]{"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};

    /**
     * 获取真实IP地址
     *
     * @param remoteAddr       客户端IP地址
     * @param getHeader        获取请求头的函数
     * @param otherHeaderNames 其他可能的IP头字段
     * @return 真实IP地址
     */
    public static String getRealIP(String remoteAddr, Function<String, String> getHeader, String... otherHeaderNames) {
        String ip = null;
        List<String> headers = Stream.concat(Stream.of(DEFAULT_IP_HEADER), Stream.of(otherHeaderNames == null ? new String[0] : otherHeaderNames)).collect(Collectors.toList());
        for (String header : headers) {
            if ((ip = processIp(getHeader.apply(header))) != null) {
                break;
            }
        }
        return ip == null ? processIp(remoteAddr) : ip;
    }

    /**
     * 处理IP地址，移除未知和无效的IP
     *
     * @param ip IP地址
     * @return 处理后的IP地址
     */
    private static String processIp(String ip) {
        if (ip != null) {
            ip = ip.trim();
            if (isUnknown(ip)) {
                return null;
            }
            if (ip.contains(",")) {
                String[] ips = ip.split(",");
                for (String subIp : ips) {
                    ip = processIp(subIp);
                    if (ip != null) {
                        return ip;
                    }
                }
            }
            return ip;
        }
        return null;
    }

    /**
     * 检查IP地址是否未知或无效
     *
     * @param ip IP地址
     * @return 如果IP未知或无效，则返回true
     */
    private static boolean isUnknown(String ip) {
        return StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip.trim());
    }
}
