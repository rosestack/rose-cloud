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
package io.github.rose.mybatis.mq.kafka;

import io.github.rose.mybatis.tenant.util.TenantContextHolder;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Headers;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;

import java.util.Map;

import static io.github.rose.core.CommonConstants.HEADER_TENANT_ID;

/**
 * Kafka 消息队列的多租户 {@link ProducerInterceptor} 实现类
 * <p>
 * 1. Producer 发送消息时，将 {@link TenantContextHolder} 租户编号，添加到消息的 Header 中 2. Consumer
 * 消费消息时，将消息的 Header 的租户编号，添加到 {@link TenantContextHolder} 中，通过
 * {@link InvocableHandlerMethod} 实现
 */
public class TenantKafkaProducerInterceptor implements ProducerInterceptor<Object, Object> {

    @Override
    public ProducerRecord<Object, Object> onSend(ProducerRecord<Object, Object> record) {
        String tenantId = TenantContextHolder.getTenantId();
        if (tenantId != null) {
            Headers headers = record.headers(); // private 属性，没有 get 方法，智能反射
            headers.add(HEADER_TENANT_ID, tenantId.toString().getBytes());
        }
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }
}
