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
package io.github.rose.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import io.github.rose.core.spring.WebUtils;
import io.github.rose.mybatis.redis.TenantRedisCacheManager;
import io.github.rose.mybatis.tenant.aspect.TenantIgnoreAspect;
import io.github.rose.mybatis.tenant.aspect.TenantJobAspect;
import io.github.rose.mybatis.tenant.feign.TenantFeignRequestInterceptor;
import io.github.rose.mybatis.tenant.filter.TenantContextFilter;
import io.github.rose.mybatis.tenant.filter.TenantSecurityFilter;
import io.github.rose.mybatis.tenant.handler.DefaultTenantLineHandler;
import io.github.rose.mybatis.tenant.handler.TenantMetaObjectHandler;
import io.github.rose.mybatis.tenant.service.TenantService;
import io.github.rose.mybatis.util.MyBatisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

import static io.github.rose.core.CommonConstants.TENANT_CONTEXT_FILTER;
import static io.github.rose.core.CommonConstants.TENANT_SECURITY_FILTER;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Configuration
@AutoConfigureBefore({MybatisCoreConfiguration.class})
@EnableConfigurationProperties({TenantProperties.class})
@ConditionalOnProperty(name = "mybatis-plus.tenant.enabled", havingValue = "true", matchIfMissing = true)
public class MybatisTenantConfiguration {
    private static final Logger log = LoggerFactory.getLogger(MybatisTenantConfiguration.class);
    
    public MybatisTenantConfiguration() {
        log.info("Initializing MybatisTenantConfiguration");
    }

    @Bean
    public TenantIgnoreAspect tenantIgnoreAspect() {
        return new TenantIgnoreAspect();
    }

    @Bean
    @ConditionalOnBean(TenantService.class)
    @ConditionalOnClass(name = "com.xxl.job.core.context.XxlJobHelper")
    public TenantJobAspect tenantJobAspect(TenantService tenantService) {
        return new TenantJobAspect(tenantService);
    }

    @Bean
    public TenantFeignRequestInterceptor feignTenantInterceptor() {
        return new TenantFeignRequestInterceptor();
    }

    @Bean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(
        MybatisPlusInterceptor interceptor, TenantProperties tenantProperties) {
        DefaultTenantLineHandler defaultTenantLineHandler =
            new DefaultTenantLineHandler(tenantProperties.getIgnoredTables());
        TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor(defaultTenantLineHandler);
        MyBatisUtils.addInterceptor(interceptor, tenantInterceptor, 0);
        return tenantInterceptor;
    }

    @Bean
    public FilterRegistrationBean<TenantContextFilter> tenantContextFilter() {
        return WebUtils.createFilterBean(new TenantContextFilter(), TENANT_CONTEXT_FILTER);
    }

    @Bean
    public FilterRegistrationBean<TenantSecurityFilter> tenantSecurityFilter(TenantProperties tenantProperties) {
        return WebUtils.createFilterBean(
            new TenantSecurityFilter(tenantProperties.getIgnoreUrls()), TENANT_SECURITY_FILTER);
    }

    @Bean
    public MetaObjectHandler metaObjectHandler(TenantProperties tenantProperties) {
        return new TenantMetaObjectHandler(StringUtils.underlineToCamel(tenantProperties.getTenantIdColumn()));
    }

    @Bean
    @Primary
    @ConditionalOnClass(RedisCacheManager.class)
    public RedisCacheManager redisCacheManager(
        RedisTemplate<String, Object> redisTemplate,
        RedisCacheConfiguration redisCacheConfiguration,
        TenantProperties tenantProperties) {
        RedisConnectionFactory connectionFactory = Objects.requireNonNull(redisTemplate.getConnectionFactory());
        RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        return new TenantRedisCacheManager(tenantProperties.getIgnoredCaches(), cacheWriter, redisCacheConfiguration);
    }
}
