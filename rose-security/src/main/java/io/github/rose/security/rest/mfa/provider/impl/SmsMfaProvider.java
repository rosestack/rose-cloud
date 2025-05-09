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
package io.github.rose.security.rest.mfa.provider.impl;

import io.github.rose.security.rest.mfa.config.SmsMfaConfig;
import io.github.rose.security.rest.mfa.provider.MfaProviderType;
import io.github.rose.security.rest.mfa.provider.SmsMfaProviderConfig;
import io.github.rose.security.util.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class SmsMfaProvider extends OtpBasedMfaProvider<SmsMfaProviderConfig, SmsMfaConfig> {
    private static final Logger log = LoggerFactory.getLogger(SmsMfaProvider.class);

    // private final SmsService smsService;

    public SmsMfaProvider(CacheManager cacheManager) {
        super(cacheManager);
        // this.smsService = smsService;
    }

    @Override
    public SmsMfaConfig generateTwoFaConfig(User user, SmsMfaProviderConfig providerConfig) {
        return new SmsMfaConfig();
    }

    @Override
    protected void sendVerificationCode(
        SecurityUser user, String verificationCode, SmsMfaProviderConfig providerConfig, SmsMfaConfig twoFaConfig) {
        log.info("send verification code {} to phoneNumber ", verificationCode, twoFaConfig.getPhoneNumber());

        // Map<String, String> messageData = Map.of(
        // "code", verificationCode,
        // "userEmail", user.getEmail()
        // );
        // String message =
        // FormatUtils.formatVariables(providerConfig.getSmsVerificationMessageTemplate(),
        // "${", "}", messageData);
        // String phoneNumber = twoFaConfig.getPhoneNumber();
        // try {
        // smsService.sendSms(user.getTenantId(), user.getMerchantId(), new
        // String[]{phoneNumber}, message);
        // } catch (RuntimeException e) {
        // throw e;
        // }
    }

    @Override
    public void check(String tenantId) {
        // if (!smsService.isConfigured(tenantId)) {
        // throw new RuntimeException("SMS util is not configured");
        // }
    }

    @Override
    public MfaProviderType getType() {
        return MfaProviderType.SMS;
    }
}
