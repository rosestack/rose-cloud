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
package io.github.rose.monitor.component;

import static java.util.Collections.emptyMap;

import de.codecentric.boot.admin.server.cloud.discovery.DefaultServiceInstanceConverter;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.Configuration;

/**
 * 针对 nacos 2.x 服务注册处理
 */
@Configuration(proxyBeanMethods = false)
public class NacosServiceInstanceConverter extends DefaultServiceInstanceConverter {

    @Override
    protected Map<String, String> getMetadata(ServiceInstance instance) {
        return (instance.getMetadata() != null)
                ? instance.getMetadata().entrySet().stream()
                        .filter((e) -> e.getKey() != null && e.getValue() != null)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                : emptyMap();
    }
}
