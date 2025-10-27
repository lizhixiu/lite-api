package com.xclite.api.utils;

import cn.hutool.core.util.XmlUtil;
import com.xclite.api.model.ApiGroup;
import com.xclite.api.model.ApiInfo;
import com.xclite.api.model.ApiParameter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * LiteApi XML 工具类
 * This class provides an alternative to JAXB for cross-JDK compatibility.
 */
public class ApiXmlUtils {

    /**
     * 转换ApiGroup对象为XML字符串
     * Marshal an ApiGroup object to XML string.
     *
     * @param apiGroup the ApiGroup to marshal
     * @return XML string representation
     */
    public static String marshalApiGroup(ApiGroup apiGroup) {
        return apiGroupToXml(apiGroup);
    }

    /**
     * 转换XML字符串为ApiGroup对象
     * Unmarshal XML string to ApiGroup object.
     *
     * @param xml the XML string
     * @return the ApiGroup object
     */
    public static ApiGroup unmarshalApiGroup(String xml) {
        return xmlToApiGroup(xml);
    }

    /**
     * 转换ApiGroup对象为XML字符串
     * Convert an ApiGroup object to XML string.
     *
     * @param apiGroup the ApiGroup to convert
     * @return XML string representation
     */
    public static String apiGroupToXml(ApiGroup apiGroup) {
        if (apiGroup == null) {
            return "";
        }

        Document doc = XmlUtil.createXml();
        Element rootElement = doc.createElement("api-group");

        // 一次性设置所有属性，减少方法调用
        String id = apiGroup.getId();
        String name = apiGroup.getName();
        String path = apiGroup.getPath();

        if (id != null) {
            rootElement.setAttribute("id", id);
        }
        if (name != null) {
            rootElement.setAttribute("name", name);
        }
        if (path != null) {
            rootElement.setAttribute("path", path);
        }

        // 添加 API 元素
        List<ApiInfo> apis = apiGroup.getApis();
        if (apis != null && !apis.isEmpty()) {
            // 预分配大小，避免动态扩容
            final Document finalDoc = doc;
            apis.forEach(apiInfo -> {
                Element apiElement = apiInfoToElement(finalDoc, apiInfo);
                rootElement.appendChild(apiElement);
            });
        }

        doc.appendChild(rootElement);
        return XmlUtil.toStr(doc, false);
    }

    /**
     * 转换XML字符串为ApiGroup对象
     * Convert XML string to ApiGroup object.
     *
     * @param xml the XML string
     * @return the ApiGroup object
     */
    public static ApiGroup xmlToApiGroup(String xml) {
        if (xml == null || xml.trim().isEmpty()) {
            return new ApiGroup();
        }

        Document doc = XmlUtil.parseXml(xml);
        Element rootElement = doc.getDocumentElement();

        ApiGroup apiGroup = new ApiGroup();

        // 一次性获取所有属性，减少重复调用 getAttribute
        apiGroup.setId(rootElement.getAttribute("id"));
        apiGroup.setName(rootElement.getAttribute("name"));
        apiGroup.setPath(rootElement.getAttribute("path"));

        // 获取 API 元素
        NodeList apiNodes = rootElement.getElementsByTagName("api");
        int apiCount = apiNodes.getLength();
        if (apiCount > 0) {
            // 预分配精确大小，避免多次扩容
            List<ApiInfo> apis = new ArrayList<>(apiCount);

            for (int i = 0; i < apiCount; i++) {
                Node apiNode = apiNodes.item(i);
                if (apiNode.getNodeType() == Node.ELEMENT_NODE) {
                    ApiInfo apiInfo = elementToApiInfo((Element) apiNode);
                    apiInfo.setApiGroup(apiGroup);
                    apis.add(apiInfo);
                }
            }
            apiGroup.setApis(apis);
        }

        return apiGroup;
    }

    /**
     * 转换ApiInfo对象为XML元素
     * Convert an ApiInfo object to XML Element.
     *
     * @param doc     the XML document
     * @param apiInfo the ApiInfo to convert
     * @return XML Element representation
     */
    private static Element apiInfoToElement(Document doc, ApiInfo apiInfo) {
        if (apiInfo == null) {
            return doc.createElement("api");
        }

        Element apiElement = doc.createElement("api");

        // 缓存属性值，减少方法调用
        String id = apiInfo.getId();
        String name = apiInfo.getName();
        String method = apiInfo.getMethod();
        String path = apiInfo.getPath();
        String description = apiInfo.getDescription();
        String script = apiInfo.getScript();
        String disabledUnknownParameter = apiInfo.getDisabledUnknownParameter();
        List<ApiParameter> parameters = apiInfo.getParameters();

        // 批量设置属性
        if (id != null) {
            apiElement.setAttribute("id", id);
        }
        if (name != null) {
            apiElement.setAttribute("name", name);
        }
        if (method != null) {
            apiElement.setAttribute("method", method);
        }
        if (path != null) {
            apiElement.setAttribute("path", path);
        }

        // 设置子元素
        if (description != null) {
            createAndAppendElement(doc, apiElement, "description", description);
        }
        if (script != null) {
            createAndAppendElement(doc, apiElement, "script", script);
        }
        if (disabledUnknownParameter != null) {
            createAndAppendElement(doc, apiElement, "disabledUnknownParameter",
                    disabledUnknownParameter);
        }

        // 添加参数
        if (parameters != null && !parameters.isEmpty()) {
            Element paramsElement = doc.createElement("parameters");

            // 使用 lambda 表达式优化循环
            final Document finalDoc = doc;
            parameters.forEach(param -> {
                Element paramElement = apiParameterToElement(finalDoc, param);
                paramsElement.appendChild(paramElement);
            });

            apiElement.appendChild(paramsElement);
        }

        return apiElement;
    }

    /**
     * 创建并添加元素的辅助方法，减少重复代码
     */
    private static void createAndAppendElement(Document doc, Element parent,
                                               String elementName, String content) {
        Element element = doc.createElement(elementName);
        element.setTextContent(content);
        parent.appendChild(element);
    }

    /**
     * 转换XML元素为ApiInfo对象
     * Convert an XML Element to ApiInfo object.
     *
     * @param element the XML Element
     * @return the ApiInfo object
     */
    private static ApiInfo elementToApiInfo(Element element) {
        ApiInfo apiInfo = new ApiInfo();

        // 一次性获取并设置所有属性
        apiInfo.setId(element.getAttribute("id"));
        apiInfo.setName(element.getAttribute("name"));
        apiInfo.setMethod(element.getAttribute("method"));
        apiInfo.setPath(element.getAttribute("path"));

        // 优化子节点遍历
        NodeList childNodes = element.getChildNodes();
        int childCount = childNodes.getLength();

        for (int i = 0; i < childCount; i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) childNode;
                String nodeName = childElement.getNodeName();

                // 使用 switch 语句优化条件判断
                switch (nodeName) {
                    case "description":
                        apiInfo.setDescription(childElement.getTextContent());
                        break;
                    case "script":
                        apiInfo.setScript(childElement.getTextContent());
                        break;
                    case "disabledUnknownParameter":
                        apiInfo.setDisabledUnknownParameter(childElement.getTextContent());
                        break;
                    case "parameters":
                        // 处理参数列表
                        NodeList paramNodes = childElement.getChildNodes();
                        int paramCount = paramNodes.getLength();
                        if (paramCount > 0) {
                            // 预分配合理大小
                            List<ApiParameter> parameters = new ArrayList<>(paramCount);

                            for (int j = 0; j < paramCount; j++) {
                                Node paramNode = paramNodes.item(j);
                                if (paramNode.getNodeType() == Node.ELEMENT_NODE &&
                                        "param".equals(paramNode.getNodeName())) {
                                    parameters.add(elementToApiParameter((Element) paramNode));
                                }
                            }

                            if (!parameters.isEmpty()) {
                                apiInfo.setParameters(parameters);
                            }
                        }
                        break;
                }
            }
        }

        return apiInfo;
    }

    /**
     * 转换ApiParameter对象为XML元素
     * Convert an ApiParameter object to XML Element.
     *
     * @param doc   the XML document
     * @param param the ApiParameter to convert
     * @return XML Element representation
     */
    private static Element apiParameterToElement(Document doc, ApiParameter param) {
        Element paramElement = doc.createElement("param");

        if (param != null) {
            // 批量获取并设置属性
            String name = param.getName();
            String type = param.getType();
            boolean required = param.isRequired();
            String desc = param.getDesc();

            if (name != null) {
                paramElement.setAttribute("name", name);
            }
            if (type != null) {
                paramElement.setAttribute("type", type);
            }
            paramElement.setAttribute("required", String.valueOf(required));
            if (desc != null) {
                paramElement.setAttribute("desc", desc);
            }
        }

        return paramElement;
    }

    /**
     * 转换XML元素为ApiParameter对象
     * Convert an XML Element to ApiParameter object.
     *
     * @param element the XML Element
     * @return the ApiParameter object
     */
    private static ApiParameter elementToApiParameter(Element element) {
        ApiParameter param = new ApiParameter();

        // 一次性获取所有属性并设置
        param.setName(element.getAttribute("name"));
        param.setType(element.getAttribute("type"));
        param.setRequired("true".equals(element.getAttribute("required")));
        param.setDesc(element.getAttribute("desc"));

        return param;
    }
}