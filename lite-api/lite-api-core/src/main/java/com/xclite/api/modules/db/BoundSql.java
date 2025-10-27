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


package com.xclite.api.modules.db;

import com.xclite.api.context.RequestContext;
import com.xclite.api.context.RequestEntity;
import com.xclite.api.modules.SQLModule;
import com.xclite.api.modules.db.inteceptor.SQLInterceptor;
import com.xclite.api.modules.db.model.SqlParameter;
import com.xclite.api.modules.db.mybatis.MybatisParser;
import com.xclite.api.modules.db.mybatis.SqlNode;
import com.xclite.api.modules.db.mybatis.TextSqlNode;
import lombok.Getter;
import lombok.Setter;
import org.ssssssss.script.runtime.RuntimeContext;

import java.sql.Types;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BoundSql {

    private static final Pattern REPLACE_MULTI_WHITE_LINE = Pattern.compile("(\r?\n(\\s*\r?\n)+)");

    private static final List<String> MYBATIS_TAGS = Arrays.asList("</where>", "</if>", "</trim>", "</set>", "</foreach>");

    private String sqlOrXml;

    /**
     * -- SETTER --
     * 设置要执行的参数
     */
    @Setter
    private List<Object> parameters = new ArrayList<>();

    @Getter
    @Setter
    private Set<String> excludeColumns;

    @Getter
    private SQLModule sqlModule;

    private Map<String, Object> bindParameters;

    @Getter
    private RuntimeContext runtimeContext;

    public BoundSql(RuntimeContext runtimeContext, String sqlOrXml, List<Object> parameters, SQLModule sqlModule) {
        this.sqlOrXml = sqlOrXml;
        this.parameters = parameters;
        this.sqlModule = sqlModule;
        this.runtimeContext = runtimeContext;
    }

    public BoundSql(RuntimeContext runtimeContext, String sqlOrXml, Map<String, Object> parameters, SQLModule sqlModule) {
        this.sqlOrXml = sqlOrXml;
        this.bindParameters = parameters;
        this.sqlModule = sqlModule;
        this.runtimeContext = runtimeContext;
        this.init();
    }

    private BoundSql(RuntimeContext runtimeContext, String sqlOrXml) {
        this.sqlOrXml = sqlOrXml;
        this.runtimeContext = runtimeContext;
        this.init();
    }

    BoundSql(RuntimeContext runtimeContext, String sql, SQLModule sqlModule) {
        this(runtimeContext, sql);
        this.sqlModule = sqlModule;
    }

    private BoundSql() {

    }

    private void init() {
        Map<String, Object> varMap = new HashMap<>();
        if (this.bindParameters != null && !this.bindParameters.isEmpty()) {
            varMap.putAll(this.bindParameters);
        } else {
            varMap.putAll(runtimeContext.getVarMap());
        }
        if (MYBATIS_TAGS.stream().anyMatch(it -> this.sqlOrXml.contains(it))) {
            SqlNode sqlNode = MybatisParser.parse(this.sqlOrXml);
            this.sqlOrXml = sqlNode.getSql(varMap);
            this.parameters = sqlNode.getParameters();
        } else {
            normal(varMap);
        }
    }

    private void normal(Map<String, Object> varMap) {
        this.sqlOrXml = TextSqlNode.parseSql(this.sqlOrXml, varMap, parameters);
        this.sqlOrXml = this.sqlOrXml == null ? null : REPLACE_MULTI_WHITE_LINE.matcher(this.sqlOrXml.trim()).replaceAll("\r\n");
    }

    public BoundSql copy(String newSqlOrXml) {
        BoundSql boundSql = new BoundSql();
        boundSql.parameters = this.parameters;
        boundSql.bindParameters = this.bindParameters;
        boundSql.sqlOrXml = newSqlOrXml;
        boundSql.excludeColumns = this.excludeColumns;
        boundSql.sqlModule = this.sqlModule;
        boundSql.runtimeContext = this.runtimeContext;
        return boundSql;
    }

    /**
     * 添加SQL参数
     */
    public void addParameter(Object value) {
        parameters.add(value);
    }

    /**
     * 获取要执行的SQL
     */
    public String getSql() {
        return sqlOrXml;
    }

    /**
     * 设置要执行的SQL
     */
    public void setSql(String sql) {
        this.sqlOrXml = sql;
    }

    /**
     * 获取要执行的参数
     */
    public Object[] getParameters() {
        return parameters.toArray();
    }

    public List<Object[]> getBatchParameters() {
        List<Object[]> args = new ArrayList<>();
        parameters.forEach(parameter -> args.add((Object[]) parameter));
        return args;
    }

    public List<SqlParameter> getDeclareParameters() {
        return this.parameters.stream()
                .map(it -> {
                    if (it instanceof SqlParameter) {
                        SqlParameter p = (SqlParameter) it;
                        if (p.getName() != null) {
                            return new SqlParameter(p.getName(), p.getSqlType());
                        }
                    }
                    return new SqlParameter(Types.NULL);
                })
                .collect(Collectors.toList());
    }


    /**
     * 获取缓存值
     */
    @SuppressWarnings({"unchecked"})
    private <T> T getCacheValue(String sql, Object[] params, Supplier<T> supplier) {
        if (sqlModule.getCacheName() == null) {
            return supplier.get();
        }
        String cacheKey = sqlModule.getSqlCache().buildSqlCacheKey(sql, params);
        Object cacheValue = sqlModule.getSqlCache().get(sqlModule.getCacheName(), cacheKey);
        if (cacheValue != null) {
            return (T) cacheValue;
        }
        T value = supplier.get();
        sqlModule.getSqlCache().put(sqlModule.getCacheName(), cacheKey, value, sqlModule.getTtl());
        return value;
    }

    /**
     * 获取缓存值
     */
    @SuppressWarnings("unchecked")
    public <T> T execute(List<SQLInterceptor> interceptors, Supplier<T> supplier, boolean cacheable) {
        RequestEntity requestEntity = RequestContext.getRequestEntity();
        interceptors.forEach(interceptor -> interceptor.preHandle(this, requestEntity));
        Supplier<T> newSupplier = () -> {
            Object result;
            try {
                result = supplier.get();
                for (SQLInterceptor interceptor : interceptors) {
                    result = interceptor.postHandle(this, result, requestEntity);
                }
            } catch (Throwable e) {
                interceptors.forEach(interceptor -> interceptor.handleException(this, e, requestEntity));
                throw e;
            }
            return (T) result;
        };
        if (cacheable) {
            return getCacheValue(this.getSql(), this.getParameters(), newSupplier);
        }
        return newSupplier.get();
    }

    public <T> T execute(List<SQLInterceptor> interceptors, Supplier<T> supplier) {
        return execute(interceptors, supplier, true);
    }
}
