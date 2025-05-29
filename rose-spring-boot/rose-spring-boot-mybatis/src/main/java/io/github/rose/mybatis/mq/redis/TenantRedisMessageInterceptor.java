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
package io.github.rose.mybatis.mq.redis;

import static io.github.rose.core.util.Constants.HEADER_TENANT_ID;

import io.github.rose.mybatis.tenant.util.TenantContextHolder;
import io.github.rose.redis.mq.interceptor.RedisMessageInterceptor;
import io.github.rose.redis.mq.message.AbstractRedisMessage;
import org.apache.commons.lang3.StringUtils;

/**
 * 多租户拦截器
 * <p>
 * 1. Producer 发送消息时，将 {@link TenantContextHolder} 租户编号，添加到消息的 Header 中 2. Consumer
 * 消费消息时，将消息的 Header 的租户编号，添加到 {@link TenantContextHolder} 中
 *
 * @author EnjoyIot
 */
public class TenantRedisMessageInterceptor implements RedisMessageInterceptor {

    @Override
    public void sendMessageBefore(AbstractRedisMessage message) {
        String tenantId = TenantContextHolder.getTenantId();
        if (tenantId != null) {
            message.addHeader(HEADER_TENANT_ID, tenantId);
        }
    }

    @Override
    public void consumeMessageBefore(AbstractRedisMessage message) {
        String tenantIdStr = message.getHeader(HEADER_TENANT_ID);
        if (StringUtils.isNotEmpty(tenantIdStr)) {
            TenantContextHolder.setTenantId(tenantIdStr);
        }
    }

    @Override
    public void consumeMessageAfter(AbstractRedisMessage message) {
        // 注意，Consumer 是一个逻辑的入口，所以不考虑原本上下文就存在租户编号的情况
        TenantContextHolder.clear();
    }
}
