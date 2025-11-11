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


package com.xclite.api.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 统一返回值对象
 *
 * @author mxd
 * 修改者: zhixiulee
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
