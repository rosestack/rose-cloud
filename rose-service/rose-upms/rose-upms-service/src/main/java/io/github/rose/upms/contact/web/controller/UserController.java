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
package io.github.rose.upms.contact.web.controller;

import static io.github.rose.upms.Constants.TENANT_ID;
import static io.github.rose.upms.Constants.USER_ID;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.rose.core.util.RestResponse;
import io.github.rose.security.util.TokenPair;
import io.github.rose.syslog.annotation.SysLog;
import io.github.rose.upms.contact.domain.UserService;
import io.github.rose.upms.domain.contact.User;
import io.github.rose.upms.model.UserRegisterRequest;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 注册用户
     *
     * @param userRegisterRequest 用户
     * @return 添加的用户
     */
    @SysLog("注册用户")
    @PostMapping("/register")
    public RestResponse<User> registerUser(@RequestBody UserRegisterRequest userRegisterRequest) {
        return RestResponse.ok(userService.registerUser(userRegisterRequest, true));
    }

    @SysLog("删除用户")
    @DeleteMapping(value = "/{userId}")
    public RestResponse<Void> deleteUser(@PathVariable(USER_ID) String userId) throws Exception {
        userService.deleteUser(userService.getById(userId));
        return RestResponse.ok();
    }

    @PostMapping("/{userId}/userCredential/enabled")
    public RestResponse<User> setUserCredentialEnabled(
            @PathVariable(USER_ID) Long userId,
            @RequestParam(required = false, defaultValue = "true") boolean userCredentialEnabled)
            throws Exception {
        return RestResponse.ok(userService.setUserCredentialEnabled(userId, userCredentialEnabled));
    }

    @GetMapping("/list")
    public RestResponse<List<User>> listUsers() {
        return RestResponse.ok(userService.list());
    }

    @GetMapping("/{userId}")
    public RestResponse<User> findUserById(@PathVariable(USER_ID) Long userId) {
        return RestResponse.ok(userService.getById(userId));
    }

    // @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @GetMapping("/{userId}/token")
    public RestResponse<TokenPair> getUserToken(@PathVariable(USER_ID) Long userId) {
        return RestResponse.ok(userService.getUserToken(userId));
    }

    // @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @GetMapping(value = "/tenant/{tenantId}/users")
    public Page<User> findUsersByTenantId(Page page, @PathVariable(TENANT_ID) String tenantId) {
        return userService.findUserByTenantId(page, tenantId);
    }
}
