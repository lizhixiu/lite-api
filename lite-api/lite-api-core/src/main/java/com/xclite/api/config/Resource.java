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


package com.xclite.api.config;

import com.jfinal.kit.PropKit;
import lombok.Setter;

/**
 * 接口存储配置
 *
 * @author mxd
 * 修改者: zhixiulee
 */
@Setter
public class Resource {

    private static final String CONFIG_PREFIX = "lite-api.resource.";

    /**
     * 存储类型，默认是文件
     */
    private String type = "file";

    /**
     * 文件存储位置
     */
    private String location = "/data/lite-api/";

    /**
     * 是否是只读模式
     */
    private boolean readonly = false;

    private boolean showUrl = false;

    /**
     * 使用数据库存储时使用的数据源
     */
    private String datasource;

    public String getType() {
        return PropKit.get(CONFIG_PREFIX + "type", type);
    }

    public String getLocation() {
        return PropKit.get(CONFIG_PREFIX + "location", location);
    }

    public boolean isReadonly() {
        return PropKit.getBoolean(CONFIG_PREFIX + "readonly", readonly);
    }

    public boolean isShowUrl() {
        return PropKit.getBoolean(CONFIG_PREFIX + "showUrl", showUrl);
    }

}
