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


import com.xclite.api.servlet.LiteHttpServletRequest;
import com.xclite.api.servlet.LiteHttpServletResponse;

/**
 * 请求上下文
 *
 * @author zhixiulee
 */
public class RequestContext {

    private static final ThreadLocal<RequestEntity> REQUEST_ENTITY_THREAD_LOCAL = new InheritableThreadLocal<>();

    public static LiteHttpServletRequest getHttpServletRequest() {
        RequestEntity requestEntity = REQUEST_ENTITY_THREAD_LOCAL.get();
        return requestEntity == null ? null : requestEntity.getRequest();
    }

    public static LiteHttpServletResponse getHttpServletResponse() {
        RequestEntity requestEntity = REQUEST_ENTITY_THREAD_LOCAL.get();
        return requestEntity == null ? null : requestEntity.getResponse();
    }

    public static RequestEntity getRequestEntity() {
        return REQUEST_ENTITY_THREAD_LOCAL.get();
    }

    public static void setRequestEntity(RequestEntity requestEntity) {
        REQUEST_ENTITY_THREAD_LOCAL.set(requestEntity);
    }

    public static void remove() {
        REQUEST_ENTITY_THREAD_LOCAL.remove();
    }

}
