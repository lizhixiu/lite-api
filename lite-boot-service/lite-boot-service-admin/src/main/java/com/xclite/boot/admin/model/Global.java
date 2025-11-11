package com.xclite.boot.admin.model;

import cn.hutool.core.util.StrUtil;
import com.jfinal.kit.PropKit;
import com.xclite.boot.admin.utils.WebUtils;
import lombok.Getter;

import java.util.Objects;

public class Global {

    /**
     * 文件上的根目录
     */
    @Getter
    public static String dir;

    public final static String USER_FILES_BASE_URL = "/userfiles/";

    public void setDir(String dir) {
        Global.dir = PropKit.get("upload.dir", dir);
    }

    /**
     * 获取上传文件的根目录
     *
     * @return 返回上传文件的根目录
     */
    public static String getUserFilesBaseDir() {
        String dir = getDir();
        if (StrUtil.isBlank(dir)) {
            try {
                dir = Objects.requireNonNull(WebUtils.getHttpServletRequest()).getSession().getServletContext().getRealPath("/");
            } catch (Exception e) {
                return "";
            }
        }
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        return dir;
    }
}