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
// import ch.qos.logback.classic.PatternLayout;
// import com.github.loki4j.logback.AbstractLoki4jEncoder;
// import com.github.loki4j.logback.ApacheHttpSender;
// import com.github.loki4j.logback.JsonEncoder;
// import com.github.loki4j.logback.Loki4jAppender;
// import org.apache.commons.lang3.StringUtils;
// import org.slf4j.LoggerFactory;
// import org.springframework.boot.config.condition.ConditionalOnClass;
// import org.springframework.boot.context.properties.EnableConfigurationProperties;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.support.env.Environment;
//
// @Configuration
// @ConditionalOnClass(Loki4jAppender.class)
// @EnableConfigurationProperties(Logging.class)
// public class LokiConfig {
//
// public LokiConfig(Logging properties, Environment environment) {
// LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
// Logging.Loki loki = properties.getLoki();
// if (loki.getUrl() != null) {
// addLokiAppender(context, loki, environment);
// }
// }
//
// public void addLokiAppender(
// LoggerContext context, Logging.Loki lokiProperties, Environment environment) {
// log.info("Add Loki4jAppender");
//
// Loki4jAppender loki4jAppender = new Loki4jAppender();
// loki4jAppender.setContext(context);
//
// AbstractLoki4jEncoder loki4jEncoder = getLoki4jEncoder(lokiProperties, environment);
// loki4jAppender.setFormat(loki4jEncoder);
//
// ApacheHttpSender apacheHttpSender = new ApacheHttpSender();
// apacheHttpSender.setUrl(lokiProperties.getUrl());
// loki4jAppender.setHttp(apacheHttpSender);
// loki4jAppender.start();
//
// context.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME).addAppender(loki4jAppender);
// }
//
// private AbstractLoki4jEncoder getLoki4jEncoder(
// Logging.Loki lokiProperties, Environment environment) {
// AbstractLoki4jEncoder loki4jEncoder = new JsonEncoder();
// AbstractLoki4jEncoder.LabelCfg labelCfg = new AbstractLoki4jEncoder.LabelCfg();
// if (StringUtils.isNotBlank(lokiProperties.getLabelPattern())) {
// labelCfg.setPattern(lokiProperties
// .getLabelPattern()
// .replace("${appName}", environment.getProperty("spring.application.name")));
// }
// loki4jEncoder.setLabel(labelCfg);
//
// if (StringUtils.isNotBlank(lokiProperties.getMessagePattern())) {
// PatternLayout patternLayout = new PatternLayout();
// patternLayout.setPattern(lokiProperties.getMessagePattern());
// loki4jEncoder.setMessage(patternLayout);
// }
//
// loki4jEncoder.setSortByTime(true);
// return loki4jEncoder;
// }
// }
