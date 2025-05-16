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
package io.github.rose.core.util;

import org.springframework.core.Ordered;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
public interface Constants {
    String PROJECT_NAME = "rose";

    //profile
    String PROFILE_PROD = "prod";
    String PROFILE_NOT_PROD = "!" + PROFILE_PROD;
    String PROFILE_TEST = "test";
    String PROFILE_NOT_TEST = "!" + PROFILE_TEST;

    //filter order
    int CORS_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE;
    int CACHING_REQUEST_FILTER_ORDER = CORS_FILTER_ORDER + 1;
    int TRACE_FILTER_ORDER = CORS_FILTER_ORDER + 2;
    int XSS_FILTER_ORDER = CORS_FILTER_ORDER + 3;
    int TENANT_CONTEXT_FILTER_ORDER = CORS_FILTER_ORDER + 5;
    // Spring Security Filter 默认为 -100，可见 org.springframework.boot.autoconfigure.security.SecurityProperties
    int TENANT_SECURITY_FILTER_ORDER = -99;

    //header
    String HEADER_TENANT_ID = "tenant-id";
    String REQUEST_START_TIME = "Request-Start-Time";
    String REQUEST_FROM_INNER = "Request-From-Inner";

    String RATELIMIT_LIMIT = "RateLimit-Limit";
    String RATELIMIT_REMAINING = "RateLimit-Remaining";
    String RATELIMIT_RESET = "RateLimit-Reset";
    String RETRY_AFTER = "Retry-After";

}
