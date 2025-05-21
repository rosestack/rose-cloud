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

import io.github.rose.core.reflect.Classes;
import io.github.rose.core.util.ServiceLoaders;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@FunctionalInterface
public interface Cache<K, V> {
    static <K, V> Cache<K, V> create(Function<? super K, ? extends V> loadingFunction) {
        return Factories.DEFAULT.createCache(CacheConfig.<K, V>builder().loadingFunction(loadingFunction).build());
    }

    static <K, V> Cache<K, V> create(CacheConfig<K, V> cacheConfig) {
        return Factories.DEFAULT.createCache(cacheConfig);
    }

    static <K, V> Cache<K, V> create(CacheConfig<K, V> cacheConfig,
                                     Class<? extends CacheFactory> factoryClass) {
        return Factories.FACTORIES.computeIfAbsent(factoryClass, Classes::instantiateDefault)
            .createCache(cacheConfig);
    }

    V get(K key);

    class Factories {
        private static final ConcurrentMap<Class<? extends CacheFactory>, CacheFactory> FACTORIES = new ConcurrentHashMap<>(loadFactory());
        private static final CacheFactory DEFAULT = resolveDefaultCacheFactory();

        private static Map<Class<? extends CacheFactory>, CacheFactory> loadFactory() {
            return ServiceLoaders.load(CacheFactory.class).stream().collect(Collectors.toMap(CacheFactory::getClass, a -> a));
        }

        private static CacheFactory resolveDefaultCacheFactory() {
            Iterator<CacheFactory> cacheFactoryIterator = FACTORIES.values().iterator();
            if (cacheFactoryIterator.hasNext()) {
                return cacheFactoryIterator.next();
            } else {
                return new SimpleLRUCache.Factory();
            }
        }
    }
}
