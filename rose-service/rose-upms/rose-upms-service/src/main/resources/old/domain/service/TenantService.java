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
package old.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.rose.upms.domain.tenant.Tenant;
import io.github.rose.upms.model.AddUserToTenantRequest;

public interface TenantService extends IService<Tenant> {

    UserTenant addUserToTenant(String tenantId, AddUserToTenantRequest request);
}
