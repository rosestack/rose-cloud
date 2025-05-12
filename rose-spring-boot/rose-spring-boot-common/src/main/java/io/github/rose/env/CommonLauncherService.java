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
package io.github.rose.env;

import io.github.rose.core.spi.LauncherService;
import io.github.rose.core.util.PropsUtil;
import io.github.rose.processor.AutoService;
import java.util.Properties;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 启动参数拓展
 *
 * @author smallchil
 */
@AutoService(LauncherService.class)
public class CommonLauncherService implements LauncherService {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Properties props = System.getProperties();
        PropsUtil.setProperty(props, "spring.main.allow-bean-definition-overriding", "true");
    }
}
