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


package com.xclite.api;

import com.jfinal.server.undertow.UndertowServer;
import com.xclite.api.config.MockApiConfig;

public class MockApp {
    public static void main(String[] args) {
        //没有指定端口，使用undertow.txt中配置端口
        UndertowServer.start(MockApiConfig.class);
    }
}
