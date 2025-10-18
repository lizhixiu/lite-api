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

/**
 * 单表API操作
 */
public enum SqlMode {

    /**
     * 执行插入动作
     */
    INSERT,
    /**
     * 执行修改动作
     */
    UPDATE,
    /**
     * 执行删除动作
     */
    DELETE,
    /**
     * 执行查询操作
     */
    SELECT,
    /**
     * 执行查询单个操作
     */
    SELECT_ONE,
    /**
     * 执行分页查询动作
     */
    PAGE,
    /**
     * 执行count查询操作
     */
    COUNT
}
