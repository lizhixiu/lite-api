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
 * 分页配置
 *
 * @author mxd
 * 修改者: zhixiulee
 */
@Setter
public class Page {

    private static final String CONFIG_PREFIX = "lite-api.page.";

    /**
     * 默认page表达式
     */
    private String page = "page";

    /**
     * 默认size表达式
     */
    private String size = "size";

    /**
     * 默认首页
     */
    private int defaultPage = 1;

    /**
     * 默认页大小
     */
    private int defaultSize = 10;

    /**
     * 最大页大小， -1 为不限制
     */
    private int maxPageSize = -1;

    public String getPage() {
        return PropKit.get(CONFIG_PREFIX + "page", page);
    }

    public String getSize() {
        return PropKit.get(CONFIG_PREFIX + "size", size);
    }

    public int getDefaultPage() {
        return PropKit.getInt(CONFIG_PREFIX + "default-page", defaultPage);
    }

    public int getDefaultSize() {
        return PropKit.getInt(CONFIG_PREFIX + "default-size", defaultSize);
    }

    public int getMaxPageSize() {
        return PropKit.getInt(CONFIG_PREFIX + "max-page-size", maxPageSize);
    }

}
