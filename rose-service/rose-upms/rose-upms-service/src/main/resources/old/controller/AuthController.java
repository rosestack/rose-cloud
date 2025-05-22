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
package old.controller;

import cc.chensoul.rose.upms.old.domain.service.AuthService;
import io.github.rose.core.util.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static io.github.rose.upms.Constants.USER_ID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @PostMapping(value = "/auth/sendActivationSms")
    public RestResponse<Void> sendActivationSms(@RequestParam(value = "phone") String phone) {
        authService.sendActivationSms(phone);
        return RestResponse.ok();
    }

    // @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @GetMapping(value = "/auth/{userId}/activationLink", produces = "text/plain")
    public String getActivationLink(@PathVariable(USER_ID) Long userId) {
        return authService.getActivationLink(userId);
    }
}
