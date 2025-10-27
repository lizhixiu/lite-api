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


package com.xclite.api.interceptor;


import com.xclite.api.context.RequestEntity;
import com.xclite.api.model.JsonBean;
import com.xclite.api.utils.ScriptManager;
import org.ssssssss.script.MagicScriptContext;

/**
 * 默认结果封装实现
 *
 * @author zhixiulee
 */
public class DefaultResultProvider implements ResultProvider {

    private final String responseScript;

    public DefaultResultProvider(String responseScript) {
        this.responseScript = responseScript;
    }

    @Override
    public Object buildResult(RequestEntity requestEntity, int code, String message, Object data) {
        long timestamp = System.currentTimeMillis();
        if (this.responseScript != null) {
            MagicScriptContext context = new MagicScriptContext();
            context.setScriptName(requestEntity.getMagicScriptContext().getScriptName());
            context.set("code", code);
            context.set("message", message);
            context.set("data", data);
            context.set("apiInfo", requestEntity.getApiInfo());
            context.set("request", requestEntity.getRequest());
            context.set("response", requestEntity.getResponse());
            context.set("timestamp", timestamp);
            context.set("requestTime", requestEntity.getRequestTime());
            context.set("executeTime", timestamp - requestEntity.getRequestTime());
            return ScriptManager.executeExpression(responseScript, context);
        } else {
            return new JsonBean<>(code, message, data, (int) (timestamp - requestEntity.getRequestTime()));
        }
    }
}
