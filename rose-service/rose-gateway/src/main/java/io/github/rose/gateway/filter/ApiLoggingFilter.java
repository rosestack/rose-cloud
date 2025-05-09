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
package io.github.rose.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 全局拦截器，作用所有的微服务
 * 1. 对请求的API调用过滤，记录接口的请求时间，方便日志审计、告警、分析等运维操作 2. 后期可以扩展对接其他日志系统
 */
@Component
public class ApiLoggingFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(ApiLoggingFilter.class);
    
    private static final String START_TIME = "startTime";

    private static final String X_REAL_IP = "X-Real-IP"; // nginx需要配置

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (log.isDebugEnabled()) {
            String info = String.format(
                "Method:{%s} Host:{%s} Path:{%s} Query:{%s}",
                exchange.getRequest().getMethod().name(),
                exchange.getRequest().getURI().getHost(),
                exchange.getRequest().getURI().getPath(),
                exchange.getRequest().getQueryParams());
            log.debug(info);
        }
        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute(START_TIME);
            if (startTime != null) {
                Long executeTime = (System.currentTimeMillis() - startTime);
                List<String> ips = exchange.getRequest().getHeaders().get(X_REAL_IP);
                String ip = ips != null ? ips.get(0) : null;
                String api = exchange.getRequest().getURI().getRawPath();

                int code = 500;
                if (exchange.getResponse().getStatusCode() != null) {
                    code = exchange.getResponse().getStatusCode().value();
                }
                // 当前仅记录日志，后续可以添加日志队列，来过滤请求慢的接口
                if (log.isDebugEnabled()) {
                    log.debug("来自IP地址：{}的请求接口：{}，响应状态码：{}，请求耗时：{}ms", ip, api, code, executeTime);
                }
            }
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
