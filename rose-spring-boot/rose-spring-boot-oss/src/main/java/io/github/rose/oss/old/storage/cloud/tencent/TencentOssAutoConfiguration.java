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
package io.github.rose.oss.old.storage.cloud.tencent;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import io.github.rose.oss.old.storage.TencentOssOperation;
import io.github.rose.oss.old.storage.properties.TencentOssProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.rose.oss.old.storage.OssOperation.OSS_CONFIG_PREFIX_TENCENT;
import static io.github.rose.oss.old.storage.OssOperation.TENCENT_OSS_OPERATION;

/**
 * OSS自动配置
 *
 * @author Levin
 * @since 2018-09-18 12:24
 **/
@Configuration
@EnableConfigurationProperties({TencentOssProperties.class})
@ConditionalOnProperty(prefix = OSS_CONFIG_PREFIX_TENCENT, name = "enabled", havingValue = "true")
public class TencentOssAutoConfiguration {

    @Bean
    public COSClient cosClient(TencentOssProperties properties) {
        COSCredentials credentials = new BasicCOSCredentials(properties.getAccessKey(), properties.getSecretKey());
        // 初始化客户端配置
        ClientConfig clientConfig = new ClientConfig(new Region(properties.getRegion()));
        return new COSClient(credentials, clientConfig);
    }

    @Bean(TENCENT_OSS_OPERATION)
    public TencentOssOperation tencentStorageOperation(COSClient cosClient, TencentOssProperties properties) {
        return new TencentOssOperation(cosClient, properties);
    }
}
