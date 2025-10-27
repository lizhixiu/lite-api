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


package com.xclite.api.servlet;

import java.util.function.Function;

/**
 * Magic请求上下文持有者接口
 * 为了向后兼容保留此接口，建议使用GenericRequestContextHolder
 */
public interface LiteRequestContextHolder {

    /**
     * 转换方法，适用于不依赖Spring的实现
     */
    default <R> R convert(Function<LiteHttpServletRequest, R> function) {
        LiteHttpServletRequest request = getRequest();
        if (request != null) {
            return function.apply(request);
        }
        return null;
    }

    LiteHttpServletRequest getRequest();

    LiteHttpServletResponse getResponse();
}