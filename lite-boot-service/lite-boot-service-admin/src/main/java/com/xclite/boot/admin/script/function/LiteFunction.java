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

package com.xclite.boot.admin.script.function;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.ssssssss.script.annotation.Comment;
import org.ssssssss.script.annotation.Function;

import java.util.List;
import java.util.Map;

/**
 * hutool涉及的常用的工具
 *
 * @author xiuj
 * @since 2024/12/18 16:02
 */
@Slf4j
public class LiteFunction implements com.xclite.api.function.LiteFunction {

    @Function
    @Comment("填充sql实体")
    public static Map<String, Object> fillSqlEntity(Map<String, Object> body, List<Map<String, Object>> params) {
        Map<String, Object> result = MapUtil.newHashMap();
        for (Map<String, Object> entry : params) {
            String key = entry.get("fieldCode").toString();
            if (entry.containsKey("fieldValue")) {
                Object value = entry.get("fieldValue");
                result.put(key, value);
            } else if (body.containsKey(key)) {
                result.put(key, body.get(key));
            }
        }
        log.info("<UNK>sqlEntity<UNK>{}", JSONUtil.toJsonStr(result));
        return result;
    }


}
