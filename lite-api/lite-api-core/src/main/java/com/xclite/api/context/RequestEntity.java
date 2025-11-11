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


package com.xclite.api.context;


import com.xclite.api.model.ApiInfo;
import com.xclite.api.servlet.LiteHttpServletRequest;
import com.xclite.api.servlet.LiteHttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.ssssssss.script.MagicScriptContext;

import java.util.Map;
import java.util.UUID;

/**
 * 请求信息
 *
 * @author mxd
 * 修改者: zhixiulee
 */
@Getter
public class RequestEntity {

    private final Long requestTime = System.currentTimeMillis();
    private final String requestId = UUID.randomUUID().toString().replace("-", "");
    private ApiInfo apiInfo;
    private LiteHttpServletRequest request;
    private LiteHttpServletResponse response;
    private Map<String, String> parameters;
    private Map<String, Object> pathVariables;
    @Setter
    private MagicScriptContext magicScriptContext;
    @Setter
    private Object requestBody;
    @Setter
    private Map<String, Object> headers;

    private RequestEntity() {

    }

    public static RequestEntity create() {
        return new RequestEntity();
    }

    public RequestEntity info(ApiInfo apiInfo) {
        this.apiInfo = apiInfo;
        return this;
    }

    public RequestEntity request(LiteHttpServletRequest request) {
        this.request = request;
        return this;
    }

    public RequestEntity response(LiteHttpServletResponse response) {
        this.response = response;
        return this;
    }


    public RequestEntity parameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    public RequestEntity pathVariables(Map<String, Object> pathVariables) {
        this.pathVariables = pathVariables;
        return this;
    }

}
