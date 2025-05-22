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
import cc.rosestack.github.iore.ratelimit.limiter.FixedWindowRateLimiter;

public class IpRateLimitRule implements RateLimitRule {
    private static final int defaultWindowSizeInMillis = 2 * 1000;
    private FixedWindowRateLimiter fixedWindowRateLimiter = null;

    public IpRateLimitRule(int limit, int windowSizeInMillis) {
        fixedWindowRateLimiter = new FixedWindowRateLimiter(limit, windowSizeInMillis);
    }

    public IpRateLimitRule(int limit) {
        this(limit, defaultWindowSizeInMillis);
    }

    @Override
    public boolean allowRequest(RateLimitContext context) {
        return fixedWindowRateLimiter.tryAcquire(context.getIp());
    }
}
