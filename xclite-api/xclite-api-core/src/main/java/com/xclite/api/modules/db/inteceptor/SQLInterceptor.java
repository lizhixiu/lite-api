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


package com.xclite.api.modules.db.inteceptor;


import com.xclite.api.context.RequestEntity;
import com.xclite.api.modules.db.BoundSql;

/**
 * SQL 拦截器
 *
 * @author zhixiulee
 */
public interface SQLInterceptor {

    /**
     * 1.1.1 新增
     *
     * @param boundSql      SQL信息
     * @param requestEntity 请求信息
     */
    default void preHandle(BoundSql boundSql, RequestEntity requestEntity) {

    }

    /**
     * @param boundSql      SQL信息
     * @param result        执行结果
     * @param requestEntity 请求信息
     */
    default Object postHandle(BoundSql boundSql, Object result, RequestEntity requestEntity) {
        return result;
    }

    /**
     * @param boundSql      SQL信息
     * @param throwable     异常信息
     * @param requestEntity 请求信息
     */
    default void handleException(BoundSql boundSql, Throwable throwable, RequestEntity requestEntity) {
    }


}
