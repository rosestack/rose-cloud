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
package io.github.rose.env;

import io.github.rose.core.util.LauncherService;
import io.github.rose.core.util.ServiceLoaders;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Comparator;
import java.util.stream.Collectors;

public class DefaultApplicationContextInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ServiceLoaders.load(LauncherService.class).stream()
            .sorted(Comparator.comparing(LauncherService::getOrder))
            .collect(Collectors.toList())
            .forEach(launcherService -> launcherService.initialize(applicationContext.getEnvironment()));
    }
}
