/*
 * MIT License
 *
 * Copyright (c) 2022 吕金泽
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
