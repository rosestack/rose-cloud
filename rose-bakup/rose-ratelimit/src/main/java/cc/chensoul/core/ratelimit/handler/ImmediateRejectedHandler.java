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
package cc.rosestack.github.iore.ratelimit.handler;

import cc.rosestack.github.iore.ratelimit.RateLimitContext;
import cc.rosestack.github.iore.ratelimit.RateLimitException;
import cc.rosestack.github.iore.ratelimit.RejectedHandler;

public class ImmediateRejectedHandler<T> implements RejectedHandler<T> {
    @Override
    public T handleRejection(RateLimitContext rateLimitContext) {
        throw new RateLimitException("Too many requests for " + rateLimitContext);
    }
}
