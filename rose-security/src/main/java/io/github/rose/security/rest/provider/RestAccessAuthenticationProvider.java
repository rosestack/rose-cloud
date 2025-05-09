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
package io.github.rose.security.rest.provider;

import io.github.rose.security.rest.token.RestAccessAuthenticationToken;
import io.github.rose.security.support.TokenFactory;
import io.github.rose.security.util.SecurityUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class RestAccessAuthenticationProvider implements AuthenticationProvider {

    private final TokenFactory tokenFactory;

    public RestAccessAuthenticationProvider(TokenFactory tokenFactory) {
        this.tokenFactory = tokenFactory;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String accessToken = (String) authentication.getCredentials();
        SecurityUser securityUser = authenticate(accessToken);
        return new RestAccessAuthenticationToken(securityUser);
    }

    public SecurityUser authenticate(String accessToken) throws AuthenticationException {
        if (StringUtils.isEmpty(accessToken)) {
            throw new BadCredentialsException("Token is invalid");
        }
        return tokenFactory.parseAccessToken(accessToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (RestAccessAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
