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

import com.jfinal.kit.StrKit;
import com.xclite.api.annotation.LiteModule;
import lombok.extern.slf4j.Slf4j;
import org.ssssssss.script.MagicResourceLoader;
import org.ssssssss.script.functions.DynamicModuleImport;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Magic模块扫描器
 * 用于扫描指定包路径下带有@LiteModule注解的类并注册模块
 */
@Slf4j
public class LiteModuleScanner {


    /**
     * 扫描指定包路径下的Magic模块并注册
     *
     * @param basePackage 基础包路径
     */
    public static void scanAndRegisterModules(String basePackage) {
        try {
            Set<Class<?>> moduleClasses = scanClassesWithAnnotation(basePackage);
            for (Class<?> moduleClass : moduleClasses) {
                registerModule(moduleClass);
            }
        } catch (Exception e) {
            log.error("扫描和注册Magic模块时出错", e);
        }
    }

    /**
     * 注册单个模块类
     *
     * @param moduleClass 模块类
     */
    private static void registerModule(Class<?> moduleClass) {
        try {
            LiteModule LiteModuleAnnotation = moduleClass.getAnnotation(LiteModule.class);
            if (LiteModuleAnnotation == null) {
                return;
            }

            String moduleName = LiteModuleAnnotation.value();
            if (StrKit.isBlank(moduleName)) {
                moduleName = moduleClass.getSimpleName();
            }

            Object moduleInstance = moduleClass.newInstance();

            log.info("注册模块:{} -> {}", moduleName, moduleClass);

            if (moduleInstance instanceof DynamicModule) {
                MagicResourceLoader.addModule(moduleName, new DynamicModuleImport(moduleClass, ((DynamicModule<?>) moduleInstance)::getDynamicModule));
            } else {
                MagicResourceLoader.addModule(moduleName, moduleInstance);
            }
        } catch (Exception e) {
            log.error("注册模块时出错: {}", moduleClass.getName(), e);
        }
    }

    /**
     * 扫描指定包路径下带有指定注解的类
     *
     * @param basePackage 基础包路径
     * @return 带有指定注解的类集合
     */
    private static Set<Class<?>> scanClassesWithAnnotation(String basePackage) {
        Set<Class<?>> classes = new HashSet<>();
        try {
            String packageDirName = basePackage.replace('.', '/');
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);

            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();

                if ("file".equals(protocol)) {
                    // 文件系统中的类
                    String filePath = java.net.URLDecoder.decode(url.getFile(), "UTF-8");
                    scanClassesInDirectory(basePackage, filePath, LiteModule.class, classes);
                } else if ("jar".equals(protocol)) {
                    // JAR包中的类
                    scanClassesInJar(url, basePackage, classes);
                }
            }
        } catch (IOException e) {
            log.error("扫描类时出错", e);
        }
        return classes;
    }

    /**
     * 扫描文件系统目录中的类
     */
    private static void scanClassesInDirectory(String basePackage, String filePath,
                                               Class<? extends Annotation> annotationClass,
                                               Set<Class<?>> classes) {
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // 递归扫描子目录
                scanClassesInDirectory(basePackage + "." + file.getName(), file.getAbsolutePath(), annotationClass, classes);
            } else if (file.getName().endsWith(".class")) {
                // 处理.class文件
                String className = basePackage + '.' + file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.getAnnotation(annotationClass) != null) {
                        classes.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    log.error("无法加载类: {}", className, e);
                }
            }
        }
    }

    /**
     * 扫描JAR包中的类
     */
    private static void scanClassesInJar(URL url, String basePackage,
                                         Set<Class<?>> classes) {
        try {
            String jarPath = url.getPath().substring(5, url.getPath().indexOf("!"));
            JarFile jar = new JarFile(jarPath);

            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (name.startsWith(basePackage.replace('.', '/')) && name.endsWith(".class")) {
                    String className = name.replace('/', '.').substring(0, name.length() - 6);
                    try {
                        Class<?> clazz = Class.forName(className);
                        if (clazz.getAnnotation((Class<? extends Annotation>) LiteModule.class) != null) {
                            classes.add(clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        log.error("无法加载类: {}", className, e);
                    }
                }
            }

            jar.close();
        } catch (IOException e) {
            log.error("扫描JAR包时出错", e);
        }
    }
}