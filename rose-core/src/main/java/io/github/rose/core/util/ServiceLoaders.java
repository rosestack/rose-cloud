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
package io.github.rose.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * ServiceLoaders is a {@link ServiceLoader} replacement which understands multiple classpaths.
 *
 * @author Tristan Tarrant
 * @author Brett Meyer
 * @since 6.0
 */
public class ServiceLoaders {
    private static final Logger log = LoggerFactory.getLogger(ServiceLoaders.class);

    public static <T> Collection<T> load(Class<T> contract) {
        return load(contract, ClassLoaders.findMostCompleteClassLoader());
    }

    public static <T> Collection<T> load(Class<T> contract, ClassLoader... loaders) {
        List<T> services = new ArrayList<>();

        if (loaders.length == 0) {
            try {
                ServiceLoader<T> loadedServices = ServiceLoader.load(contract);
                addServices(loadedServices, services);
            } catch (Exception e) {
                // Ignore
            }
        }

        for (ClassLoader loader : loaders) {
            if (loader == null) {
                throw new NullPointerException();
            }
            try {
                ServiceLoader<T> loadedServices = ServiceLoader.load(contract, loader);
                addServices(loadedServices, services);
            } catch (Exception e) {
                // Ignore
            }
        }

        if (services.isEmpty()) {
            log.info("No service impls found: {}", contract.getSimpleName());
        }

        PriorityUtils.sortByObjectPriority(services);
        return services;
    }

    private static <T> void addServices(ServiceLoader<T> loadedServices, List<T> services) {
        Iterator<T> i = loadedServices.iterator();
        while (i.hasNext()) {
            T service = i.next();
            if (!services.contains(service.getClass().getName())) {
                services.add(service);
                log.info("Loading service impl: {}", service.getClass().getName());
            } else {
                log.info("Ignoring already loaded service: {}", service.getClass().getName());
            }
        }
    }
}
