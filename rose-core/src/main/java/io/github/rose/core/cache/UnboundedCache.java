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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public class UnboundedCache<K, V> implements Cache<K, V> {
    private final ConcurrentMap<K, V> map;
    private final Function<? super K, ? extends V> loadingFunction;

    private UnboundedCache(CacheConfig<K, V> cacheConfig) {
        loadingFunction = cacheConfig.getLoadingFunction();
        this.map = new ConcurrentHashMap<>(cacheConfig.getInitialSize(), 0.75f,
                1);
    }

    @Override
    public V get(K key) {
        return map.computeIfAbsent(key, loadingFunction);
    }

    public static class Factory implements CacheFactory {
        @Override
        public <K, V> Cache<K, V> createCache(CacheConfig<K, V> cacheConfig) {
            return new UnboundedCache<>(cacheConfig);
        }
    }
}
