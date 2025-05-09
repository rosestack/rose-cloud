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
package old.controller;

import cc.chensoul.rose.upms.old.domain.service.TenantService;
import io.github.rose.core.util.RestResponse;
import io.github.rose.upms.model.AddUserToTenantRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    @PostMapping("/{tenantId}/user")
    public RestResponse<UserTenant> addUserToTenant(
        @PathVariable String tenantId, @Valid @RequestBody AddUserToTenantRequest request) {
        return RestResponse.ok(tenantService.addUserToTenant(tenantId, request));
    }
}
