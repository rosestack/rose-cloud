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

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletionStage;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
class AsyncTest {

    @Test
    public void testNoCustomExecutor() {
        CompletionStage<Void> completionStage = Async.runAsync(() -> {
        });
        assertNull(completionStage.toCompletableFuture().join());

        completionStage = Async.supplyAsync(() -> null);
        assertNull(completionStage.toCompletableFuture().join());
    }
}
