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
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import com.xclite.api.annotation.LiteModule;
import lombok.extern.slf4j.Slf4j;
import org.ssssssss.script.annotation.Comment;

import java.util.Map;

/**
 * @author xiuj
 */
@LiteModule("enjoy")
@Slf4j
public class EnjoyModule {

    /**
     * 使用 Enjoy 模板引擎将模板内容渲染为字符串。
     *
     * @param templateSource 模板内容
     * @param data           模板数据，用于填充模板中的变量
     * @return 渲染后的字符串
     */
    @Comment("enjoy模板渲染")
    public String renderToString(@Comment(name = "templateSource", value = "模板内容") String templateSource,
                                 @Comment(name = "data", value = "模板内容") Map<String, Object> data) {
        try {
            // 获取默认的 Enjoy 模板引擎实例
            Engine engine = Engine.use();
            // 根据模板内容创建模板对象
            Template template = engine.getTemplateByString(templateSource);
            // 使用模板数据渲染模板，并将结果转换为字符串
            return template.renderToString(data);
        } catch (Exception e) {
            // 记录异常信息，方便调试和监控
            log.error(StrUtil.format("Failed to render template: {}", e.getMessage()), e);
            return "";
        }
    }
}
