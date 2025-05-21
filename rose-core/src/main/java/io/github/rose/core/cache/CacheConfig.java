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

import java.util.function.Function;

public class CacheConfig<K, V> {
    private int initialSize;
    private int maxSize;
    private int concurrencyLevel;
    private Function<? super K, ? extends V> loadingFunction;

    private CacheConfig(Builder<K, V> builder) {
        this.initialSize = builder.initialSize;
        this.maxSize = builder.maxSize;
        this.concurrencyLevel = builder.concurrencyLevel;
        this.loadingFunction = builder.loadingFunction;
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    public int getInitialSize() {
        return initialSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getConcurrencyLevel() {
        return concurrencyLevel;
    }

    public Function<? super K, ? extends V> getLoadingFunction() {
        return loadingFunction;
    }

    public static class Builder<K, V> {
        private int initialSize = 256;
        private int maxSize = 1024;
        private int concurrencyLevel = Math.max(1, Runtime.getRuntime().availableProcessors());
        private Function<? super K, ? extends V> loadingFunction;

        public Builder<K, V> initialSize(int initialSize) {
            this.initialSize = initialSize;
            return this;
        }

        public Builder<K, V> maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public Builder<K, V> concurrencyLevel(int concurrencyLevel) {
            this.concurrencyLevel = concurrencyLevel;
            return this;
        }

        public Builder<K, V> loadingFunction(Function<? super K, ? extends V> loadingFunction) {
            this.loadingFunction = loadingFunction;
            return this;
        }

        public CacheConfig<K, V> build() {
            return new CacheConfig<>(this);
        }
    }
}
