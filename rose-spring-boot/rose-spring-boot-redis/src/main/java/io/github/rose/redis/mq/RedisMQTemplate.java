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
package io.github.rose.redis.mq;

import io.github.rose.core.json.JsonUtils;
import io.github.rose.redis.mq.interceptor.RedisMessageInterceptor;
import io.github.rose.redis.mq.message.AbstractRedisMessage;
import io.github.rose.redis.mq.pubsub.AbstractRedisChannelMessage;
import io.github.rose.redis.mq.stream.AbstractRedisStreamMessage;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Redis MQ 操作模板类
 *
 * @author EnjoyIot
 */
public class RedisMQTemplate {

    private final RedisTemplate<String, ?> redisTemplate;
    /**
     * 拦截器数组
     */
    private final List<RedisMessageInterceptor> interceptors = new ArrayList<>();

    public RedisMQTemplate(RedisTemplate<String, ?> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisTemplate<String, ?> getRedisTemplate() {
        return redisTemplate;
    }

    public List<RedisMessageInterceptor> getInterceptors() {
        return interceptors;
    }

    /**
     * 发送 Redis 消息，基于 Redis pub/sub 实现
     *
     * @param message 消息
     */
    public <T extends AbstractRedisChannelMessage> void send(T message) {
        try {
            sendMessageBefore(message);
            // 发送消息
            redisTemplate.convertAndSend(message.getChannel(), JsonUtils.toString(message));
        } finally {
            sendMessageAfter(message);
        }
    }

    /**
     * 发送 Redis 消息，基于 Redis Stream 实现
     *
     * @param message 消息
     * @return 消息记录的编号对象
     */
    public <T extends AbstractRedisStreamMessage> RecordId send(T message) {
        try {
            sendMessageBefore(message);
            // 发送消息
            return redisTemplate
                .opsForStream()
                .add(StreamRecords.newRecord()
                    .ofObject(Objects.requireNonNull(JsonUtils.toString(message))) // 设置内容
                    .withStreamKey(message.getStreamKey())); // 设置 stream key
        } finally {
            sendMessageAfter(message);
        }
    }

    /**
     * 添加拦截器
     *
     * @param interceptor 拦截器
     */
    public void addInterceptor(RedisMessageInterceptor interceptor) {
        interceptors.add(interceptor);
    }

    private void sendMessageBefore(AbstractRedisMessage message) {
        // 正序
        interceptors.forEach(interceptor -> interceptor.sendMessageBefore(message));
    }

    private void sendMessageAfter(AbstractRedisMessage message) {
        // 倒序
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            interceptors.get(i).sendMessageAfter(message);
        }
    }
}
