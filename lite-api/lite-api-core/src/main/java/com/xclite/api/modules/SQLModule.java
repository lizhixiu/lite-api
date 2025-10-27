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


package com.xclite.api.modules;

import cn.hutool.core.util.StrUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbPro;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.dialect.Dialect;
import com.xclite.api.annotation.LiteModule;
import com.xclite.api.context.RequestContext;
import com.xclite.api.context.RequestEntity;
import com.xclite.api.interceptor.ResultProvider;
import com.xclite.api.modules.db.BoundSql;
import com.xclite.api.modules.db.ColumnMapperAdapter;
import com.xclite.api.modules.db.cache.SqlCache;
import com.xclite.api.modules.db.inteceptor.NamedTableInterceptor;
import com.xclite.api.modules.db.inteceptor.SQLInterceptor;
import com.xclite.api.modules.db.model.Page;
import com.xclite.api.modules.db.model.SqlParameter;
import com.xclite.api.modules.db.provider.PageProvider;
import com.xclite.api.modules.db.table.NamedTable;
import com.xclite.api.plugin.LiteApiPlugin;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.ssssssss.script.MagicScriptContext;
import org.ssssssss.script.annotation.Comment;
import org.ssssssss.script.functions.DynamicAttribute;
import org.ssssssss.script.runtime.RuntimeContext;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * sql模块
 *
 * @author zhixiulee
 */
@Slf4j
@LiteModule("db")
public class SQLModule implements DynamicAttribute<SQLModule, SQLModule>, DynamicModule<SQLModule> {

    // 分页查询order by 正则表达式
    Pattern REPLACE_ORDER_BY = Pattern.compile("\\s+order\\s+by\\s+[^,\\s]+(\\s+asc|\\s+desc)?(\\s*,\\s*[^,\\s]+(\\s+asc|\\s+desc)?)*\\s*$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    // 插件实例
    private static final LiteApiPlugin PLUGIN = LiteApiPlugin.getInstance();
    // sql 拦截器
    private final List<SQLInterceptor> sqlInterceptors = PLUGIN.getSqlInterceptors();
    // 命名表拦截器
    private final List<NamedTableInterceptor> namedTableInterceptors = PLUGIN.getNamedTableInterceptors();
    // 缓存名称
    private String cacheName;
    // 缓存过期时间
    private long ttl;
    // 是否开启逻辑删除
    @Getter
    private boolean logicDelete = PLUGIN.getCrud() != null && PLUGIN.getCrud().isLogicDelete();
    // 逻辑删除列
    private String logicDeleteColumn = PLUGIN.getCrud() != null ? PLUGIN.getCrud().getLogicDeleteColumn() : null;
    // 逻辑删除值
    private String logicDeleteValue = PLUGIN.getCrud() != null ? PLUGIN.getCrud().getLogicDeleteValue() : null;
    // 分页查询提供器
    private final PageProvider pageProvider = PLUGIN.getPageProvider();
    // 结果提供器
    private final ResultProvider resultProvider = PLUGIN.getResultProvider();
    // 列映射适配器
    private ColumnMapperAdapter columnMapperAdapter = PLUGIN.getColumnMapperAdapter();
    // sql 缓存
    private SqlCache sqlCache = PLUGIN.getSqlCache();
    // 默认数据源
    @Getter
    private String defaultDataSourceKey;

    public static List<SqlParameter> params;

    /**
     * 数据源切换
     *
     * @param key 数据源key
     * @return 动态模块实例
     */
    @Override
    @Transient
    public SQLModule getDynamicAttribute(String key) {
        SQLModule sqlModule = cloneSQLModule();
        sqlModule.setDefaultDataSourceKey(key);
        return sqlModule;
    }

    /**
     * 获取动态模块实例
     *
     * @param context 上下文
     * @return 动态模块实例
     */
    @Override
    public SQLModule getDynamicModule(MagicScriptContext context) {
        String dataSourceKey = context.getString("default_data_source");
        if (StrUtil.isEmpty(dataSourceKey)) return this;
        SQLModule newSqlModule = cloneSQLModule();
        newSqlModule.setDefaultDataSourceKey(dataSourceKey);
        return newSqlModule;
    }

    /**
     * 设置默认数据源
     *
     * @param defaultDataSourceKey 数据源key
     */
    @Transient
    public void setDefaultDataSourceKey(String defaultDataSourceKey) {
        this.defaultDataSourceKey = defaultDataSourceKey;
    }

    public DbPro getDb() {
        return StrUtil.isNotBlank(defaultDataSourceKey) ? Db.use(defaultDataSourceKey) : Db.use();
    }

    /**
     * 查询List
     */
    @Comment("查询SQL，返回List类型结果")
    public List<Map<String, Object>> select(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml) {
        return select(runtimeContext, sqlOrXml, null);
    }

    /**
     * 查询List，并传入变量信息
     *
     * @param runtimeContext 上下文
     * @param sqlOrXml       `SQL`语句或`xml`
     * @param params         变量信息
     * @return List类型结果
     */
    @Comment("查询SQL，并传入变量信息，返回List类型结果")
    public List<Map<String, Object>> select(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml, @Comment(name = "params", value = "变量信息") Map<String, Object> params) {
        return select(new BoundSql(runtimeContext, sqlOrXml, params, this));
    }

    /**
     * 查询List
     *
     * @param boundSql 绑定sql
     * @return List类型结果
     */
    @Transient
    public List<Map<String, Object>> select(BoundSql boundSql) {
        return boundSql.execute(this.sqlInterceptors, () -> queryForList(boundSql));
    }

    /**
     * 查询List
     *
     * @param boundSql 绑定sql
     * @return List类型结果
     */
    @Transient
    private List<Map<String, Object>> queryForList(BoundSql boundSql) {
        List<Record> records = getDb().find(boundSql.getSql(), boundSql.getParameters());
        if (boundSql.getExcludeColumns() != null) {
            records.forEach(row -> boundSql.getExcludeColumns().forEach(row::remove));
        }
        List<Record> newRecords = new ArrayList<>();
        for (Record record : records) {
            newRecords.add(convertToDefaultColumnNames(record));
        }
        List<Map<String, Object>> results = new ArrayList<>();
        for (Record record : newRecords) {
            results.add(record.toMap());
        }
        return results;
    }

    /**
     * 查询总条目数
     *
     * @param runtimeContext 上下文
     * @param sqlOrXml       `SQL`语句或`xml`
     * @return 总条目数
     */
    @Comment("查询总条目数")
    public Integer count(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml) {
        return count(runtimeContext, sqlOrXml, null);
    }

    /**
     * 查询总条目数，并传入变量信息
     *
     * @param runtimeContext 上下文
     * @param sqlOrXml       `SQL`语句或`xml`
     * @param params         变量信息
     * @return 总条目数
     */
    @Comment("查询总条目数，并传入变量信息")
    public Integer count(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml, @Comment(name = "params", value = "变量信息") Map<String, Object> params) {
        BoundSql boundSql = new BoundSql(runtimeContext, sqlOrXml, params, this);
        BoundSql countBoundSql = boundSql.copy(getCountSql(boundSql.getSql()));
        return selectInt(countBoundSql);
    }

    /**
     * 获取count sql
     *
     * @param sql 原始sql
     * @return count sql
     */
    private String getCountSql(String sql) {
        return "select count(1) from ( \n" + REPLACE_ORDER_BY.matcher(sql).replaceAll("") + "\n ) count_";
    }


    /**
     * 查询int值
     *
     * @param runtimeContext 上下文
     * @param sqlOrXml       `SQL`语句或`xml`
     * @return int值
     */
    @Comment("查询int值，适合单行单列int的结果")
    public Integer selectInt(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml) {
        return selectInt(runtimeContext, sqlOrXml, null);
    }


    /**
     * 查询int值
     *
     * @param runtimeContext 上下文
     * @param sqlOrXml       `SQL`语句或`xml`
     * @param params         变量信息
     * @return int值
     */
    @Comment("查询int值，并传入变量信息，适合单行单列int的结果")
    public Integer selectInt(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml, @Comment(name = "params", value = "变量信息") Map<String, Object> params) {
        return selectInt(new BoundSql(runtimeContext, sqlOrXml, params, this));
    }

    /**
     * 查询int值
     *
     * @param boundSql 绑定sql
     * @return int值
     */
    @Transient
    public Integer selectInt(BoundSql boundSql) {
//        assertDatasourceNotNull();
        return boundSql.execute(this.sqlInterceptors, () -> getDb().queryInt(boundSql.getSql(), boundSql.getParameters()));
    }

    /**
     * 查询Map
     *
     * @param runtimeContext 上下文
     * @param sqlOrXml       `SQL`语句或`xml`
     * @return Map类型结果
     */
    @Comment("查询单条结果，查不到返回null")
    public Map<String, Object> selectOne(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml) {
        return selectOne(runtimeContext, sqlOrXml, null);
    }

    /**
     * 查询Map,并传入变量信息
     *
     * @param runtimeContext 上下文
     * @param sqlOrXml       `SQL`语句或`xml`
     * @param params         变量信息
     * @return Map类型结果
     */
    @Comment("查询单条结果，并传入变量信息，查不到返回null")
    public Map<String, Object> selectOne(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml, @Comment(name = "params", value = "变量信息") Map<String, Object> params) {
        return selectOne(new BoundSql(runtimeContext, sqlOrXml, params, this));
    }

    /**
     * 查询Map
     *
     * @param boundSql 绑定sql
     * @return Map类型结果
     */
    @Transient
    public Map<String, Object> selectOne(BoundSql boundSql) {
//        assertDatasourceNotNull();
        return boundSql.execute(this.sqlInterceptors, () -> {
//            Record row = dataSourceNode.getJdbcTemplate().query(boundSql.getSql(), new SingleRowResultSetExtractor<>(this.columnMapRowMapper), boundSql.getParameters());
            Record row = getDb().findFirst(boundSql.getSql(), boundSql.getParameters());
            if (row == null) {
                return null;
            }
            if (boundSql.getExcludeColumns() != null) {
                boundSql.getExcludeColumns().forEach(row::remove);
            }
            row = convertToDefaultColumnNames(row);
            return row.toMap();
        });
    }

    /**
     * 转换为默认列名
     *
     * @param row 记录
     * @return 转换后的记录
     */
    private Record convertToDefaultColumnNames(Record row) {
        Record newRecord = new Record();
        for (String columnName : row.getColumnNames()) {
            String newColumnName = columnMapperAdapter.getDefault().mapping(columnName);
            newRecord.set(newColumnName, row.get(columnName));
        }
        return newRecord;
    }

    /**
     * 查询单行单列的值
     *
     * @param runtimeContext 上下文
     * @param sqlOrXml       `SQL`语句或`xml`
     * @return 单行单列的值
     */
    @Comment("查询单行单列的值")
    public Object selectValue(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml) {
        return selectValue(runtimeContext, sqlOrXml, null);
    }

    /**
     * 查询单行单列的值，并传入变量信息
     *
     * @param runtimeContext 上下文
     * @param sqlOrXml       `SQL`语句或`xml`
     * @param params         变量信息
     * @return 单行单列的值
     */
    @Comment("查询单行单列的值，并传入变量信息")
    public Object selectValue(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml, @Comment(name = "params", value = "变量信息") Map<String, Object> params) {
//        assertDatasourceNotNull();
        BoundSql boundSql = new BoundSql(runtimeContext, sqlOrXml, params, this);
        return boundSql.execute(this.sqlInterceptors, () -> getDb().query(boundSql.getSql(), boundSql.getParameters()));
    }

    /**
     * 分页查询
     *
     * @param runtimeContext 上下文
     * @param sqlOrXml       `SQL`语句或`xml`
     * @param params         变量信息
     * @return 分页结果
     */
    @Comment("执行分页查询，分页条件自动获取")
    public Object page(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml, @Comment(name = "params", value = "变量信息") Map<String, Object> params) {
        return page(new BoundSql(runtimeContext, sqlOrXml, params, this));
    }

    /**
     * 分页查询,并传入变量信息
     *
     * @param runtimeContext 上下文
     * @param sqlOrXml       `SQL`语句或`xml`
     * @return 分页结果
     */
    @Comment("执行分页查询，并传入变量信息，分页条件自动获取")
    public Object page(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml) {
        return page(runtimeContext, sqlOrXml, (Map<String, Object>) null);
    }

    /**
     * 分页查询（手动传入page和pageSize参数）
     *
     * @param runtimeContext 上下文
     * @param sqlOrXml       `SQL`语句或`xml`
     * @param page           页码
     * @param pageSize       每页条数
     * @return 分页结果
     */
    @Comment("执行分页查询，分页条件手动传入")
    public Object page(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml, @Comment(name = "page", value = "页码") int page, @Comment(name = "pageSize", value = "每页条数") int pageSize) {
        return page(runtimeContext, sqlOrXml, page, pageSize, null);
    }

    /**
     * 分页查询（手动传入page和pageSize参数）
     *
     * @param runtimeContext 上下文
     * @param sqlOrXml       `SQL`语句或`xml`
     * @param page           页码
     * @param pageSize       每页条数
     * @param params         变量信息
     * @return 分页结果
     */
    @Comment("执行分页查询，并传入变量信息，分页条件手动传入")
    public Object page(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml, @Comment(name = "page", value = "页码") int page, @Comment(name = "pageSize", value = "每页条数") int pageSize, @Comment(name = "params", value = "变量信息") Map<String, Object> params) {
        BoundSql boundSql = new BoundSql(runtimeContext, sqlOrXml, params, this);
        return page(boundSql, new Page(page, pageSize));
    }

    /**
     * 分页查询（手动传入分页SQL语句）
     */
    @Comment("执行分页查询，分页`SQL`语句手动传入")
    public Object page(RuntimeContext runtimeContext, @Comment(name = "countSqlOrXml", value = "count语句") String countSqlOrXml, @Comment(name = "sqlOrXml", value = "查询语句") String sqlOrXml) {
        return page(runtimeContext, countSqlOrXml, sqlOrXml, null);
    }

    /**
     * 分页查询（手动传入分页SQL语句）
     *
     * @param runtimeContext 上下文
     * @param countSqlOrXml  count语句
     * @param sqlOrXml       查询语句
     * @param params         变量信息
     * @return 分页结果
     */
    @Comment("执行分页查询，并传入变量信息，分页`SQL`countSqlOrXml")
    public Object page(RuntimeContext runtimeContext, @Comment(name = "countSqlOrXml", value = "count语句") String countSqlOrXml, @Comment(name = "sqlOrXml", value = "查询语句") String sqlOrXml, @Comment(name = "params", value = "变量信息") Map<String, Object> params) {
        int count = selectInt(new BoundSql(runtimeContext, countSqlOrXml, params, this));
        Page page = pageProvider.getPage(runtimeContext);
        BoundSql boundSql = new BoundSql(runtimeContext, sqlOrXml, params, this);
        return page(count, boundSql, page, null);
    }

    /**
     * 分页查询（手动传入count）
     *
     * @param runtimeContext 上下文
     * @param count          总条数
     * @param sqlOrXml       查询语句
     * @param limit          限制条数
     * @param offset         跳过条数
     * @param params         变量信息
     * @return 分页结果
     */
    @Comment("执行分页查询，并传入变量信息，分页`SQL`count")
    public Object page(RuntimeContext runtimeContext, @Comment(name = "count", value = "总条数") int count, @Comment(name = "sqlOrXml", value = "查询语句") String sqlOrXml, @Comment(name = "limit", value = "限制条数") int limit, @Comment(name = "offset", value = "跳过条数") int offset, @Comment(name = "params", value = "变量信息") Map<String, Object> params) {
        BoundSql boundSql = new BoundSql(runtimeContext, sqlOrXml, params, this);
        return page(count, boundSql, new Page(limit, offset), null);
    }

    /**
     * 分页查询（手动传入count）
     *
     * @param count    总条数
     * @param boundSql 查询语句
     * @param page     分页信息
     * @param dialect  数据库方言
     * @return 分页结果
     */
    private Object page(int count, BoundSql boundSql, Page page, Dialect dialect) {
        List<Map<String, Object>> list = null;
        if (count > 0) {
            BoundSql pageBoundSql = buildPageBoundSql(dialect, boundSql, page.getPageNumber(), page.getPageSize());
            list = pageBoundSql.execute(this.sqlInterceptors, () -> queryForList(pageBoundSql));
        }
        RequestEntity requestEntity = RequestContext.getRequestEntity();
        return resultProvider.buildPageResult(requestEntity, page, count, list);
    }

    /**
     * 构建分页查询语句
     *
     * @param dialect  数据库方言
     * @param boundSql 查询语句
     * @param page     页码
     * @param pageSize 每页条数
     * @return 分页查询语句
     */
    private BoundSql buildPageBoundSql(Dialect dialect, BoundSql boundSql, int page, int pageSize) {
        String pageSql = dialect.forPaginate(page, pageSize, new StringBuilder(boundSql.getSql()));
        return boundSql.copy(pageSql);
    }

    /**
     * 分页查询（自动获取分页信息）
     *
     * @param boundSql 查询语句
     * @return 分页结果
     */
    @Transient
    public Object page(BoundSql boundSql) {
        Page page = pageProvider.getPage(boundSql.getRuntimeContext());
        return page(boundSql, page);
    }

    /**
     * 分页查询（自动获取分页信息）
     *
     * @param boundSql 查询语句
     * @param page     分页信息
     * @return 分页结果
     */
    @Transient
    public Object page(BoundSql boundSql, Page page) {
//        assertDatasourceNotNull();
        BoundSql countBoundSql = boundSql.copy(getCountSql(boundSql.getSql()));
        int count = selectInt(countBoundSql);
        return page(count, boundSql, page, getDb().getConfig().getDialect());
    }


    /**
     * 执行update
     *
     * @param runtimeContext 上下文
     * @param sqlOrXml       查询语句
     * @return 受影响行数
     */
    @Comment("执行update操作，返回受影响行数")
    public int update(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml) {
        return update(runtimeContext, sqlOrXml, null);
    }

    /**
     * 执行update，并传入变量信息
     */
    @Comment("执行update操作，并传入变量信息，返回受影响行数")
    public int update(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml, @Comment(name = "params", value = "变量信息") Map<String, Object> params) {
        return update(new BoundSql(runtimeContext, sqlOrXml, params, this));
    }

    /**
     * 执行update
     *
     * @param boundSql 查询语句
     * @return 受影响行数
     */
    @Transient
    public int update(BoundSql boundSql) {
//        assertDatasourceNotNull();
        return boundSql.execute(sqlInterceptors, () -> getDb().update(boundSql.getSql(), boundSql.getParameters()));
    }

    /**
     * 插入并返回主键
     *
     * @param runtimeContext 上下文
     * @param sqlOrXml       查询语句
     * @return 插入主键
     */
    @Comment("执行insert操作，返回插入主键")
    public Object insert(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml) {
        return insert(runtimeContext, sqlOrXml, null, null);
    }

    /**
     * 插入并返回主键，并传入变量信息
     *
     * @param runtimeContext 上下文
     * @param sqlOrXml       查询语句
     * @param params         变量信息
     * @return 插入主键
     */
    @Comment("执行insert操作，并传入变量信息，返回插入主键")
    public Object insert(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml, @Comment(name = "params", value = "变量信息") Map<String, Object> params) {
        return insert(runtimeContext, sqlOrXml, null, params);
    }

    /**
     * 插入并返回主键
     *
     * @param runtimeContext 上下文
     * @param sqlOrXml       查询语句
     * @param primary        主键列
     * @return 插入主键
     */
    @Comment("执行insert操作，返回插入主键")
    public Object insert(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml, @Comment(name = "primary", value = "主键列") String primary) {
        return insert(runtimeContext, sqlOrXml, primary, null);
    }

    /**
     * 插入并返回主键
     *
     * @param runtimeContext 上下文
     * @param sqlOrXml       查询语句
     * @param primary        主键列
     * @param params         变量信息
     * @return 插入主键
     */
    @Comment("执行insert操作，并传入主键和变量信息，返回插入主键")
    public Object insert(RuntimeContext runtimeContext, @Comment(name = "sqlOrXml", value = "`SQL`语句或`xml`") String sqlOrXml, @Comment(name = "primary", value = "主键列") String primary, @Comment(name = "params", value = "变量信息") Map<String, Object> params) {
        return insert(new BoundSql(runtimeContext, sqlOrXml, params, this), primary);
    }

    /**
     * 执行insert
     *
     * @param boundSql 查询语句
     * @param primary  主键列
     * @return 插入主键
     */
    @Transient
    public Object insert(BoundSql boundSql, String primary) {
        return boundSql.execute(sqlInterceptors, () -> getDb().update(boundSql.getSql(), boundSql.getParameters()), false);
    }

    /**
     * 指定table，进行单表操作
     *
     * @param tableName 表名
     * @return 单表操作对象
     */
    @Comment("指定table，进行单表操作")
    public NamedTable table(@Comment(name = "tableName", value = "表名") String tableName) {
        return new NamedTable(tableName, this, columnMapperAdapter, namedTableInterceptors);
    }

    /**
     * 获取缓存名称
     *
     * @return 缓存名称
     */
    @Transient
    public String getCacheName() {
        return cacheName;
    }

    /**
     * 设置缓存名称
     *
     * @param cacheName 缓存名称
     */
    @Transient
    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    /**
     * 获取缓存对象
     *
     * @return 缓存对象
     */
    @Transient
    public SqlCache getSqlCache() {
        return sqlCache;
    }

    /**
     * 设置缓存对象
     *
     * @param sqlCache 缓存对象
     */
    @Transient
    public void setSqlCache(SqlCache sqlCache) {
        this.sqlCache = sqlCache;
    }

    /**
     * 获取缓存有效期
     *
     * @return 缓存有效期
     */
    @Transient
    public long getTtl() {
        return ttl;
    }

    /**
     * 设置缓存有效期
     *
     * @param ttl 缓存有效期
     */
    @Transient
    public void setTtl(long ttl) {
        this.ttl = ttl;
    }


    @Transient
    public String getLogicDeleteColumn() {
        return logicDeleteColumn;
    }

    @Transient
    public void setLogicDeleteColumn(String logicDeleteColumn) {
        this.logicDeleteColumn = logicDeleteColumn;
    }

    @Transient
    public String getLogicDeleteValue() {
        return logicDeleteValue;
    }

    @Transient
    public void setLogicDeleteValue(String logicDeleteValue) {
        this.logicDeleteValue = logicDeleteValue;
    }

    /**
     * 采用驼峰列名
     *
     * @return SQLModule
     */
    @Comment("采用驼峰列名")
    public SQLModule camel() {
        return columnCase("camel");
    }

    /**
     * 采用帕斯卡列名
     *
     * @return SQLModule
     */
    @Comment("采用帕斯卡列名")
    public SQLModule pascal() {
        return columnCase("pascal");
    }

    /**
     * 采用全小写列名
     *
     * @return SQLModule
     */
    @Comment("采用全小写列名")
    public SQLModule lower() {
        return columnCase("lower");
    }

    /**
     * 采用全大写列名
     *
     * @return SQLModule
     */
    @Comment("采用全大写列名")
    public SQLModule upper() {
        return columnCase("upper");
    }

    /**
     * 列名保持原样
     *
     * @return SQLModule
     */
    @Comment("列名保持原样")
    public SQLModule normal() {
        return columnCase("default");
    }

    /**
     * 指定列名转换
     *
     * @param name 列名转换类型
     * @return SQLModule
     */
    @Comment("指定列名转换")
    public SQLModule columnCase(String name) {
        SQLModule sqlModule = cloneSQLModule();
        sqlModule.columnMapperAdapter.setDefault(name);
        return sqlModule;
    }

    /**
     * 克隆SQLModule
     *
     * @return SQLModule
     */
    @Transient
    public SQLModule cloneSQLModule() {
        SQLModule sqlModule = new SQLModule();
//        sqlModule.setDynamicDataSource(this.dynamicDataSource);
//        sqlModule.setDataSourceNode(this.dataSourceNode);
//        sqlModule.setPageProvider(this.pageProvider);
//        sqlModule.setColumnMapperProvider(this.columnMapperAdapter);
//        sqlModule.setColumnMapRowMapper(this.columnMapRowMapper);
//        sqlModule.setRowMapColumnMapper(this.rowMapColumnMapper);
        sqlModule.columnMapperAdapter = new ColumnMapperAdapter();
        sqlModule.columnMapperAdapter.setDefault(LiteApiPlugin.getInstance().getLiteApiProperties().getSqlColumnCase());
        sqlModule.setSqlCache(this.sqlCache);
        sqlModule.setTtl(this.ttl);
        sqlModule.setCacheName(this.cacheName);
//        sqlModule.setResultProvider(this.resultProvider);
//        sqlModule.setDialectAdapter(this.dialectAdapter);
//        sqlModule.setSqlInterceptors(this.sqlInterceptors);
//        sqlModule.setLogicDeleteValue(this.logicDeleteValue);
//        sqlModule.setLogicDeleteColumn(this.logicDeleteColumn);
//        sqlModule.setNamedTableInterceptors(this.namedTableInterceptors);
        return sqlModule;
    }

    /**
     * 使用缓存
     *
     * @param cacheName 缓存名
     * @param ttl       过期时间
     */
    @Comment("使用缓存")
    public SQLModule cache(@Comment(name = "cacheName", value = "缓存名") String cacheName, @Comment(name = "ttl", value = "过期时间") long ttl) {
        if (cacheName == null) {
            return this;
        }
        SQLModule sqlModule = cloneSQLModule();
        sqlModule.setCacheName(cacheName);
        sqlModule.setTtl(ttl);
        return sqlModule;
    }

    /**
     * 使用缓存（采用默认缓存时间）
     *
     * @param cacheName 缓冲名
     */
    @Comment("使用缓存，过期时间采用默认配置")
    public SQLModule cache(@Comment(name = "cacheName", value = "缓存名") String cacheName) {
        return cache(cacheName, 0);
    }

    /**
     * 开启事务，在一个回调中进行操作
     *
     * @param function 回调函数
     */
    @Comment("开启事务，并在回调中处理")
    public Object transaction(@Comment(name = "function", value = "回调函数，如：()=>{....}") Function<?, ?> function) {
        // 创建事务
        return getDb().transaction(tx -> function.apply(null));
    }
}
