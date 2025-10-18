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


package com.xclite.api.config;

import com.jfinal.kit.PropKit;

/**
 * CRUD 配置
 *
 * @author zhixiulee
 */
public class Crud {

    private static final String CONFIG_PREFIX = "lite-api.crud.";

    /**
     * 是否开启逻辑删除
     */
    public boolean isLogicDelete() {
        return PropKit.getBoolean(CONFIG_PREFIX + "logic-delete", false);
    }

    /**
     * 逻辑删除列
     */
    public String getLogicDeleteColumn() {
        return PropKit.get(CONFIG_PREFIX + "logic-delete-column", "is_del");
    }

    /**
     * 逻辑删除值
     */
    public String getLogicDeleteValue() {
        return PropKit.get(CONFIG_PREFIX + "logic-delete-value", "1");

    }

}
