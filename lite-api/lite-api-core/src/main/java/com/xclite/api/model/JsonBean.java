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


package com.xclite.api.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 统一返回值对象
 *
 * @author zhixiulee
 */
@Setter
@Getter
public class JsonBean<T> {

    /**
     * 状态码
     */
    private int code = 1;

    /**
     * 状态说明
     */
    private String message = "success";

    /**
     * 实际数据
     */
    private T data;

    /**
     * 服务器时间
     */
    private long timestamp = System.currentTimeMillis();

    private Integer executeTime;

    public JsonBean(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public JsonBean(int code, String message, T data, Integer executeTime) {
        this(code, message, data);
        this.executeTime = executeTime;
    }

    public JsonBean(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public JsonBean() {
    }

    public JsonBean(JsonCode jsonCode) {
        this(jsonCode, null);
    }

    public JsonBean(JsonCode jsonCode, T data) {
        this(jsonCode.getCode(), jsonCode.getMessage(), data);
    }

    public JsonBean(T data) {
        this.data = data;
    }

}
