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
import lombok.Setter;

/**
 * 接口存储配置
 *
 * @author zhixiulee
 */
@Setter
public class Resource {

    private static final String CONFIG_PREFIX = "lite-api.resource.";

    /**
     * 存储类型，默认是文件
     */
    private String type = "file";

    /**
     * 文件存储位置
     */
    private String location = "/data/lite-api/";

    /**
     * 是否是只读模式
     */
    private boolean readonly = false;

    private boolean showUrl = false;

    /**
     * 使用数据库存储时使用的数据源
     */
    private String datasource;

    public String getType() {
        return PropKit.get(CONFIG_PREFIX + "type", type);
    }

    public String getLocation() {
        return PropKit.get(CONFIG_PREFIX + "location", location);
    }

    public boolean isReadonly() {
        return PropKit.getBoolean(CONFIG_PREFIX + "readonly", readonly);
    }

    public boolean isShowUrl() {
        return PropKit.getBoolean(CONFIG_PREFIX + "showUrl", showUrl);
    }

}
