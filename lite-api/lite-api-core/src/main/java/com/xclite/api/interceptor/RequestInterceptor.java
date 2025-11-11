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


package com.xclite.api.interceptor;

import com.xclite.api.context.RequestEntity;
import com.xclite.api.model.ApiInfo;
import com.xclite.api.servlet.LiteHttpServletRequest;
import com.xclite.api.servlet.LiteHttpServletResponse;
import org.ssssssss.script.MagicScriptContext;


/**
 * 请求拦截器
 *
 * @author mxd
 * 修改者: zhixiulee
 */
public interface RequestInterceptor {

	/**
	 * 请求之前执行
	 *
	 * @param requestEntity 请求对象
	 * @return 当返回对象时，直接将此对象返回到页面，返回null时，继续执行后续操作
	 */
	default Object preHandle(RequestEntity requestEntity) throws Exception {
		return preHandle(requestEntity.getApiInfo(), requestEntity.getMagicScriptContext(), requestEntity.getRequest(), requestEntity.getResponse());
	}

	/**
	 * 请求之前执行
	 *
	 * @param info     接口信息
	 * @param context  脚本上下文
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @return 当返回对象时，直接将此对象返回到页面，返回null时，继续执行后续操作
	 * @throws Exception 处理失败时抛出的异常
	 */
	default Object preHandle(ApiInfo info, MagicScriptContext context, LiteHttpServletRequest request, LiteHttpServletResponse response) throws Exception {
		return null;
	}


	/**
	 * 执行完毕之后执行
	 *
	 * @param info        接口信息
	 * @param context     脚本上下文
	 * @param returnValue 即将要返回到页面的值
	 * @param request     HttpServletRequest
	 * @param response    HttpServletResponse
	 * @return 返回到页面的对象, 当返回null时执行后续拦截器，否则直接返回该值，不执行后续拦截器
     */
	default Object postHandle(ApiInfo info, MagicScriptContext context, Object returnValue, LiteHttpServletRequest request, LiteHttpServletResponse response) {
		return null;
	}

	/**
	 * 执行完毕之后执行
	 *
	 * @param requestEntity 请求对象
	 * @param returnValue   即将要返回到页面的值
	 * @return 返回到页面的对象, 当返回null时执行后续拦截器，否则直接返回该值，不执行后续拦截器
	 */
	default Object postHandle(RequestEntity requestEntity, Object returnValue) throws Exception {
		return postHandle(requestEntity.getApiInfo(), requestEntity.getMagicScriptContext(), returnValue, requestEntity.getRequest(), requestEntity.getResponse());
	}

	/**
	 * 接口执行完毕之后执行
	 *
	 * @param requestEntity 请求对象
	 * @param returnValue   即将要返回到页面的值
	 * @param throwable     异常对象
	 */
	default void afterCompletion(RequestEntity requestEntity, Object returnValue, Throwable throwable) {
		afterCompletion(requestEntity.getApiInfo(), requestEntity.getMagicScriptContext(), returnValue, requestEntity.getRequest(), requestEntity.getResponse(), throwable);
	}

	/**
	 * 接口执行完毕之后执行
	 *
	 * @param info        接口信息
	 * @param context     脚本上下文
	 * @param returnValue 即将要返回到页面的值
	 * @param request     HttpServletRequest
	 * @param response    HttpServletResponse
	 * @param throwable   异常对象
	 */
	default void afterCompletion(ApiInfo info, MagicScriptContext context, Object returnValue, LiteHttpServletRequest request, LiteHttpServletResponse response, Throwable throwable) {

	}

}
