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
package io.github.rose.security.config;

import static io.github.rose.security.SecurityProperties.DEFAULT_PATH_TO_SKIP;

import io.github.rose.security.SecurityProperties;
import io.github.rose.security.rest.filter.RestAccessProcessingFilter;
import io.github.rose.security.rest.filter.RestLoginProcessingFilter;
import io.github.rose.security.rest.filter.RestRefreshProcessingFilter;
import io.github.rose.security.support.IpAuthenticationDetailSource;
import io.github.rose.security.util.SkipPathRequestMatcher;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
@AutoConfiguration
@AutoConfigureOrder(-1)
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig {

    private final ApplicationContext applicationContext;

    private final AccessDeniedHandler accessDeniedHandler;

    private final SecurityProperties securityProperties;

    private final AuthenticationSuccessHandler restAuthenticationSuccessHandler;

    private final AuthenticationFailureHandler restAuthenticationFailureHandler;

    private final AuthenticationManager authenticationManager;

    public WebSecurityConfig(
            ApplicationContext applicationContext,
            AccessDeniedHandler accessDeniedHandler,
            SecurityProperties securityProperties,
            AuthenticationSuccessHandler restAuthenticationSuccessHandler,
            AuthenticationFailureHandler restAuthenticationFailureHandler,
            AuthenticationManager authenticationManager) {
        this.applicationContext = applicationContext;
        this.accessDeniedHandler = accessDeniedHandler;
        this.securityProperties = securityProperties;
        this.restAuthenticationSuccessHandler = restAuthenticationSuccessHandler;
        this.restAuthenticationFailureHandler = restAuthenticationFailureHandler;
        this.authenticationManager = authenticationManager;
    }

    // @Nullable
    // @Qualifier("oauth2AuthenticationSuccessHandler")
    // private ObjectProvider<AuthenticationSuccessHandler>
    // oauth2AuthenticationSuccessHandler;
    // @Nullable
    // @Qualifier("oauth2AuthenticationFailureHandler")
    // private ObjectProvider<AuthenticationFailureHandler>
    // oauth2AuthenticationFailureHandler;

    // private HttpCookieOAuth2AuthorizationRequestRepository
    // httpCookieOAuth2AuthorizationRequestRepository;
    // private OAuth2AuthorizationRequestResolver oAuth2AuthorizationRequestResolver;
    // OAuth2Configuration oauth2Configuration;

    /**
     * 配置 URL 的安全配置
     * <p>
     * anyRequest | 匹配所有请求路径 access | SpringEl表达式结果为true时可以访问 anonymous | 匿名可以访问 denyAll |
     * 用户不能访问 fullyAuthenticated | 用户完全认证可以访问（非remember-me下自动登录） hasAnyAuthority |
     * 如果有参数，参数表示权限，则其中任何一个权限可以访问 hasAnyRole | 如果有参数，参数表示角色，则其中任何一个角色可以访问 hasAuthority |
     * 如果有参数，参数表示权限，则其权限可以访问 hasIpAddress | 如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问 hasRole |
     * 如果有参数，参数表示角色，则其角色可以访问 permitAll | 用户可以任意访问 rememberMe | 允许通过remember-me登录的用户访问
     * authenticated | 用户登录后可访问
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 开启跨域
                .cors(Customizer.withDefaults())
                // CSRF 禁用，因为不使用 Session
                .csrf(AbstractHttpConfigurer::disable)
                // 基于 token 机制，所以不需要 Session
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                // 一堆自定义的 Spring Security 处理器
                .exceptionHandling(c -> c.accessDeniedHandler(accessDeniedHandler));

        Set<String> permitUrls = getPermitUrlsFromAnnotation();
        httpSecurity.authorizeHttpRequests(c -> c.antMatchers(HttpMethod.GET, "/*.html", "/*.css", "/*.js")
                .permitAll()
                .antMatchers(DEFAULT_PATH_TO_SKIP)
                .permitAll()
                .antMatchers(permitUrls.toArray(new String[0]))
                .permitAll()
                .antMatchers(securityProperties.getPathsToSkip().toArray(new String[0]))
                .permitAll()
                .antMatchers(securityProperties.getBaseUrl())
                .authenticated());

        httpSecurity
                .addFilterBefore(restLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(restAccessTokenProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(restRefreshProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    protected RestLoginProcessingFilter restLoginProcessingFilter() throws Exception {
        RestLoginProcessingFilter filter = new RestLoginProcessingFilter(
                securityProperties.getLoginUrl(), restAuthenticationSuccessHandler, restAuthenticationFailureHandler);
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationDetailsSource(new IpAuthenticationDetailSource());
        return filter;
    }

    @Bean
    protected RestAccessProcessingFilter restAccessTokenProcessingFilter() throws Exception {
        SkipPathRequestMatcher matcher =
                new SkipPathRequestMatcher(securityProperties.getPathsToSkip(), securityProperties.getBaseUrl());
        RestAccessProcessingFilter filter = new RestAccessProcessingFilter(matcher, restAuthenticationFailureHandler);
        filter.setAuthenticationManager(this.authenticationManager);
        filter.setAuthenticationDetailsSource(new IpAuthenticationDetailSource());
        return filter;
    }

    protected RestRefreshProcessingFilter restRefreshProcessingFilter() throws Exception {
        RestRefreshProcessingFilter filter = new RestRefreshProcessingFilter(
                securityProperties.getTokenRefreshUrl(),
                restAuthenticationSuccessHandler,
                restAuthenticationFailureHandler);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    private Set<String> getPermitUrlsFromAnnotation() {
        RequestMappingHandlerMapping requestMappingHandlerMapping =
                (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping");

        Set<String> result = new HashSet<>();
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            if (!handlerMethod.hasMethodAnnotation(PermitAll.class)) {
                continue;
            }
            Set<String> urls = new HashSet<>();
            if (entry.getKey().getPatternsCondition() != null) {
                urls.addAll(entry.getKey().getPatternsCondition().getPatterns());
            }
            if (entry.getKey().getPathPatternsCondition() != null) {
                urls.addAll(entry.getKey().getPathPatternsCondition().getPatterns().stream()
                        .map(PathPattern::getPatternString)
                        .collect(Collectors.toList()));
            }
            result.addAll(urls);
        }
        return result;
    }
}
