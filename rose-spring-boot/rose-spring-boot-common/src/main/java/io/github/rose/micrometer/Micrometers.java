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
package io.github.rose.micrometer;

import feign.Retryer;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 异步记录监控指标
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 4.0.4
 */
public abstract class Micrometers {

    public static final Retryer DEFAULT = new Retryer.Default(100L, TimeUnit.SECONDS.toMillis(1L), 3);

    private static final ExecutorService asyncExecutor = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors(),
        new BasicThreadFactory.Builder().namingPattern("metric-pool-%d").build());

    public static void async(Runnable runnable) {
        asyncExecutor.execute(runnable);
    }
}
