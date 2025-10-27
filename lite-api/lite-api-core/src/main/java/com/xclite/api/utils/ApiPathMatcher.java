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


package com.xclite.api.utils;

import com.xclite.api.loader.LiteApiLoader;
import com.xclite.api.model.ApiInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * API路径匹配工具类
 * 用于匹配请求路径与API定义路径
 */
public class ApiPathMatcher {

    // 缓存编译后的正则表达式，提高性能
    private static final Map<String, Pattern> patternCache = new ConcurrentHashMap<>();

    /**
     * 匹配请求路径与API路径
     *
     * @param requestPath   请求路径
     * @param requestMethod HTTP方法
     * @return 匹配到的ApiInfo，如果没有匹配到则返回null
     */
    public static ApiInfo match(String requestPath, String requestMethod) {
        // Normalize the request path
        String normalizedRequestPath = normalizePath(requestPath);

        // 从LiteApiLoader缓存中查找匹配的API
        Map<String, ApiInfo> apiCache = LiteApiLoader.getApiCache();

        for (Map.Entry<String, ApiInfo> entry : apiCache.entrySet()) {
            String apiPath = entry.getKey();
            ApiInfo apiInfo = entry.getValue();

            // Normalize the API path for comparison
            String normalizedApiPath = normalizePath(apiPath);

            // 检查HTTP方法是否匹配 (case insensitive)
            if (!requestMethod.equalsIgnoreCase(apiInfo.getMethod())) {
                continue;
            }

            // 检查路径是否匹配
            if (isPathMatch(normalizedRequestPath, normalizedApiPath)) {
                return apiInfo;
            }
        }

        return null;
    }

    /**
     * Normalize path by ensuring it starts with / and doesn't have trailing slashes
     *
     * @param path 路径
     * @return 标准化路径
     */
    private static String normalizePath(String path) {
        if (path == null || path.isEmpty()) {
            return "/";
        }

        // Ensure path starts with /
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        // Remove trailing slash except for root path
        if (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        return path;
    }

    /**
     * 检查请求路径是否与API路径匹配
     *
     * @param requestPath 请求路径
     * @param apiPath     API定义路径
     * @return 是否匹配
     */
    public static boolean isPathMatch(String requestPath, String apiPath) {
        // 简单路径匹配 (case insensitive)
        if (requestPath.equalsIgnoreCase(apiPath)) {
            return true;
        }

        // 支持路径变量的匹配（例如：/users/{id}）
        if (apiPath.contains("{") && apiPath.contains("}")) {
            // 将API路径转换为正则表达式
            String regex = convertPathToRegex(apiPath);
            return requestPath.matches(regex);
        }

        return false;
    }

    /**
     * 将路径转换为正则表达式
     *
     * @param path 路径
     * @return 正则表达式
     */
    private static String convertPathToRegex(String path) {
        // 检查缓存
        if (patternCache.containsKey(path)) {
            return patternCache.get(path).pattern();
        }

        // 转换路径变量 {variable} 为正则表达式捕获组 ([^/]+)
        // 同时转义其他正则表达式特殊字符
        StringBuilder regexBuilder = new StringBuilder();
        int lastEnd = 0;
        
        // 查找所有路径变量
        Pattern varPattern = Pattern.compile("\\{([^}]+)}");
        Matcher varMatcher = varPattern.matcher(path);
        
        while (varMatcher.find()) {
            // 添加路径变量之前的部分（需要转义特殊字符）
            String literalPart = path.substring(lastEnd, varMatcher.start());
            regexBuilder.append(Pattern.quote(literalPart));
            
            // 添加路径变量捕获组
            regexBuilder.append("([^/]+)");
            
            lastEnd = varMatcher.end();
        }
        
        // 添加最后一部分（需要转义特殊字符）
        if (lastEnd < path.length()) {
            String literalPart = path.substring(lastEnd);
            regexBuilder.append(Pattern.quote(literalPart));
        }
        
        regexBuilder.insert(0, "^");
        regexBuilder.append("$");

        String regex = regexBuilder.toString();
        
        // 缓存编译后的正则表达式
        patternCache.put(path, Pattern.compile(regex));

        return regex;
    }

    /**
     * 提取路径变量
     *
     * @param requestPath 请求路径
     * @param apiPath     API定义路径
     * @return 路径变量映射
     */
    public static Map<String, String> extractPathVariables(String requestPath, String apiPath) {
        Map<String, String> pathVariables = new ConcurrentHashMap<>();

        if (!apiPath.contains("{") || !apiPath.contains("}")) {
            return pathVariables;
        }

        // 使用我们新的转换方法来获取正确的正则表达式
        String regex = convertPathToRegex(apiPath);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(requestPath);

        if (matcher.matches()) {
            // 提取变量名和值
            Pattern varPattern = Pattern.compile("\\{([^}]+)}");
            Matcher varMatcher = varPattern.matcher(apiPath);
            int groupIndex = 1;

            while (varMatcher.find()) {
                String varName = varMatcher.group(1);
                String varValue = matcher.group(groupIndex);
                pathVariables.put(varName, varValue);
                groupIndex++;
            }
        }

        return pathVariables;
    }
}