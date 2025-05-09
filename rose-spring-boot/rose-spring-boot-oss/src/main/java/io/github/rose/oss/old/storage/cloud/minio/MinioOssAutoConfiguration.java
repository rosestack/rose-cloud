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
package io.github.rose.oss.old.storage.cloud.minio;

import io.github.rose.oss.old.storage.MinioOssOperation;
import io.github.rose.oss.old.storage.properties.MinioOssProperties;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static io.github.rose.oss.old.storage.OssOperation.MINIO_OSS_OPERATION;
import static io.github.rose.oss.old.storage.OssOperation.OSS_CONFIG_PREFIX_MINIO;

/**
 * @author Levin
 */
@EnableConfigurationProperties(MinioOssProperties.class)
@ConditionalOnProperty(prefix = OSS_CONFIG_PREFIX_MINIO, name = "enabled", havingValue = "true")
public class MinioOssAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(MinioOssAutoConfiguration.class);

    @Bean
    public MinioClient minioClient(MinioOssProperties properties) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = MinioClient.builder()
            .endpoint(properties.getEndpoint())
            .credentials(properties.getAccessKey(), properties.getSecretKey())
            .region(properties.getRegion())
            .build();
        minioClient.setTimeout(
            properties.getConnectTimeout().toMillis(),
            properties.getWriteTimeout().toMillis(),
            properties.getReadTimeout().toMillis());
        try {
            log.debug("Checking if bucket {} exists", properties.getBucket());
            boolean b = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(properties.getBucket()).build());
            if (!b) {
                throw new RuntimeException(properties.getBucket() + "Bucket does not exists");
            }
        } catch (Exception e) {
            log.error("Error while checking bucket", e);
            throw e;
        }
        return minioClient;
    }

    @Bean(MINIO_OSS_OPERATION)
    public MinioOssOperation minioOssOperation(MinioClient minioClient, MinioOssProperties properties) {
        return new MinioOssOperation(minioClient, properties);
    }
}
