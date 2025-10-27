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
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * lite-api配置信息
 *
 * @author zhixiulee
 */
public class LiteApiProperties {

    // 配置前缀常量
    private static final String CONFIG_PREFIX = "lite-api.";

    /**
     * 版本号
     */
    @Getter
    private final String version = "2025.10.01";

    /**
     * 分页信息
     */
    @Getter
    private final Page page = new Page();

    /**
     * 缓存配置
     */
    @Getter
    private final Cache cache = new Cache();

    /**
     * 资源配置
     */
    @Getter
    private final Resource resource = new Resource();

    /**
     * 响应码配置
     */
    @Getter
    private final ResponseCode responseCode = new ResponseCode();

    /**
     * CRUD 配置
     */
    @Getter
    private final Crud crud = new Crud();

    public String getDataSourceKeys() {
        return PropKit.getProp().getProperties().getProperty(CONFIG_PREFIX + "datasource.keys", "");
    }

    /**
     * 数据库列名大小写转换策略
     *
     * @return 默认是default
     */
    public String getSqlColumnCase() {
        return PropKit.get(CONFIG_PREFIX + "sql-column-case", "default");
    }

    /**
     * 是否要打印banner
     */
    public boolean isBanner() {
        return PropKit.getProp().getBoolean(CONFIG_PREFIX + "banner", true);
    }

    /**
     * 自动导入的模块,多个用","分隔
     */
    public List<String> getAutoImportModuleList() {
        //自动导入的模块,多个用","分隔
        String autoImportModule = PropKit.get(CONFIG_PREFIX + "auto-import-module", "db");
        return Arrays.asList(autoImportModule.replaceAll("\\s", "").split(","));
    }

    /**
     * 可自动导入的包（目前只支持以.*结尾的通配符），多个用","分隔
     */
    public List<String> getAutoImportPackageList() {
        String autoImportPackage = PropKit.get(CONFIG_PREFIX + "auto-import-package", "");
        return Arrays.asList(autoImportPackage.replaceAll("\\s", "").split(","));
    }

    /**
     * 接口路径前缀
     */
    public String getPrefix(String prefix) {
        return PropKit.get("lite-api.prefix", prefix);
    }

    /**
     * 是否抛出异常
     */
    public boolean isThrowException() {
        return PropKit.getProp().getBoolean(CONFIG_PREFIX + "throw-exception", false);
    }

    /**
     * 是否允许覆盖应用接口
     */
    public boolean isAllowOverride() {
        return PropKit.getProp().getBoolean(CONFIG_PREFIX + "allow-override", false);
    }

    /**
     * 是否要打印地址
     */
    public boolean isShowUrl() {
        return PropKit.getProp().getBoolean(CONFIG_PREFIX + "show-url", true);
    }

    public boolean isShowSql() {
        return PropKit.getProp().getBoolean(CONFIG_PREFIX + "show-sql", true);
    }

    /**
     * 编译缓存容量
     */
    public int getCompileCacheSize() {
        return PropKit.getInt(CONFIG_PREFIX + "compile-cache-size", 500);
    }

    /**
     * JSON响应结构表达式
     */
    public String getResponse() {
        return PropKit.get(CONFIG_PREFIX + "response", "");
    }

    /**
     * 线程池大小
     */
    public int getThreadPoolExecutorSize() {
        return PropKit.getInt(CONFIG_PREFIX + "thread-pool-executor-size", 10);
    }

}
