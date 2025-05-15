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

import cc.rose-group.github.iore.ratelimit.handler.DefaultValueRejectedHandler;
import cc.rose-group.github.iore.ratelimit.rule.UserLevelRateLimitRule;

public class RateLimitDemo {
    public static void main(String[] args) {
        // 创建限流规则
        RateLimitRule rule = new UserLevelRateLimitRule();

        // 创建拒绝策略
        RejectedHandler<String> rejectedHandler = new DefaultValueRejectedHandler<>("Rate limit exceeded");

        // 创建限流器
        RateLimitExecutor<String> rateLimitExecutor = new RateLimitExecutor<>(rule, rejectedHandler);

        // 创建限流上下文
        RateLimitContext context = new RateLimitContext();
        context.setUserLevel("normal"); // 普通用户

        // 模拟请求
        for (int i = 1; i <= 10; i++) {
            int finalI = i;
            String result = rateLimitExecutor.execute(context, () -> "Processed request " + finalI);
            try {
                Thread.sleep(300); // 模拟请求间隔
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
