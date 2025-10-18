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

import cn.hutool.core.util.StrUtil;
import com.jfinal.render.Render;
import com.jfinal.render.RenderManager;
import com.xclite.api.annotation.LiteModule;
import com.xclite.api.context.RequestContext;
import com.xclite.api.interceptor.ResultProvider;
import com.xclite.api.servlet.LiteHttpServletRequest;
import com.xclite.api.servlet.LiteHttpServletResponse;
import org.ssssssss.script.annotation.Comment;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * response模块
 *
 * @author zhixiulee
 */
@LiteModule("response")
public class ResponseModule {

    private ResultProvider resultProvider;

    // 添加无参构造函数
    public ResponseModule() {
        // 无参构造函数，用于模块注册
    }

    public ResponseModule(ResultProvider resultProvider) {
        this.resultProvider = resultProvider;
    }

    /**
     * 自行构建分页结果
     *
     * @param total  条数
     * @param values 数据内容
     */
    @Comment("返回自定义分页结果")
    public Object page(@Comment(name = "total", value = "总条数") long total,
                       @Comment(name = "values", value = "当前结果集") List<Map<String, Object>> values) {
        return resultProvider.buildPageResult(RequestContext.getRequestEntity(), null, total, values);
    }

    /**
     * 自定义json结果
     *
     * @param value json内容
     */
    @Comment("自定义返回json内容")
    public Render json(@Comment(name = "value", value = "返回对象") Object value) {
        Render render = RenderManager.me().getRenderFactory().getJsonRender(value);
        render.setContext(getRequest().getRequest(), getResponse().getResponse());
        render.render();
        return render;
    }

    /**
     * 添加Header
     */
    @Comment("添加response header")
    public ResponseModule addHeader(@Comment(name = "key", value = "header名") String key,
                                    @Comment(name = "value", value = "header值") String value) {
        if (StrUtil.isNotBlank(key)) {
            LiteHttpServletResponse response = getResponse();
            if (response != null) {
                response.addHeader(key, value);
            }
        }
        return this;
    }

    /**
     * 设置header
     */
    @Comment("设置response header")
    public ResponseModule setHeader(@Comment(name = "key", value = "header名") String key,
                                    @Comment(name = "value", value = "header值") String value) {
        if (StrUtil.isNotBlank(key)) {
            LiteHttpServletResponse response = getResponse();
            if (response != null) {
                response.setHeader(key, value);
            }
        }
        return this;
    }

    /**
     * 获取OutputStream
     *
     */
    @Comment("获取OutputStream")
    public OutputStream getOutputStream() throws IOException {
        LiteHttpServletResponse response = getResponse();
        return response.getOutputStream();
    }


    @Comment("终止输出，执行此方法后不会对结果进行任何输出及处理")
    public Render end() {
        Render render = RenderManager.me().getRenderFactory().getNullRender();
        render.setContext(getRequest().getRequest(), getResponse().getResponse());
        render.render();
        return render;
    }

    private LiteHttpServletRequest getRequest() {
        return RequestContext.getHttpServletRequest();
    }

    private LiteHttpServletResponse getResponse() {
        return RequestContext.getHttpServletResponse();
    }

//	/**
//	 * 展示图片
//	 *
//	 * @param value 图片内容
//	 * @param mime  图片类型，image/png,image/jpeg,image/gif
//	 */
//	@Comment("输出图片")
//	public ResponseEntity image(@Comment(name = "value", value = "图片内容，如`byte[]`") Object value,
//								@Comment(name = "mime", value = "图片类型，如`image/png`、`image/jpeg`、`image/gif`") String mime) {
//		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, mime).body(value);
//
//	}

    /**
     * 输出文本
     *
     * @param text 文本内容
     */
    @Comment("输出文本")
    public Render text(@Comment(name = "text", value = "文本内容") String text) {
        Render render = RenderManager.me().getRenderFactory().getTextRender(text);
        render.setContext(getRequest().getRequest(), getResponse().getResponse());
        render.render();
        return render;
    }

    /**
     * 重定向
     *
     * @param url 目标网址
     */
    @Comment("重定向")
    public Render redirect(@Comment(name = "url", value = "目标网址") String url) throws IOException {
        Render render = RenderManager.me().getRenderFactory().getRedirect301Render(url);
        render.setContext(getRequest().getRequest(), getResponse().getResponse());
        render.render();
        return render;
    }

}
