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
package io.github.rose.core.spring;

import io.github.rose.core.json.JsonUtils;
import io.github.rose.core.util.NetUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;

import static org.springframework.http.HttpHeaders.USER_AGENT;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class WebUtils extends org.springframework.web.util.WebUtils {
    private static final List<String> CLIENT_IP_HEADER_NAMES = Arrays.asList(
        "X-Forwarded-For",
        "X-Real-IP",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_CLIENT_IP",
        "HTTP_X_FORWARDED_FOR");

    public static String getUsername() {
        HttpServletRequest request = WebUtils.ofRequest().get();
        Principal userPrincipal = request.getUserPrincipal();
        if (userPrincipal != null) {
            return userPrincipal.getName();
        }
        return null;
    }

    public static String getUserAgent(final HttpServletRequest request) {
        if (request != null) {
            return request.getHeader(USER_AGENT);
        }
        return null;
    }

    public static String getClientIp(HttpServletRequest request, String... otherHeaderNames) {
        if (request == null) {
            return null;
        }

        List<String> headers = new ArrayList<>(CLIENT_IP_HEADER_NAMES);
        Arrays.stream(otherHeaderNames).forEach(header -> headers.add(header));

        String ip;
        for (String header : headers) {
            ip = request.getHeader(header);
            if (NetUtils.isIp4Address(ip)) {
                return ip;
            }
        }
        return request.getRemoteHost();
    }

    public static String getValue(String headerName) {
        return getValue(ofRequest().orElse(null), headerName);
    }

    public static String getValue(HttpServletRequest request, String headerName) {
        if (request == null) {
            return null;
        }
        return StringUtils.defaultString(request.getParameter(headerName), request.getHeader(headerName));
    }

    public static String constructUrl(HttpServletRequest request) {
        return String.format(Locale.getDefault(), "%s://%s%s", getScheme(request), getDomainNameAndPort(request), request.getRequestURI());
    }

    public static String getScheme(HttpServletRequest request) {
        String scheme = request.getScheme();
        String forwardedProto = request.getHeader("x-forwarded-proto");
        if (forwardedProto != null) {
            scheme = forwardedProto;
        }
        return scheme;
    }

    public static String getDomainName(HttpServletRequest request) {
        return request.getServerName();
    }

    public static String getDomainNameAndPort(HttpServletRequest request) {
        String domainName = getDomainName(request);
        String scheme = getScheme(request);
        int port = getPort(request);
        if (needsPort(scheme, port)) {
            domainName += ":" + port;
        }
        return domainName;
    }

    private static boolean needsPort(String scheme, int port) {
        boolean isHttpDefault = "http".equalsIgnoreCase(scheme) && port == 80;
        boolean isHttpsDefault = "https".equalsIgnoreCase(scheme) && port == 443;
        return !isHttpDefault && !isHttpsDefault;
    }

    public static int getPort(HttpServletRequest request) {
        String forwardedProto = request.getHeader("x-forwarded-proto");

        int serverPort = request.getServerPort();
        if (request.getHeader("x-forwarded-port") != null) {
            try {
                serverPort = request.getIntHeader("x-forwarded-port");
            } catch (NumberFormatException e) {
            }
        } else if (forwardedProto != null) {
            switch (forwardedProto) {
                case "http":
                    serverPort = 80;
                    break;
                case "https":
                    serverPort = 443;
                    break;
            }
        }
        return serverPort;
    }

    public static void renderJson(int httpStatus, Object result) throws IOException {
        HttpServletResponse response = getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus);

        JsonUtils.writeValue(response.getWriter(), result);
    }

    public static void renderJson(Object result) throws IOException {
        renderJson(HttpServletResponse.SC_OK, result);
    }

    public static Optional<HttpServletRequest> ofRequest() {
        return Optional.of(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
    }

    public static HttpServletRequest getRequest() {
        return ofRequest().orElseThrow(() -> new IllegalStateException("Request is null"));
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }
}
