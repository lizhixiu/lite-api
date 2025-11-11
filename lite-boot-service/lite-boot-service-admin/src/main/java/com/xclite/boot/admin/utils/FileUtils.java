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

package com.xclite.boot.admin.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.jfinal.upload.UploadFile;
import com.xclite.boot.admin.model.Global;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FileUtils {

    /**
     * 创建文件属性
     *
     * @param originalFilename 原始文件名
     * @param resTable         资源表
     * @return 包含文件路径、内部名称、后缀等信息的Map
     */
    public static Map<String, String> createFileAttr(String originalFilename, String resTable) {
        String ret = Global.USER_FILES_BASE_URL + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN) + File.separator + resTable + File.separator;
        String suffix = FileUtil.getSuffix(originalFilename);
        String fileInnerName = IdUtil.simpleUUID() + "." + suffix;
        Map<String, String> map = new HashMap<>();
        map.put("ret", ret);
        map.put("fileInnerName", fileInnerName);
        map.put("filePath", ret.substring(1) + fileInnerName);
        map.put("fileNames", originalFilename);
        map.put("suffix", suffix);
        return map;
    }

    /**
     * 保存文件
     *
     * @param file     上传的文件
     * @param resTable 资源表
     * @return 包含文件信息的Map，如果保存失败则返回null
     */
    public static Map<String, String> saveFile(UploadFile file, String resTable) {
        Map<String, String> fileAttr = createFileAttr(file.getFileName(), resTable);
        String fileNames = fileAttr.get("fileNames");
        String ret = fileAttr.get("ret");
        String suffix = fileAttr.get("suffix");
        String fileInnerName = fileAttr.get("fileInnerName");
        String realPath = Global.getUserFilesBaseDir() + ret;
        FileUtil.mkdir(FileUtil.normalize(realPath));
        File tempFile = new File(realPath + fileInnerName);
        if (!tempFile.getParentFile().exists()) {
            FileUtil.mkdir(tempFile.getParentFile());
        }
        if (!tempFile.exists()) {
//            TODO
//                file.transferTo(tempFile);
        }
        Map<String, String> params = new HashMap<>();
        params.put("state", "SUCCESS");
        params.put("fileName", fileNames);
        params.put("fileInnerName", fileInnerName);
        params.put("size", file.getFile().length() + "");
        params.put("type", suffix);
        params.put("filePath", fileAttr.get("filePath"));
        return params;
    }

    /**
     * 获取文件
     *
     * @param fileData 文件数据
     * @return 文件对象，如果文件不存在则返回null
     */
    public static File getFile(Map<String, String> fileData) {
        if (fileData == null || fileData.isEmpty()) {
            return null;
        }
        String filePath = Global.getUserFilesBaseDir() + fileData.get("filePath");
        if (!FileUtil.exist(filePath) || !FileUtil.isFile(filePath)) return null;
        return FileUtil.file(filePath);
    }

    /**
     * 删除文件
     *
     * @param fileData 文件数据
     * @return 如果删除成功或文件不存在则返回true，否则返回false
     */
    public static boolean delFile(Map<String, String> fileData) {
        if (fileData == null || fileData.isEmpty()) {
            return false;
        }
        String filePath = Global.getUserFilesBaseDir() + fileData.get("filePath");
        if (!FileUtil.exist(filePath) && !FileUtil.isFile(filePath)) return true;
        return FileUtil.del(filePath);
    }
}