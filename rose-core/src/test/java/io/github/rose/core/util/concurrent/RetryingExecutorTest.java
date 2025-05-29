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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class RetryingExecutorTest {
    private RetryingExecutor executor = new RetryingExecutor("test", Duration.ofMillis(100));
    private CountDownLatch latch = new CountDownLatch(1);

    @Test
    public void testExecution() throws InterruptedException {
        executor.execute(latch::countDown);
        try {
            executor.start();
            assertThat(latch.await(1, TimeUnit.SECONDS)).isTrue();
        } finally {
            executor.stop();
        }
    }

    @Test
    public void testRetry() throws InterruptedException {
        AtomicInteger count = new AtomicInteger();
        executor.execute(() -> {
            count.incrementAndGet();
            throw new RuntimeException("");
        });
        try {
            executor.start();
            assertThat(latch.await(1, TimeUnit.SECONDS)).isFalse();
            assertThat(count.get()).isGreaterThan(1);
        } finally {
            executor.stop();
        }
    }
}
