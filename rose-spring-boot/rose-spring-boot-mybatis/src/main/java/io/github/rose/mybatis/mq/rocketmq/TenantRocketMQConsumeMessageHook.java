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

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.github.rose.mybatis.tenant.util.TenantContextHolder;
import org.apache.rocketmq.client.hook.ConsumeMessageContext;
import org.apache.rocketmq.client.hook.ConsumeMessageHook;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;

import java.util.List;

import static io.github.rose.core.CommonConstants.HEADER_TENANT_ID;

/**
 * RocketMQ 消息队列的多租户 {@link ConsumeMessageHook} 实现类
 * <p>
 * Consumer 消费消息时，将消息的 Header 的租户编号，添加到 {@link TenantContextHolder} 中，通过
 * {@link InvocableHandlerMethod} 实现
 */
public class TenantRocketMQConsumeMessageHook implements ConsumeMessageHook {

    @Override
    public String hookName() {
        return getClass().getSimpleName();
    }

    @Override
    public void consumeMessageBefore(ConsumeMessageContext context) {
        // 校验，消息必须是单条，不然设置租户可能不正确
        List<MessageExt> messages = context.getMsgList();
        Assert.isTrue(messages.size() == 1, "消息条数({})不正确", messages.size());
        // 设置租户编号
        String tenantId = messages.get(0).getUserProperty(HEADER_TENANT_ID);
        if (StringUtils.isNotEmpty(tenantId)) {
            TenantContextHolder.setTenantId(tenantId);
        }
    }

    @Override
    public void consumeMessageAfter(ConsumeMessageContext context) {
        TenantContextHolder.clear();
    }
}
