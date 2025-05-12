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
package io.github.rose.mybatis.tenant.filter;

import io.github.rose.core.spring.WebUtils;
import io.github.rose.core.util.RestResponse;
import io.github.rose.mybatis.tenant.util.TenantContextHolder;
import io.github.rose.security.util.SecurityUser;
import io.github.rose.security.util.SecurityUtils;
import java.io.IOException;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 多租户 Security Web 过滤器 1. 如果是登陆的用户，校验是否有权限访问该租户，避免越权问题。 2. 如果请求未带租户的编号，检查是否是忽略的
 * URL，否则也不允许访问。 3. 校验租户是合法，例如说被禁用、到期
 *
 * @author EnjoyIot
 */
public class TenantSecurityFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(TenantSecurityFilter.class);

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final Set<String> ignoreUrls;

    public TenantSecurityFilter(Set<String> ignoreUrls) {
        this.ignoreUrls = ignoreUrls;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String tenantId = TenantContextHolder.getTenantId();

        // 如果非允许忽略租户的 URL，则校验租户是否合法
        if (!isIgnoreUrl(request)) {
            // 如果请求未带租户的编号，不允许访问。
            if (tenantId == null) {
                log.error("[doFilterInternal][URL({}/{}) 未传递租户编号]", request.getRequestURI(), request.getMethod());
                WebUtils.renderJson(HttpStatus.FORBIDDEN.value(), RestResponse.error("请求的租户标识未传递"));
                return;
            }
            // // 校验租户是合法，例如说被禁用、到期
            // try {
            // tenantFrameworkService.validTenant(tenantId);
            // } catch (Throwable ex) {
            // CommonResult<?> result =
            // globalExceptionHandler.allExceptionHandler(request, ex);
            // ServletUtils.writeJSON(response, result);
            // return;
            // }
        } else {
            // 如果是允许忽略租户的 URL，若未传递租户编号，则默认忽略租户编号，避免报错
            if (tenantId == null) {
                TenantContextHolder.setIgnore(true);
            }
        }

        // 1. 登陆的用户，校验是否有权限访问该租户，避免越权问题。
        SecurityUser user = SecurityUtils.getCurrentUser();
        if (user != null) {
            // 如果获取不到租户编号，则尝试使用登陆用户的租户编号
            if (tenantId == null) {
                tenantId = user.getTenants().get(0);
                TenantContextHolder.setTenantId(tenantId);
                // 如果传递了租户编号，则进行比对租户编号，避免越权问题
            } else if (!user.getTenants().contains(TenantContextHolder.getTenantId())) {
                log.error(
                        "用户{}越权访问租户({}) URL({}/{})]",
                        user.getUsername(),
                        TenantContextHolder.getTenantId(),
                        request.getRequestURI(),
                        request.getMethod());
                WebUtils.renderJson(HttpStatus.FORBIDDEN.value(), RestResponse.error("您无权访问该租户的数据"));
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isIgnoreUrl(HttpServletRequest request) {
        // 快速匹配，保证性能
        if (!ignoreUrls.contains(request.getRequestURI())) {
            return true;
        }
        // 逐个 Ant 路径匹配
        for (String url : ignoreUrls) {
            if (pathMatcher.match(url, request.getRequestURI())) {
                return true;
            }
        }
        return false;
    }
}
