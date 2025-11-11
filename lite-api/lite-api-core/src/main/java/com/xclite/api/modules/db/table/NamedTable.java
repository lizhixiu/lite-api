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

package com.xclite.api.modules.db.table;

import cn.hutool.core.util.StrUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.xclite.api.exception.LiteApiException;
import com.xclite.api.modules.SQLModule;
import com.xclite.api.modules.db.BoundSql;
import com.xclite.api.modules.db.ColumnMapperAdapter;
import com.xclite.api.modules.db.inteceptor.NamedTableInterceptor;
import com.xclite.api.modules.db.model.Page;
import com.xclite.api.modules.db.model.SqlMode;
import org.ssssssss.script.annotation.Comment;
import org.ssssssss.script.runtime.RuntimeContext;

import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 表操作
 *
 * @author mxd
 * 修改者: zhixiulee
 */
public class NamedTable {

    String tableName;
    SQLModule sqlModule;

    boolean useLogic = false;
    boolean withBlank = false;
    String logicDeleteColumn;
    Object logicDeleteValue;


    Map<String, Object> columns = new HashMap<>();
    Set<String> excludeColumns = new HashSet<>();
    List<String> fields = new ArrayList<>();
    List<String> groups = new ArrayList<>();
    List<String> orders = new ArrayList<>();


    String primary;
    Object defaultPrimaryValue;

    Where where = new Where(this);
    ColumnMapperAdapter columnMapperAdapter;
    List<NamedTableInterceptor> namedTableInterceptors;


    public NamedTable(String tableName, SQLModule sqlModule, ColumnMapperAdapter columnMapperAdapter, List<NamedTableInterceptor> namedTableInterceptors) {
        this.tableName = tableName;
        this.sqlModule = sqlModule;
        this.columnMapperAdapter = columnMapperAdapter;
        this.namedTableInterceptors = namedTableInterceptors;

        this.useLogic = sqlModule.isLogicDelete();

        if (this.useLogic) {
            this.logicDeleteColumn = sqlModule.getLogicDeleteColumn();
            String deleteValue = sqlModule.getLogicDeleteValue();
            this.logicDeleteValue = deleteValue;
            if (deleteValue != null) {
                boolean isString = deleteValue.startsWith("'") || deleteValue.startsWith("\"");
                if (isString && deleteValue.length() > 2) {
                    this.logicDeleteValue = deleteValue.substring(1, deleteValue.length() - 1);
                } else {
                    this.logicDeleteValue = Integer.parseInt(deleteValue);
                }
            }
        }
    }

    private NamedTable() {
    }

    @Comment("使用逻辑删除")
    public NamedTable logic() {
        this.useLogic = true;
        return this;
    }

    @Comment("更新空值")
    public NamedTable withBlank() {
        this.withBlank = true;
        return this;
    }

    @Comment("设置主键名，update时使用")
    public NamedTable primary(@Comment(name = "primary", value = "主键列") String primary) {
        this.primary = primary;
        return this;
    }

    @Comment("设置主键名，并设置默认主键值(主要用于insert)")
    public NamedTable primary(@Comment(name = "primary", value = "主键列") String primary,
                              @Comment(name = "defaultPrimaryValue", value = "默认值") Serializable defaultPrimaryValue) {
        this.primary = primary;
        this.defaultPrimaryValue = defaultPrimaryValue;
        return this;
    }

    @Comment("设置单列的值")
    public NamedTable column(@Comment(name = "property", value = "列名") String property,
                             @Comment(name = "value", value = "值") Object value) {
        this.columns.put(property, value);
        return this;
    }

    @Comment("设置查询的列，如`column('a')`")
    public NamedTable column(@Comment(name = "property", value = "查询的列") String property) {
        if (StrUtil.isNotBlank(property)) {
            this.fields.add(property);
        }
        return this;
    }

    @Comment("执行插入,返回主键")
    public Object insert(RuntimeContext runtimeContext,
                         @Comment(name = "data", value = "各项列和值") Map<String, Object> data) {
        Record record = new Record();
        if (primary != null) {
            record.set(primary, defaultPrimaryValue);
        }
        preHandle(SqlMode.INSERT);
        if (data != null) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Object v = entry.getValue();
                if (v == null) {
                    continue;
                }
                record.set(columnMapperAdapter.getDefault().unmapping(entry.getKey()), v);
            }
        }


        if (data != null) {
            data.forEach((key, value) -> this.columns.put(columnMapperAdapter.getDefault().unmapping(key), value));
        }
        if (this.defaultPrimaryValue != null && StrUtil.isBlank(Objects.toString(this.columns.getOrDefault(this.primary, "")))) {
            if (this.defaultPrimaryValue instanceof Supplier) {
                this.columns.put(this.primary, ((Supplier<?>) this.defaultPrimaryValue).get());
            } else {
                this.columns.put(this.primary, this.defaultPrimaryValue);
            }
        }
        preHandle(SqlMode.INSERT);
        Collection<Map.Entry<String, Object>> entries = filterNotBlanks();
        if (entries.isEmpty()) {
            throw new LiteApiException("参数不能为空");
        }
        String builder = "insert into " +
                tableName +
                "(" +
                StrUtil.join(",", entries.stream().map(Map.Entry::getKey).toArray()) +
                ") values (" +
                StrUtil.join(",", Collections.nCopies(entries.size(), "?")) +
                ")";
        Object value = sqlModule.insert(new BoundSql(runtimeContext, builder, entries.stream().map(Map.Entry::getValue).collect(Collectors.toList()), sqlModule), this.primary);
        if (value == null && StrUtil.isNotBlank(this.primary)) {
            return this.columns.get(this.primary);
        }
        return value;
    }

    @Comment("执行插入,返回主键")
    public Object insert(RuntimeContext runtimeContext) {
        return insert(runtimeContext, null);
    }


    @Comment("批量插入")
    public int batchInsert(RuntimeContext runtimeContext,
                           @Comment(name = "collection", value = "各项列和值") Collection<Map<String, Object>> collection, @Comment("batchSize") int batchSize) {
        List<Record> records = new ArrayList<>();
        for (Map<String, Object> data : collection) {
            Record record = new Record();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Object v = entry.getValue();
                record.set(columnMapperAdapter.getDefault().unmapping(entry.getKey()), v);
            }
        }
        int[] result = sqlModule.getDb().batchSave(tableName, records, batchSize);
        int count = 0;
        for (int i = 0; i < result.length; i++) {
            if (result[i] == -1) {
                throw new RuntimeException("批量插入失败，第" + i + "条数据");
            }
            count++;
        }
        return count;
    }

    @Comment("批量插入")
    public int batchInsert(RuntimeContext runtimeContext, @Comment(name = "collection", value = "各项列和值") Collection<Map<String, Object>> collection) {
        return batchInsert(runtimeContext, collection, 100);
    }


    @Comment("执行update语句")
    public int update(RuntimeContext runtimeContext) {
        return update(runtimeContext, null);
    }

    @Comment("执行update语句")
    public int update(RuntimeContext runtimeContext,
                      @Comment(name = "data", value = "各项列和值") Map<String, Object> data,
                      @Comment(name = "isUpdateBlank", value = "是否更新空值字段") boolean isUpdateBlank) {
        if (null != data) {
            data.forEach((key, value) -> this.columns.put(columnMapperAdapter.getDefault().unmapping(key), value));
        }
        preHandle(SqlMode.UPDATE);
        Object primaryValue = null;
        if (StrUtil.isNotBlank(this.primary)) {
            primaryValue = this.columns.remove(this.primary);
        }
        this.withBlank = isUpdateBlank;
        List<Map.Entry<String, Object>> entries = new ArrayList<>(filterNotBlanks());
        if (entries.isEmpty()) {
            throw new LiteApiException("要修改的列不能为空");
        }
        StringBuilder builder = new StringBuilder();
        builder.append("update ");
        builder.append(tableName);
        builder.append(" set ");
        List<Object> params = new ArrayList<>();
        for (int i = 0, size = entries.size(); i < size; i++) {
            Map.Entry<String, Object> entry = entries.get(i);
            builder.append(entry.getKey()).append(" = ?");
            params.add(entry.getValue());
            if (i + 1 < size) {
                builder.append(",");
            }
        }
        if (!where.isEmpty()) {
            builder.append(where.getSql());
            params.addAll(where.getParams());
        } else if (primaryValue != null) {
            builder.append(" where ").append(this.primary).append(" = ?");
            params.add(primaryValue);
        } else {
            throw new LiteApiException("主键值不能为空");
        }
        return sqlModule.update(new BoundSql(runtimeContext, builder.toString(), params, sqlModule));

    }

    private Collection<Map.Entry<String, Object>> filterNotBlanks() {
        if (this.withBlank) {
            return this.columns.entrySet()
                    .stream()
                    .filter(it -> !excludeColumns.contains(it.getKey()))
                    .collect(Collectors.toList());
        }
        return this.columns.entrySet()
                .stream()
                .filter(it -> StrUtil.isNotBlank(Objects.toString(it.getValue(), "")))
                .filter(it -> !excludeColumns.contains(it.getKey()))
                .collect(Collectors.toList());
    }

    private void preHandle(SqlMode sqlMode) {
        if (this.namedTableInterceptors != null) {
            this.namedTableInterceptors.forEach(interceptor -> interceptor.preHandle(sqlMode, this));
        }
    }


    @Comment("执行update语句")
    public int update(RuntimeContext runtimeContext,
                      @Comment(name = "data", value = "各项列和值") Map<String, Object> data) {
        return update(runtimeContext, data, this.withBlank);
    }

    @Comment("执行`select`查询")
    public List<Map<String, Object>> select(RuntimeContext runtimeContext) {
        preHandle(SqlMode.SELECT);
        return sqlModule.select(buildSelect(runtimeContext));
    }

    private BoundSql buildSelect(RuntimeContext runtimeContext) {
        StringBuilder builder = new StringBuilder();
        builder.append("select ");
        List<String> fields = this.fields.stream()
                .filter(it -> !excludeColumns.contains(it))
                .collect(Collectors.toList());
        if (fields.isEmpty()) {
            builder.append("*");
        } else {
            builder.append(StrUtil.join(",", fields));
        }
        builder.append(" from ").append(tableName);
        List<Object> params = buildWhere(builder);
        if (!groups.isEmpty()) {
            builder.append(" group by ");
            builder.append(String.join(",", groups));
        }
        if (!orders.isEmpty()) {
            builder.append(" order by ");
            builder.append(String.join(",", orders));
        }
        BoundSql boundSql = new BoundSql(runtimeContext, builder.toString(), params, sqlModule);
        boundSql.setExcludeColumns(excludeColumns);
        return boundSql;
    }

    private List<Object> buildWhere(StringBuilder builder) {
        List<Object> params = new ArrayList<>();
        if (!where.isEmpty()) {
            where.and();
            where.ne(useLogic, logicDeleteColumn, logicDeleteValue);
            builder.append(where.getSql());
            params.addAll(where.getParams());
        } else if (useLogic) {
            where.ne(logicDeleteColumn, logicDeleteValue);
            builder.append(where.getSql());
            params.addAll(where.getParams());
        }
        return params;
    }


    @Comment("执行`selectOne`查询")
    public Map<String, Object> selectOne(RuntimeContext runtimeContext) {
        preHandle(SqlMode.SELECT_ONE);
        return sqlModule.selectOne(buildSelect(runtimeContext));
    }

    @Comment("执行分页查询")
    public Object page(RuntimeContext runtimeContext) {
        preHandle(SqlMode.PAGE);
        return sqlModule.page(buildSelect(runtimeContext));
    }

    @Comment("执行分页查询，分页条件手动传入")
    public Object page(RuntimeContext runtimeContext,
                       @Comment(name = "limit", value = "限制条数") int limit,
                       @Comment(name = "offset", value = "跳过条数") int offset) {
        preHandle(SqlMode.PAGE);
        return sqlModule.page(buildSelect(runtimeContext), new Page(limit, offset));
    }

    @Comment("拼接`order by xxx asc/desc`")
    public NamedTable orderBy(@Comment(name = "property", value = "要排序的列") String property,
                              @Comment(name = "sort", value = "`asc`或`desc`") String sort) {
        this.orders.add(property + " " + sort);
        return this;
    }

    @Comment("拼接`order by xxx asc`")
    public NamedTable orderBy(@Comment(name = "property", value = "要排序的列") String property) {
        return orderBy(property, "asc");
    }

    @Comment("拼接`order by xxx desc`")
    public NamedTable orderByDesc(@Comment(name = "property", value = "要排序的列") String property) {
        return orderBy(property, "desc");
    }

    @Comment("执行delete语句")
    public int delete(RuntimeContext runtimeContext) {
        preHandle(SqlMode.DELETE);
        if (useLogic) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put(logicDeleteColumn, logicDeleteValue);
            return update(runtimeContext, dataMap);
        }
        if (where.isEmpty()) {
            throw new LiteApiException("delete语句不能没有条件");
        }
        String builder = "delete from " +
                tableName +
                where.getSql();
        return sqlModule.update(new BoundSql(runtimeContext, builder, where.getParams(), sqlModule));
    }

    @Comment("拼接`group by`")
    public NamedTable groupBy(@Comment(name = "properties", value = "要分组的列") String... properties) {
        this.groups.addAll(Arrays.stream(properties).collect(Collectors.toList()));
        return this;
    }

    @Comment("保存到表中，当主键有值时则修改，否则插入")
    public Object save(RuntimeContext runtimeContext) {
        return this.save(runtimeContext, null, false);
    }

    @Comment("保存到表中，当主键有值时则修改，否则插入")
    public Object save(RuntimeContext runtimeContext,
                       @Comment(name = "data", value = "各项列和值") Map<String, Object> data,
                       @Comment(name = "beforeQuery", value = "是否根据id查询有没有数据") boolean beforeQuery) {
        if (StrUtil.isBlank(this.primary)) {
            throw new LiteApiException("请设置主键");
        }
        if (data != null) {
            data.forEach((key, value) -> this.columns.put(columnMapperAdapter.getDefault().unmapping(key), value));
        }
        Object primaryValue = this.columns.get(this.primary);
        if (data != null && StrUtil.isBlank(Objects.toString(primaryValue, ""))) {
            primaryValue = data.get(this.primary);
        }
        if (beforeQuery) {
            if (primaryValue != null && StrUtil.isNotBlank(Objects.toString(primaryValue))) {
                List<Object> params = new ArrayList<>();
                params.add(primaryValue);
                Integer count = sqlModule.selectInt(new BoundSql(runtimeContext, "select count(*) count from " + this.tableName + " where " + this.primary + " = ?", params, sqlModule));
                if (count == 0) {
                    return insert(runtimeContext, data);
                }
                return update(runtimeContext, data);
            } else {
                return insert(runtimeContext, data);
            }
        }

        if (primaryValue != null && StrUtil.isNotBlank(Objects.toString(primaryValue, ""))) {
            return update(runtimeContext, data);
        }
        return insert(runtimeContext, data);
    }

    @Comment("保存到表中，当主键有值时则修改，否则插入")
    public Object save(RuntimeContext runtimeContext,
                       @Comment(name = "beforeQuery", value = "是否根据id查询有没有数据") boolean beforeQuery) {
        return this.save(runtimeContext, null, beforeQuery);
    }

    @Comment("保存到表中，当主键有值时则修改，否则插入")
    public Object save(RuntimeContext runtimeContext,
                       @Comment(name = "data", value = "各项列和值") Map<String, Object> data) {
        return this.save(runtimeContext, data, false);
    }


    @Comment("查询条数")
    public int count(RuntimeContext runtimeContext) {
        preHandle(SqlMode.COUNT);
        StringBuilder builder = new StringBuilder();
        builder.append("select count(1) from ").append(tableName);
        List<Object> params = buildWhere(builder);
        if (!params.isEmpty()) {
            return Db.queryInt(builder.toString(), params);
        }
        return Db.queryInt(builder.toString());
    }

    @Comment("判断是否存在")
    public boolean exists(RuntimeContext runtimeContext) {
        return count(runtimeContext) > 0;
    }

    @Comment("设置查询的列，如`columns('a','b','c')`")
    public NamedTable columns(@Comment(name = "properties", value = "各项列") String... properties) {
        if (properties != null) {
            for (String property : properties) {
                column(property);
            }
        }
        return this;
    }

    @Comment("设置查询的列，如`columns(['a','b','c'])`")
    public NamedTable columns(@Comment(name = "properties", value = "查询的列") Collection<String> properties) {
        if (properties != null) {
            columns(properties.toArray(new String[0]));
        }
        return this;
    }

    @Comment("拼接where")
    public Where where() {
        return where;
    }
}
