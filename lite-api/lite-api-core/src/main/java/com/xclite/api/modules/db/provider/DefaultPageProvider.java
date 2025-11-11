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


package com.xclite.api.modules.db.provider;

import cn.hutool.core.util.NumberUtil;
import com.xclite.api.modules.db.model.Page;
import org.ssssssss.script.runtime.RuntimeContext;

import java.util.Objects;

/**
 * 分页对象默认提取接口
 *
 * @author mxd
 * 修改者: zhixiulee
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
        if (maxPageSize > 0) {
            pageSize = Math.min(pageSize, this.maxPageSize);
        }
        // 计算limit以及offset
        return new Page(page, pageSize);
    }
}
