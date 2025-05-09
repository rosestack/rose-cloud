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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rose.security.rest.mfa.config.OtpBasedMfaConfig;
import io.github.rose.security.rest.mfa.provider.MfaProvider;
import io.github.rose.security.rest.mfa.provider.OtpBasedMfaProviderConfig;
import io.github.rose.security.util.SecurityUser;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import static io.github.rose.security.CacheConstants.TWO_FA_VERIFICATION_CODE_CACHE;

@Component
@RequiredArgsConstructor
public abstract class OtpBasedMfaProvider<C extends OtpBasedMfaProviderConfig, A extends OtpBasedMfaConfig>
    implements MfaProvider<C, A> {

    private final CacheManager cacheManager;

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public final void prepareVerificationCode(SecurityUser user, C providerConfig, A twoFaConfig) {
        String verificationCode = RandomStringUtils.randomNumeric(6);
        sendVerificationCode(user, verificationCode, providerConfig, twoFaConfig);
        cacheManager
            .getCache(TWO_FA_VERIFICATION_CODE_CACHE)
            .put(
                TWO_FA_VERIFICATION_CODE_CACHE + ":" + user.getUsername(),
                objectMapper.writeValueAsBytes(
                    new Otp(System.currentTimeMillis(), verificationCode, twoFaConfig)));
    }

    protected abstract void sendVerificationCode(
        SecurityUser user, String verificationCode, C providerConfig, A accountConfig);

    @Override
    @SneakyThrows
    public final boolean checkVerificationCode(SecurityUser user, String code, C providerConfig, A twoFaConfig) {
        String correctVerificationCode = cacheManager
            .getCache(TWO_FA_VERIFICATION_CODE_CACHE)
            .get(TWO_FA_VERIFICATION_CODE_CACHE + ":" + user.getUsername())
            .toString();
        Otp otp = objectMapper.readValue(correctVerificationCode, Otp.class);
        if (correctVerificationCode != null) {
            if (System.currentTimeMillis() - otp.getTimestamp()
                > TimeUnit.SECONDS.toMillis(providerConfig.getVerificationCodeExpireTime())) {
                cacheManager
                    .getCache(TWO_FA_VERIFICATION_CODE_CACHE)
                    .evict(TWO_FA_VERIFICATION_CODE_CACHE + ":" + user.getUsername());
                return false;
            }
            if (code.equals(otp.getValue()) && twoFaConfig.equals(otp.getTwoFaConfig())) {
                cacheManager
                    .getCache(TWO_FA_VERIFICATION_CODE_CACHE)
                    .evict(TWO_FA_VERIFICATION_CODE_CACHE + ":" + user.getUsername());
                return true;
            }
        }
        return false;
    }

    @Data
    public static class Otp implements Serializable {

        private final long timestamp;

        private final String value;

        private final OtpBasedMfaConfig twoFaConfig;
    }
}
