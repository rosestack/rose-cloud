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
package io.github.rose.core.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class SimpleLRUCache<K, V> implements Cache<K, V> {
    private final LinkedHashMap<K, V> map;
    private final Function<? super K, ? extends V> loadingFunction;

    private SimpleLRUCache(CacheConfig<K, V> cacheConfig) {
        loadingFunction = cacheConfig.getLoadingFunction();
        final int maxSize = cacheConfig.getMaxSize();
        this.map = new LinkedHashMap<K, V>(cacheConfig.getInitialSize(), 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > maxSize;
            }
        };
    }

    @Override
    public V get(K key) {
        synchronized (map) {
            return map.computeIfAbsent(key, loadingFunction);
        }
    }

    public static class Factory implements CacheFactory {
        @Override
        public <K, V> Cache<K, V> createCache(CacheConfig<K, V> cacheConfig) {
            return new SimpleLRUCache<>(cacheConfig);
        }
    }
}
