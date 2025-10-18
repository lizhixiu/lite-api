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

/**
 * 常量类
 *
 * @author zhixiulee
 */
public class LiteConstants {


    /**
     * 脚本中session的变量名
     */
    public static final String VAR_NAME_SESSION = "session";

    /**
     * 脚本中cookie的变量名
     */
    public static final String VAR_NAME_COOKIE = "cookie";

    /**
     * 脚本中路径变量的变量名
     */
    public static final String VAR_NAME_PATH_VARIABLE = "path";

    /**
     * 脚本中header的变量名
     */
    public static final String VAR_NAME_HEADER = "header";

    /**
     * 脚本中RequestBody的变量名
     */
    public static final String VAR_NAME_REQUEST_BODY = "body";

    /**
     * 执行成功的code值
     */
    public static int RESPONSE_CODE_SUCCESS = 1;

    /**
     * 执行成功的message值
     */
    public static final String RESPONSE_MESSAGE_SUCCESS = "success";

    /**
     * 执行出现异常的code值
     */
    public static int RESPONSE_CODE_EXCEPTION = -1;

    /**
     * 参数验证未通过的code值
     */
    public static int RESPONSE_CODE_INVALID = 0;

}
