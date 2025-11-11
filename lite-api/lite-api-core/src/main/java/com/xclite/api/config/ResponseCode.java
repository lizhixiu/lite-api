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
 * json结果code配置
 *
 * @author mxd
 * 修改者: zhixiulee
 */
@Setter
public class ResponseCode {

    /**
     * 响应码配置前缀常量
     */
    private static final String CONFIG_PREFIX = "lite-api.response-code.";

    /**
     * 执行成功的code值
     */
    private int success = 1;

    /**
     * 参数验证未通过的code值
     */
    private int invalid = 0;

    /**
     * 执行出现异常的code值
     */
    private int exception = -1;

    public int getSuccess() {
        return PropKit.getInt(CONFIG_PREFIX + "success", success);
    }

    public int getInvalid() {
        return PropKit.getInt(CONFIG_PREFIX + "invalid", invalid);
    }

    public int getException() {
        return PropKit.getInt(CONFIG_PREFIX + "exception", exception);
    }

}
