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


package com.xclite.api.loader;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.xclite.api.model.ApiGroup;
import com.xclite.api.model.ApiInfo;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * API加载器
 * 用于加载和处理API配置文件，并提供全局缓存访问
 */
@Slf4j
public class LiteApiLoader {

    private final String workingDir;
    private static final String API_BASE_PATH_PREFIX = "api";

    private int succeed = 0;
    private int error = 0;
    private int merge = 0;

    // 全局缓存，使用ConcurrentHashMap保证线程安全
    private static final ConcurrentHashMap<String, ApiGroup> apiGroupCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ApiInfo> apiCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ApiInfo> apiIdCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> moduleCache = new ConcurrentHashMap<>();

    // 线程池用于并发处理
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    // JAXB上下文，创建一次重复使用
    private final JAXBContext jaxbContext;

    public LiteApiLoader(String workingDir) {
        this.workingDir = workingDir;
        try {
            this.jaxbContext = JAXBContext.newInstance(ApiGroup.class);
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to create JAXB context", e);
        }
    }

    /**
     * 高性能并发读取并解析指定目录下的所有XML文件
     * 使用CompletableFuture实现读取和解析的并发处理
     *
     * @return 解析成功的ApiGroup列表
     */
    public List<ApiGroup> loadAllApiGroupsConcurrently() {
        File apiDir = new File(workingDir + File.separator + API_BASE_PATH_PREFIX);
        if (!apiDir.exists() || !apiDir.isDirectory()) {
            log.warn("API directory does not exist or is not a directory: {}", apiDir.getAbsolutePath());
            return new ArrayList<>();
        }

        // 获取所有XML文件
        List<File> xmlFiles = FileUtil.loopFiles(apiDir, file -> file.getName().endsWith(".xml"));

        if (xmlFiles.isEmpty()) {
            log.info("No XML files found in directory: {}", apiDir.getAbsolutePath());
            return new ArrayList<>();
        }

        log.info("Found {} XML files to process", xmlFiles.size());

        // 并发读取和解析所有XML文件
        List<CompletableFuture<ApiGroup>> futures = xmlFiles.stream().map(file -> CompletableFuture.supplyAsync(() -> {
            try {
                // 读取文件内容
                String xmlContent = FileUtil.readString(file, StandardCharsets.UTF_8);

                // 解析XML为ApiGroup对象
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                ApiGroup apiGroup = (ApiGroup) unmarshaller.unmarshal(new StringReader(xmlContent));
                // 记录ApiGroup与文件路径的映射关系
                apiGroup.setXmlPath(file.getAbsolutePath());

                log.debug("Successfully parsed XML file: {}", file.getName());
                return apiGroup;
            } catch (Exception e) {
                log.error("Failed to parse XML file: {}", file.getName(), e);
                return null;
            }
        }, executorService)).collect(Collectors.toList());

        // 等待所有任务完成并收集结果
        return futures.stream().map(CompletableFuture::join).filter(java.util.Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 高性能加载所有API配置
     * 边读取边解析，提高整体性能
     *
     * @return 是否加载成功
     */
    public boolean loadAll() {
        try {
            // 清空之前的缓存
            apiGroupCache.clear();
            apiCache.clear();
            apiIdCache.clear();
            moduleCache.clear();

            // 重置计数器
            succeed = 0;
            error = 0;
            merge = 0;

            // 并发加载所有ApiGroup
            List<ApiGroup> apiGroups = loadAllApiGroupsConcurrently();

            if (apiGroups.isEmpty()) {
                log.warn("No API groups loaded");
                return true;
            }

            // 缓存所有ApiGroup和ApiInfo
            for (ApiGroup apiGroup : apiGroups) {
                try {
                    // 构建完整的groupIdKey
                    String groupPathKey = LiteApiLoaderUtils.getApiGroupPathKey(apiGroup);
                    String groupIdKey = groupPathKey.replace("/", "_");
//                    log.info("groupPathKey: {}, groupIdKey: {}", groupPathKey, groupIdKey);
                    if (apiGroupCache.containsKey(groupIdKey)) {
                        merge++;
                        log.warn("Merge api group: {}", apiGroup.getId());
                    } else {
                        succeed++;
                    }
                    // 缓存ApiGroup
                    apiGroupCache.put(groupIdKey, apiGroup);
                    // 缓存模块
                    if (apiGroup.getPath() != null && apiGroup.getPath().contains("/")) {
                        String[] pathParts = apiGroup.getPath().split("/");
                        if (pathParts.length > 1) {
                            String moduleKey = pathParts[1];
                            moduleCache.put(moduleKey, moduleKey);
                        }
                    }

                    // 缓存ApiInfo (包括嵌套的API)
                    if (apiGroup.getApis() != null) {
                        for (ApiInfo apiInfo : apiGroup.getApis()) {
                            // 递归处理所有API（包括嵌套的API）

                            String apiKey = apiInfo.getId();
                            if (StrUtil.isNotBlank(apiInfo.getPath())) {
                                apiKey = apiInfo.getPath();
                            }

                            // 如果apiKey为空或空白，则跳过该API
                            if (StrUtil.isBlank(apiKey)) {
                                log.warn("Skipping API with blank path in group: {}", groupPathKey);
                                continue;
                            }

                            // 构建完整的API路径
                            String fullPath = LiteApiLoaderUtils.buildFullApiPath(groupPathKey, apiKey);

                            log.debug("Caching API: {} -> Group: {}, API: {}, Method: {}", fullPath, groupPathKey, apiKey, apiInfo.getMethod());

                            apiInfo.setFullPath(fullPath);
                            apiInfo.setApiGroup(apiGroup);
                            apiCache.put(fullPath, apiInfo);
                            String fullApiId = groupIdKey + "_" + apiInfo.getId();
//                            log.info("Caching API: {} -> Full API ID: {}", fullPath, fullApiId);
                            apiIdCache.put(fullApiId, apiInfo);
                        }

                    }


                } catch (Exception e) {
                    error++;
                    log.error("Failed to cache API group: {}", apiGroup.getId(), e);
                }
            }

            log.info("API loading completed. Success: {}, Error: {}, Merge: {}, Total API Groups: {}, Total APIs: {}", succeed, error, merge, apiGroupCache.size(), apiCache.size());

            return true;
        } catch (Exception e) {
            log.error("Failed to load APIs", e);
            return false;
        }
    }

    public int getApiGroupCount() {
        return apiGroupCache.size();
    }

    public int getApiCount() {
        return apiCache.size();
    }

    /**
     * 获取所有缓存的ApiGroup
     *
     * @return ApiGroup列表
     */
    public List<ApiGroup> getAllApiGroups() {
        return new ArrayList<>(apiGroupCache.values());
    }

    /**
     * 根据ID获取特定的ApiGroup
     *
     * @param id ApiGroup ID
     * @return ApiGroup对象，如果不存在则返回null
     */
    public ApiGroup getApiGroupById(String id) {
        return apiGroupCache.get(id);
    }

    /**
     * 根据key获取特定的ApiInfo
     *
     * @param key API key (groupPath/apiPath)
     * @return ApiInfo对象，如果不存在则返回null
     */
    public ApiInfo getApiInfoByKey(String key) {
        return apiCache.get(key);
    }

    public ApiInfo getApiInfoById(String id) {
        return apiIdCache.get(id);
    }

    /**
     * 关闭线程池资源
     */
    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    public void printApiCache() {
        apiCache.forEach((key, value) -> log.info("API Cache: {} -> {}", key, value));
    }

    /**
     * 获取API缓存的只读视图
     *
     * @return API缓存映射
     */
    public static Map<String, ApiInfo> getApiCache() {
        return new ConcurrentHashMap<>(apiCache);
    }

    /**
     * 根据路径获取API信息
     *
     * @param path API路径
     * @return ApiInfo对象，如果不存在则返回null
     */
    public static ApiInfo getApiInfoByPath(String path) {
        return apiCache.get(path);
    }

    /**
     * 将ApiGroup保存到XML文件（如果文件存在则更新，不存在则新增）
     *
     * @param apiGroup ApiGroup对象
     * @param filePath 文件路径
     * @return 是否保存成功
     */
    public boolean saveOrUpdateApiGroupToXml(ApiGroup apiGroup, String filePath) {
        try {
            // 更新ApiGroup与文件路径的映射关系
            apiGroup.setXmlPath(filePath);

            File file = new File(filePath);

            // 如果文件存在，则先加载现有内容并合并
            if (file.exists()) {
                log.info("Updating existing XML file: {}", filePath);
                return updateApiGroupInXml(apiGroup, filePath);
            } else {
                log.info("Creating new XML file: {}", filePath);
                return saveApiGroupToXml(apiGroup, filePath);
            }
        } catch (Exception e) {
            log.error("Failed to save or update ApiGroup to XML file: {}", filePath, e);
            return false;
        }
    }

    /**
     * 更新XML文件中的ApiGroup（只更新变化的部分，保持未变化的部分不变）
     *
     * @param newApiGroup 新的ApiGroup对象
     * @param filePath    文件路径
     * @return 是否更新成功
     */
    public boolean updateApiGroupInXml(ApiGroup newApiGroup, String filePath) {
        try {
            // 先加载现有的ApiGroup
            ApiGroup existingApiGroup = loadApiGroupFromXml(filePath);

            if (existingApiGroup != null) {
                // 智能合并：只更新变化的部分
                smartMergeApiGroups(existingApiGroup, newApiGroup);

                // 保存合并后的结果
                return saveApiGroupToXml(existingApiGroup, filePath);
            } else {
                // 如果无法加载现有文件，则直接保存新内容
                return saveApiGroupToXml(newApiGroup, filePath);
            }
        } catch (Exception e) {
            log.error("Failed to update ApiGroup in XML file: {}", filePath, e);
            return false;
        }
    }

    /**
     * 从XML文件加载ApiGroup
     *
     * @param filePath 文件路径
     * @return ApiGroup对象，如果加载失败则返回null
     */
    public ApiGroup loadApiGroupFromXml(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return null;
            }

            String xmlContent = FileUtil.readString(file, StandardCharsets.UTF_8);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            ApiGroup apiGroup = (ApiGroup) unmarshaller.unmarshal(new StringReader(xmlContent));

            log.debug("Successfully loaded ApiGroup from XML file: {}", filePath);
            return apiGroup;
        } catch (Exception e) {
            log.error("Failed to load ApiGroup from XML file: {}", filePath, e);
            return null;
        }
    }

    /**
     * 智能合并两个ApiGroup对象（只更新变化的部分，保持未变化的部分不变）
     *
     * @param existingGroup 现有的ApiGroup
     * @param newGroup      新的ApiGroup
     */
    private void smartMergeApiGroups(ApiGroup existingGroup, ApiGroup newGroup) {
        try {
            // 只有当新值不为空且与现有值不同时才更新基本信息
            if (StrUtil.isNotBlank(newGroup.getName()) && !newGroup.getName().equals(existingGroup.getName())) {
                existingGroup.setName(newGroup.getName());
                log.debug("Updated group name from '{}' to '{}'", existingGroup.getName(), newGroup.getName());
            }

            if (StrUtil.isNotBlank(newGroup.getPath()) && !newGroup.getPath().equals(existingGroup.getPath())) {
                existingGroup.setPath(newGroup.getPath());
                log.debug("Updated group path from '{}' to '{}'", existingGroup.getPath(), newGroup.getPath());
            }

            // 合并API列表，只更新变化的API
            if (newGroup.getApis() != null && !newGroup.getApis().isEmpty()) {
                // 如果现有组没有API列表，则直接使用新的
                if (existingGroup.getApis() == null) {
                    existingGroup.setApis(new ArrayList<>(newGroup.getApis()));
                    log.debug("Initialized API list with {} APIs", newGroup.getApis().size());
                } else {
                    // 智能合并API列表
                    smartMergeApiLists(existingGroup.getApis(), newGroup.getApis());
                }
            }

            log.debug("Successfully smart merged ApiGroups: {} with {}", existingGroup.getId(), newGroup.getId());
        } catch (Exception e) {
            log.error("Failed to smart merge ApiGroups", e);
        }
    }

    /**
     * 智能合并API列表（只更新变化的API，保持未变化的API不变）
     *
     * @param existingApis 现有的API列表
     * @param newApis      新的API列表
     */
    private void smartMergeApiLists(List<ApiInfo> existingApis, List<ApiInfo> newApis) {
        try {
            // 创建现有API的映射表以便快速查找
            Map<String, ApiInfo> existingApiMap = new HashMap<>();
            for (ApiInfo api : existingApis) {
                existingApiMap.put(api.getId(), api);
            }

            // 处理新的API列表
            for (ApiInfo newApi : newApis) {
                ApiInfo existingApi = existingApiMap.get(newApi.getId());

                if (existingApi != null) {
                    // 如果存在相同ID的API，则智能合并
                    smartMergeApiInfo(existingApi, newApi);
                } else {
                    // 如果不存在相同ID的API，则添加新的API
                    existingApis.add(newApi);
                    log.debug("Added new API: {}", newApi.getId());
                }
            }
        } catch (Exception e) {
            log.error("Failed to smart merge API lists", e);
        }
    }

    /**
     * 智能合并两个ApiInfo对象（只更新变化的字段，保持未变化的字段不变）
     *
     * @param existingApi 现有的ApiInfo
     * @param newApi      新的ApiInfo
     */
    private void smartMergeApiInfo(ApiInfo existingApi, ApiInfo newApi) {
        try {
            // 只有当新值不为空且与现有值不同时才更新字段
            if (StrUtil.isNotBlank(newApi.getName()) && !newApi.getName().equals(existingApi.getName())) {
                existingApi.setName(newApi.getName());
                log.debug("Updated API name from '{}' to '{}'", existingApi.getName(), newApi.getName());
            }

            if (StrUtil.isNotBlank(newApi.getMethod()) && !newApi.getMethod().equals(existingApi.getMethod())) {
                existingApi.setMethod(newApi.getMethod());
                log.debug("Updated API method from '{}' to '{}'", existingApi.getMethod(), newApi.getMethod());
            }

            if (StrUtil.isNotBlank(newApi.getPath()) && !newApi.getPath().equals(existingApi.getPath())) {
                existingApi.setPath(newApi.getPath());
                log.debug("Updated API path from '{}' to '{}'", existingApi.getPath(), newApi.getPath());
            }

            if (StrUtil.isNotBlank(newApi.getDescription()) && !newApi.getDescription().equals(existingApi.getDescription())) {
                existingApi.setDescription(newApi.getDescription());
                log.debug("Updated API description");
            }

            if (StrUtil.isNotBlank(newApi.getScript()) && !newApi.getScript().equals(existingApi.getScript())) {
                existingApi.setScript(newApi.getScript());
                log.debug("Updated API script");
            }

            // 注意：参数、请求体定义等其他字段也可以按需添加智能合并逻辑
            log.debug("Successfully smart merged API: {}", existingApi.getId());
        } catch (Exception e) {
            log.error("Failed to smart merge API info for API: {}", existingApi.getId(), e);
        }
    }

    /**
     * 将ApiGroup保存到XML文件
     *
     * @param apiGroup ApiGroup对象
     * @param filePath 文件路径
     * @return 是否保存成功
     */
    public boolean saveApiGroupToXml(ApiGroup apiGroup, String filePath) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            StringWriter writer = new StringWriter();
            marshaller.marshal(apiGroup, writer);

            FileUtil.writeString(writer.toString(), new File(filePath), StandardCharsets.UTF_8);
            log.info("Successfully saved ApiGroup to XML file: {}", filePath);
            return true;
        } catch (Exception e) {
            log.error("Failed to save ApiGroup to XML file: {}", filePath, e);
            return false;
        }
    }

    /**
     * 在指定的ApiGroup中添加新的ApiInfo
     *
     * @param groupId ApiGroup的ID
     * @param apiInfo 要添加的ApiInfo
     * @return 是否添加成功
     */
    public boolean addApiInfoToGroup(String groupId, ApiInfo apiInfo) {
        try {
            ApiGroup apiGroup = apiGroupCache.get(groupId);
            if (apiGroup == null) {
                log.warn("ApiGroup not found: {}", groupId);
                return false;
            }

            // 添加ApiInfo到ApiGroup
            List<ApiInfo> apis = apiGroup.getApis();
            if (apis == null) {
                apis = new ArrayList<>();
                apiGroup.setApis(apis);
            }
            apis.add(apiInfo);

            // 更新缓存

            String groupKey = LiteApiLoaderUtils.getApiGroupPathKey(apiGroup);

            // 缓存新的ApiInfo
            String apiKey = apiInfo.getId();
            if (StrUtil.isNotBlank(apiInfo.getPath())) {
                apiKey = apiInfo.getPath();
            }

            // 如果apiKey为空或空白，则跳过该API
            if (StrUtil.isBlank(apiKey)) {
                log.warn("Skipping API with blank path in group: {}", groupKey);
                return false;
            }

            // 构建完整的API路径
            String fullPath = LiteApiLoaderUtils.buildFullApiPath(groupKey, apiKey);

            log.debug("Caching new API: {} -> Group: {}, API: {}, Method: {}", fullPath, groupKey, apiKey, apiInfo.getMethod());

            apiCache.put(fullPath, apiInfo);

            log.info("Successfully added ApiInfo to group: {}", groupId);
            return true;
        } catch (Exception e) {
            log.error("Failed to add ApiInfo to group: {}", groupId, e);
            return false;
        }
    }

    /**
     * 保存ApiInfo到对应的XML文件
     * 自动查找ApiGroup对应的文件路径并更新
     *
     * @param apiGroup ApiGroup
     * @param apiInfo 要保存的ApiInfo
     * @return 是否保存成功
     */
    public boolean saveApiInfoToXml(ApiGroup apiGroup, ApiInfo apiInfo) {
        String groupId = apiGroup.getId();
        try {

            // 添加或更新ApiInfo到ApiGroup
            List<ApiInfo> apis = apiGroup.getApis();
            if (apis == null) {
                apis = new ArrayList<>();
                apiGroup.setApis(apis);
            }

            // 查找是否已存在相同ID的API
            boolean found = false;
            for (int i = 0; i < apis.size(); i++) {
                if (apis.get(i).getId().equals(apiInfo.getId())) {
                    apis.set(i, apiInfo);
                    found = true;
                    break;
                }
            }

            // 如果不存在，则添加新的API
            if (!found) {
                apis.add(apiInfo);
            }

            // 获取文件路径
            String filePath = apiGroup.getXmlPath();
            if (filePath == null) {
                // 如果没有记录文件路径，则根据groupId构造默认路径
                filePath = workingDir + File.separator + API_BASE_PATH_PREFIX + File.separator + groupId + ".xml";
                log.info("No file path recorded for group {}, using default path: {}", groupId, filePath);
            }

            // 保存到XML文件
            boolean saved = saveOrUpdateApiGroupToXml(apiGroup, filePath);

            if (saved) {
                log.info("Successfully saved ApiInfo {} to XML file for group {}", apiInfo.getId(), groupId);
            } else {
                log.error("Failed to save ApiInfo {} to XML file for group {}", apiInfo.getId(), groupId);
            }

            return saved;
        } catch (Exception e) {
            log.error("Failed to save ApiInfo to XML file for group: {}", groupId, e);
            return false;
        }
    }

    /**
     * 创建一个新的ApiInfo对象
     *
     * @param id     API ID
     * @param method HTTP方法
     * @param path   API路径
     * @param script API脚本
     * @return 新创建的ApiInfo对象
     */
    public ApiInfo createApiInfo(String id, String method, String path, String script) {
        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setId(id);
        apiInfo.setMethod(method);
        apiInfo.setPath(path);
        apiInfo.setScript(script);
        return apiInfo;
    }

    /**
     * 创建一个新的ApiGroup对象
     *
     * @param id   Group ID
     * @param name Group名称
     * @param path Group路径
     * @return 新创建的ApiGroup对象
     */
    public ApiGroup createApiGroup(String id, String name, String path) {
        ApiGroup apiGroup = new ApiGroup();
        apiGroup.setId(id);
        apiGroup.setName(name);
        apiGroup.setPath(path);
        apiGroup.setApis(new ArrayList<>());
        return apiGroup;
    }

    /**
     * 创建一个新的ApiGroup对象并指定文件路径
     *
     * @param id       Group ID
     * @param name     Group名称
     * @param path     Group路径
     * @param filePath XML文件路径
     * @return 新创建的ApiGroup对象
     */
    public ApiGroup createApiGroupWithFile(String id, String name, String path, String filePath) {
        ApiGroup apiGroup = createApiGroup(id, name, path);
        // 记录文件路径
        apiGroup.setXmlPath(filePath);
        return apiGroup;
    }

    public Map<String, String> getModuleCache() {
        return moduleCache;
    }

}