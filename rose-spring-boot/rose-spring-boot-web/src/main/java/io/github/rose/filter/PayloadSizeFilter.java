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

import io.github.rose.core.jackson.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class PayloadSizeFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(PayloadSizeFilter.class);

    private final Map<String, Long> limits = new LinkedHashMap<>();

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public PayloadSizeFilter(String limitsConfiguration) {
        for (String limit : limitsConfiguration.split(";")) {
            try {
                String urlPathPattern = limit.split("=")[0];
                long maxPayloadSize = Long.parseLong(limit.split("=")[1]);
                limits.put(urlPathPattern, maxPayloadSize);
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to parse size limits configuration: " + limitsConfiguration);
            }
        }
        log.info("Initialized payload size filter with configuration: {}", limitsConfiguration);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        for (String url : limits.keySet()) {
            if (pathMatcher.match(url, request.getRequestURI())) {
                if (checkMaxPayloadSizeExceeded(request, response, limits.get(url))) {
                    return;
                }
                break;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean checkMaxPayloadSizeExceeded(
        HttpServletRequest request, HttpServletResponse response, long maxPayloadSize) throws IOException {
        if (request.getContentLength() > maxPayloadSize) {
            log.info(
                "[{}] [{}] Payload size {} exceeds the limit of {} bytes",
                request.getRemoteAddr(),
                request.getRequestURL(),
                request.getContentLength(),
                maxPayloadSize);
            handleMaxPayloadSizeExceededException(response, new RuntimeException("Payload size exceeds the limit of " + maxPayloadSize + " bytes"));
            return true;
        }
        return false;
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }

    private void handleMaxPayloadSizeExceededException(
        HttpServletResponse response, RuntimeException exception) throws IOException {
        response.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
        JacksonUtils.writeValue(response.getWriter(), exception.getMessage());
    }
}
