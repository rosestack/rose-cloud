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
package io.github.rose.redis.mq.config;

import io.github.rose.redis.config.RedisCacheConfig;
import io.github.rose.redis.mq.RedisMQTemplate;
import io.github.rose.redis.mq.interceptor.RedisMessageInterceptor;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis 消息队列 Producer 配置类
 *
 * @author EnjoyIot
 */
@AutoConfiguration(after = RedisCacheConfig.class)
public class EnjoyRedisMQProducerAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(EnjoyRedisMQProducerAutoConfiguration.class);

    @Bean
    public RedisMQTemplate redisMQTemplate(
            StringRedisTemplate redisTemplate, List<RedisMessageInterceptor> interceptors) {
        RedisMQTemplate redisMQTemplate = new RedisMQTemplate(redisTemplate);
        // 添加拦截器
        interceptors.forEach(redisMQTemplate::addInterceptor);
        return redisMQTemplate;
    }
}
