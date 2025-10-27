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
 * @author zhixiulee
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
