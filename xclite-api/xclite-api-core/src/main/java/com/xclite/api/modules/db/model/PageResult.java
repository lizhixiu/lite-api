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


package com.xclite.api.modules.db.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分页执行结果
 *
 * @author zhixiulee
 */
@Setter
@Getter
public class PageResult<T> {

	/**
	 * 总条数
	 */
	private long total;

	/**
	 * 数据项
	 */
	private List<T> list;

	public PageResult(long total, List<T> list) {
		this.total = total;
		this.list = list;
	}

	public PageResult() {
	}

}
