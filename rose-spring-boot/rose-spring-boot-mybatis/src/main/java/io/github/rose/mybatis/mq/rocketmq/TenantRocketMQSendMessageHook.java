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
package io.github.rose.mybatis.mq.rocketmq;

import io.github.rose.mybatis.tenant.util.TenantContextHolder;
import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;

import static io.github.rose.core.CommonConstants.HEADER_TENANT_ID;

/**
 * RocketMQ 消息队列的多租户 {@link SendMessageHook} 实现类
 * <p>
 * Producer 发送消息时，将 {@link TenantContextHolder} 租户编号，添加到消息的 Header 中
 *
 * @author EnjoyIot
 */
public class TenantRocketMQSendMessageHook implements SendMessageHook {

    @Override
    public String hookName() {
        return getClass().getSimpleName();
    }

    @Override
    public void sendMessageBefore(SendMessageContext sendMessageContext) {
        String tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) {
            return;
        }
        sendMessageContext.getMessage().putUserProperty(HEADER_TENANT_ID, tenantId.toString());
    }

    @Override
    public void sendMessageAfter(SendMessageContext sendMessageContext) {
    }
}
