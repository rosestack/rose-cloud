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
package io.github.rose.mybatis.datapermission.util;

import io.github.rose.mybatis.datapermission.annotation.DataPermission;
import io.github.rose.mybatis.datapermission.aop.DataPermissionContextHolder;

import java.util.concurrent.Callable;

/**
 * 数据权限 Util
 */
public class DataPermissionUtils {

    private static DataPermission DATA_PERMISSION_DISABLE;

    @DataPermission(enable = false)
    private static DataPermission getDisableDataPermissionDisable() {
        if (DATA_PERMISSION_DISABLE == null) {
            try {
                DATA_PERMISSION_DISABLE = DataPermissionUtils.class
                    .getDeclaredMethod("getDisableDataPermissionDisable")
                    .getAnnotation(DataPermission.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return DATA_PERMISSION_DISABLE;
    }

    /**
     * 忽略数据权限，执行对应的逻辑
     *
     * @param runnable 逻辑
     */
    public static void executeIgnore(Runnable runnable) {
        DataPermission dataPermission = getDisableDataPermissionDisable();
        DataPermissionContextHolder.add(dataPermission);
        try {
            // 执行 runnable
            runnable.run();
        } finally {
            DataPermissionContextHolder.remove();
        }
    }

    /**
     * 忽略数据权限，执行对应的逻辑
     *
     * @param callable 逻辑
     * @return 执行结果
     */
    public static <T> T executeIgnore(Callable<T> callable) {
        DataPermission dataPermission = getDisableDataPermissionDisable();
        DataPermissionContextHolder.add(dataPermission);
        try {
            // 执行 callable
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DataPermissionContextHolder.remove();
        }
    }
}
