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


import com.xclite.api.model.JsonCode;

/**
 * JSON响应结构常量类
 *
 * @author zhixiulee
 */
public interface JsonCodeConstants {

    JsonCode IS_READ_ONLY = new JsonCode(-1, "当前为只读模式，无法操作");

}
