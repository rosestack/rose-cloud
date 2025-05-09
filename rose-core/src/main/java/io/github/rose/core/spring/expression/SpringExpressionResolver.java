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
package io.github.rose.core.spring.expression;

import io.github.rose.core.spring.SpringContextHolder;
import io.github.rose.core.util.Maps;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.time.*;
import java.util.*;
import java.util.function.Function;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class SpringExpressionResolver implements Function<Object, Object> {

    private static final int HOUR_23 = 23;

    private static final int MINUTE_59 = 59;

    private static final int SECOND_59 = 59;

    private static final ParserContext PARSER_CONTEXT = new TemplateParserContext("${", "}");

    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    private static final SpelExpressionParser EXPRESSION_PARSER = new SpelExpressionParser(
        new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, SpringExpressionResolver.class.getClassLoader()));

    private static SpringExpressionResolver INSTANCE;

    private final StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

    protected SpringExpressionResolver() {
        Properties properties = System.getProperties();
        evaluationContext.setVariable("systemProperties", properties);
        evaluationContext.setVariable("sysProps", properties);

        Map<String, String> environment = System.getenv();
        evaluationContext.setVariable("environmentVars", environment);
        evaluationContext.setVariable("environmentVariables", environment);
        evaluationContext.setVariable("envVars", environment);
        evaluationContext.setVariable("env", environment);

        evaluationContext.setVariable("tempDir", System.getProperty("java.io.tmpdir"));
        evaluationContext.setVariable("zoneId", ZoneId.systemDefault().getId());

        evaluationContext.setBeanResolver(new BeanFactoryResolver(SpringContextHolder.getApplicationContext()));
    }

    /**
     * Gets instance of the resolver as a singleton.
     *
     * @return the instance
     */
    public static SpringExpressionResolver getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SpringExpressionResolver();
        }
        INSTANCE.initializeDynamicVariables();
        return INSTANCE;
    }

    /**
     * 从切面中，单个解析 EL 表达式的结果
     *
     * @param joinPoint        切面点
     * @param expressionString EL 表达式数组
     * @return 执行界面
     */
    public static Object parseExpression(JoinPoint joinPoint, String expressionString) {
        Map<String, Object> result = parseExpressions(joinPoint, Collections.singletonList(expressionString));
        return result.get(expressionString);
    }

    /**
     * 从切面中，批量解析 EL 表达式的结果
     *
     * @param joinPoint         切面点
     * @param expressionStrings EL 表达式数组
     * @return 结果，key 为表达式，value 为对应值
     */
    public static Map<String, Object> parseExpressions(JoinPoint joinPoint, List<String> expressionStrings) {
        if (CollectionUtils.isEmpty(expressionStrings)) {
            return Maps.of();
        }

        // 第一步，构建解析的上下文 EvaluationContext
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // 使用 spring 的 ParameterNameDiscoverer 获取方法形参名数组
        String[] paramNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        if (ArrayUtils.isNotEmpty(paramNames)) {
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }

        // 第二步，逐个参数解析
        Map<String, Object> result = new HashMap<>(expressionStrings.size());
        expressionStrings.forEach(key -> {
            Object value = EXPRESSION_PARSER.parseExpression(key).getValue(context);
            result.put(key, value);
        });
        return result;
    }

    /**
     * Resolve string.
     *
     * @param value the value
     * @return the string
     */
    public Object resolve(final String value) {
        if (StringUtils.isNotBlank(value)) {
            log.trace("Parsing expression as [{}]", value);
            Expression expression = EXPRESSION_PARSER.parseExpression(value, PARSER_CONTEXT);
            Object result = expression.getValue(evaluationContext);
            log.trace("Parsed expression result is [{}]", result);
            return result;
        }
        return value;
    }

    @Override
    public Object apply(final Object o) {
        return resolve(o.toString());
    }

    private void initializeDynamicVariables() {
        evaluationContext.setVariable("randomNumber2", RandomStringUtils.randomNumeric(2));
        evaluationContext.setVariable("randomNumber4", RandomStringUtils.randomNumeric(4));
        evaluationContext.setVariable("randomNumber6", RandomStringUtils.randomNumeric(6));
        evaluationContext.setVariable("randomNumber8", RandomStringUtils.randomNumeric(8));

        evaluationContext.setVariable("randomString4", RandomStringUtils.randomAlphabetic(4));
        evaluationContext.setVariable("randomString6", RandomStringUtils.randomAlphabetic(6));
        evaluationContext.setVariable("randomString8", RandomStringUtils.randomAlphabetic(8));

        evaluationContext.setVariable("uuid", UUID.randomUUID().toString());

        evaluationContext.setVariable(
            "localDateTime", LocalDateTime.now(ZoneId.systemDefault()).toString());
        evaluationContext.setVariable(
            "localDateTimeUtc", LocalDateTime.now(Clock.systemUTC()).toString());

        val localStartWorkDay =
            LocalDate.now(ZoneId.systemDefault()).atStartOfDay().plusHours(8);
        evaluationContext.setVariable("localStartWorkDay", localStartWorkDay.toString());
        evaluationContext.setVariable(
            "localEndWorkDay", localStartWorkDay.plusHours(9).toString());

        val localStartDay = LocalDate.now(ZoneId.systemDefault()).atStartOfDay();
        evaluationContext.setVariable("localStartDay", localStartDay.toString());
        evaluationContext.setVariable(
            "localEndDay",
            localStartDay
                .plusHours(HOUR_23)
                .plusMinutes(MINUTE_59)
                .plusSeconds(SECOND_59)
                .toString());

        evaluationContext.setVariable(
            "localDate", LocalDate.now(ZoneId.systemDefault()).toString());
        evaluationContext.setVariable(
            "localDateUtc", LocalDate.now(Clock.systemUTC()).toString());

        evaluationContext.setVariable(
            "zonedDateTime", ZonedDateTime.now(ZoneId.systemDefault()).toString());
        evaluationContext.setVariable(
            "zonedDateTimeUtc", ZonedDateTime.now(Clock.systemUTC()).toString());
    }
}
