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

/**
 * CRUD 配置
 *
 * @author mxd
 * 修改者: zhixiulee
 */
public class Crud {

    private static final String CONFIG_PREFIX = "lite-api.crud.";

    /**
     * 是否开启逻辑删除
     */
    public boolean isLogicDelete() {
        return PropKit.getBoolean(CONFIG_PREFIX + "logic-delete", false);
    }

    /**
     * 逻辑删除列
     */
    public String getLogicDeleteColumn() {
        return PropKit.get(CONFIG_PREFIX + "logic-delete-column", "is_del");
    }

    /**
     * 逻辑删除值
     */
    public String getLogicDeleteValue() {
        return PropKit.get(CONFIG_PREFIX + "logic-delete-value", "1");

    }

}
