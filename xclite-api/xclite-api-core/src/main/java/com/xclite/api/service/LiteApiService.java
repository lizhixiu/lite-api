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


package com.xclite.api.service;


import java.util.Map;

public interface LiteApiService {

    /**
     * 执行LiteApi中的接口,原始内容，不包含code以及message信息
     *
     * @param method  请求方法
     * @param path    请求路径
     * @param context 变量信息
     */
    <T> T execute(String method, String path, Map<String, Object> context);

    /**
     * 执行LiteApi中的接口,带code和message信息
     *
     * @param method  请求方法
     * @param path    请求路径
     * @param context 变量信息
     */
    <T> T call(String method, String path, Map<String, Object> context);

    /**
     * 执行LiteApi中的函数
     *
     * @param path    函数路径
     * @param context 变量信息
     */
    <T> T invoke(String path, Map<String, Object> context);


}
