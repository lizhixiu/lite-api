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
import com.jfinal.plugin.activerecord.dialect.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 方言适配器
 *
 * @author mxd
 */
@Slf4j
public class DialectAdapter {

    private static final List<LiteDialect> dialectList = new ArrayList<>();

    public DialectAdapter() {
        add(new LiteDialect("ansi", new AnsiSqlDialect()));
        add(new LiteDialect("h2", new H2Dialect()));
        add(new LiteDialect("informix", new InformixDialect()));
        add(new LiteDialect("mysql", new MysqlDialect()));
        add(new LiteDialect("oracle", new OracleDialect()));
        add(new LiteDialect("postgresql", new PostgreSqlDialect()));
        add(new LiteDialect("sqlite3", new Sqlite3Dialect()));
        add(new LiteDialect("sqlserver", new SqlServerDialect()));
    }

    public void add(LiteDialect liteDialect) {
        dialectList.add(0, liteDialect);
    }

    public static Dialect getDialect(String dbType) {
        for (LiteDialect dialect : dialectList) {
            if (StrUtil.equals(dialect.getDbType(), dbType)) {
                return dialect.getDialect();
            }
        }
        log.warn("lite-api无法获取dbType为{}的dialect", dbType);
        return null;
    }

}
