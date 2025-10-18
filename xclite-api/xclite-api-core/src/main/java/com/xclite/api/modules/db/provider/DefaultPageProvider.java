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

import cn.hutool.core.util.NumberUtil;
import com.xclite.api.modules.db.model.Page;
import org.ssssssss.script.runtime.RuntimeContext;

import java.util.Objects;

/**
 * 分页对象默认提取接口
 */
public class DefaultPageProvider implements PageProvider {

	/**
	 * page参数名
	 */
	private final String pageName;

	/**
	 * pageSize参数名
	 */
	private final String pageSize;

	/**
	 * 默认分页大小
	 */
	private final int defaultPageSize;

	/**
	 * 默认页数
	 */
	private final int defaultPage;

	/**
	 * 最大页数
	 */
	private final int maxPageSize;

	public DefaultPageProvider(String pageName, String pageSize, int defaultPage, int defaultPageSize, int maxPageSize) {
		this.pageName = pageName;
		this.pageSize = pageSize;
		this.defaultPageSize = defaultPageSize;
		this.defaultPage = defaultPage;
		this.maxPageSize = maxPageSize;
	}


	@Override
	public Page getPage(RuntimeContext context) {
		// 改为从脚本中获取
		int page = NumberUtil.parseInt(Objects.toString(context.eval(this.pageName), null), this.defaultPage);
		int pageSize = NumberUtil.parseInt(Objects.toString(context.eval(this.pageSize), null), this.defaultPageSize);
		if(maxPageSize > 0){
			pageSize = Math.min(pageSize, this.maxPageSize);
		}
		// 计算limit以及offset
		return new Page(page, pageSize);

	}
}
