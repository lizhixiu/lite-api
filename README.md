# XCLite API

## 简介

XCLite API 是一个基于 [JFinal](https://gitee.com/jfinal/jfinal) 框架的轻量级 API 敏捷开发框架。通过约定优于配置的方式，实现统一的标准，让您用尽可能简单的方式完成尽可能多的需求。告别 CRUD，拒绝重复劳动，远离搬砖。

XCLite API 借鉴了 [Magic-API](https://gitee.com/ssssssss-team/magic-api) 的优秀设计理念，结合 JFinal 框架的高性能特性，为开发者提供了一套简洁高效的 API 开发解决方案。

## 特性

- **零编码开发**：无需定义 `Controller`、`Service`、`Dao`、`Model` 等 Java 对象即可完成常见的 HTTP API 接口开发
- **基于 JFinal**：基于 JFinal 5.x 框架，继承其高性能、简洁开发的特点
- **可视化界面**：提供 UI 界面测试 API 接口
- **多数据库支持**：支持 MySQL、MariaDB、Oracle、DB2、PostgreSQL、SQLServer 等支持 JDBC 规范的数据库
- **动态脚本**：基于 Magic-Script 的动态编译技术，无需重启，即时生效
- **多数据源**：支持多数据源配置，支持在线配置数据源
- **分页查询**：支持分页查询以及自定义分页查询
- **SQL 缓存**：支持 SQL 缓存，以及自定义 SQL 缓存

### 文档地址
访问 [Lite API 文档](http://lite-api.demoeg.com/)

### mock接口演示地址

访问 [Mock 接口演示](http://lite-api.demoeg.com:8088/debug/index.html#/) 进行测试

### demo接口演示地址

访问 [Demo 接口演示](http://lite-api.demoeg.com:8099/debug/index.html#/) 进行测试

### 在线测试接口

访问 [本地测试接口](http://localhost:8088/debug/index.html) 进行测试

## 示例项目

### xclite-service-mock 项目截图
| <img src="docs/image/mock/scui/Snipaste_2025-10-18_23-21-40.png" alt="lite-api mock 1" width="350"> | <img src="docs/image/mock/scui/Snipaste_2025-10-18_23-21-41.png" alt="lite-api mock 2" width="350"> | <img src="docs/image/mock/scui/Snipaste_2025-10-18_23-25-25.png" alt="lite-api mock 3" width="350"> | <img src="docs/image/mock/scui/Snipaste_2025-10-18_23-25-34.png" alt="lite-api mock 4" width="350"> |
|------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------|
| <img src="docs/image/mock/scui/Snipaste_2025-10-18_23-25-53.png" alt="lite-api mock 5" width="350"> | <img src="docs/image/mock/scui/Snipaste_2025-10-18_23-26-09.png" alt="lite-api mock 6" width="350"> | <img src="docs/image/mock/scui/Snipaste_2025-10-18_23-27-05.png" alt="lite-api mock 7" width="350"> | <img src="docs/image/mock/scui/Snipaste_2025-10-18_23-27-20.jpg" alt="lite-api mock 8" width="350"> |


### xclite-service-demo 项目截图
| <img src="docs/image/demo/db/SQL_分页.png" alt="SQL_分页" width="350" height="200"> | <img src="docs/image/demo/db/SQL_列表.png" alt="SQL_列表" width="350" height="200"> | <img src="docs/image/demo/db/SQL_查询单值.png" alt="SQL_查询单值" width="350" height="200"> | <img src="docs/image/demo/db/SQL_查询数值.png" alt="SQL_查询数值" width="350" height="200"> |
|----------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
| <img src="docs/image/demo/db/Table_分页.png" alt="Table_分页" width="350" height="200"> | <img src="docs/image/demo/db/Table_列表.png" alt="lite-api db 2" width="350" height="200"> | <img src="docs/image/demo/db/Table_删除.png" alt="Table_删除" width="350" height="200"> | <img src="docs/image/demo/db/Table_数目.png" alt="Table_数目" width="350" height="200"> |
| <img src="docs/image/demo/db/OTHER_事务.png" alt="OTHER_事务" width="350" height="200"> | <img src="docs/image/demo/db/OTHER_多数据源.png" alt="OTHER_多数据源" width="350" height="200"> | <img src="docs/image/demo/db/OTHER_缓存.png" alt="OTHER_缓存" width="350" height="200"> | <img src="docs/image/demo/db/OTHER_驼峰.png" alt="OTHER_驼峰" width="350" height="200"> |

### xclite-api 结合ide ai ( Qoder TRAE 插件)开发功能截图
| <img src="docs/image/ai/demo/qoder.jpg" alt="qoder" width="350" height="200">| <img src="docs/image/ai/demo/api代码自动生成.jpg" alt="api代码自动生成" width="350" height="200">  | <img src="docs/image/ai/demo/trae自动提示.png" alt="trae自动提示" width="350" height="200"> | <img src="docs/image/ai/demo/展示效果.jpg" alt="展示效果" width="350" height="200"> |
|---------------------------------------------------------------------------------------|-------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------|



## 交流

| 微信群                                                                   | QQ群                                                                                                        |
|-----------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|
| <img src="docs/image/joinus/wx-group-qrcode.jpg" alt="作者微信" width="350"> | <img src="docs/image/joinus/qq-group-qrcode.png" alt="QQ群" width="350">                                    |
| 备注：加群，邀您加入群聊                                                          | <a href="https://qm.qq.com/cgi-bin/qm/qr?k=7955497&jump_from=webapi" target="_blank">点击加入 QQ 群：7955497</a> |

## License

This project is licensed under the Apache License, Version 2.0 - see the [LICENSE](LICENSE) file for details.
