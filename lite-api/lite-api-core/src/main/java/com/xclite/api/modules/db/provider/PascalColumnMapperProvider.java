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


package com.xclite.api.modules.db.provider;

/**
 * 帕斯卡命名转换
 *
 * @author zhixiulee
 */
public class PascalColumnMapperProvider implements ColumnMapperProvider {

	@Override
	public String name() {
		return "pascal";
	}

	@Override
	public String mapping(String columnName) {
		if (columnName == null) {
			return null;
		}
		columnName = columnName.toLowerCase();
		boolean upperCase = false;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < columnName.length(); i++) {
			char ch = columnName.charAt(i);
			if (ch == '_') {
				upperCase = true;
			} else if (upperCase || i == 0) {
				sb.append(Character.toUpperCase(ch));
				upperCase = false;
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	@Override
	public String unmapping(String name) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);
			if (i > 0 && Character.isUpperCase(ch)) {
				sb.append("_");
			}
			sb.append(Character.toLowerCase(ch));
		}
		return sb.toString();
	}
}
