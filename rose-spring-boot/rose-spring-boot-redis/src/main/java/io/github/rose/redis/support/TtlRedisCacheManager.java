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
package io.github.rose.redis.support;

import io.github.rose.core.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * 支持自定义过期时间的 {@link RedisCacheManager} 实现类
 * <p>
 * 在 {@link Cacheable#cacheNames()} 格式为 "key#120s" 时，# 后面的为过期时间
 */
@Slf4j
public class TtlRedisCacheManager extends RedisCacheManager {
    private static final int CACHE_LENGTH = 2;

    public TtlRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    public TtlRedisCacheManager(
        RedisCacheWriter cacheWriter,
        RedisCacheConfiguration defaultCacheConfiguration,
        String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, true, initialCacheNames);
    }

    @Override
    protected RedisCache createRedisCache(String name, @Nullable RedisCacheConfiguration cacheConfig) {
        if (!StringUtils.hasLength(name) || !name.contains(StringPool.HASH)) {
            return super.createRedisCache(name, cacheConfig);
        }
        String[] names = name.split(StringPool.HASH);
        if (names.length < CACHE_LENGTH) {
            return super.createRedisCache(names[0], cacheConfig);
        }
        if (cacheConfig != null) {
            String ttlStr = names[1].split(StringPool.COLON)[0];
            cacheConfig = cacheConfig.entryTtl(Duration.ofSeconds(Long.parseLong(ttlStr)));
        }
        return super.createRedisCache(names[0], cacheConfig);
    }
}
