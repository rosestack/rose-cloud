[![Build](https://github.com/rosestack/rose/actions/workflows/build.yml/badge.svg)](https://github.com/rosestack/rose/actions/workflows/build.yml)
[![Maven](https://img.shields.io/maven-central/v/io.github.rosestack/rose.svg)](https://repo1.maven.org/maven2/io/github/rosestack/rose/)
![License](https://img.shields.io/github/license/rosestack/rose.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=io.github.rosestack%3Arose&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=io.github.rosestack%3Arose)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=io.github.rosestack%3Arose&metric=coverage)](https://sonarcloud.io/dashboard?id=io.github.rosestack%3Arose)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=io.github.rosestack%3Arose&metric=ncloc)](https://sonarcloud.io/dashboard?id=io.github.rosestack%3Arose)
[![codecov.io](https://codecov.io/github/rosestack/rose/coverage.svg?branch=main)](https://codecov.io/github/rosestack/rose?branch=main)
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/rosestack/rose.svg)](http://isitmaintained.com/project/rosestack/rose "Average time to resolve an issue")
[![Percentage of issues still open](http://isitmaintained.com/badge/open/rosestack/rose.svg)](http://isitmaintained.com/project/rosestack/rose "Percentage of issues still open")

# rose

## 环境要求

- Java 8+
- Maven 3.6.0+

## 项目结构

rose 是一个多模块 Maven 项目，面向企业级、微服务架构应用。主要结构包括：

- **rose-core**：核心工具包，提供异常处理、函数式工具、JSON 处理、反射、Spring 扩展、校验等通用能力，是所有模块的基础。
- **rose-mybatis**：扩展 MyBatis，支持数据权限、加密、租户等企业常用功能。
- **rose-processor**：注解处理器模块，实现自动服务注册和编译期代码生成。
- **rose-spring-boot**：Spring Boot 扩展，包含 core、mybatis、redis、syslog、web 等子模块，提供自动配置和集成。
- **rose-spring-cloud**：Spring Cloud 扩展，主要用于 Feign 等微服务集成。
- **rose-service**：业务微服务实现，包括 gateway、iot、monitor、upms 等，每个服务有独立 API 和实现。
- **rose-bakup**：历史/备份代码（如 jpa、ratelimit），便于参考或迁移。
- **rose-test**：测试相关模块。
- **config、scripts、src/site/markdown**：配置、脚本、文档等辅助目录。

## 技术栈

**技术栈亮点：** Java、Spring Boot、Spring Cloud、MyBatis-Plus、Feign、AspectJ、Vavr、Jakarta Validation、Docker、Nacos 等。

| 技术栈                  | 版本         | 最新版本                                                                                                                                                                                                                  | 备注 |
|----------------------|------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----|
| Spring Boot          | 2.7.18     | ![](https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2&metadataUrl=https://repo1.maven.org/maven2/org/springframework/boot/spring-boot-dependencies/maven-metadata.xml)                        |    |
| Spring Cloud         | 2021.0.9   | ![](https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2021&metadataUrl=https://repo1.maven.org/maven2/org/springframework/cloud/spring-cloud-dependencies/maven-metadata.xml)                   |    |
| Spring Cloud Alibaba | 2021.0.6.2 | ![](https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2021.0&metadataUrl=https://repo1.maven.org/maven2/com/alibaba/cloud/spring-cloud-alibaba-dependencies/maven-metadata.xml)                 |    |
| Spring Authorization | 0.4.5      | ![](https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=0&metadataUrl=https://repo1.maven.org/maven2/org/springframework/security/spring-security-oauth2-authorization-server/maven-metadata.xml) |    |
| Spring Boot Admin    | 2.7.16     | ![](https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2&metadataUrl=https://repo1.maven.org/maven2/de/codecentric/spring-boot-admin-dependencies/maven-metadata.xml)                            |    |
| MyBatis Plus         | 3.5.12     | ![](https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=3&metadataUrl=https://repo1.maven.org/maven2/com/baomidou/mybatis-plus-bom/maven-metadata.xml)                                            |    |
| SpringDoc OpenAPI    | 1.8.0      | ![](https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=1&metadataUrl=https://repo1.maven.org/maven2/org/springdoc/springdoc-openapi/maven-metadata.xml)                                          |    |
| Nacos                | 2.5.1      | ![](https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2&metadataUrl=https://repo1.maven.org/maven2/com/alibaba/nacos/nacos-all/maven-metadata.xml)                                              |    |

## 开发指南

### 快速开始

1. 克隆项目

```bash
git clone https://github.com/rosestack/rose.git
cd rose
```

2. 构建所有模块

```bash
mvn clean package -DskipTests
```

3. 启动示例服务（以 rose-service/rose-gateway 为例）

```bash
cd rose-service/rose-gateway
mvn spring-boot:run
```

4. 访问服务
   默认端口为 8080，可通过 http://localhost:8080 访问。

### 站点

上传站点到 GitHub Pages：

```bash
mvn clean site site:stage scm-publish:publish-scm 
```

### 发布到中央仓库

更新发布版本：

```bash
mvn versions:set -DprocessAllModules=true -DgenerateBackupPoms=false -DnewVersion=0.0.1
```

发布到中央仓库：

```bash
mvn -DskipTests -Prelease deploy
```

### 代码质量

```bash
mvn verify javadoc:javadoc
mvn sonar:sonar -Dsonar.token=$SONAR_TOKEN
```

## 集成与最佳实践

- spring-boot-microservice-best-practices: https://github.com/abhisheksr01/spring-boot-microservice-best-practices

1. 测试
    - 单元测试
    - Cucumber 端到端测试
    - 变异测试：Pitest
    - 本地测试
2. 开发加速器
    - Lombok
    - WireMock
    - TestContainer
3. 分析与质量检查
    - Checkstyle
    - Jacoco
    - Hadolint
4. API 文档
    - SpringDoc
    - Yapi
5. DevSecOps
    - 依赖漏洞检查 - owasp
    - Docker 镜像漏洞检查 - Trivy
    - 基础设施代码静态分析 - Snyk
    - 渗透测试 - Pentest
6. 持续集成/持续交付
    - CircleCI
    - Concourse
    - Jenkins
7. 平台
    - Kubernetes
8. 版本管理
    - Cocogitto
9. 分支管理
    - GitFlow
    - GitHub Flow
    - GitLab Flow

## TODO

- [ ] 使用 Redis + Lua 基于令牌桶实现限流
- [ ] 通过分布式事务 Seata 保证告警、整改和任务的状态一致性
- [ ] 使用 Spring Security OAuth2 实现用户认证和授权
- [ ] 使用限流 + 队列保证回调接口（供三方系统使用的）的可靠性，使用 Redis + MySQL 实现去重
- [ ] 使用 AOP + Redis 防止重复提交
- [ ] 使用 SkyWalking + Prometheus + Grafana 监控服务
- [ ] 使用 Sentinel + OpenFeign 实现熔断、降级、限流

## 参考项目/链接

- https://github.com/microsphere-projects
- https://gitee.com/battcn/wemirr-platform
- https://gitee.com/zhijiantianya/ruoyi-vue-pro
- https://gitee.com/zhijiantianya/yudao-cloud
- https://github.com/cenbow/dante-engine
- https://gitee.com/log4j/pig
- https://gitee.com/open-enjoy/enjoy-iot
- https://github.com/mojohaus/versions/tree/master/.github
- https://github.com/seedstack/shed
