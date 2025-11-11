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

import com.xclite.api.function.LiteFunction;
import org.ssssssss.script.annotation.Comment;
import org.ssssssss.script.annotation.Function;

import java.util.Date;

/**
 * hutool涉及的常用的工具
 *
 * @author xiuj
 * @since 2024/12/18 16:02
 */
public class HutoolFunction implements LiteFunction {

    @Function
    @Comment("获取当前时间")
    public static Date now() {
        return new Date();
    }

}
