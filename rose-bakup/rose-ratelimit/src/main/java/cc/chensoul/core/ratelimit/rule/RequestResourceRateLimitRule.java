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
package cc.rosestack.github.iore.ratelimit.rule;

import cc.rosestack.github.iore.ratelimit.RateLimitContext;
import cc.rosestack.github.iore.ratelimit.RateLimitRule;
import cc.rosestack.github.iore.ratelimit.limiter.TokenBucketRateLimiter;

import java.util.HashMap;
import java.util.Map;

public class RequestResourceRateLimitRule implements RateLimitRule {
    private final Map<String, Integer> resourceLimits; // 接口 -> 限流阈值

    public RequestResourceRateLimitRule() {
        resourceLimits = new HashMap<>();
        resourceLimits.put("order", 1000); // 核心业务接口：每秒1000次
        resourceLimits.put("userInfo", 500); // 非核心业务接口：每秒500次
        resourceLimits.put("admin", 100);   // 管理类接口：每秒100次
    }

    @Override
    public boolean allowRequest(RateLimitContext context) {
        String apiName = context.getRequestResource();
        int limit = resourceLimits.getOrDefault(apiName, 100); // 默认管理类接口
        // 实现限流逻辑（如令牌桶算法）
        TokenBucketRateLimiter tokenBucketRateLimiter = new TokenBucketRateLimiter(limit, 1000);
        return tokenBucketRateLimiter.tryAcquire(context.getRequestResource());
    }
}
