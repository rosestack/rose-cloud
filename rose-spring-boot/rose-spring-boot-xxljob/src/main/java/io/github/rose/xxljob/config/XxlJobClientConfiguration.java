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
package io.github.rose.xxljob.config;

import io.github.rose.xxljob.core.XxlJobAutoRegister;
import io.github.rose.xxljob.service.JobGroupService;
import io.github.rose.xxljob.service.JobInfoService;
import io.github.rose.xxljob.service.JobLoginService;
import io.github.rose.xxljob.service.impl.JobGroupServiceImpl;
import io.github.rose.xxljob.service.impl.JobInfoServiceImpl;
import io.github.rose.xxljob.service.impl.JobLoginServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnBean(RestTemplate.class)
@EnableConfigurationProperties(XxlJobProperties.class)
@ConditionalOnProperty(prefix = "xxl.job.client", name = "enabled", havingValue = "true")
public class XxlJobClientConfiguration {
    private final RestTemplate restTemplate;
    private final XxlJobProperties xxlJobProperties;

    @Bean
    public JobLoginService jobLoginService() {
        return new JobLoginServiceImpl(restTemplate, xxlJobProperties);
    }

    @Bean
    public JobGroupService jobGroupService() {
        return new JobGroupServiceImpl(
            jobLoginService(), restTemplate, xxlJobProperties.getAdmin().getAddresses());
    }

    @Bean
    public JobInfoService jobInfoService() {
        return new JobInfoServiceImpl(jobLoginService(), restTemplate, xxlJobProperties);
    }

    @Bean
    public XxlJobAutoRegister xxlJobAutoRegister() {
        log.info("Initializing XxlJobAutoRegister");

        return new XxlJobAutoRegister(jobGroupService(), jobInfoService(), xxlJobProperties);
    }
}
