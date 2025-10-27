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

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.xclite.api.annotation.LiteModule;
import lombok.Getter;
import lombok.Setter;
import org.ssssssss.script.annotation.Comment;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@LiteModule("http")
public class HttpModule {

    private final Map<String, String> headers = new HashMap<>();
    private Class<?> responseType = Object.class;
    private final Map<String, Object> params = new HashMap<>();
    private final Map<String, Object> formData = new HashMap<>();
    private String url;
    private Method method = Method.GET;
    private Object requestBody;
    @Getter
    @Setter
    private String contentType;

    public HttpModule() {
        // 无参构造函数，Hutool不需要模板实例
    }

    public HttpModule(String url) {
        this.url = url;
    }

    @Comment("创建连接")
    public HttpModule connect(@Comment(name = "url", value = "目标URL") String url) {
        return new HttpModule(url);
    }

    @Comment("设置URL参数")
    public HttpModule param(@Comment(name = "key", value = "参数名") String key,
                            @Comment(name = "values", value = "参数值") Object... values) {
        if (values != null && values.length > 0) {
            if (values.length == 1) {
                this.params.put(key, values[0]);
            } else {
                this.params.put(key, values);
            }
        }
        return this;
    }

    @Comment("批量设置URL参数")
    public HttpModule param(@Comment(name = "values", value = "参数值") Map<String, Object> values) {
        values.forEach((key, value) -> param(key, Objects.toString(value, "")));
        return this;
    }

    @Comment("设置form参数")
    public HttpModule data(@Comment(name = "key", value = "参数名") String key,
                           @Comment(name = "values", value = "参数值") Object... values) {
        if (values != null && values.length > 0) {
            if (values.length == 1) {
                this.formData.put(key, values[0]);
            } else {
                this.formData.put(key, values);
            }
        }
        return this;
    }

    @Comment("批量设置form参数")
    public HttpModule data(@Comment(name = "values", value = "参数值") Map<String, Object> values) {
        values.forEach((key, value) -> data(key, Objects.toString(value, "")));
        return this;
    }

    @Comment("设置header")
    public HttpModule header(@Comment(name = "key", value = "header名") String key,
                             @Comment(name = "value", value = "header值") String value) {
        headers.put(key, value);
        return this;
    }

    @Comment("批量设置header")
    public HttpModule header(@Comment(name = "values", value = "header值") Map<String, Object> values) {
        values.entrySet()
                .stream()
                .filter(it -> it.getValue() != null)
                .forEach(entry -> header(entry.getKey(), entry.getValue().toString()));
        return this;
    }

    @Comment("设置请求方法，默认GET")
    public HttpModule method(@Comment(name = "method", value = "请求方法") String method) {
        this.method = Method.valueOf(method.toUpperCase());
        return this;
    }

    @Comment("设置`RequestBody`")
    public HttpModule body(@Comment(name = "requestBody", value = "`RequestBody`") Object requestBody) {
        this.requestBody = requestBody;
        this.contentType("application/json");
        return this;
    }

    @Comment("设置`ContentType`")
    public HttpModule contentType(@Comment(name = "contentType", value = "Content-Type值") String contentType) {
        this.contentType = contentType;
        this.headers.put("Content-Type", contentType);
        return this;
    }

    @Comment("设置返回值为`byte[]`")
    public HttpModule expectBytes() {
        this.responseType = byte[].class;
        return this;
    }

    @Comment("设置返回值为`String`")
    public HttpModule expectString() {
        this.responseType = String.class;
        return this;
    }

    @Comment("发送`POST`请求")
    public Object post() {
        this.method = Method.POST;
        return this.execute();
    }

    @Comment("发送`GET`请求")
    public Object get() {
        this.method = Method.GET;
        return this.execute();
    }

    @Comment("发送`PUT`请求")
    public Object put() {
        this.method = Method.PUT;
        return this.execute();
    }

    @Comment("发送`DELETE`请求")
    public Object delete() {
        this.method = Method.DELETE;
        return this.execute();
    }

    @Comment("发送`HEAD`请求")
    public Object head() {
        this.method = Method.HEAD;
        return this.execute();
    }

    @Comment("发送`OPTIONS`请求")
    public Object options() {
        this.method = Method.OPTIONS;
        return this.execute();
    }

    @Comment("发送`TRACE`请求")
    public Object trace() {
        this.method = Method.TRACE;
        return this.execute();
    }

    @Comment("发送`PATCH`请求")
    public Object patch() {
        this.method = Method.PATCH;
        return this.execute();
    }

    @Comment("执行请求")
    public Object execute() {
        HttpRequest request = HttpRequest.of(url).method(method);

        // 设置请求头
        if (MapUtil.isNotEmpty(headers)) {
            request.addHeaders(headers);
        }

        // 设置URL参数
        if (MapUtil.isNotEmpty(params)) {
            request.form(params);
        }

        // 设置请求体或表单数据
        if (MapUtil.isNotEmpty(formData)) {
            request.form(formData);
        } else if (requestBody != null) {
            if (requestBody instanceof String) {
                request.body((String) requestBody);
            } else {
                // 这里假设是JSON对象，实际可能需要根据contentType做不同处理
                request.body(Objects.toString(requestBody));
            }
        }

        try {
            HttpResponse response = request.execute();

            // 根据responseType返回不同类型的结果
            if (responseType == byte[].class) {
                return response.bodyBytes();
            } else if (responseType == String.class) {
                return response.body();
            } else {
                // 返回包含状态码和响应体的Map，模拟ResponseEntity
                Map<String, Object> result = new HashMap<>();
                result.put("statusCode", response.getStatus());
                result.put("body", response.body());
                result.put("headers", response.headers());
                return result;
            }
        } catch (Exception e) {
            throw new RuntimeException("HTTP请求执行失败: " + e.getMessage(), e);
        }
    }

}