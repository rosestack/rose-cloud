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
package io.github.rose.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Slf4j
@Configuration
@ConditionalOnClass(RetryOperationsInterceptor.class)
public class RetryConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "configServerRetryInterceptor")
    public RetryOperationsInterceptor configServerRetryInterceptor() {
        log.info("Changing backOffOptions  to initial: {}, multiplier: {}, maxInterval: {}", 1000, 1.2, 5000);
        return RetryInterceptorBuilder.stateless()
            .backOffOptions(1000, 1.2, 5000)
            .maxAttempts(3)
            .build();
    }
}
