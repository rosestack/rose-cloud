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
package io.github.rose.core.util;

import io.github.rose.processor.AutoService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.annotation.OrderUtils;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
class ServiceLoadersTest {
    private static final Logger log = LoggerFactory.getLogger(ServiceLoadersTest.class);

    @Test
    void load() {
        Collection<Service> launcherServices = ServiceLoaders.load(Service.class);
        launcherServices.forEach(service -> log.info(service.getClass().getName()));

        Map<? extends Class<? extends Service>, Service> serviceMap = launcherServices.stream().collect(Collectors.toMap(Service::getClass, a -> a));
        serviceMap.forEach((aClass, service) -> log.info("{} {}", aClass.getName(), PriorityUtils.priorityOfObject(service)));
    }

    @Test
    void testLoad() {
    }


    public interface Service {

    }

    @AutoService(Service.class)
    public static class AService implements Service, Ordered {
        public AService() {
        }

        @Override
        public int getOrder() {
            return 10;
        }
    }

    @Order(-10)
    @AutoService(Service.class)
    public static class BService implements Service {
        public BService() {
        }
    }
}
