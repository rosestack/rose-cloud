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
package io.github.rose.core.function;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A {@link Supplier} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedSupplier<T> {
    /**
     * @see {@link Unchecked#supplier(CheckedSupplier)}
     */
    static <T> Supplier<T> unchecked(CheckedSupplier<T> supplier) {
        return Unchecked.supplier(supplier);
    }

    /**
     * @see {@link Unchecked#supplier(CheckedSupplier, Consumer)}
     */
    static <T> Supplier<T> unchecked(CheckedSupplier<T> supplier, Consumer<Throwable> handler) {
        return Unchecked.supplier(supplier, handler);
    }

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get() throws Throwable;
}
