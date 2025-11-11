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

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.sql.SqlFormatter;
import com.xclite.api.context.RequestEntity;
import com.xclite.api.model.ApiInfo;
import com.xclite.api.modules.db.BoundSql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 默认打印SQL实现
 *
 * @author mxd
 * 修改者: zhixiulee
 */
public class DefaultSqlInterceptor implements SQLInterceptor {

    public void handleLog(BoundSql boundSql, RequestEntity requestEntity) {
        ApiInfo apiInfo = requestEntity.getApiInfo();
        String str = StrUtil.format("API:{}/{}（{}:{}）", apiInfo.getApiGroup().getName(), apiInfo.getName(), apiInfo.getMethod(), apiInfo.getFullPath());
        Logger logger = LoggerFactory.getLogger(str);
        String parameters = Arrays.stream(boundSql.getParameters()).map(it -> {
            if (it == null) {
                return "null";
            }
            if (it instanceof Object[]) {
                return "[" + Stream.of((Object[]) it).map(x -> x == null ? "null" : (x + "(" + x.getClass().getSimpleName() + ")")).collect(Collectors.joining(", ")) + "]";
            }
            return it + "(" + it.getClass().getSimpleName() + ")";
        }).collect(Collectors.joining(", "));
        String dataSourceName = boundSql.getSqlModule().getDefaultDataSourceKey();
        logger.info("执行SQL：{}", "\n\t" + SqlFormatter.format(boundSql.getSql().trim()));
        if (dataSourceName != null) {
            logger.info("数据源：{}", dataSourceName);
        }
        if (!parameters.isEmpty()) {
            logger.info("SQL参数：{}", parameters);
        }
    }

    @Override
    public Object postHandle(BoundSql boundSql, Object result, RequestEntity requestEntity) {
        handleLog(boundSql, requestEntity);
        return result;
    }

    @Override
    public void handleException(BoundSql boundSql, Throwable throwable, RequestEntity requestEntity) {
        handleLog(boundSql, requestEntity);
    }
}
