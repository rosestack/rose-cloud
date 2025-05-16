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
package io.github.rose.mybatis.mq.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

/**
 * 多租户的 Kafka 的 {@link EnvironmentPostProcessor} 实现类
 * <p>
 * Kafka Producer 发送消息时，增加 {@link TenantKafkaProducerInterceptor} 拦截器
 */
public class TenantKafkaEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(TenantKafkaEnvironmentPostProcessor.class);

    private static final String PROPERTY_KEY_INTERCEPTOR_CLASSES =
            "spring.kafka.producer.properties.interceptor.classes";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // 添加 TenantKafkaProducerInterceptor 拦截器
        try {
            String value = environment.getProperty(PROPERTY_KEY_INTERCEPTOR_CLASSES);
            if (StringUtils.isEmpty(value)) {
                value = TenantKafkaProducerInterceptor.class.getName();
            } else {
                value += "," + TenantKafkaProducerInterceptor.class.getName();
            }
            environment.getSystemProperties().put(PROPERTY_KEY_INTERCEPTOR_CLASSES, value);
        } catch (NoClassDefFoundError ignore) {
            // 如果触发 NoClassDefFoundError 异常，说明 TenantKafkaProducerInterceptor 类不存在，即没引入
            // kafka-spring 依赖
        }
    }
}
