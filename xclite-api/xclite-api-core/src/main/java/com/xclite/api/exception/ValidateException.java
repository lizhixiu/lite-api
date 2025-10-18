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


package com.xclite.api.exception;


import com.xclite.api.model.JsonCode;
import lombok.Getter;

/**
 * 接口验证异常
 */
@Getter
public class ValidateException extends RuntimeException {

    private final JsonCode jsonCode;

    public ValidateException(JsonCode jsonCode, String message) {
        super(message);
        this.jsonCode = jsonCode;
    }

}
