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

import io.github.rose.boot.util.FilterUtils;
import io.github.rose.core.CommonConstants;
import io.github.rose.core.json.Java8TimeModule;
import io.github.rose.core.util.date.DatePattern;
import io.github.rose.filter.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static io.github.rose.core.CommonConstants.*;
import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnWebApplication(type = SERVLET)
@EnableConfigurationProperties({XssProperties.class})
public class WebMvcConfiguration implements WebMvcConfigurer {
    private static final Logger log = LoggerFactory.getLogger(WebMvcConfiguration.class);

    private final XssProperties xssProperties;

    @Value("${server.http.max-response-time-to-log-in-ms:2000}")
    private int maxResponseTimeToLogInMs;

    @Value("${server.http.max_payload_size:/api/image*/**=52428800;/api/resource/**=52428800;/api/**=16777216}")
    private String maxPayloadSizeConfig;

    public WebMvcConfiguration(XssProperties xssProperties) {
        this.xssProperties = xssProperties;
    }

    /**
     * 增加GET请求参数中时间类型转换 {@link Java8TimeModule}
     * <ul>
     * <li>HH:mm:ss -> LocalTime</li>
     * <li>yyyy-MM-dd -> LocalDate</li>
     * <li>yyyy-MM-dd HH:mm:ss -> LocalDateTime</li>
     * </ul>
     *
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setTimeFormatter(DatePattern.NORM_TIME_FORMATTER);
        registrar.setDateFormatter(DatePattern.NORM_DATE_FORMATTER);
        registrar.setDateTimeFormatter(DatePattern.NORM_DATETIME_FORMATTER);
        registrar.registerFormatters(registry);
    }

    /**
     * 系统国际化文件配置
     *
     * @return MessageSource
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        return messageSource;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("**")
            .allowedHeaders("*")
            .allowedMethods("*")
            .maxAge(86400);
    }

    @Bean
    public CommonsRequestLoggingFilter commonsRequestLoggingFilter() {
        final CommonsRequestLoggingFilter filter = new CustomRequestLoggingFilter(maxResponseTimeToLogInMs);
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(1000);
        filter.setIncludeHeaders(true);
        filter.setIncludeClientInfo(true);
        return filter;
    }

    @Bean
    public FilterRegistrationBean<TraceFilter> traceFilter() {
        return FilterUtils.createFilterBean(new TraceFilter(), TRACE_FILTER);
    }

    @Bean
    public FilterRegistrationBean<CachingRequestFilter> cachingRequestFilter() {
        return FilterUtils.createFilterBean(new CachingRequestFilter(), CACHING_REQUEST_FILTER);
    }

    @Bean
    @ConditionalOnProperty(value = CommonConstants.PROJECT_NAME + ".xss.enabled", havingValue = "true")
    public FilterRegistrationBean<XssFilter> xxsFilter() {
        return FilterUtils.createFilterBean(new XssFilter(xssProperties.getExcludeUrls()), XSS_FILTER);
    }

    @Bean
    protected PayloadSizeFilter payloadSizeFilter() {
        return new PayloadSizeFilter(maxPayloadSizeConfig);
    }
}
