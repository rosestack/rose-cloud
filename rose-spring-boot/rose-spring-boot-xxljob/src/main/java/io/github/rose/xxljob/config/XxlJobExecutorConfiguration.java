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

import com.alibaba.ttl.TtlRunnable;
import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import io.github.rose.core.exception.BusinessException;
import io.github.rose.core.spring.SpringContextHolder;
import io.github.rose.core.util.StringPool;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Locale;
import java.util.stream.Collectors;

@Configuration
@EnableAsync
@ConditionalOnClass(XxlJobSpringExecutor.class)
@EnableConfigurationProperties(XxlJobProperties.class)
public class XxlJobExecutorConfiguration {
    private static final Logger log = LoggerFactory.getLogger(XxlJobExecutorConfiguration.class);

    private static final String XXL_JOB_ADMIN = "rose-xxljob";
    private final ObjectProvider<DiscoveryClient> discoveryClientObjectProvider;

    public XxlJobExecutorConfiguration(ObjectProvider<DiscoveryClient> discoveryClientObjectProvider) {
        this.discoveryClientObjectProvider = discoveryClientObjectProvider;
    }

    private static String getServiceUrl(ServiceInstance instance) {
        return String.format(Locale.getDefault(),
            "%s://%s:%s", instance.isSecure() ? "https" : "http", instance.getHost(), instance.getPort());
    }

    @Bean
    public BeanPostProcessor threadPoolTaskExecutorBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (!(bean instanceof ThreadPoolTaskExecutor)) {
                    return bean;
                }
                // 修改提交的任务，接入 TransmittableThreadLocal
                ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) bean;
                executor.setTaskDecorator(TtlRunnable::get);
                return executor;
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "xxl.job.executor", name = "enabled", havingValue = "true", matchIfMissing = true)
    public XxlJobExecutor xxlJobExecutor(XxlJobProperties properties) {
        log.info("Initializing XxlJobExecutor");

        XxlJobProperties.ExecutorProperties executor = properties.getExecutor();
        XxlJobExecutor xxlJobExecutor = new XxlJobSpringExecutor();
        xxlJobExecutor.setIp(executor.getIp());
        xxlJobExecutor.setPort(executor.getPort());
        xxlJobExecutor.setLogRetentionDays(executor.getLogRetentionDays());
        xxlJobExecutor.setAccessToken(properties.getAccessToken());
        xxlJobExecutor.setAppname(SpringContextHolder.getApplicationName());
        xxlJobExecutor.setLogPath(executor.getLogPath());

        if (discoveryClientObjectProvider.getIfAvailable() != null) {
            DiscoveryClient discoveryClient = discoveryClientObjectProvider.getObject();
            String serverList = discoveryClient.getServices().stream()
                .filter(s -> s.contains(XXL_JOB_ADMIN))
                .flatMap(s -> discoveryClient.getInstances(s).stream())
                .map(XxlJobExecutorConfiguration::getServiceUrl)
                .collect(Collectors.joining(StringPool.COMMA));
            xxlJobExecutor.setAdminAddresses(serverList);
        } else {
            if (StringUtils.isBlank(properties.getAdmin().getAddresses())) {
                throw new BusinessException("调度器地址不能为空");
            }
            xxlJobExecutor.setAdminAddresses(properties.getAdmin().getAddresses());
        }
        return xxlJobExecutor;
    }
}
