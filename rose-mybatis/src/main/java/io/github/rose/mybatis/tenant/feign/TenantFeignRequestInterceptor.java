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
package io.github.rose.mybatis.tenant.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.github.rose.mybatis.tenant.util.TenantContextHolder;
import org.apache.commons.lang3.StringUtils;

import static io.github.rose.core.util.Constants.HEADER_TENANT_ID;

public class TenantFeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String tenantId = TenantContextHolder.getTenantId();
        if (StringUtils.isNotBlank(tenantId)) {
            requestTemplate.header(HEADER_TENANT_ID, tenantId);
        }
    }
}
