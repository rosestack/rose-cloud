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

import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * @author zhijun.chen
 * @since 2.16.3
 */
public class CachingRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        ServletRequest requestWrapper = new CachingHttpServletRequestWrapper(request);
        chain.doFilter(requestWrapper, response);
    }

    public class CachingHttpServletRequestWrapper extends HttpServletRequestWrapper {
        private final byte[] bodyBytes;
        private final Map<String, String[]> parameterMap;

        public CachingHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            this.bodyBytes = readRequestBody(request);
            this.parameterMap = super.getParameterMap();
        }

        private byte[] readRequestBody(HttpServletRequest request) throws IOException {
            request.setCharacterEncoding("UTF-8");
            try (InputStream inputStream = request.getInputStream()) {
                return StreamUtils.copyToByteArray(inputStream);
            }
        }

        @Override
        public BufferedReader getReader() {
            return ObjectUtils.isEmpty(this.bodyBytes)
                ? null
                : new BufferedReader(new InputStreamReader(getInputStream()));
        }

        /**
         * 重写 getParameterMap() 方法，解决 undertow 中流被读取后，会进行标记，从而导致无法正确获取 body 中的表单数据的问题
         *
         * @return Map<String, String [ ]> parameterMap
         */
        @Override
        public Map<String, String[]> getParameterMap() {
            return this.parameterMap;
        }

        @Override
        public ServletInputStream getInputStream() {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bodyBytes);
            return new ServletInputStream() {
                @Override
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                    throw new UnsupportedOperationException("ReadListener is not supported");
                }
            };
        }
    }
}
