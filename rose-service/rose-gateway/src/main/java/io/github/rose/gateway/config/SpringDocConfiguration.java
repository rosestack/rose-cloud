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
package io.github.rose.gateway.config;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.alibaba.nacos.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * swagger 3.0 展示
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "springdoc.api-docs.enabled", matchIfMissing = true)
public class SpringDocConfiguration implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(SpringDocConfiguration.class);

    private final SwaggerUiConfigProperties swaggerUiConfigProperties;

    private final DiscoveryClient discoveryClient;

    public SpringDocConfiguration(
        SwaggerUiConfigProperties swaggerUiConfigProperties, DiscoveryClient discoveryClient) {
        this.swaggerUiConfigProperties = swaggerUiConfigProperties;
        this.discoveryClient = discoveryClient;
    }

    /**
     * 在初始化后调用的方法，用于注册SwaggerDocRegister订阅器
     */
    @Override
    public void afterPropertiesSet() {
        SwaggerDocRegister swaggerDocRegister = new SwaggerDocRegister(swaggerUiConfigProperties, discoveryClient);
        // 手动调用一次，避免监听事件掉线问题
        swaggerDocRegister.onEvent(null);
        NotifyCenter.registerSubscriber(swaggerDocRegister);
    }
}

/**
 * Swagger文档注册器，继承自Subscriber<InstancesChangeEvent>
 */
class SwaggerDocRegister extends Subscriber<InstancesChangeEvent> {

    private final SwaggerUiConfigProperties swaggerUiConfigProperties;

    private final DiscoveryClient discoveryClient;

    SwaggerDocRegister(SwaggerUiConfigProperties swaggerUiConfigProperties, DiscoveryClient discoveryClient) {
        this.swaggerUiConfigProperties = swaggerUiConfigProperties;
        this.discoveryClient = discoveryClient;
    }

    /**
     * 事件回调方法，处理InstancesChangeEvent事件
     *
     * @param event 事件对象
     */
    @Override
    public void onEvent(InstancesChangeEvent event) {
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> swaggerUrlSet = discoveryClient.getServices().stream()
            .flatMap(serviceId -> discoveryClient.getInstances(serviceId).stream())
            .filter(instance ->
                StringUtils.isNotBlank(instance.getMetadata().get("spring-doc")))
            .map(instance -> {
                AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl =
                    new AbstractSwaggerUiConfigProperties.SwaggerUrl();
                swaggerUrl.setName(instance.getServiceId());
                swaggerUrl.setUrl(String.format(Locale.getDefault(),
                    "/%s/v3/api-docs", instance.getMetadata().get("spring-doc")));
                return swaggerUrl;
            })
            .collect(Collectors.toSet());

        swaggerUiConfigProperties.setUrls(swaggerUrlSet);
    }

    /**
     * 订阅类型方法，返回订阅的事件类型
     *
     * @return 订阅的事件类型
     */
    @Override
    public Class<? extends Event> subscribeType() {
        return InstancesChangeEvent.class;
    }
}
