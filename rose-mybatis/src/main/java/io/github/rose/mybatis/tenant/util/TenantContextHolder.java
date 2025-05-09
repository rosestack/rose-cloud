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
package io.github.rose.mybatis.tenant.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import io.github.rose.core.exception.BusinessException;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class TenantContextHolder {

    private final ThreadLocal<String> THREAD_LOCAL_TENANT = new TransmittableThreadLocal<>();

    private final ThreadLocal<Boolean> THREAD_LOCAL_IGNORED = TransmittableThreadLocal.withInitial(() -> false);

    public void setIgnore(Boolean ignored) {
        THREAD_LOCAL_IGNORED.set(ignored);
    }

    public String getTenantId() {
        return THREAD_LOCAL_TENANT.get();
    }

    public void setTenantId(String tenantId) {
        THREAD_LOCAL_TENANT.set(tenantId);
    }

    public String getRequiredTenantId() {
        String tenantId = getTenantId();
        if (StringUtils.isBlank(tenantId)) {
            throw new BusinessException("TenantContextHolder不存在租户");
        }
        return tenantId;
    }

    public Boolean isIgnored() {
        return THREAD_LOCAL_IGNORED.get();
    }

    public void clear() {
        THREAD_LOCAL_TENANT.remove();
        THREAD_LOCAL_IGNORED.remove();
    }
}
