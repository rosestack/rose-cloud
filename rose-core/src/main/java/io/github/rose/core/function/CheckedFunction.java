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
package io.github.rose.core.function;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A {@link Function} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedFunction<T, R> {

    /**
     * @see {@link Sneaky#function(CheckedFunction)}
     */
    static <T, R> Function<T, R> sneaky(CheckedFunction<T, R> function) {
        return Sneaky.function(function);
    }

    /**
     * @see {@link Unchecked#function(CheckedFunction)}
     */
    static <T, R> Function<T, R> unchecked(CheckedFunction<T, R> function) {
        return Unchecked.function(function);
    }

    /**
     * @see {@link Unchecked#function(CheckedFunction, Consumer)}
     */
    static <T, R> Function<T, R> unchecked(CheckedFunction<T, R> function, Consumer<Throwable> handler) {
        return Unchecked.function(function, handler);
    }

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    R apply(T t) throws Exception;
}
