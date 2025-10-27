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
 * @author zhixiulee
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
