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


package com.xclite.api.web.action;

import cn.hutool.core.bean.BeanUtil;
import com.jfinal.core.Controller;
import com.xclite.api.loader.LiteApiLoaderUtils;
import com.xclite.api.model.ApiGroup;
import com.xclite.api.model.ApiInfo;
import com.xclite.api.model.JsonBean;
import com.xclite.api.plugin.LiteApiPlugin;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API操作类
 *
 * @author zhixiulee
 */
@Slf4j
public class LiteApiAction extends Controller {

    public void health(){
        JsonBean<Map<String, Object>> jsonBean = new JsonBean<>(200, "success");
        Map<String, Object> data = new HashMap<>();
        data.put("status", LiteApiPlugin.getInstance().isStarted());
        data.put("apiCount", LiteApiPlugin.getInstance().getApiLoader().getApiCount());
        jsonBean.setData(data);
        renderJson(jsonBean);
    }

    /**
     * 刷新API
     */
    public void refresh() {
        LiteApiPlugin plugin = LiteApiPlugin.getInstance();

        if (plugin.reloadApis()) {
            JsonBean<Void> jsonBean = new JsonBean<>(200, "APIs reloaded successfully");
            renderJson(jsonBean);
        } else {
            JsonBean<Void> jsonBean = new JsonBean<>(500, "Failed to reload APIs");
            renderJson(jsonBean);
        }
    }

    /**
     * 获取API组的树形结构
     */
    public void tree() {
        LiteApiPlugin plugin = LiteApiPlugin.getInstance();

        // 获取所有API组
        List<ApiGroup> apiGroups = plugin.getApiLoader().getAllApiGroups();

        // 转换为Element Plus树形组件格式
        List<Map<String, Object>> treeData = convertToElementPlusFormat(apiGroups);

        // 返回树形数据
        JsonBean<List<Map<String, Object>>> jsonBean = new JsonBean<>(200, "success", treeData);
        renderJson(jsonBean);
    }

    /**
     * 获取指定ID的API信息
     */
    public void getApi() {
        LiteApiPlugin plugin = LiteApiPlugin.getInstance();

        String id = getRequest().getParameter("id");

        // 获取所有API组
        ApiInfo sourceApiInfo = plugin.getApiLoader().getApiInfoById(id);

        if (sourceApiInfo == null) {
            // 返回API信息
            JsonBean<ApiInfo> jsonBean = new JsonBean<>(500, "API not found");
            renderJson(jsonBean);
            return;
        }

        ApiInfo apiInfo = new ApiInfo();
        BeanUtil.copyProperties(sourceApiInfo, apiInfo);
        apiInfo.setPath(LiteApiLoaderUtils.buildFullApiPath(normalizeGroupPath(apiInfo.getApiGroup().getPath()), apiInfo.getPath()));


        // 返回API信息
        JsonBean<ApiInfo> jsonBean = new JsonBean<>(200, "success", apiInfo);
        renderJson(jsonBean);
    }


    /**
     * 将API Group数据转换为Element Plus树形组件格式
     */
    private List<Map<String, Object>> convertToElementPlusFormat(List<ApiGroup> apiGroups) {
        List<Map<String, Object>> result = new ArrayList<>();

        LiteApiPlugin plugin = LiteApiPlugin.getInstance();
        Map<String, String> moduleCache = plugin.getApiLoader().getModuleCache();

        // 用于快速查找节点的映射表（key: 节点ID，value: 节点对象）
        Map<String, Map<String, Object>> nodeMap = new HashMap<>();

        // 1. 处理模块节点（来自moduleCache）
        for (Map.Entry<String, String> entry : moduleCache.entrySet()) {
            Map<String, Object> moduleNode = new HashMap<>();
            moduleNode.put("id", entry.getKey());           // 模块节点ID
            moduleNode.put("pid", "0");                      // 根节点pid为0
            moduleNode.put("label", entry.getValue());
            moduleNode.put("value", entry.getKey());
            moduleNode.put("type", "module");               // 节点类型
            moduleNode.put("children", new ArrayList<>());  // 初始化子节点列表
            result.add(moduleNode);                         // 将模块节点添加到结果集
            nodeMap.put(entry.getKey(), moduleNode);        // 缓存模块节点，便于后续查找
        }

        // 2. 处理API分组节点（来自apiGroups）并创建层级结构
        for (ApiGroup group : apiGroups) {
            // 构建API分组节点基本信息
            Map<String, Object> groupNode = new HashMap<>();
            groupNode.put("id", group.getId());           // 组节点ID
            groupNode.put("label", group.getName());
            groupNode.put("value", group.getId());

            // 标准化组路径，与LiteApiLoader保持一致
            String normalizedGroupPath = normalizeGroupPath(group.getPath());
            groupNode.put("path", normalizedGroupPath);
            groupNode.put("type", "group");               // 节点类型
            groupNode.put("children", new ArrayList<>()); // 初始化子节点列表

            String groupPathKey = LiteApiLoaderUtils.getApiGroupPathKey(group).replace("/", "_");

            // 添加API列表作为分组的子节点
            if (group.getApis() != null && !group.getApis().isEmpty()) {
                List<Map<String, Object>> apiNodes = new ArrayList<>();
                for (ApiInfo api : group.getApis()) {
                    Map<String, Object> apiNode = new HashMap<>();
                    apiNode.put("id", groupPathKey + "_" + api.getId());  // API节点ID
                    apiNode.put("pid", group.getId());                     // 父ID为组ID
                    apiNode.put("label", api.getName());
                    apiNode.put("value", api.getId());
                    apiNode.put("method", api.getMethod());

                    // 构建完整的API路径
                    String fullApiPath = LiteApiLoaderUtils.buildFullApiPath(normalizedGroupPath, api.getPath());
                    apiNode.put("path", fullApiPath);
                    apiNode.put("description", api.getDescription());
                    apiNode.put("type", "api");                            // 节点类型

                    apiNodes.add(apiNode);
                }
                // 对API节点按标签排序
                apiNodes.sort((a, b) -> {
                    String labelA = (String) a.get("label");
                    String labelB = (String) b.get("label");
                    if (labelA == null) labelA = "";
                    if (labelB == null) labelB = "";
                    return labelA.compareTo(labelB);
                });
                groupNode.put("children", apiNodes);
            }

            // 将分组节点添加到层级结构中
            addToHierarchicalStructure(group, groupNode, result, nodeMap);
        }

        // 对模块节点按标签排序
        result.sort((a, b) -> {
            String labelA = (String) a.get("label");
            String labelB = (String) b.get("label");
            if (labelA == null) labelA = "";
            if (labelB == null) labelB = "";
            return labelA.compareTo(labelB);
        });

        // 对每个模块的子节点进行排序
        sortChildrenRecursively(result);

        return result;
    }

    /**
     * 递归地对所有子节点进行排序
     */
    @SuppressWarnings("unchecked")
    private void sortChildrenRecursively(List<Map<String, Object>> nodes) {
        for (Map<String, Object> node : nodes) {
            List<Map<String, Object>> children = (List<Map<String, Object>>) node.get("children");
            if (children != null && !children.isEmpty()) {
                // 按标签排序子节点
                children.sort((a, b) -> {
                    String labelA = (String) a.get("label");
                    String labelB = (String) b.get("label");
                    if (labelA == null) labelA = "";
                    if (labelB == null) labelB = "";
                    return labelA.compareTo(labelB);
                });

                // 递归排序子节点的子节点
                sortChildrenRecursively(children);
            }
        }
    }

    /**
     * 将分组节点添加到层级结构中
     */
    @SuppressWarnings("unchecked")
    private void addToHierarchicalStructure(ApiGroup group, Map<String, Object> groupNode,
                                            List<Map<String, Object>> result,
                                            Map<String, Map<String, Object>> nodeMap) {
        String normalizedPath = normalizeGroupPath(group.getPath());
        String[] pathSegments = normalizedPath.split("/");

        // 过滤空段并移除根段
        List<String> segments = new ArrayList<>();
        for (String segment : pathSegments) {
            if (!segment.isEmpty()) {
                segments.add(segment);
            }
        }

        // 至少需要一个段
        if (segments.isEmpty()) {
            return;
        }

        // 如果只有一个段，直接添加到对应的模块下
        if (segments.size() == 1) {
            String moduleId = segments.get(0);
            Map<String, Object> moduleNode = nodeMap.get(moduleId);
            if (moduleNode != null) {
                List<Map<String, Object>> children = (List<Map<String, Object>>) moduleNode.get("children");
                children.add(groupNode);
                groupNode.put("pid", moduleId);
                nodeMap.put(group.getId(), groupNode);
            }
            return;
        }

        // 对于多段路径，创建中间层级
        Map<String, Object> parentNode;
        String parentId = segments.get(0); // 第一个段作为模块ID

        // 获取模块节点
        Map<String, Object> moduleNode = nodeMap.get(parentId);
        if (moduleNode == null) {
            return; // 模块不存在
        }

        parentNode = moduleNode;

        // 创建中间节点
        String currentPath = "/" + parentId;
        for (int i = 1; i < segments.size() - 1; i++) {
            String segment = segments.get(i);
            currentPath = currentPath + "/" + segment;

            // 检查中间节点是否已存在
            Map<String, Object> intermediateNode = nodeMap.get(currentPath);
            if (intermediateNode == null) {
                // 创建中间节点
                intermediateNode = new HashMap<>();
                intermediateNode.put("id", currentPath);
                intermediateNode.put("pid", parentId);
                intermediateNode.put("label", segment);
                intermediateNode.put("value", currentPath);
                intermediateNode.put("type", "folder"); // 中间节点类型
                intermediateNode.put("children", new ArrayList<>());

                // 添加到父节点
                List<Map<String, Object>> parentChildren = (List<Map<String, Object>>) parentNode.get("children");
                parentChildren.add(intermediateNode);

                nodeMap.put(currentPath, intermediateNode);
                // 注意：不要将中间节点添加到result中，只添加模块节点
            }

            parentNode = intermediateNode;
            parentId = currentPath;
        }

        // 最后一个段是实际的组节点
        groupNode.put("pid", parentId);
        List<Map<String, Object>> parentChildren = (List<Map<String, Object>>) parentNode.get("children");
        parentChildren.add(groupNode);
        nodeMap.put(group.getId(), groupNode);
    }


    /**
     * 标准化组路径，与LiteApiLoader保持一致
     */
    private String normalizeGroupPath(String groupPath) {
        if (groupPath == null || groupPath.trim().isEmpty()) {
            return "/";
        }

        // 确保以/开头
        if (!groupPath.startsWith("/")) {
            groupPath = "/" + groupPath;
        }

        // 移除末尾的/（除非是根路径）
        if (groupPath.length() > 1 && groupPath.endsWith("/")) {
            groupPath = groupPath.substring(0, groupPath.length() - 1);
        }

        return groupPath;
    }


}