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

package com.xclite.boot.admin.interceptor;

import cn.hutool.core.util.IdUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.redis.Redis;
import com.xclite.api.context.RequestEntity;
import com.xclite.api.interceptor.RequestInterceptor;
import com.xclite.api.model.ApiInfo;
import com.xclite.api.servlet.LiteHttpServletRequest;
import com.xclite.api.servlet.LiteHttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ssssssss.script.MagicScriptContext;

import java.util.Date;
import java.util.Map;

@Slf4j
public class PermissionInterceptor implements RequestInterceptor {

    @Override
    public Object preHandle(RequestEntity requestEntity) {
        if (requestEntity.getRequest().getHeader("Token") != null && Redis.use().get(requestEntity.getRequest().getHeader("Token").replace("Bearer ", "")) != null) {
            Object user = Redis.use().get(requestEntity.getRequest().getHeader("Token").replace("Bearer ", ""));
            requestEntity.getMagicScriptContext().set("SESSION_USER", user);
        }
        // 待增加参数规则判断
        return preHandle(requestEntity.getApiInfo(), requestEntity.getMagicScriptContext(), requestEntity.getRequest(), requestEntity.getResponse());
    }

    /*
     * 当返回对象时，直接将此对象返回到页面，返回null时，继续执行后续操作
     */
    @Override
    public Object preHandle(ApiInfo info, MagicScriptContext context, LiteHttpServletRequest request, LiteHttpServletResponse response) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object postHandle(RequestEntity requestEntity, Object returnValue) {
        Object user = null;
        String token = requestEntity.getRequest().getHeader("Token");
        if (requestEntity.getRequest().getHeader("Token") != null && Redis.use().get(requestEntity.getRequest().getHeader("Token").replace("Bearer ", "")) != null) {
            user = Redis.use().get(requestEntity.getRequest().getHeader("Token").replace("Bearer ", ""));
        }
        if (user == null) {
            return null;
        }

        Map<String, Object> userMap = (Map<String, Object>) user;
        try {
            LiteHttpServletRequest request = requestEntity.getRequest();
            ApiInfo info = requestEntity.getApiInfo();
            String sql = "insert into lite_log_oper(id,api_name_txt, api_path_txt, api_method_code, cost_time_num, rec_create_id,rec_create_ref, rec_create_dt, user_agent_txt, user_ip_txt, rec_org_id,login_token_txt) values(?,?,?,?,?,?,?,?,?,?,?,?)";
            Db.update(sql,
                    IdUtil.fastSimpleUUID(),
                    info.getName(),
                    request.getRequestURI(),
                    request.getMethod(),
                    System.currentTimeMillis() - requestEntity.getRequestTime(),
                    userMap.get("id"),
                    userMap.get("nameTxt"),
                    new Date(requestEntity.getRequestTime()),
                    request.getHeader("User-Agent"),
                    request.getRemoteAddr(),
                    userMap.get("orgId"),
                    token);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

}
