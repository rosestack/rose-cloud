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
package io.github.rose.security.rest.mfa;

import io.github.rose.security.SecurityProperties;
import io.github.rose.security.rest.mfa.config.EmailMfaConfig;
import io.github.rose.security.rest.mfa.config.MfaConfig;
import io.github.rose.security.rest.mfa.config.SmsMfaConfig;
import io.github.rose.security.rest.mfa.provider.MfaProvider;
import io.github.rose.security.rest.mfa.provider.MfaProviderConfig;
import io.github.rose.security.rest.mfa.provider.MfaProviderType;
import io.github.rose.security.support.TokenFactory;
import io.github.rose.security.util.SecurityUser;
import io.github.rose.security.util.SecurityUtils;
import io.github.rose.security.util.TokenPair;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.repeat;

@Service
@RequiredArgsConstructor
public class DefaultMfaSettingService implements MfaSettingService {

    private static final RuntimeException PROVIDER_NOT_CONFIGURED_ERROR =
        new RuntimeException("mfa provider is not configured");

    private static final RuntimeException PROVIDER_NOT_AVAILABLE_ERROR =
        new RuntimeException("mfa provider is not available");

    private final Map<MfaProviderType, MfaProvider<MfaProviderConfig, MfaConfig>> providers =
        new EnumMap<>(MfaProviderType.class);

    private final TokenFactory tokenFactory;

    private final MfaProperties mfaProperties;

    private final SecurityProperties securityProperties;

    private static String obfuscate(
        String input, int seenMargin, char obfuscationChar, int startIndexInclusive, int endIndexExclusive) {
        String part = input.substring(startIndexInclusive, endIndexExclusive);
        String obfuscatedPart;
        if (part.length() <= seenMargin * 2) {
            obfuscatedPart = repeat(obfuscationChar, part.length());
        } else {
            obfuscatedPart = part.substring(0, seenMargin)
                + repeat(obfuscationChar, part.length() - seenMargin * 2)
                + part.substring(part.length() - seenMargin);
        }
        return input.substring(0, startIndexInclusive) + obfuscatedPart + input.substring(endIndexExclusive);
    }

    @Autowired
    private void setProviders(Collection<MfaProvider> providers) {
        providers.forEach(provider -> {
            this.providers.put(provider.getType(), provider);
        });
    }

    @Override
    public void prepareVerificationCode() {
        MfaConfig mfaConfig = mfaProperties.getDefaultConfig();
        MfaProviderConfig providerConfig = mfaProperties
            .getProviderConfig(mfaConfig.getProviderType())
            .orElseThrow(() -> PROVIDER_NOT_CONFIGURED_ERROR);
        getTwoFaProvider(mfaConfig.getProviderType())
            .prepareVerificationCode(SecurityUtils.getCurrentUser(), providerConfig, mfaConfig);
    }

    @Override
    public TokenPair checkVerificationCode(String verificationCode) {
        SecurityUser user = SecurityUtils.getCurrentUser();
        MfaConfig mfaConfig = mfaProperties.getDefaultConfig();
        MfaProviderConfig providerConfig = mfaProperties
            .getProviderConfig(mfaConfig.getProviderType())
            .orElseThrow(() -> PROVIDER_NOT_CONFIGURED_ERROR);

        boolean verificationSuccess = false;
        if (StringUtils.isNotBlank(verificationCode)) {
            if (StringUtils.isNumeric(verificationCode) || mfaConfig.getProviderType() == MfaProviderType.BACKUP_CODE) {
                verificationSuccess = getTwoFaProvider(mfaConfig.getProviderType())
                    .checkVerificationCode(user, verificationCode, providerConfig, mfaConfig);
            }
        }

        if (verificationSuccess) {
            return tokenFactory.createTokenPair(user);
        } else {
            throw new RuntimeException("Verification code is incorrect");
        }
    }

    private MfaProvider<MfaProviderConfig, MfaConfig> getTwoFaProvider(MfaProviderType providerType) {
        return Optional.ofNullable(providers.get(providerType)).orElseThrow(() -> PROVIDER_NOT_AVAILABLE_ERROR);
    }

    @Override
    public List<MfaAuthController.TwoFaProviderInfo> getAvailableTwoFaProviders() {
        return mfaProperties.getAllConfigs().stream()
            .map(config -> {
                String contact = null;
                switch (config.getProviderType()) {
                    case SMS:
                        String phoneNumber = ((SmsMfaConfig) config).getPhoneNumber();
                        contact =
                            obfuscate(phoneNumber, 2, '*', phoneNumber.indexOf('+') + 1, phoneNumber.length());
                        break;
                    case EMAIL:
                        String email = ((EmailMfaConfig) config).getEmail();
                        contact = obfuscate(email, 2, '*', 0, email.indexOf('@'));
                        break;
                }
                return MfaAuthController.TwoFaProviderInfo.builder()
                    .type(config.getProviderType())
                    .useByDefault(config.isUseByDefault())
                    .contact(contact)
                    .minVerificationCodeSendPeriod(mfaProperties.getMinVerificationCodeSendPeriod())
                    .build();
            })
            .collect(Collectors.toList());
    }
}
