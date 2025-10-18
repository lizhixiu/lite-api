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


import com.xclite.api.modules.db.provider.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 列名转换适配器
 *
 * @author zhixiulee
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
