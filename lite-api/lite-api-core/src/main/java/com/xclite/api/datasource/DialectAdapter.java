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
 * 修改者: zhixiulee
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
