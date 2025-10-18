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


package com.xclite.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

@Setter
public class ApiInfo {

    private String id;
    private String name;
    private String method;
    private String path;
    private String description;
    private List<ApiParameter> parameters;
    private String script;
    @Getter
    private String fullPath;
    @Getter @Setter
    private ApiGroup apiGroup;

    private String disabledUnknownParameter;

    @XmlAttribute
    public String getId() {
        return id;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    @XmlAttribute
    public String getMethod() {
        return method;
    }

    @XmlAttribute
    public String getPath() {
        return path;
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    @XmlElementWrapper(name = "parameters")
    @XmlElement(name = "param")
    public List<ApiParameter> getParameters() {
        return parameters;
    }

    @XmlElement
    public String getScript() {
        return script;
    }

    @XmlElement
    public String getDisabledUnknownParameter() {
        return disabledUnknownParameter;
    }

}