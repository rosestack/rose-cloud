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
package io.github.rose.feign.retry;

import feign.RetryableException;
import io.github.rose.feign.annotation.FeignRetry;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * FeignRetry 注解切面注入 retryTemplate
 * <p>
 * {@link org.springframework.cloud.loadbalancer.blocking.retry.BlockingLoadBalancedRetryPolicy}.
 */
@Aspect
public class FeignRetryAspect {
    private static final Logger log = LoggerFactory.getLogger(FeignRetryAspect.class);

    @Around("@annotation(feignRetry)")
    public Object retry(ProceedingJoinPoint joinPoint, FeignRetry feignRetry) throws Throwable {
        Method method = getCurrentMethod(joinPoint);

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(prepareBackOffPolicy(feignRetry));
        retryTemplate.setRetryPolicy(prepareSimpleRetryPolicy(feignRetry));

        // 重试
        return retryTemplate.execute(arg0 -> {
            int retryCount = arg0.getRetryCount();
            log.info(
                    "Sending request method: {}, max attempt: {}, delay: {}, retryCount: {}",
                    method.getName(),
                    feignRetry.maxAttempt(),
                    feignRetry.backoff().delay(),
                    retryCount);
            return joinPoint.proceed(joinPoint.getArgs());
        });
    }

    /**
     * 构造重试策略
     *
     * @param feignRetry 重试注解
     * @return BackOffPolicy
     */
    private BackOffPolicy prepareBackOffPolicy(FeignRetry feignRetry) {
        if (feignRetry.backoff().multiplier() != 0) {
            ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
            backOffPolicy.setInitialInterval(feignRetry.backoff().delay());
            backOffPolicy.setMaxInterval(feignRetry.backoff().maxDelay());
            backOffPolicy.setMultiplier(feignRetry.backoff().multiplier());
            return backOffPolicy;
        } else {
            FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
            fixedBackOffPolicy.setBackOffPeriod(feignRetry.backoff().delay());
            return fixedBackOffPolicy;
        }
    }

    /**
     * 构造重试策略
     *
     * @param feignRetry 重试注解
     * @return SimpleRetryPolicy
     */
    private SimpleRetryPolicy prepareSimpleRetryPolicy(FeignRetry feignRetry) {
        Map<Class<? extends Throwable>, Boolean> policyMap = new HashMap<>();
        policyMap.put(RetryableException.class, true); // Connection refused or time out

        for (Class<? extends Throwable> t : feignRetry.include()) {
            policyMap.put(t, true);
        }

        return new SimpleRetryPolicy(feignRetry.maxAttempt(), policyMap, true);
    }

    private Method getCurrentMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
