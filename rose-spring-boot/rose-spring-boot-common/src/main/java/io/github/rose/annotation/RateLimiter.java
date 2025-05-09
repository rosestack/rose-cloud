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
package io.github.rose.annotation;

import io.github.rose.ratelimiter.LimitType;

import java.lang.annotation.*;

/**
 * 限流注解
 *
 * @author canghe
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 限流key
     */
    public String key() default RATE_LIMIT_KEY;

    /**
     * 限流时间,单位秒
     */
    public int time() default 60;

    /**
     * 限流次数
     */
    public int count() default 100;

    /**
     * 限流类型
     */
    public LimitType limitType() default LimitType.DEFAULT;
}
