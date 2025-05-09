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
package io.github.rose.mybatis.tenant.handler;

import io.github.rose.mybatis.extension.interceptor.DefaultMetaObjectHandler;
import io.github.rose.mybatis.tenant.util.TenantContextHolder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public class TenantMetaObjectHandler extends DefaultMetaObjectHandler {

    private final String tenantFiledName;

    @Override
    public void insertFill(MetaObject metaObject) {
        super.insertFill(metaObject);
        fillValIfNullByName(tenantFiledName, TenantContextHolder.getTenantId(), metaObject, false);
    }
}
