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


package com.xclite.api.datasource;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.Dialect;
import com.jfinal.plugin.druid.DruidPlugin;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class LiteDynamicDataSource {

    private final static String CONFIG_PREFIX = "lite-api.datasource.";

    private final List<IPlugin> dataSourcePlugins = new ArrayList<>();
    private final List<IPlugin> activeRecordPlugins = new ArrayList<>();

    public LiteDynamicDataSource(String dataSourceKeys) {
        init(dataSourceKeys);
    }

    private void init(String keys) {

        if (StrUtil.isBlank(keys)) {
            return;
        }
        for (String datasourceKey : convertToArray(keys)) {
            // 初始化数据源

            // 配置DruidPlugin
            DruidPlugin druidPlugin = getDruidPlugin(datasourceKey);
            String dbType = getDbType(datasourceKey);
            WallFilter wallFilter = new WallFilter(); // 加强数据库安全
            wallFilter.setDbType(dbType);
            druidPlugin.addFilter(wallFilter);
            StatFilter statFilter = new StatFilter();// 配置数据监控
            statFilter.setMergeSql(true);
            statFilter.setLogSlowSql(true);
            statFilter.setSlowSqlMillis(1500);
            druidPlugin.addFilter(statFilter); // 添加 StatFilter 才会有统计数据

            // 配置ActiveRecordPlugin
            ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(datasourceKey, druidPlugin);
            activeRecordPlugin.setDialect(getDialect(datasourceKey));
            activeRecordPlugin.setTransactionLevel(Connection.TRANSACTION_READ_COMMITTED);
            activeRecordPlugin.setShowSql(PropKit.getBoolean("devMode", false));
            activeRecordPlugin.getEngine().setToClassPathSourceFactory();

            log.info("注册数据源：{}", datasourceKey);
            dataSourcePlugins.add(0, druidPlugin);
            activeRecordPlugins.add(0, activeRecordPlugin);
            druidPlugin.start();
            activeRecordPlugin.start();
        }
    }

    /**
     * 将逗号分隔的字符串转换为字符串列表，自动处理空字符串、首尾空格、方括号等情况
     *
     * @param input 逗号分隔的字符串，支持方括号包裹（如 "[a, b, c]"）
     * @return 转换后的字符串列表，若输入为空或仅包含空格则返回空列表
     */
    private static List<String> convertToArray(String input) {
        // 1. 去除首尾空白，若为空则返回空数组
        String trimmed = StrUtil.trimToEmpty(input);
        if (StrUtil.isEmpty(trimmed)) {
            return Collections.emptyList();
        }

        // 2. 移除首尾方括号（支持嵌套括号的安全移除，如 "[[a], b]" 仅移除最外层）
        String content = StrUtil.removeSuffix(StrUtil.removePrefix(trimmed, "["), "]");

        // 3. 按 ", " 分割为数组（自动处理空内容为长度 0 的数组）
        return StrUtil.split(content, ", ");
    }

    /**
     * 获取 DruidPlugin 实例
     *
     * @param datasourceKey 数据源键
     * @return DruidPlugin 实例
     */
    public static DruidPlugin getDruidPlugin(String datasourceKey) {
        String jdbcUrl = PropKit.get(CONFIG_PREFIX + datasourceKey + ".url").trim();
        String user = PropKit.get(CONFIG_PREFIX + datasourceKey + ".username").trim();
        String password = PropKit.get(CONFIG_PREFIX + datasourceKey + ".password").trim();
        String driverClass = PropKit.get(CONFIG_PREFIX + datasourceKey + ".driver-class-name").trim();

        return new DruidPlugin(jdbcUrl, user, password, driverClass);
    }

    public static String getDbType(String datasourceKey) {
        return PropKit.get(CONFIG_PREFIX + datasourceKey + ".dbType").trim();
    }

    public static Dialect getDialect(String datasourceKey) {
        String dbType = getDbType(datasourceKey);
        return DialectAdapter.getDialect(dbType);
    }

    public void stop() {
        for (IPlugin plugin : activeRecordPlugins) {
            plugin.stop();
        }
        for (IPlugin plugin : dataSourcePlugins) {
            plugin.stop();
        }
    }
}
