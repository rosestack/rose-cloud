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
// package cc.chensoul.web.logging;
//
// import ch.qos.logback.classic.LoggerContext;
// import com.fasterxml.jackson.support.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import net.logstash.logback.appender.LogstashTcpSocketAppender;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.ObjectProvider;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.config.condition.ConditionalOnClass;
// import org.springframework.boot.info.BuildProperties;
// import org.springframework.context.annotation.Configuration;
//
// import java.util.HashMap;
// import java.util.Map;
//
/// **
// * Copy from jhipster
// */
// @Configuration
// @ConditionalOnClass({LogstashTcpSocketAppender.class, ObjectMapper.class})
// public class LogstashConfig {
// @Value("${spring.application.name}")
// String appName;
//
// @Value("${server.port}")
// String serverPort;
//
// public LogstashConfig(
// Logging logging, ObjectProvider<BuildProperties> buildProperties, ObjectMapper
// mybatis-mapper)
// throws JsonProcessingException {
// Map<String, String> map = new HashMap<>();
// map.put("app_name", appName);
// map.put("app_port", serverPort);
// buildProperties.ifAvailable(it -> map.put("version", it.getVersion()));
// String customFields = mybatis-mapper.writeValueAsString(map);
//
// LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
// if (logging.isUseJsonFormat()) {
// LogstashUtils.addJsonConsoleAppender(context, customFields);
// }
//
// Logging.Logstash logstashProperties =
// logging.getLogstash();
// if (logstashProperties.isEnabled()) {
// LogstashUtils.addLogstashTcpSocketAppender(context, customFields, logstashProperties);
// }
// if (logging.isUseJsonFormat() || logstashProperties.isEnabled()) {
// LogstashUtils.addContextListener(context, customFields, logging);
// }
// }
// }
