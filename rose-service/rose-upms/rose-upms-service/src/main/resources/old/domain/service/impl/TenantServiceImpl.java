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
package old.domain.service.impl;

import cc.chensoul.rose.upms.old.domain.mapper.TenantMapper;
import cc.chensoul.rose.upms.old.domain.model.event.SaveEntityEvent;
import cc.chensoul.rose.upms.old.domain.service.TenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.rose.core.spring.SpringContextHolder;
import io.github.rose.upms.contact.domain.UserService;
import io.github.rose.upms.domain.contact.User;
import io.github.rose.upms.domain.tenant.Tenant;
import io.github.rose.upms.model.AddUserToTenantRequest;
import io.github.rose.upms.model.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {

    private final UserService userService;

    private final UserTenantMapper userTenantMapper;

    @Override
    public UserTenant addUserToTenant(String tenantId, AddUserToTenantRequest request) {
        User user = userService.getById(request.getUserId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        Tenant tenant = getById(tenantId);
        if (tenant == null) {
            throw new BusinessException("租户不存在");
        }

        UserTenant userTenant = new UserTenant();
        userTenant.setUserId(request.getUserId());
        userTenant.setTenantId(tenantId);
        userTenant.setStatus(UserStatus.PENDING.getCode());
        userTenantMapper.insert(userTenant);

        SpringContextHolder.publishEvent(SaveEntityEvent.builder()
            .entity(userTenant)
            .oldEntity(null)
            .created(true)
            .build());

        return userTenant;
    }
}
