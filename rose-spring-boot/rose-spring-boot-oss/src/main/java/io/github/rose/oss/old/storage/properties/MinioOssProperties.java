/*
 * Copyright © 2025 rose-group.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.rose.oss.old.storage.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Objects;

import static io.github.rose.oss.old.storage.OssOperation.OSS_CONFIG_PREFIX_MINIO;

/**
 * minio 配置信息
 *
 * @author Levin
 */
@ConfigurationProperties(prefix = OSS_CONFIG_PREFIX_MINIO)
public class MinioOssProperties extends BaseOssProperties {

    /**
     * minio实例的URL。包括端口。如果未提供端口，则采用HTTP端口。
     */
    private String endpoint = "https://play.min.io";

    private int port = 80;

    private String region;

    /**
     * minio实例上的访问
     */
    private String accessKey = "Q3AM3UQ867SPQQA43P2F";

    /**
     * minio实例上的密钥（密码）
     */
    private String secretKey = "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG";

    /**
     * If the scheme is not provided in {@code url} property, define if the connection is
     * done via HTTP or HTTPS.
     */
    private Boolean secure = false;

    /**
     * 在执行器上注册的度量配置前缀。
     */
    private String metricName = "minio.oss";

    /**
     * 连接超时时间。
     */
    private Duration connectTimeout = Duration.ofSeconds(10);

    /**
     * 写入超时时间
     */
    private Duration writeTimeout = Duration.ofSeconds(60);

    /**
     * 读取超时时间
     */
    private Duration readTimeout = Duration.ofSeconds(10);

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Boolean getSecure() {
        return secure;
    }

    public void setSecure(Boolean secure) {
        this.secure = secure;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Duration getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(Duration writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MinioOssProperties that = (MinioOssProperties) o;
        return port == that.port && Objects.equals(endpoint, that.endpoint) && Objects.equals(region, that.region) && Objects.equals(accessKey, that.accessKey) && Objects.equals(secretKey, that.secretKey) && Objects.equals(secure, that.secure) && Objects.equals(metricName, that.metricName) && Objects.equals(connectTimeout, that.connectTimeout) && Objects.equals(writeTimeout, that.writeTimeout) && Objects.equals(readTimeout, that.readTimeout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endpoint, port, region, accessKey, secretKey, secure, metricName, connectTimeout, writeTimeout, readTimeout);
    }
}
