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
package io.github.rose.security.rest.handler;

import io.github.rose.core.spring.WebUtils;
import io.github.rose.security.rest.mfa.MfaAuthenticationToken;
import io.github.rose.security.support.TokenFactory;
import io.github.rose.security.util.SecurityUser;
import io.github.rose.security.util.TokenPair;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenFactory tokenFactory;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        TokenPair tokenPair;
        if (authentication instanceof MfaAuthenticationToken) {
            tokenPair = tokenFactory.createPreVerificationTokenPair(securityUser);
        } else {
            tokenPair = tokenFactory.createTokenPair(securityUser);
        }

        WebUtils.renderJson(HttpStatus.OK.value(), tokenPair);
    }
}
