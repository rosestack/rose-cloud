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
package io.github.rose.security.rest.mfa.config;

import io.github.rose.security.rest.mfa.provider.MfaProviderType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class TotpMfaConfig extends MfaConfig {

    @NotBlank
    @Pattern(regexp = "otpauth://totp/(\\S+?):(\\S+?)\\?issuer=(\\S+?)&secret=(\\w+?)", message = "is invalid")
    private String authUrl;

    @Override
    public MfaProviderType getProviderType() {
        return MfaProviderType.TOTP;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    @Override
    public String toString() {
        return "TotpMfaConfig{" +
            "authUrl='" + authUrl + '\'' +
            ", serializeHiddenFields=" + serializeHiddenFields +
            '}';
    }
}
