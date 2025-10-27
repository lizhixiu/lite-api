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


package com.xclite.api.context;

import com.xclite.api.servlet.LiteHttpSession;

import java.util.HashMap;

/**
 * Session Context 用于脚本中获取Session信息
 *
 * @author zhixiulee
 */
public class SessionContext extends HashMap<String, Object> {

	private final LiteHttpSession session;

	public SessionContext(LiteHttpSession session) {
		this.session = session;
	}

	@Override
	public Object get(Object key) {
		return session != null ? session.getAttribute(key.toString()) : null;
	}

	@Override
	public Object put(String key, Object value) {
		Object oldValue = session.getAttribute(key);
		session.setAttribute(key, value);
		return oldValue;
	}
}
