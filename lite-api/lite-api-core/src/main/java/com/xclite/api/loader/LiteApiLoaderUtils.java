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


package com.xclite.api.loader;

import cn.hutool.core.util.StrUtil;
import com.xclite.api.model.ApiGroup;

public class LiteApiLoaderUtils {

    /**
     * 获取API组的路径键，用于在路由中标识API组
     */
    public static String getApiGroupPathKey(ApiGroup apiGroup) {
        String groupIdKey = apiGroup.getId();
        if (StrUtil.isNotBlank(apiGroup.getPath())) {
            groupIdKey = apiGroup.getPath();
        }

        // 确保groupKey以/开头
        if (!groupIdKey.startsWith("/")) {
            groupIdKey = "/" + groupIdKey;
        }

        // 移除末尾的斜杠（除非是根路径）
        if (groupIdKey.length() > 1 && groupIdKey.endsWith("/")) {
            groupIdKey = groupIdKey.substring(0, groupIdKey.length() - 1);
        }
        return groupIdKey;
    }


    /**
     * 构建完整的API路径，与LiteApiLoader保持一致
     */
    public static String buildFullApiPath(String groupPath, String apiPath) {
        if (apiPath == null || apiPath.trim().isEmpty()) {
            return groupPath;
        }

        String fullPath;
        if (apiPath.startsWith("/")) {
            // 如果apiPath以/开头，直接连接（避免双斜杠）
            fullPath = groupPath + apiPath;
        } else {
            fullPath = groupPath + "/" + apiPath;
        }

        // 标准化路径：替换多个连续的/为单个/
        fullPath = fullPath.replaceAll("/+", "/");

        // 确保路径不以/结尾（除非是根路径）
        if (fullPath.length() > 1 && fullPath.endsWith("/")) {
            fullPath = fullPath.substring(0, fullPath.length() - 1);
        }

        return fullPath;
    }

}
