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
package com.xclite.api.modules;

import com.jfinal.upload.MultipartRequest;
import com.jfinal.upload.UploadFile;
import com.xclite.api.annotation.LiteModule;
import com.xclite.api.servlet.LiteHttpServletRequest;
import com.xclite.api.servlet.javaee.LiteJavaEEHttpServletRequest;
import com.xclite.api.servlet.javaee.LiteJavaEERequestContextHolder;
import com.xclite.api.utils.IpUtils;
import org.ssssssss.script.annotation.Comment;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * request 模块
 *
 * @author mxd
 * 修改者: zhixiulee
 */
@LiteModule("request")
public class RequestModule {


    // 添加无参构造函数
    public RequestModule() {
        // 无参构造函数，用于模块注册
    }

    /**
     * 获取文件信息
     *
     * @param name 参数名
     */
    @Comment("获取文件")
    public static UploadFile getFile(@Comment(name = "name", value = "参数名") String name) {
        MultipartRequest request = getMultipartHttpServletRequest();
        if (request == null) {
            return null;
        }
        List<UploadFile> uploadFiles = request.getFiles();
        if (uploadFiles == null || uploadFiles.isEmpty()) {
            return null;
        }
        for (UploadFile uploadFile : uploadFiles) {
            if (uploadFile.getParameterName().equals(name)) {
                return uploadFile;
            }
        }
        return null;
    }

    /**
     * 获取文件信息
     *
     * @param name 参数名
     */
    @Comment("获取多个文件")
    public static List<UploadFile> getFiles(@Comment(name = "name", value = "参数名") String name) {
        MultipartRequest request = getMultipartHttpServletRequest();
        if (request == null) {
            return null;
        }

        return request.getFiles().stream().filter(it -> !(it == null) && it.getParameterName().equals(name)).collect(Collectors.toList());
    }

    /**
     * 获取原生HttpServletRequest对象
     */
    @Comment("获取原生HttpServletRequest对象")
    public static LiteHttpServletRequest get() {
        return new LiteJavaEEHttpServletRequest(LiteJavaEERequestContextHolder.getCurrentRequest());
    }

    private static MultipartRequest getMultipartHttpServletRequest() {
        LiteHttpServletRequest request = get();
        if (request.isMultipart()) {
            return request.resolveMultipart();
        }
        return null;
    }

    /**
     * 根据参数名获取参数值集合
     *
     * @param name 参数名
     */
    @Comment("根据请求参数名获取值")
    public List<String> getValues(@Comment(name = "name", value = "参数名") String name) {
        LiteHttpServletRequest request = get();
        String[] values = request.getParameterValues(name);
        return values == null ? null : Arrays.asList(values);
    }

    /**
     * 根据header名获取header集合
     *
     * @param name 参数名
     */
    @Comment("根据header名获取值")
    public List<String> getHeaders(@Comment(name = "name", value = "header名") String name) {
        LiteHttpServletRequest request = get();
        Enumeration<String> headers = request.getHeaders(name);
        return headers == null ? null : Collections.list(headers);
    }

    @Comment("获取客户端IP")
    public String getClientIP(String... otherHeaderNames) {
        LiteHttpServletRequest request = get();
        return IpUtils.getRealIP(request.getRemoteAddr(), request::getHeader, otherHeaderNames);
    }
}
