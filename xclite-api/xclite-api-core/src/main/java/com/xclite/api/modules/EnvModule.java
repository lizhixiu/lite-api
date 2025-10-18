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

import com.jfinal.kit.PropKit;
import com.xclite.api.annotation.LiteModule;
import org.ssssssss.script.annotation.Comment;

/**
 * env模块
 *
 * @author zhixiulee
 */
@LiteModule("env")
public class EnvModule {

    @Comment("获取配置")
    public String get(@Comment(name = "key", value = "配置项") String key) {
        return PropKit.get(key);
    }


    @Comment("获取配置")
    public String get(@Comment(name = "key", value = "配置项") String key,
                      @Comment(name = "defaultValue", value = "未配置时的默认值") String defaultValue) {
        return PropKit.get(key, defaultValue);
    }


}
