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
package io.github.rose.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

@ConfigurationProperties(prefix = "security", ignoreUnknownFields = false)
public class SecurityProperties {

    public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/api/auth/login";

    public static final String TOKEN_REFRESH_ENTRY_POINT = "/api/auth/token";

    public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**";

    public static final String[] DEFAULT_PATH_TO_SKIP = new String[] {
        TOKEN_REFRESH_ENTRY_POINT,
        FORM_BASED_LOGIN_ENTRY_POINT,
        "/api/noauth/**",
        "/error",
        "/actuator/**",
        "/api/upms/mail/oauth2/code"
    };

    private String baseUrl = TOKEN_BASED_AUTH_ENTRY_POINT;

    private String loginUrl = FORM_BASED_LOGIN_ENTRY_POINT;

    private String tokenRefreshUrl = TOKEN_REFRESH_ENTRY_POINT;

    private List<String> pathsToSkip = new ArrayList<>();

    private Long accessTokenExpireTime = 9000L;

    private Long refreshTokenExpireTime = 604800L;

    private JwtProperties jwt = new JwtProperties();

    public SecurityProperties() {}

    public List<String> getPathsToSkip() {
        if (CollectionUtils.isEmpty(pathsToSkip)) {
            pathsToSkip.addAll(Arrays.asList(DEFAULT_PATH_TO_SKIP));
        }
        return pathsToSkip;
    }

    public void setPathsToSkip(List<String> pathsToSkip) {
        this.pathsToSkip = pathsToSkip;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getTokenRefreshUrl() {
        return tokenRefreshUrl;
    }

    public void setTokenRefreshUrl(String tokenRefreshUrl) {
        this.tokenRefreshUrl = tokenRefreshUrl;
    }

    public Long getAccessTokenExpireTime() {
        return accessTokenExpireTime;
    }

    public void setAccessTokenExpireTime(Long accessTokenExpireTime) {
        this.accessTokenExpireTime = accessTokenExpireTime;
    }

    public Long getRefreshTokenExpireTime() {
        return refreshTokenExpireTime;
    }

    public void setRefreshTokenExpireTime(Long refreshTokenExpireTime) {
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    public JwtProperties getJwt() {
        return jwt;
    }

    public void setJwt(JwtProperties jwt) {
        this.jwt = jwt;
    }

    public static class JwtProperties {
        private boolean enabled = false;

        private String tokenIssuer = "rose.rose-group.github.io";

        private String tokenSigningKey =
                "secret12345678901234567890123456789012345678901234567890123456789012345678901234567890";

        public JwtProperties() {}

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getTokenIssuer() {
            return tokenIssuer;
        }

        public void setTokenIssuer(String tokenIssuer) {
            this.tokenIssuer = tokenIssuer;
        }

        public String getTokenSigningKey() {
            return tokenSigningKey;
        }

        public void setTokenSigningKey(String tokenSigningKey) {
            this.tokenSigningKey = tokenSigningKey;
        }
    }
}
