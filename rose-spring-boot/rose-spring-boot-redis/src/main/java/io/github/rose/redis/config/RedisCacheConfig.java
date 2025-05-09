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
package io.github.rose.redis.config;

import io.github.rose.redis.support.TtlRedisCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheStatisticsCollector;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@EnableCaching
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class RedisCacheConfig {
    private static final Logger log = LoggerFactory.getLogger(RedisCacheConfig.class);
    
    private final CacheProperties cacheProperties;
    private final CacheManagerCustomizers cacheManagerCustomizers;

    public RedisCacheConfig(CacheProperties cacheProperties, CacheManagerCustomizers cacheManagerCustomizers) {
        this.cacheProperties = cacheProperties;
        this.cacheManagerCustomizers = cacheManagerCustomizers;
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        log.info("Initializing RedisCacheManager");

        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory)
            .withStatisticsCollector(CacheStatisticsCollector.create());
        TtlRedisCacheManager cacheManager = new TtlRedisCacheManager(
            redisCacheWriter,
            redisCacheConfiguration(),
            cacheProperties.getCacheNames().toArray(new String[]{}));
        cacheManager.setTransactionAware(false);
        return this.cacheManagerCustomizers.customize(cacheManager);
    }

    private RedisCacheConfiguration redisCacheConfiguration() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.java()));

        CacheProperties.Redis redisProperties = this.cacheProperties.getRedis();
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }

        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }

        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }

        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }
}
