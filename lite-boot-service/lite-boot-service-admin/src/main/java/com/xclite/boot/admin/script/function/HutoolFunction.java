package com.xclite.boot.admin.script.function;

import com.xclite.api.function.LiteFunction;
import org.ssssssss.script.annotation.Comment;
import org.ssssssss.script.annotation.Function;

import java.util.Date;

/**
 * hutool涉及的常用的工具
 *
 * @author xiuj
 * @since 2024/12/18 16:02
 */
public class HutoolFunction implements LiteFunction {

    @Function
    @Comment("获取当前时间")
    public static Date now() {
        return new Date();
    }

}
