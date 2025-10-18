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
 * @author zhixiulee
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
