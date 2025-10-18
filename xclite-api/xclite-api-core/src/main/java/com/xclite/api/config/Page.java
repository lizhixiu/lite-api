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
 * 分页配置
 *
 * @author zhixiulee
 */
@Setter
public class Page {

    private static final String CONFIG_PREFIX = "lite-api.page.";

    /**
     * 默认page表达式
     */
    private String page = "page";

    /**
     * 默认size表达式
     */
    private String size = "size";

    /**
     * 默认首页
     */
    private int defaultPage = 1;

    /**
     * 默认页大小
     */
    private int defaultSize = 10;

    /**
     * 最大页大小， -1 为不限制
     */
    private int maxPageSize = -1;

    public String getPage() {
        return PropKit.get(CONFIG_PREFIX + "page", page);
    }

    public String getSize() {
        return PropKit.get(CONFIG_PREFIX + "size", size);
    }

    public int getDefaultPage() {
        return PropKit.getInt(CONFIG_PREFIX + "default-page", defaultPage);
    }

    public int getDefaultSize() {
        return PropKit.getInt(CONFIG_PREFIX + "default-size", defaultSize);
    }

    public int getMaxPageSize() {
        return PropKit.getInt(CONFIG_PREFIX + "max-page-size", maxPageSize);
    }

}
