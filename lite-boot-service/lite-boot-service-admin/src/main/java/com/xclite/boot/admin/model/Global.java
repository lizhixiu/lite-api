/*
 * MIT License
 *
 * Copyright (c) 2022 吕金泽
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