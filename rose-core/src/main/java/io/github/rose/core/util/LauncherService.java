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

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface LauncherService extends Ordered, Comparable<LauncherService> {

    void initialize(ConfigurableApplicationContext applicationContext);

    /**
     * 获取排列顺序
     *
     * @return order
     */
    @Override
    default int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * 对比排序
     *
     * @param o LauncherService
     * @return compare
     */
    @Override
    default int compareTo(LauncherService o) {
        return Integer.compare(this.getOrder(), o.getOrder());
    }
}
