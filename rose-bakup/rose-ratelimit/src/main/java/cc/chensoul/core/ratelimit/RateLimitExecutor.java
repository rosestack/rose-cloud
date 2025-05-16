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
package cc.rose-group.github.iore.ratelimit;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class RateLimitExecutor<T> {
    private final RateLimitRule rateLimitRule;
    private final RejectedHandler<T> rejectedHandler;

    public RateLimitExecutor(RateLimitRule rateLimitRule, RejectedHandler<T> rejectedHandler) {
        this.rateLimitRule = rateLimitRule;
        this.rejectedHandler = rejectedHandler;
    }

    public T execute(RateLimitContext context, Supplier<T> supplier) {
        if (rateLimitRule.allowRequest(context)) {
            return supplier.get(); // 允许通过，执行原始逻辑
        } else {
            return rejectedHandler.handleRejection(context); // 被限流，执行拒绝策略
        }
    }

    public void execute(RateLimitContext context, Consumer<T> consumer, T t) {
        if (rateLimitRule.allowRequest(context)) {
            consumer.accept(t); // 允许通过，执行原始逻辑
        } else {
            rejectedHandler.handleRejection(context); // 被限流，执行拒绝策略
        }
    }
}
