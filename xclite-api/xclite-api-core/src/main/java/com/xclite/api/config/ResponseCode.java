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


package com.xclite.api.config;

import com.jfinal.kit.PropKit;
import lombok.Setter;

/**
 * json结果code配置
 *
 * @author zhixiulee
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
