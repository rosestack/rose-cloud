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
package io.github.rose.filter;

import io.github.rose.core.util.date.DatePattern;
import io.github.rose.core.util.date.TimeUtils;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class CustomRequestLoggingFilter extends CommonsRequestLoggingFilter {
    public static final String START_TIME = "x-request-start-time";
    private static final Logger log = LoggerFactory.getLogger(CustomRequestLoggingFilter.class);
    private final int maxResponseTimeToLogInMs;

    private List<String> ignoreHeaders =
            Arrays.asList("password", "authorization", "token", "accessToken", "access_token", "refreshToken");

    public CustomRequestLoggingFilter(int maxResponseTimeToLogInMs) {
        this.maxResponseTimeToLogInMs = maxResponseTimeToLogInMs;
    }

    @PostConstruct
    public void init() {
        Predicate<String> headerPredicate =
                headerName -> ObjectUtils.isEmpty(ignoreHeaders) || !ignoreHeaders.contains(headerName);
        if (getHeaderPredicate() == null) {
            setHeaderPredicate(headerPredicate);
        } else {
            setHeaderPredicate(getHeaderPredicate().or(headerPredicate));
        }
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return true;
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        request.setAttribute(START_TIME, System.currentTimeMillis());
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        final Long startTime = (Long) request.getAttribute(START_TIME);
        if (startTime != null) {
            final long cost = System.currentTimeMillis() - startTime;
            message = message + ", cost " + cost + " ms";

            if (cost >= this.maxResponseTimeToLogInMs) {
                String execTime =
                        TimeUtils.format(TimeUtils.fromMilliseconds(startTime), DatePattern.NORM_DATETIME_MS_PATTERN);
                log.warn("[SLOW_REQUEST] {} {} {} {}", execTime, request.getMethod(), request.getRequestURI(), cost);
            }
        }
        log.info(message);
    }
}
