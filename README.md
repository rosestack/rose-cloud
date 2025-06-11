[![Build](https://github.com/rosestack/rose/actions/workflows/build.yml/badge.svg)](https://github.com/rosestack/rose/actions/workflows/maven-build.yml)
[![Maven](https://img.shields.io/maven-central/v/io.github.rosestack/rose.svg)](https://repo1.maven.org/maven2/io/github/rosestack/rose/)
![License](https://img.shields.io/github/license/rosestack/rose.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=io.github.rosestack%3Arose&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=io.github.rosestack%3Arose)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=io.github.rosestack%3Arose&metric=coverage)](https://sonarcloud.io/dashboard?id=io.github.rosestack%3Arose)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=io.github.rosestack%3Arose&metric=ncloc)](https://sonarcloud.io/dashboard?id=io.github.rosestack%3Arose)
[![codecov.io](https://codecov.io/github/rosestack/rose/coverage.svg?branch=main)](https://codecov.io/github/rosestack/rose?branch=main)
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/rosestack/rose.svg)](http://isitmaintained.com/project/rosestack/rose "Average time to resolve an issue")
[![Percentage of issues still open](http://isitmaintained.com/badge/open/rosestack/rose.svg)](http://isitmaintained.com/project/rosestack/rose "Percentage of issues still open")

# rose

## Requirements

- Java 8+
- Maven 3.6.0+

## Features

|      Tech stack      |  Version   |                                                                                                    Latest Version                                                                                                     | Notes |
|----------------------|------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------|
| Spring Boot          | 2.7.18     | ![](https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2&metadataUrl=https://repo1.maven.org/maven2/org/springframework/boot/spring-boot-dependencies/maven-metadata.xml)                        |       |
| Spring Cloud         | 2021.0.9   | ![](https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2021&metadataUrl=https://repo1.maven.org/maven2/org/springframework/cloud/spring-cloud-dependencies/maven-metadata.xml)                   |       |
| Spring Cloud Alibaba | 2021.0.6.2 | ![](https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2021.0&metadataUrl=https://repo1.maven.org/maven2/com/alibaba/cloud/spring-cloud-alibaba-dependencies/maven-metadata.xml)                 |       |
| Spring Authorization | 0.4.5      | ![](https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=0&metadataUrl=https://repo1.maven.org/maven2/org/springframework/security/spring-security-oauth2-authorization-server/maven-metadata.xml) |       |
| Spring Boot Admin    | 2.7.16     | ![](https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2&metadataUrl=https://repo1.maven.org/maven2/de/codecentric/spring-boot-admin-dependencies/maven-metadata.xml)                            |       |
| MyBatis Plus         | 3.5.12     | ![](https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=3&metadataUrl=https://repo1.maven.org/maven2/com/baomidou/mybatis-plus-bom/maven-metadata.xml)                                            |       |
| SpringDoc OpenAPI    | 1.8.0      | ![](https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=1&metadataUrl=https://repo1.maven.org/maven2/org/springdoc/springdoc-openapi/maven-metadata.xml)                                          |
| Nacos                | 2.5.1      | ![](https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2&metadataUrl=https://repo1.maven.org/maven2/com/alibaba/nacos/nacos-all/maven-metadata.xml)                                              |

## Instructions

### Build

### Build

```bash
mvn clean package
```

### Test

```bash
mvn clean verify
```

### Site

Upload sites to gitHub pages:

```bash
mvn clean site site:stage scm-publish:publish-scm 
```

### Release

Update Release version:

```bash
mvn versions:set -DprocessAllModules=true -DgenerateBackupPoms=false -DnewVersion=0.0.1
```

Publish to Central:

```bash
mvn -DskipTests -Prelease deploy
```

### Sonar

```bash
mvn verify javadoc:javadoc
mvn sonar:sonar -Dsonar.token=$SONAR_TOKEN
```

### Integrations

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

## 

- [ ] 使用 Redis + Lua 基于令牌桶实现限流
- [ ] 通过分布式事务 Seata 保证告警、整改和任务的状态一致性
- [ ] 使用 Spring Security OAuth2 实现用户认证和授权
- [ ] 使用限流 + 队列保证回调接口（供三方系统使用的）的可靠性，使用 Redis + MySQL 实现去重
- [ ] 使用 AOP + Redis 防止重复提交
- [ ] 使用 SkyWalking + Prometheus + Grafana 监控服务
- [ ] 使用 Sentinel + OpenFeign 实现熔断、降级、限流

## Preference

- https://github.com/microsphere-projects
- https://gitee.com/battcn/wemirr-platform
- https://gitee.com/zhijiantianya/ruoyi-vue-pro
- https://gitee.com/zhijiantianya/yudao-cloud
- https://github.com/cenbow/dante-engine
- https://gitee.com/log4j/pig
- https://gitee.com/open-enjoy/enjoy-iot
- https://github.com/mojohaus/versions/tree/master/.github
- https://github.com/seedstack/shed

