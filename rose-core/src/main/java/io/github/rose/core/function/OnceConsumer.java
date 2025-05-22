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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public final class OnceConsumer<T> {

    final T t;

    private final AtomicBoolean hasRun = new AtomicBoolean(false);

    private OnceConsumer(final T t) {
        this.t = t;
    }

    public static <T> OnceConsumer<T> of(final T t) {
        return new OnceConsumer<>(t);
    }

    /**
     * Apply a computation on subject only once. <pre><code>
     * List&lt;String&gt; lst = new ArrayList&lt;&gt;();
     *
     * OnceConsumer&lt;List&lt;String&gt;&gt; once = OnceConsumer.of(lst);
     * once.applyOnce((l) -&gt; l.add("Hello World"));
     * once.applyOnce((l) -&gt; l.add("Hello World"));
     *
     * assertThat(lst).hasSize(1).contains("Hello World");
     *
     * </code></pre>
     *
     * @param consumer computation run once with input t
     */
    public void applyOnce(final Consumer<T> consumer) {
        if (hasRun.compareAndSet(false, true)) {
            consumer.accept(t);
        }
    }
}
