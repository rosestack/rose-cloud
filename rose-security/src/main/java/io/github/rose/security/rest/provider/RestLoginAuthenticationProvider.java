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
package io.github.rose.security.rest.provider;

import io.github.rose.security.rest.mfa.MfaAuthenticationToken;
import io.github.rose.security.rest.mfa.MfaProperties;
import io.github.rose.security.util.SecurityUser;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

public class RestLoginAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder encoder;

    private final MfaProperties mfaProperties;

    public RestLoginAuthenticationProvider(
            UserDetailsService userDetailsService, PasswordEncoder encoder, MfaProperties mfaProperties) {
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
        this.mfaProperties = mfaProperties;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        SecurityUser securityUser = authenticateByUsernameAndPassword(authentication, username, password);

        if (mfaProperties.isEnabled()) {
            return new MfaAuthenticationToken(securityUser);
        }

        return new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
    }

    private SecurityUser authenticateByUsernameAndPassword(
            Authentication authentication, String username, String password) {
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Username or password not valid");
        }

        return new SecurityUser(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
