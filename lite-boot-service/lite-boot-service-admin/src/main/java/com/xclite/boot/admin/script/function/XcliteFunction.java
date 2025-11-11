package com.xclite.boot.admin.script.function;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.xclite.api.function.LiteFunction;
import lombok.extern.slf4j.Slf4j;
import org.ssssssss.script.annotation.Comment;
import org.ssssssss.script.annotation.Function;

import java.util.List;
import java.util.Map;

/**
 * hutool涉及的常用的工具
 *
 * @author xiuj
 * @since 2024/12/18 16:02
 */
@Slf4j
public class XcliteFunction implements LiteFunction {

    @Function
    @Comment("填充sql实体")
    public static Map<String, Object> fillSqlEntity(Map<String, Object> body, List<Map<String, Object>> params) {
//        return IdUtil.fastSimpleUUID();
//        log.info("<UNK>1<UNK>{}", body.getClass().getSimpleName());
//        log.info("<UNK>2<UNK>{}", params.getClass().getSimpleName());
        Map<String, Object> result = MapUtil.newHashMap();
//        log.info("<UNK>body<UNK>{}", JSONUtil.toJsonStr(body));
        for (Map<String, Object> entry : params) {
//            log.info("<UNK>entry<UNK>{}", JSONUtil.toJsonStr(entry));
            String key = entry.get("fieldCode").toString();
            if (entry.containsKey("fieldValue")) {
                Object value = entry.get("fieldValue");
                result.put(key, value);
//                log.info("<UNK>4<UNK>{}", entry.getValue());
            } else if (body.containsKey(key)) {
//                log.info("{}:{}1", key, body.get(key));
                result.put(key, body.get(key));
//                log.info("{}:{}2", key, body.get(key));
            }
        }
        log.info("<UNK>sqlEntity<UNK>{}", JSONUtil.toJsonStr(result));
        return result;
    }


}
