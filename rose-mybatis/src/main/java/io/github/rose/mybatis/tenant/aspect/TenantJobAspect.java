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
package io.github.rose.mybatis.tenant.aspect;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xxl.job.core.context.XxlJobHelper;
import io.github.rose.core.jackson.JacksonUtils;
import io.github.rose.core.util.FormatUtils;
import io.github.rose.mybatis.tenant.annotation.TenantJob;
import io.github.rose.mybatis.tenant.service.TenantService;
import io.github.rose.mybatis.tenant.util.TenantUtils;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 多租户 JobHandler AOP 任务执行时，会按照租户逐个执行 Job 的逻辑
 * <p>
 * 注意，需要保证 JobHandler 的幂等性。因为 Job 因为某个租户执行失败重试时，之前执行成功的租户也会再次执行。
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
@Aspect
public class TenantJobAspect {

    private final TenantService tenantService;

    public TenantJobAspect(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @Around("@annotation(tenantJob)")
    public String around(ProceedingJoinPoint joinPoint, TenantJob tenantJob) {
        List<String> tenantIds = tenantService.getTenantIds();
        if (CollectionUtils.isEmpty(tenantIds)) {
            return null;
        }

        // 逐个租户，执行 Job
        Map<String, String> results = new ConcurrentHashMap<>();
        tenantIds.parallelStream().forEach(tenantId -> {
            TenantUtils.execute(tenantId, () -> {
                try {
                    joinPoint.proceed();
                } catch (Throwable e) {
                    results.put(tenantId, ExceptionUtils.getRootCauseMessage(e));
                    XxlJobHelper.log(FormatUtils.format(
                            "{}租户执行任务({})，发生异常：{}]",
                            tenantId,
                            joinPoint.getSignature(),
                            ExceptionUtils.getStackTrace(e)));
                }
            });
        });
        // 如果 results 非空，说明发生了异常，标记 XXL-Job 执行失败
        if (CollectionUtils.isNotEmpty(results)) {
            XxlJobHelper.handleFail(JacksonUtils.toString(results));
        }
        return JacksonUtils.toString(results);
    }
}
