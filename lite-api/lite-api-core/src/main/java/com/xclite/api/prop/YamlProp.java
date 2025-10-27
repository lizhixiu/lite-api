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


package com.xclite.api.prop;

import com.jfinal.kit.Prop;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * YAML配置文件加载器
 * 支持将YAML文件转换为JFinal的Prop对象
 *
 * @author zhixiulee
 */
public class YamlProp extends Prop {

    /**
     * 构造函数
     *
     * @param yamlFile YAML文件路径（类路径下）
     * @throws IllegalArgumentException 如果文件不存在或解析失败
     */
    public YamlProp(String yamlFile) {
        super();
        loadYaml(yamlFile);
    }

    /**
     * 加载YAML文件
     *
     * @param yamlFile YAML文件路径
     * @throws IllegalArgumentException 如果文件不存在或解析失败
     */
    private void loadYaml(String yamlFile) {
        if (yamlFile == null || yamlFile.trim().isEmpty()) {
            throw new IllegalArgumentException("YAML file path cannot be null or empty");
        }

        Properties properties = new Properties();

        try (InputStream in = YamlProp.class.getClassLoader().getResourceAsStream(yamlFile)) {
            if (in == null) {
                throw new IllegalArgumentException("YAML file not found in classpath: " + yamlFile);
            }

            Yaml yaml = new Yaml();
            LinkedHashMap<String, Object> map = yaml.loadAs(in, LinkedHashMap.class);

            if (map != null && !map.isEmpty()) {
                convertToProperties(properties, map, "");
            }

            this.properties = properties;

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to load YAML file: " + yamlFile, e);
        }
    }

    /**
     * 递归将Map转换为Properties
     *
     * @param properties 目标Properties对象
     * @param map        源Map对象
     * @param prefix     属性前缀
     */
    private static void convertToProperties(Properties properties, Map<String, Object> map, String prefix) {
        if (properties == null || map == null) {
            return;
        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // 跳过null键
            if (key == null) {
                continue;
            }

            String fullKey = prefix + key;

            if (value instanceof Map) {
                // 递归处理嵌套的Map
                @SuppressWarnings("unchecked")
                Map<String, Object> nestedMap = (Map<String, Object>) value;
                convertToProperties(properties, nestedMap, fullKey + ".");
            } else if (value != null) {
                // 只处理非null值
                String stringValue = value.toString();
                if (!stringValue.trim().isEmpty()) {
                    properties.setProperty(fullKey, stringValue);
                }
            }
        }
    }

}