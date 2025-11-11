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

package com.xclite.boot.admin.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.Map;

@Slf4j
public class CodeService {

    public void generateApi( Map<String,Object> table, String genApiPath) {
//
//        String tableCode = (String) table.get("tableCode");
        String sourceCode = (String) table.get("sourceCode");

        String name = table.get("apiTemplatePath").toString();
        String path = StrUtil.endWith(genApiPath, "/") ? genApiPath : (genApiPath + "/") + name;
        //System.out.println( path );
        File file = new File(path);
        if (!FileUtil.exist(file)) FileUtil.mkParentDirs(file);

        FileUtil.writeUtf8String(sourceCode, file);
    }

    public void createVue(Map<String,Object> table, String genVuePath) {

//        String tableCode = (String) table.get("tableCode");
        String sourceCode = (String) table.get("sourceCode");

        //System.out.println( genVuePath );
        Map<String,Object> template = (Map<String,Object>) table.get("template");
        String name = table.get("vuePath").toString() + template.get("name");
        String path = StrUtil.endWith(genVuePath, "/") ? genVuePath : (genVuePath + "/") + name;
        //System.out.println( path );
        File file = new File(path);
        if (!FileUtil.exist(file)) FileUtil.mkParentDirs(file);

        FileUtil.writeUtf8String(sourceCode, file);

    }


    /**
     * 为字符串的每一行内容添加三个tab前缀
     * @param content 原始字符串内容
     * @return 每行添加了三个tab前缀的新字符串
     */
    public static String addThreeTabsToEachLine(String content) {
        // 定义三个tab的前缀
        String threeTabs = "\t\t\t";

        // 使用StrUtil.splitToArray将字符串按行分割
        String[] lines = StrUtil.splitToArray(content, '\n');

        // 创建一个StringBuilder用于构建结果
        StringBuilder result = new StringBuilder();

        // 遍历每一行，添加三个tab前缀
        for (int i = 0; i < lines.length; i++) {
            result.append(threeTabs).append(lines[i]);
            // 除了最后一行外，每行结束添加换行符
            if (i < lines.length - 1) {
                result.append('\n');
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        String source = "body.ids.each(id => {\n" +
                "    #if(tableTypeCode=='TREE')\n" +
                "    var sql = \n" +
                "    \"\"\"\n" +
                "        select count(1) from #(lowerCaseTableCode) a where a.#(delFlagField) = '0' and a.pid = #{id}\n" +
                "    \"\"\";\n" +
                "    var count = db.selectInt(sql)\n" +
                "    if(count > 0){\n" +
                "        exit 500,'存在下级数据,不允许删除'\n" +
                "    }\n" +
                "    #end\n" +
                "\n" +
                "    var result = db.table(\"#(lowerCaseTableCode)\").where().eq(\"id\",id).delete();\n" +
                "\n" +
                "    return result;\n" +
                ")}\n" +
                "\n" +
                "return 1;";

        // 调用新方法处理source并输出
        String formattedSource = addThreeTabsToEachLine(source);
        System.out.println(formattedSource);
    }
}
