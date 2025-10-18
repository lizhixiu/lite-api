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


import com.jfinal.config.*;
import com.jfinal.json.FastJsonFactory;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.template.Engine;
import com.xclite.api.handler.ApiHandler;
import com.xclite.api.plugin.LiteApiPlugin;
import com.xclite.api.prop.YamlProp;
import com.xclite.api.web.routes.LiteRoutes;

public class MockApiConfig extends JFinalConfig {

    private static final Prop p = loadConfig();

    private static Prop loadConfig() {
        return PropKit.append(new YamlProp("application-dev.yml"));
    }

    @Override
    public void configConstant(Constants me) {
        me.setJsonFactory(new FastJsonFactory());
    }

    @Override
    public void configRoute(Routes me) {
        // 添加LiteApi路由
        me.add(new LiteRoutes());
    }

    @Override
    public void configEngine(Engine me) {

    }

    @Override
    public void configPlugin(Plugins me) {
        LiteApiPlugin apiPlugin = LiteApiPlugin.getInstance();
        me.add(apiPlugin);
    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {
        // 添加动态API处理器
        me.add(new ApiHandler());
    }

}