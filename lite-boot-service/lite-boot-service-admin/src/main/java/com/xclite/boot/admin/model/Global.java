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

package com.xclite.boot.admin.model;

import cn.hutool.core.util.StrUtil;
import com.jfinal.kit.PropKit;
import com.xclite.boot.admin.utils.WebUtils;
import lombok.Getter;

import java.util.Objects;

public class Global {

    /**
     * 文件上的根目录
     */
    @Getter
    public static String dir;

    public final static String USER_FILES_BASE_URL = "/userfiles/";

    public void setDir(String dir) {
        Global.dir = PropKit.get("upload.dir", dir);
    }

    /**
     * 获取上传文件的根目录
     *
     * @return 返回上传文件的根目录
     */
    public static String getUserFilesBaseDir() {
        String dir = getDir();
        if (StrUtil.isBlank(dir)) {
            try {
                dir = Objects.requireNonNull(WebUtils.getHttpServletRequest()).getSession().getServletContext().getRealPath("/");
            } catch (Exception e) {
                return "";
            }
        }
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        return dir;
    }
}