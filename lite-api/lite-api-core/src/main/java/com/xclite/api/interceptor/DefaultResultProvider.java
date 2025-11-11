/*
 * MIT License
 *
 * Copyright (c) 2020 小东
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


package com.xclite.api.interceptor;


import com.xclite.api.context.RequestEntity;
import com.xclite.api.model.JsonBean;
import com.xclite.api.utils.ScriptManager;
import org.ssssssss.script.MagicScriptContext;

/**
 * 默认结果封装实现
 *
 * @author mxd
 * 修改者: zhixiulee
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
