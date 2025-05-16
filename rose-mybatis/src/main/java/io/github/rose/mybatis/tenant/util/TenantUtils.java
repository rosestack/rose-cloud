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

import io.github.rose.core.function.CheckedRunnable;
import io.github.rose.core.function.CheckedSupplier;

public class TenantUtils {

    /**
     * 使用指定租户，执行对应的逻辑
     * <p>
     * 注意，如果当前是忽略租户的情况下，会被强制设置成不忽略租户 当然，执行完成后，还是会恢复回去
     *
     * @param tenantId 租户编号
     * @param runnable 逻辑
     */
    public static void execute(String tenantId, CheckedRunnable runnable) {
        String oldTenantId = TenantContextHolder.getTenantId();
        Boolean oldIgnore = TenantContextHolder.isIgnored();
        try {
            TenantContextHolder.setTenantId(tenantId);
            TenantContextHolder.setIgnore(Boolean.FALSE);
            runnable.run();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            TenantContextHolder.setTenantId(oldTenantId);
            TenantContextHolder.setIgnore(oldIgnore);
        }
    }

    public static <V> V execute(String tenantId, CheckedSupplier<V> supplier) {
        String oldTenantId = TenantContextHolder.getTenantId();
        Boolean oldIgnore = TenantContextHolder.isIgnored();
        try {
            TenantContextHolder.setTenantId(tenantId);
            TenantContextHolder.setIgnore(Boolean.FALSE);
            return supplier.get();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            TenantContextHolder.setTenantId(oldTenantId);
            TenantContextHolder.setIgnore(oldIgnore);
        }
    }

    /**
     * 忽略租户，执行对应的逻辑
     *
     * @param runnable 逻辑
     */
    public static void executeIgnore(CheckedRunnable runnable) {
        Boolean oldIgnore = TenantContextHolder.isIgnored();
        try {
            TenantContextHolder.setIgnore(Boolean.TRUE);
            runnable.run();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            TenantContextHolder.setIgnore(oldIgnore);
        }
    }

    public static <V> V executeIgnore(CheckedSupplier<V> supplier) {
        Boolean oldIgnore = TenantContextHolder.isIgnored();
        try {
            TenantContextHolder.setIgnore(Boolean.TRUE);
            return supplier.get();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            TenantContextHolder.setIgnore(oldIgnore);
        }
    }
}
