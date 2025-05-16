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
package io.github.rose.mybatis.redis;

import static io.github.rose.core.util.StringPool.COLON;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.github.rose.mybatis.tenant.util.TenantContextHolder;
import io.github.rose.redis.support.TtlRedisCacheManager;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

/**
 * 多租户的 {@link RedisCacheManager} 实现类
 * <p>
 * 操作指定 name 的 {@link Cache} 时，自动拼接租户后缀，格式为 name + ":" + tenantId + 后缀
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
public class TenantRedisCacheManager extends TtlRedisCacheManager {

    private final Set<String> ignoredCaches;

    public TenantRedisCacheManager(
            Set<String> ignoredCaches,
            RedisCacheWriter cacheWriter,
            RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.ignoredCaches = ignoredCaches;
    }

    @Override
    public Cache getCache(String name) {
        // 如果开启多租户，则 name 拼接租户后缀
        if (!TenantContextHolder.isIgnored()
                && StringUtils.isNotBlank(TenantContextHolder.getTenantId())
                && (CollectionUtils.isEmpty(ignoredCaches) || !ignoredCaches.contains(name))) {
            name = name + COLON + TenantContextHolder.getTenantId();
        }

        return super.getCache(name);
    }
}
