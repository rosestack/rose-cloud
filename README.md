# rose

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.rose-group/rose-microservice/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.rose-group/rose-microservice)
![Maven](https://img.shields.io/maven-central/v/io.github.rose-group/rose-microservice.svg)
![License](https://img.shields.io/github/license/rose-group/rose-microservice.svg)
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/rose-group/rose-microservice.svg)](http://isitmaintained.com/project/rose-group/rose-microservice "Average time to resolve an issue")
[![Percentage of issues still open](http://isitmaintained.com/badge/open/rose-group/rose-microservice.svg)](http://isitmaintained.com/project/rose-group/rose-microservice "Percentage of issues still open")

## Prerequisites

- Jdk 8+
- Maven 3.4.5

## TODO

- [ ] 使用 Redis + Lua 基于令牌桶实现限流
- [ ] 通过分布式事务 Seata 保证告警、整改和任务的状态一致性
- [ ] 使用 Spring Security OAuth2 实现用户认证和授权
- [ ] 使用限流 + 队列保证回调接口（供三方系统使用的）的可靠性，使用 Redis + MySQL 实现去重
- [ ] 使用 AOP + Redis 防止重复提交
- [ ] 使用 SkyWalking + Prometheus + Grafana 监控服务
- [ ] 使用 Sentinel + OpenFeign 实现熔断、降级、限流

## Tech stack

| Tech stack           | Version    | Latest Version                                                                                                                                                                                                               | Notes |
|----------------------|------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------|
| Spring Boot          | 2.7.18     | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2&metadataUrl=https://repo1.maven.org/maven2/org/springframework/boot/spring-boot-dependencies/maven-metadata.xml">                        |       |
| Spring Cloud         | 2021.0.9   | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2021&metadataUrl=https://repo1.maven.org/maven2/org/springframework/cloud/spring-cloud-dependencies/maven-metadata.xml">                   |       |
| Spring Cloud Alibaba | 2021.0.6.2 | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2021.0&metadataUrl=https://repo1.maven.org/maven2/com/alibaba/cloud/spring-cloud-alibaba-dependencies/maven-metadata.xml">                 |       |
| Spring Authorization | 0.4.5      | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=0&metadataUrl=https://repo1.maven.org/maven2/org/springframework/security/spring-security-oauth2-authorization-server/maven-metadata.xml"> |       |
| Spring Boot Admin    | 2.7.16     | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2&metadataUrl=https://repo1.maven.org/maven2/de/codecentric/spring-boot-admin-dependencies/maven-metadata.xml">                            |       |
| MyBatis Plus	        | 3.5.12     | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=3&metadataUrl=https://repo1.maven.org/maven2/com/baomidou/mybatis-plus-bom/maven-metadata.xml">                                            |       |
| SpringDoc OpenAPI    | 1.8.0      | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=1&metadataUrl=https://repo1.maven.org/maven2/org/springdoc/springdoc-openapi/maven-metadata.xml">                                          
| Nacos                | 2.5.1      | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2&metadataUrl=https://repo1.maven.org/maven2/com/alibaba/nacos/nacos-all/maven-metadata.xml">                                              

## Integrations

spring-boot-microservice-best-practices: https://github.com/abhisheksr01/spring-boot-microservice-best-practices

1. Testing

- Unit Test
- Cucumber End-to-End Test
- Mutation Testing: Pitest
- Local Testing

2. Development Accelerators

- Lombok
- WireMock
- TestContainer

3. Analysis and Quality Checks

- Checkstyle
- Jacoco
- Hadolint

4. API Documentation

- SpringDoc
- Yapi

5. DevSecOps

- Dependency Vulnerability Check - owasp
- Docker Image Vulnerability Check- Trivy
- Infrastructure as Code static analysis - Snyk
- Penetration Test - Pentest

6. CICD

- CircleCI
- Concourse
- Jenkins

7. Platforms

- Kubernetes

8. Versioning

- Cocogitto

9. Branch

- GitFlow
- GitHub Flow
- GitLab Flow

## 开发原则

1. 每开发一个功能，要包含详细的自动化测试
2. 每发现一个bug，修复时要包含详细的自动化测试，测试内容包含能复现 bug 的测试
3. [十二要素应用宣言](https://12factor.net/zh_cn/)
    1. 基准代码：一份基准代码，多份部署。每个应用只对应一份基准代码，但可以同时存在多份部署
    2. 依赖：显式声明依赖关系
    3. 配置：**代码和配置严格分离，将应用的配置存储于环境变量中**
    4. 后端服务：把后端服务当作附加资源
    5. 构建，发布，运行：**严格区分构建，发布，运行这三个步骤**
    6. 进程：以一个或多个无状态进程运行应用
    7. 端口绑定：通过端口绑定来提供服务
    8. 并发：通过进程模型进行扩展
    9. 易处理：快速启动和优雅终止可最大化健壮性
    10. 开发环境与线上环境等价：尽可能的保持开发，预发布，线上环境相同
    11. 日志：把日志当作事件流
    12. 管理进程：后台管理任务当作一次性进程运行
4. 评价一个项目是否优秀的其中一个因素：在不修改基础代码和基础数据的情况下，是否可以随时开源

## Preference

- https://github.com/microsphere-projects
- https://gitee.com/battcn/wemirr-platform
- https://gitee.com/zhijiantianya/ruoyi-vue-pro
- https://gitee.com/zhijiantianya/yudao-cloud
- https://github.com/cenbow/dante-engine
- https://gitee.com/log4j/pig
- https://gitee.com/open-enjoy/enjoy-iot
- https://github.com/mojohaus/versions/tree/master/.github
