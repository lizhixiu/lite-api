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


package com.xclite.api.modules.db.inteceptor;


import com.xclite.api.context.RequestEntity;
import com.xclite.api.modules.db.BoundSql;

/**
 * SQL 拦截器
 *
 * @author mxd
 * 修改者: zhixiulee
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
