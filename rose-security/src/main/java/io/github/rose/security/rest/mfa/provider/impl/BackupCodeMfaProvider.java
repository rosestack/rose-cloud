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

import io.github.rose.security.rest.mfa.config.BackupCodeMfaConfig;
import io.github.rose.security.rest.mfa.provider.BackupCodeMfaProviderConfig;
import io.github.rose.security.rest.mfa.provider.MfaProvider;
import io.github.rose.security.rest.mfa.provider.MfaProviderType;
import io.github.rose.security.util.SecurityUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BackupCodeMfaProvider implements MfaProvider<BackupCodeMfaProviderConfig, BackupCodeMfaConfig> {

    private static String generateCodes(int count, int length) {
        return Stream.generate(() -> RandomStringUtils.random(length, "0123456789abcdef"))
            .distinct()
            .limit(count)
            .collect(Collectors.joining(","));
    }

    @Override
    public BackupCodeMfaConfig generateTwoFaConfig(User user, BackupCodeMfaProviderConfig providerConfig) {
        BackupCodeMfaConfig config = new BackupCodeMfaConfig();
        config.setCodes(generateCodes(providerConfig.getCodesQuantity(), 8));
        config.setSerializeHiddenFields(true);
        return config;
    }

    @Override
    public boolean checkVerificationCode(
        SecurityUser user,
        String code,
        BackupCodeMfaProviderConfig providerConfig,
        BackupCodeMfaConfig accountConfig) {
        if (CollectionUtils.contains(accountConfig.getCodesForJson().iterator(), code)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public MfaProviderType getType() {
        return MfaProviderType.BACKUP_CODE;
    }
}
