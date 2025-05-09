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
package io.github.rose.security.rest.filter;

import io.github.rose.core.jackson.JacksonUtils;
import io.github.rose.security.exception.AuthMethodNotSupportedException;
import io.github.rose.security.rest.param.LoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger log = LoggerFactory.getLogger(RestLoginProcessingFilter.class);
    
    private final AuthenticationSuccessHandler successHandler;

    private final AuthenticationFailureHandler failureHandler;

    public RestLoginProcessingFilter(
        String defaultProcessUrl,
        AuthenticationSuccessHandler successHandler,
        AuthenticationFailureHandler failureHandler) {
        super(defaultProcessUrl);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            if (log.isDebugEnabled()) {
                log.debug("{} authentication method not supported", request.getMethod());
            }
            throw new AuthMethodNotSupportedException("Authentication method not supported");
        }

        LoginRequest loginRequest;
        try {
            loginRequest = JacksonUtils.readValue(request.getReader(), LoginRequest.class);
        } catch (Exception e) {
            throw new AuthenticationServiceException("Invalid login request payload");
        }

        if (StringUtils.isEmpty(loginRequest.getUsername()) || StringUtils.isEmpty(loginRequest.getPassword())) {
            throw new AuthenticationServiceException("Username or password is empty");
        }

        UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        token.setDetails(authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(
        HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(
        HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
        throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
