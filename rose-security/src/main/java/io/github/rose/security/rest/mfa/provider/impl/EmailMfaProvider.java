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
package io.github.rose.security.rest.mfa.provider.impl;

import io.github.rose.security.rest.mfa.config.EmailMfaConfig;
import io.github.rose.security.rest.mfa.provider.EmailMfaProviderConfig;
import io.github.rose.security.rest.mfa.provider.MfaProviderType;
import io.github.rose.security.util.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class EmailMfaProvider extends OtpBasedMfaProvider<EmailMfaProviderConfig, EmailMfaConfig> {
    private static final Logger log = LoggerFactory.getLogger(EmailMfaProvider.class);

    // private final MailService mailService;

    protected EmailMfaProvider(CacheManager cacheManager) {
        super(cacheManager);
        // this.mailService = mailService;
    }

    @Override
    public EmailMfaConfig generateTwoFaConfig(User user, EmailMfaProviderConfig providerConfig) {
        EmailMfaConfig config = new EmailMfaConfig();
        // config.setEmail(user.getEmail());
        return config;
    }

    @Override
    public void check(String tenantId) {
        // try {
        // mailService.testConnection(tenantId);
        // } catch (Exception e) {
        // throw new RuntimeException("Mail util is not set up");
        // }
    }

    @Override
    protected void sendVerificationCode(
            SecurityUser user,
            String verificationCode,
            EmailMfaProviderConfig providerConfig,
            EmailMfaConfig twoFaConfig) {
        log.info("send verification code {} to email {}", verificationCode, twoFaConfig.getEmail());
        // mailService.sendTwoFaVerificationEmail(twoFaConfig.getEmail(),
        // verificationCode, providerConfig.getVerificationCodeLifetime());
    }

    @Override
    public MfaProviderType getType() {
        return MfaProviderType.EMAIL;
    }
}
