/*
 * Copyright © 2025 rosestack.github.io
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
package io.github.rose.core.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.function.Function;

public class GuavaCache<K, V> implements Cache<K, V> {
    private final LoadingCache<K, V> cache;

    private GuavaCache(CacheConfig<K, V> cacheConfig) {
        final Function<? super K, ? extends V> loadingFunction = cacheConfig.getLoadingFunction();
        cache = CacheBuilder.newBuilder()
            .initialCapacity(cacheConfig.getInitialSize())
            .maximumSize(cacheConfig.getMaxSize())
            .build(new CacheLoader<K, V>() {
                @Override
                public V load(K key) throws Exception {
                    return loadingFunction.apply(key);
                }
            });
    }

    @Override
    public V get(K key) {
        return cache.getUnchecked(key);
    }

    public static class Factory implements CacheFactory {
        @Override
        public <K, V> Cache<K, V> createCache(CacheConfig<K, V> cacheConfig) {
            return new GuavaCache<>(cacheConfig);
        }
    }
}
