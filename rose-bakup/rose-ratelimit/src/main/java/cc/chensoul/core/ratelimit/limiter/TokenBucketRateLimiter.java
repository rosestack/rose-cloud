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
package cc.rosestack.github.iore.ratelimit.limiter;

import cc.rosestack.github.iore.ratelimit.RateLimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

// 令牌桶算法
public class TokenBucketRateLimiter implements RateLimiter {
    private final ConcurrentHashMap<String, com.google.common.util.concurrent.RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();
    private final Integer limit; // 限流阈值
    private final long windowSizeInMillis; // 时间窗口大小

    public TokenBucketRateLimiter(Integer limit, long windowSizeInMillis) {
        this.limit = limit;
        this.windowSizeInMillis = windowSizeInMillis;
    }

    @Override
    public boolean tryAcquire(String key) {
        com.google.common.util.concurrent.RateLimiter rateLimiter = rateLimiterMap.computeIfAbsent(key, k -> com.google.common.util.concurrent.RateLimiter.create(limit, windowSizeInMillis, TimeUnit.MILLISECONDS));
        return rateLimiter.tryAcquire();
    }
}


