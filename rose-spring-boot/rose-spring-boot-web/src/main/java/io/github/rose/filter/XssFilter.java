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
package io.github.rose.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.owasp.encoder.Encode;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 防止XSS攻击的过滤器
 */
public class XssFilter extends OncePerRequestFilter {

    public final List<String> excludes;

    public XssFilter(List<String> excludes) {
        this.excludes = excludes;
    }

    public static boolean matches(String str, List<String> strs) {
        if (ObjectUtils.isEmpty(str) || ObjectUtils.isEmpty(strs)) {
            return false;
        }
        for (String pattern : strs) {
            if (new AntPathMatcher().match(pattern, str)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String method = request.getMethod();
        if (!HttpMethod.POST.matches(method) && !HttpMethod.PUT.matches(method) && !HttpMethod.PATCH.matches(method)) {
            return true;
        }
        return matches(request.getServletPath(), excludes);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(request);
        chain.doFilter(xssRequest, response);
    }

    public static class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
        public XssHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values != null) {
                int length = values.length;
                String[] escapeValues = new String[length];
                for (int i = 0; i < length; i++) {
                    escapeValues[i] = Encode.forUriComponent(values[i]);
                }
                return escapeValues;
            }
            return super.getParameterValues(name);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            String input = StreamUtils.copyToString(super.getInputStream(), StandardCharsets.UTF_8);
            if (StringUtils.isEmpty(input)) {
                return super.getInputStream();
            }
            byte[] inputBytes = Encode.forHtml(input).getBytes(StandardCharsets.UTF_8);
            final ByteArrayInputStream bis = new ByteArrayInputStream(inputBytes);
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return true;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public int available() throws IOException {
                    return inputBytes.length;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public int read() throws IOException {
                    return bis.read();
                }
            };
        }
    }
}
