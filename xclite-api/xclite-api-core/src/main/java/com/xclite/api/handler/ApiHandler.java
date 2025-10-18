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


package com.xclite.api.handler;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.jfinal.handler.Handler;
import com.jfinal.render.Render;
import com.jfinal.render.RenderManager;
import com.xclite.api.config.LiteApiProperties;
import com.xclite.api.context.CookieContext;
import com.xclite.api.context.RequestContext;
import com.xclite.api.context.RequestEntity;
import com.xclite.api.context.SessionContext;
import com.xclite.api.exception.ValidateException;
import com.xclite.api.interceptor.RequestInterceptor;
import com.xclite.api.interceptor.ResultProvider;
import com.xclite.api.model.ApiInfo;
import com.xclite.api.model.JsonBean;
import com.xclite.api.plugin.LiteApiPlugin;
import com.xclite.api.servlet.LiteHttpServletRequest;
import com.xclite.api.servlet.LiteHttpServletResponse;
import com.xclite.api.servlet.javaee.LiteJavaEEHttpServletRequest;
import com.xclite.api.servlet.javaee.LiteJavaEEHttpServletResponse;
import com.xclite.api.servlet.javaee.LiteJavaEERequestContextHolder;
import com.xclite.api.utils.ApiPathMatcher;
import com.xclite.api.utils.ScriptManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ssssssss.script.MagicResourceLoader;
import org.ssssssss.script.MagicScriptContext;
import org.ssssssss.script.exception.MagicExitException;
import org.ssssssss.script.exception.MagicScriptAssertException;
import org.ssssssss.script.runtime.ExitValue;
import org.ssssssss.script.runtime.function.MagicScriptLambdaFunction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.xclite.api.config.LiteConstants.*;

/**
 * 动态API处理器
 * 用于拦截和处理动态API请求
 */
@Slf4j
public class ApiHandler extends Handler {

    private static final LiteApiPlugin PLUGIN = LiteApiPlugin.getInstance();
    private static final LiteApiProperties liteApiProperties = PLUGIN.getLiteApiProperties();
    private static final List<RequestInterceptor> requestInterceptors = PLUGIN.getRequestInterceptors();
    private static final ResultProvider resultProvider = PLUGIN.getResultProvider();


    public ApiHandler() {
        super();
        MagicResourceLoader.addFunctionLoader(this::lookupLambdaFunction);
    }

    private Object lookupLambdaFunction(MagicScriptContext context, String name) {
        int index = name.indexOf(":");
        if (index > -1) {
            String method = name.substring(0, index);
            String path = name.substring(index + 1);
            ApiInfo info = ApiPathMatcher.match(path, method);
            if (info != null) {
                String scriptName = info.getScript();
                return (MagicScriptLambdaFunction) (variables, args) -> {
                    MagicScriptContext newContext = new MagicScriptContext();
                    Map<String, Object> varMap = new LinkedHashMap<>(context.getRootVariables());
                    varMap.putAll(variables.getVariables(context));
                    newContext.setScriptName(scriptName);
                    newContext.putMapIntoContext(varMap);
                    Object value = ScriptManager.executeScript(info.getScript(), newContext);
                    if (value instanceof ExitValue) {
                        throw new MagicExitException((ExitValue) value);
                    }
                    return value;
                };
            }
        }
        return null;
    }


    @SneakyThrows
    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
//        log.info("DynamicApiHandler handling request: " + target + " with method: " + request.getMethod());

        try {
            LiteJavaEERequestContextHolder.setRequestContext(request, response);

            // 尝试匹配动态API路径
            ApiInfo apiInfo = ApiPathMatcher.match(target, request.getMethod());

            if (apiInfo != null) {
//                log.info("API:{}/{}（{}:{}）", apiInfo.getGroupName(), apiInfo.getName(), apiInfo.getMethod(), target);
                // 标记请求已处理
                isHandled[0] = true;

                LiteHttpServletRequest liteRequest = new LiteJavaEEHttpServletRequest(request);
                LiteHttpServletResponse liteResponse = new LiteJavaEEHttpServletResponse(response);
                // 处理API请求
                Object result = handleDynamicApiRequest(liteRequest, liteResponse, apiInfo, target);
//            log.info("DynamicApiHandler handling response: " + JSONUtil.toJsonStr(o));
                if (result instanceof JsonBean && ((JsonBean<?>) result).getData() instanceof Render) {
                    return;
                }
                RenderManager.me().getRenderFactory().getJsonRender(result).setContext(request, response)
                        .render();
                return;
            }
//            else {
//                log.info("No API matched for path: " + target + " with method: " + request.getMethod());
//            }

            // 如果没有匹配到动态API，继续执行下一个处理器
            next.handle(target, request, response, isHandled);
        } finally {
            // 3. 请求结束后强制清理 ThreadLocal（避免内存泄漏）
            LiteJavaEERequestContextHolder.clearRequestContext();
        }
    }

    /**
     * 处理动态API请求
     *
     * @param request  HTTP请求
     * @param response HTTP响应
     * @param apiInfo  API信息
     * @param target   请求目标路径
     */
    private Object handleDynamicApiRequest(LiteHttpServletRequest request, LiteHttpServletResponse response, ApiInfo apiInfo, String target) throws Throwable {

//        log.info("Handling dynamic API request for: " + target);

        Map<String, Object> headers = new LinkedHashMap<>(ServletUtil.getHeaderMap(request.getRequest()));
        Map<String, String> pathVariables = ApiPathMatcher.extractPathVariables(target, apiInfo.getFullPath());

        Map<String, String> parameters = ServletUtil.getParamMap(request.getRequest());

        RequestEntity requestEntity = RequestEntity.create()
                .info(apiInfo)
                .request(request)
                .response(response)
                // 兼容 spring boot 3.0
                .pathVariables(new HashMap<>(pathVariables))
                .parameters(parameters);

        requestEntity.setHeaders(headers);
        Object bodyValue = readRequestBody(requestEntity.getRequest());
        requestEntity.setRequestBody(bodyValue);
//        log.info("DynamicApiHandler handleDynamicApiRequest, requestEntity: {}", requestEntity.getRequestBody());

        requestEntity.setRequestBody(bodyValue);
        String scriptName = apiInfo.getName();
        MagicScriptContext context = createMagicScriptContext(scriptName);
        requestEntity.setMagicScriptContext(context);
        try {

            for (String key : requestEntity.getParameters().keySet()) {
                context.set(key, requestEntity.getParameters().get(key));
            }

            context.putMapIntoContext(requestEntity.getPathVariables());
            // 设置 cookie 变量
            context.set(VAR_NAME_COOKIE, new CookieContext(requestEntity.getRequest()));
            // 设置 header 变量
            context.set(VAR_NAME_HEADER, headers);
            // 设置 session 变量
            context.set(VAR_NAME_SESSION, new SessionContext(requestEntity.getRequest().getSession()));
            // 设置 path 变量
            context.set(VAR_NAME_PATH_VARIABLE, requestEntity.getPathVariables());
            // 设置 body 变量
            if (bodyValue != null) {
                context.set(VAR_NAME_REQUEST_BODY, bodyValue);
            }
        } catch (ValidateException e) {
            return afterCompletion(requestEntity, resultProvider.buildResult(requestEntity, RESPONSE_CODE_INVALID, e.getMessage()));
        } catch (Throwable root) {
            return processException(requestEntity, root);
        }

        RequestContext.setRequestEntity(requestEntity);
        Object value;
        // 执行前置拦截器
        if ((value = doPreHandle(requestEntity)) != null) {
            return afterCompletion(requestEntity, value);
        }

        return invokeRequest(requestEntity);
    }


    private Object invokeRequest(RequestEntity requestEntity) throws Throwable {
        try {
            MagicScriptContext context = requestEntity.getMagicScriptContext();
            Object result = ScriptManager.executeScript(requestEntity.getApiInfo().getScript(), context);
            Object value = result;
            // 执行后置拦截器
            if ((value = doPostHandle(requestEntity, value)) != null) {
                return afterCompletion(requestEntity, value);
            }
            // 对返回结果包装处理
            return afterCompletion(requestEntity, response(requestEntity, result));
        } catch (Throwable root) {
            return processException(requestEntity, root);
        } finally {
            RequestContext.remove();
        }
    }

    /**
     * 执行后置拦截器
     */
    private Object doPostHandle(RequestEntity requestEntity, Object value) throws Exception {
        for (RequestInterceptor requestInterceptor : requestInterceptors) {
            Object target = requestInterceptor.postHandle(requestEntity, value);
            if (target != null) {
                return afterCompletion(requestEntity, target);
            }
        }
        return null;
    }

    /**
     * 包装返回结果
     */
    private Object response(RequestEntity requestEntity, Object value) {
        return resultProvider.buildResult(requestEntity, value);
    }


    /**
     * 执行前置拦截器
     */
    private Object doPreHandle(RequestEntity requestEntity) throws Exception {
        for (RequestInterceptor requestInterceptor : requestInterceptors) {
            Object value = requestInterceptor.preHandle(requestEntity);
            if (value != null) {
                return value;
            }
        }
        return null;
    }


    private Object afterCompletion(RequestEntity requestEntity, Object returnValue) {
        return afterCompletion(requestEntity, returnValue, null);
    }

    private Object afterCompletion(RequestEntity requestEntity, Object returnValue, Throwable throwable) {
        for (RequestInterceptor requestInterceptor : PLUGIN.getRequestInterceptors()) {
            try {
                requestInterceptor.afterCompletion(requestEntity, returnValue, throwable);
            } catch (Exception e) {
                log.warn("执行afterCompletion出现出错", e);
            }
        }
        return returnValue;
    }

    private Object processException(RequestEntity requestEntity, Throwable root) throws Throwable {
        Throwable parent = root;
        do {
            if (parent instanceof MagicScriptAssertException) {
                MagicScriptAssertException sae = (MagicScriptAssertException) parent;
                return afterCompletion(requestEntity, resultProvider.buildResult(requestEntity, sae.getCode(), sae.getMessage()), root);
            }
        } while ((parent = parent.getCause()) != null);
        if (liteApiProperties.isThrowException()) {
            afterCompletion(requestEntity, null, root);
            throw root;
        }
        log.error("接口{}请求出错", requestEntity.getRequest().getRequestURI(), root);
        return afterCompletion(requestEntity, resultProvider.buildException(requestEntity, root), root);
    }

    /**
     * 构建 MagicScriptContext
     */
    private MagicScriptContext createMagicScriptContext(String scriptName) {
        // 构建脚本上下文
        MagicScriptContext context = new MagicScriptContext();
        context.setScriptName(scriptName);
        return context;
    }

    /**
     * 读取RequestBody - 不使用Spring Boot的版本
     */
    private Object readRequestBody(LiteHttpServletRequest request) throws IOException {
        String contentType = request.getContentType();
        if (contentType == null) {
            return null;
        }

        // 只处理JSON类型的请求体
        if (contentType.contains("application/json")) {
            return readJsonRequestBody(request);
        } else if (contentType.contains("application/x-www-form-urlencoded")) {
            return readFormRequestBody(request);
        } else if (contentType.contains("multipart/form-data")) {
            return readMultipartRequestBody(request);
        }

        // 其他类型返回原始字符串
        return readTextRequestBody(request);
    }

    /**
     * 读取JSON请求体
     */
    private Object readJsonRequestBody(LiteHttpServletRequest request) throws IOException {
        String body = readTextRequestBody(request);
        if (body.trim().isEmpty()) {
            return null;
        }

        try {
            // 使用Hutool的JSON工具解析
            return JSONUtil.parse(body);
        } catch (Exception e) {
            // 如果解析失败，返回原始字符串
            return body;
        }
    }

    /**
     * 读取表单请求体
     */
    private Map<String, Object> readFormRequestBody(LiteHttpServletRequest request) {
        Map<String, String[]> parameterMap = ((HttpServletRequest) request.getRequest()).getParameterMap();
        Map<String, Object> result = new LinkedHashMap<>();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            if (values.length == 1) {
                result.put(key, values[0]);
            } else {
                result.put(key, Arrays.asList(values));
            }
        }

        return result;
    }

    /**
     * 读取 multipart 请求体
     */
    private Map<String, Object> readMultipartRequestBody(LiteHttpServletRequest request) {
        // 简化处理，返回参数映射
        return readFormRequestBody(request);
    }

    /**
     * 读取纯文本请求体
     */
    private String readTextRequestBody(LiteHttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                request.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }
}