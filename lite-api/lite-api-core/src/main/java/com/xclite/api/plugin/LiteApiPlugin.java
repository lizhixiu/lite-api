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


package com.xclite.api.plugin;

import cn.hutool.core.util.StrUtil;
import com.jfinal.plugin.IPlugin;
import com.xclite.api.config.*;
import com.xclite.api.datasource.DialectAdapter;
import com.xclite.api.datasource.LiteDynamicDataSource;
import com.xclite.api.function.LiteFunction;
import com.xclite.api.interceptor.DefaultResultProvider;
import com.xclite.api.interceptor.RequestInterceptor;
import com.xclite.api.interceptor.ResultProvider;
import com.xclite.api.loader.LiteApiLoader;
import com.xclite.api.modules.LiteModuleScanner;
import com.xclite.api.modules.db.ColumnMapperAdapter;
import com.xclite.api.modules.db.cache.DefaultSqlCache;
import com.xclite.api.modules.db.cache.SqlCache;
import com.xclite.api.modules.db.inteceptor.DefaultSqlInterceptor;
import com.xclite.api.modules.db.inteceptor.NamedTableInterceptor;
import com.xclite.api.modules.db.inteceptor.SQLInterceptor;
import com.xclite.api.modules.db.provider.DefaultPageProvider;
import com.xclite.api.modules.db.provider.PageProvider;
import com.xclite.api.utils.VersionUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssssssss.script.MagicResourceLoader;
import org.ssssssss.script.MagicScript;
import org.ssssssss.script.MagicScriptEngine;
import org.ssssssss.script.exception.MagicScriptRuntimeException;
import org.ssssssss.script.functions.DynamicModuleImport;
import org.ssssssss.script.functions.ExtensionMethod;
import org.ssssssss.script.parsing.ast.statement.AsyncCall;
import org.ssssssss.script.reflection.JavaReflection;

import java.util.*;

@Slf4j
public class LiteApiPlugin implements IPlugin {

    private static volatile LiteApiPlugin instance;
    @Getter
    protected volatile boolean isStarted = false;
    @Getter
    private final LiteApiProperties liteApiProperties = new LiteApiProperties();
    @Getter
    private LiteApiLoader apiLoader;
    /**
     * 获取请求拦截器
     */
    @Getter
    private final List<RequestInterceptor> requestInterceptors = new ArrayList<>();
    private final List<LiteFunction> functions = new ArrayList<>();
    private final List<ExtensionMethod> extensionMethods = new ArrayList<>();
    private final Map<String, Object> liteModules = new HashMap<>();

    private LiteDynamicDataSource liteDynamicDataSource;
    /**
     * 获取数据库方言适配器
     */
    @Getter
    private final DialectAdapter dialectAdapter = new DialectAdapter();
    @Getter
    private PageProvider pageProvider;
    @Getter
    private ResultProvider resultProvider;
    @Getter
    private ColumnMapperAdapter columnMapperAdapter;
    @Getter
    @Setter
    private SqlCache sqlCache;
    @Getter
    private final List<SQLInterceptor> sqlInterceptors = new ArrayList<>();
    @Getter
    private final List<NamedTableInterceptor> namedTableInterceptors = new ArrayList<>();
    @Getter
    private Crud crud;


    /**
     * 获取单例实例
     */
    public static LiteApiPlugin getInstance() {
        if (instance == null) {
            synchronized (LiteApiPlugin.class) {
                if (instance == null) {
                    instance = new LiteApiPlugin();
                }
            }
        }
        return instance;
    }

    /**
     * 私有构造函数，强制使用单例
     */
    private LiteApiPlugin() {
    }

    @Override
    public boolean start() {
        if (isStarted) {
            return true;
        }

        try {

            // 配置异步调用线程池大小
            AsyncCall.setThreadPoolExecutorSize(liteApiProperties.getThreadPoolExecutorSize());
            // 配置编译缓存大小
            MagicScript.setCompileCache(liteApiProperties.getCompileCacheSize());
            // 设置响应结果的code值
            ResponseCode responseCodeConfig = liteApiProperties.getResponseCode();
            LiteConstants.RESPONSE_CODE_SUCCESS = responseCodeConfig.getSuccess();
            LiteConstants.RESPONSE_CODE_INVALID = responseCodeConfig.getInvalid();
            LiteConstants.RESPONSE_CODE_EXCEPTION = responseCodeConfig.getException();

            // 配置结果封装实现
            if (resultProvider == null) {
                resultProvider = new DefaultResultProvider(StrUtil.isNotBlank(liteApiProperties.getResponse()) ? liteApiProperties.getResponse() : null);
            }

            // 注册数据库模块
            setupDbModule();
            // 注册Lite模块
            setupLiteModules();
            // 注册Lite函数
            functions.forEach(JavaReflection::registerFunction);

            log.info("模式:{}, lite-api工作目录:{}", liteApiProperties.getResource().getType(), liteApiProperties.getResource().getLocation());
            // 加载所有API配置文件
            apiLoader = new LiteApiLoader(liteApiProperties.getResource().getLocation());
            boolean loadSuccess = apiLoader.loadAll();
            if (!loadSuccess) {
                log.error("Failed to load APIs");
                return false;
            }
            // 打印所有注册的API
            if (liteApiProperties.getResource().isShowUrl()) {
                apiLoader.printApiCache();
            }
            for (RequestInterceptor requestInterceptor : requestInterceptors) {
                log.info("注册请求拦截器：{}", requestInterceptor.getClass());
            }

            // 打印banner
            if (liteApiProperties.isBanner()) {
                printBanner();
            }

            isStarted = true;
            log.info("LiteApiPlugin started successfully");
            return true;

        } catch (Exception e) {
            log.error("Failed to start LiteApiPlugin", e);
            return false;
        }
    }

    private void setupDbModule() {
        // 初始化动态数据源
        if (StrUtil.isBlank(liteApiProperties.getDataSourceKeys())) {
            return;
        }
        // 初始化动态数据源
        liteDynamicDataSource = new LiteDynamicDataSource(liteApiProperties.getDataSourceKeys());
        // 配置分页实现
        if (pageProvider == null) {
            Page pageConfig = liteApiProperties.getPage();
            log.info("未找到分页实现,采用默认分页实现,分页配置:(页码={},页大小={},默认首页={},默认页大小={},最大页大小={})", pageConfig.getPage(), pageConfig.getSize(), pageConfig.getDefaultPage(), pageConfig.getDefaultSize(), pageConfig.getMaxPageSize());
            pageProvider = new DefaultPageProvider(pageConfig.getPage(), pageConfig.getSize(), pageConfig.getDefaultPage(), pageConfig.getDefaultSize(), pageConfig.getMaxPageSize());
        }
        // 配置SQL列名映射实现
        columnMapperAdapter = new ColumnMapperAdapter();
        if (StrUtil.isNotBlank(liteApiProperties.getSqlColumnCase())) {
            columnMapperAdapter.setDefault(liteApiProperties.getSqlColumnCase());
        }
        // 配置SQL缓存实现
        if (sqlCache == null && liteApiProperties.getCache().isEnable()) {
            Cache cacheConfig = liteApiProperties.getCache();
            log.info("未找到SQL缓存实现，采用默认缓存实现(LRU+TTL)，缓存配置:(容量={},TTL={})", cacheConfig.getCapacity(), cacheConfig.getTtl());
            sqlCache = new DefaultSqlCache(liteApiProperties.getCache().getCapacity(), liteApiProperties.getCache().getTtl());
        }
        // 配置SQL打印实现
        if (liteApiProperties.isShowSql()) {
            sqlInterceptors.add(new DefaultSqlInterceptor());
        }
        // 配置CRUD实现
        crud = liteApiProperties.getCrud();
    }

    /**
     * 注册Lite模块
     */
    private void setupLiteModules() {
        // 设置脚本import时 class加载策略（移除 Spring 依赖）
        MagicResourceLoader.setClassLoader((className) -> {
            try {
                // 使用当前线程的上下文类加载器加载类（标准 Java 类加载方式）
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                if (classLoader == null) {
                    // 若上下文类加载器为空，使用系统类加载器作为 fallback
                    classLoader = ClassLoader.getSystemClassLoader();
                }
                return classLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                // 保持原异常类型，确保脚本引擎错误处理一致
                throw new MagicScriptRuntimeException(e);
            }
        });
        log.info("注册模块:{} -> {}", "log", Logger.class);
        MagicResourceLoader.addModule("log", new DynamicModuleImport(Logger.class, context -> LoggerFactory.getLogger(Objects.toString(context.getScriptName(), "Unknown"))));
        // 扫描并注册Magic模块
        LiteModuleScanner.scanAndRegisterModules("com.xclite.api.modules");
        liteModules.forEach((moduleName, moduleClass) -> {
            log.info("注册模块:{} -> {}", moduleName, moduleClass);
            MagicResourceLoader.addModule(moduleName, moduleClass);
        });
        // 自动导入模块
        List<String> importModules = liteApiProperties.getAutoImportModuleList();
        MagicResourceLoader.getModuleNames().stream().filter(importModules::contains).forEach(moduleName -> {
            log.info("自动导入模块：{}", moduleName);
            MagicScriptEngine.addDefaultImport(moduleName, MagicResourceLoader.loadModule(moduleName));
        });
        // 注册扩展方法
        extensionMethods.forEach(extension -> extension.supports().forEach(support -> {
            log.info("注册扩展:{} -> {}", support, extension.getClass());
            JavaReflection.registerMethodExtension(support, extension);
        }));
    }

    @Override
    public boolean stop() {
        isStarted = false;
        if (liteDynamicDataSource != null) {
            liteDynamicDataSource.stop();
        }
        log.info("LiteApiPlugin stopped");
        return true;
    }

    /**
     * 添加请求拦截器
     */
    public void addRequestInterceptor(RequestInterceptor requestInterceptor) {
        requestInterceptors.add(requestInterceptor);
    }

    /**
     * 添加函数
     */
    public void addFunction(LiteFunction function) {
        functions.add(function);
    }

    /**
     * 添加扩展方法
     */
    public void addExtensionMethod(ExtensionMethod extensionMethod) {
        extensionMethods.add(extensionMethod);
    }

    /**
     * 添加SQL拦截器
     */
    public void addSqlInterceptor(SQLInterceptor sqlInterceptor) {
        sqlInterceptors.add(sqlInterceptor);
    }

    /**
     * 添加命名表拦截器
     */
    public void addNamedTableInterceptor(NamedTableInterceptor namedTableInterceptor) {
        namedTableInterceptors.add(namedTableInterceptor);
    }

    /**
     * 添加Lite模块
     */
    public void addLiteModule(String moduleName, Object moduleClass) {
        liteModules.put(moduleName, moduleClass);
    }

    /**
     * 重新加载API配置
     */
    public boolean reloadApis() {
        if (!isStarted) {
            log.warn("Plugin not started, cannot reload APIs");
            return false;
        }

        try {
            if (apiLoader != null) {
                boolean success = apiLoader.loadAll();
                if (success) {
                    log.info("APIs reloaded successfully");
                } else {
                    log.error("Failed to reload APIs");
                }
                return success;
            }
            return false;
        } catch (Exception e) {
            log.error("Error reloading APIs", e);
            return false;
        }
    }

    /**
     * 打印banner
     */
    public void printBanner() {
        System.out.println("  _       _   _               _              _ ");
        System.out.println(" | |     (_) | |_    ___     / \\     _ __   (_)");
        System.out.println(" | |     | | | __|  / _ \\   / _ \\   | '_ \\  | |");
        System.out.println(" | |___  | | | |_  |  __/  / ___ \\  | |_) | | |");
        System.out.println(" |_____| |_|  \\__|  \\___| /_/   \\_\\ | .__/  |_|");
        System.out.println("                                    |_|        " + VersionUtils.getVersion());
    }
}