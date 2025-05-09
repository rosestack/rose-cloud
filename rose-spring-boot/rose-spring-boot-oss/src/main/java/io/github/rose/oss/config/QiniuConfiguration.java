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
package io.github.rose.oss.config;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import io.github.rose.oss.QiniuTemplate;
import io.github.rose.oss.props.OssProperties;
import io.github.rose.oss.rule.OssRule;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Qiniu配置类
 *
 * @author Chill
 */
@AllArgsConstructor
@AutoConfigureAfter(OssConfiguration.class)
@ConditionalOnClass({Auth.class, UploadManager.class, BucketManager.class})
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(value = "oss.name", havingValue = "qiniu")
public class QiniuConfiguration {

    private final OssProperties ossProperties;

    private final OssRule ossRule;

    @Bean
    @ConditionalOnMissingBean(com.qiniu.storage.Configuration.class)
    public com.qiniu.storage.Configuration qnConfiguration() {
        return new com.qiniu.storage.Configuration(Region.autoRegion());
    }

    @Bean
    @ConditionalOnMissingBean(Auth.class)
    public Auth auth() {
        return Auth.create(ossProperties.getAccessKey(), ossProperties.getSecretKey());
    }

    @Bean
    @ConditionalOnBean(com.qiniu.storage.Configuration.class)
    public UploadManager uploadManager(com.qiniu.storage.Configuration cfg) {
        return new UploadManager(cfg);
    }

    @Bean
    @ConditionalOnBean(com.qiniu.storage.Configuration.class)
    public BucketManager bucketManager(com.qiniu.storage.Configuration cfg) {
        return new BucketManager(Auth.create(ossProperties.getAccessKey(), ossProperties.getSecretKey()), cfg);
    }

    @Bean
    @ConditionalOnBean({Auth.class, UploadManager.class, BucketManager.class})
    @ConditionalOnMissingBean(QiniuTemplate.class)
    public QiniuTemplate qiniuTemplate(Auth auth, UploadManager uploadManager, BucketManager bucketManager) {
        return new QiniuTemplate(auth, uploadManager, bucketManager, ossProperties, ossRule);
    }
}
