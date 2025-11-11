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

package com.xclite.api.modules.db;


import com.xclite.api.modules.db.provider.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 列名转换适配器
 *
 * @author mxd
 * 修改者: zhixiulee
 */
public class ColumnMapperAdapter {

    ColumnMapperProvider defaultColumnMapperProvider;

    Map<String, ColumnMapperProvider> columnMapperProviders = new HashMap<>();

    public ColumnMapperAdapter() {
        setDefault(new DefaultColumnMapperProvider());
        add(new CamelColumnMapperProvider());
        add(new PascalColumnMapperProvider());
        add(new LowerColumnMapperProvider());
        add(new UpperColumnMapperProvider());
    }

    public void add(ColumnMapperProvider columnMapperProvider) {
        columnMapperProviders.put(columnMapperProvider.name(), columnMapperProvider);
    }

    public void setDefault(ColumnMapperProvider columnMapperProvider) {
        defaultColumnMapperProvider = columnMapperProvider;
        add(columnMapperProvider);
    }

    public void setDefault(String name) {
        defaultColumnMapperProvider = columnMapperProviders.get(name);
    }

    public ColumnMapperProvider getDefault() {
        return defaultColumnMapperProvider;
    }
}
