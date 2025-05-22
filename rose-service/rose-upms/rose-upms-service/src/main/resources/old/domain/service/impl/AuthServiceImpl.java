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
package old.domain.service.impl;

import cc.chensoul.rose.upms.old.domain.service.AuthService;
import cc.chensoul.rose.upms.old.domain.service.SmsService;
import cc.chensoul.rose.upms.old.domain.service.SystemSettingService;
import io.github.rose.core.validation.Validators;
import io.github.rose.upms.contact.domain.UserService;
import io.github.rose.upms.domain.account.Credential;
import io.github.rose.upms.domain.contact.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static cc.chensoul.rose.upms.old.domain.service.impl.UserServiceImpl.ACTIVATE_URL_PATTERN;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final SystemSettingService systemSettingService;

    private final SmsService smsService;

    @Override
    public void sendActivationSms(String phone) {
        User user = userService.findUserByPhone(phone);
        Credential userCredential = Validators.checkNotNull(userService.findUserCredentialByUserId(user.getId()));
        if (!userCredential.getStatus() && userCredential.getActivateToken() != null) {
            String baseUrl = systemSettingService.getBaseUrl();
            String activateUrl = String.format(ACTIVATE_URL_PATTERN, baseUrl, userCredential.getActivateToken());
            smsService.sendActivationSms(activateUrl, user.getm());
        } else {
            throw new BusinessException("用户已经激活！");
        }
    }

    @Override
    public String getActivationLink(Long userId) {
        Credential userCredential = Validators.checkNotNull(userService.findUserCredentialByUserId(userId));
        if (!userCredential.getStatus() && userCredential.getActivateToken() != null) {
            String baseUrl = systemSettingService.getBaseUrl();
            return String.format(ACTIVATE_URL_PATTERN, baseUrl, userCredential.getActivateToken());
        } else {
            throw new BusinessException("用户已经激活！");
        }
    }
}
