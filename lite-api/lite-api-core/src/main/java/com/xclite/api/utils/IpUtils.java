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

package com.xclite.api.utils;

import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * IP地址工具类，用于获取真实IP地址
 *
 * @author mxd
 * 修改者: zhixiulee
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
