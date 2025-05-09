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

import io.github.rose.core.jackson.JacksonUtils;
import io.github.rose.security.rest.mfa.config.MfaConfig;
import io.github.rose.security.rest.mfa.provider.MfaProviderConfig;
import io.github.rose.security.rest.mfa.provider.MfaProviderType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@ConfigurationProperties(prefix = "security.mfa", ignoreUnknownFields = false)
public class MfaProperties {

    private boolean enabled = false;

    @Valid
    @NotEmpty
    private List<Map<String, Object>> providers;

    @NotNull
    @Min(value = 5)
    private Integer minVerificationCodeSendPeriod;

    @Min(value = 0, message = "must be positive")
    private Integer maxVerificationFailuresBeforeUserLockout;

    @NotNull
    @Min(value = 60)
    private Long totalAllowedTimeForVerification = 3600L; // sec

    @Valid
    @NotNull
    private List<Map<String, Object>> configs;

    public List<MfaConfig> getAllConfigs() {
        return configs.stream()
            .map(twoFaConfig -> JacksonUtils.fromString(JacksonUtils.toString(twoFaConfig), MfaConfig.class))
            .collect(Collectors.toList());
    }

    public MfaConfig getDefaultConfig() {
        return getAllConfigs().stream()
            .filter(MfaConfig::isUseByDefault)
            .findAny()
            .orElse(null);
    }

    public Optional<MfaProviderConfig> getProviderConfig(MfaProviderType providerType) {
        return Optional.ofNullable(providers).flatMap(providersConfigs -> providersConfigs.stream()
            .map(providerConfig ->
                JacksonUtils.fromString(JacksonUtils.toString(providerConfig), MfaProviderConfig.class))
            .filter(providerConfig -> providerConfig.getProviderType().equals(providerType))
            .findFirst());
    }
}
