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

import io.github.rose.mybatis.tenant.annotation.TenantIgnore;
import io.github.rose.mybatis.tenant.util.TenantUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 忽略多租户的 Aspect，基于 {@link TenantIgnore} 注解实现，用于一些全局的逻辑。 例如说，一个定时任务，读取所有数据，进行处理。
 * 又例如说，读取所有数据，进行缓存。
 * <p>
 * 整体逻辑的实现，和 {@link TenantUtils#executeIgnore(CheckedRunnable)} 需要保持一致
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
@Aspect
public class TenantIgnoreAspect {
    private static final Logger log = LoggerFactory.getLogger(TenantIgnoreAspect.class);

    @Around("@annotation(tenantIgnore)")
    public Object around(ProceedingJoinPoint joinPoint, TenantIgnore tenantIgnore) throws Throwable {
        return TenantUtils.executeIgnore(() -> joinPoint.proceed());
    }
}
