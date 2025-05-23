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
package io.github.rose.core.util.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public final class Async {

    private Async() {
    }

    public static <U> CompletionStage<U> supplyAsync(Supplier<U> supplier) {
        return SameExecutorCompletionStage.of(CompletableFuture.supplyAsync(supplier), null);
    }

    public static <U> CompletionStage<U> supplyAsync(Supplier<U> supplier, Executor executor) {
        return SameExecutorCompletionStage.of(CompletableFuture.supplyAsync(supplier, executor), executor);
    }

    public static CompletionStage<Void> runAsync(Runnable runnable, Executor executor) {
        return SameExecutorCompletionStage.of(CompletableFuture.runAsync(runnable, executor), executor);
    }

    public static CompletionStage<Void> runAsync(Runnable runnable) {
        return SameExecutorCompletionStage.of(CompletableFuture.runAsync(runnable), null);
    }
}
