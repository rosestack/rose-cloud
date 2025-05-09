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

import io.github.rose.core.util.EscapeUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

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
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return handleExcludeURL(request);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }

    private boolean handleExcludeURL(HttpServletRequest request) {
        String url = request.getServletPath();
        String method = request.getMethod();
        // GET DELETE 不过滤
        if (method == null || HttpMethod.GET.matches(method) || HttpMethod.DELETE.matches(method)) {
            return true;
        }
        return matches(url, excludes);
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
                    // 防xss攻击和过滤前后空格
                    escapeValues[i] = EscapeUtils.clean(values[i]).trim();
                }
                return escapeValues;
            }
            return super.getParameterValues(name);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            // 为空，直接返回
            String json = IOUtils.toString(super.getInputStream(), "utf-8");
            if (StringUtils.isEmpty(json)) {
                return super.getInputStream();
            }

            // xss过滤
            json = EscapeUtils.clean(json).trim();
            byte[] jsonBytes = json.getBytes("utf-8");
            final ByteArrayInputStream bis = new ByteArrayInputStream(jsonBytes);
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
                    return jsonBytes.length;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                }

                @Override
                public int read() throws IOException {
                    return bis.read();
                }
            };
        }
    }
}
