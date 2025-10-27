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


package com.xclite.api.modules.db.model;

import org.ssssssss.script.reflection.JavaReflection;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SqlTypes {

    private static final Map<String, Integer> SQL_TYPE_MAPPINGS;

    static {
        Field[] fields = Types.class.getFields();
        SQL_TYPE_MAPPINGS = Stream.of(fields)
                .collect(Collectors.toMap(field -> field.getName().toLowerCase(), field -> (Integer) JavaReflection.getFieldValue(Types.class, field)));
    }

    public static Integer getSqlType(String type, boolean defaultNull) {
        Integer value = SQL_TYPE_MAPPINGS.get(type.toLowerCase());
        if (value == null && defaultNull) {
            return Types.NULL;
        }
        return value;
    }
}
