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
package cc.rose-group.github.iore.ratelimit.rule;

import cc.rose-group.github.iore.ratelimit.RateLimitContext;
import cc.rose-group.github.iore.ratelimit.RateLimitRule;
import cc.rose-group.github.iore.ratelimit.limiter.FixedWindowRateLimiter;

import java.util.HashMap;
import java.util.Map;

public class UserLevelRateLimitRule implements RateLimitRule {
    private final Map<String, FixedWindowRateLimiter> userLevelLimits; // 用户等级 -> 限流阈值
    private final int defaultWindowSizeInMillis = 2 * 1000;
    private final FixedWindowRateLimiter defaultRateLimiter = new FixedWindowRateLimiter(1, defaultWindowSizeInMillis);

    public UserLevelRateLimitRule() {
        userLevelLimits = new HashMap<>();
        userLevelLimits.put("normal", new FixedWindowRateLimiter(1, defaultWindowSizeInMillis));
        userLevelLimits.put("silver", new FixedWindowRateLimiter(30, defaultWindowSizeInMillis));
        userLevelLimits.put("gold", new FixedWindowRateLimiter(50, defaultWindowSizeInMillis));
        userLevelLimits.put("vip", new FixedWindowRateLimiter(100, defaultWindowSizeInMillis));
    }

    @Override
    public boolean allowRequest(RateLimitContext context) {
        String userLevel = context.getUserLevel();
        FixedWindowRateLimiter userRateLimiter = userLevelLimits.getOrDefault(userLevel, defaultRateLimiter);
        return userRateLimiter.tryAcquire(context.getUserLevel());
    }
}
