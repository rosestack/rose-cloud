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
package io.github.rose.security.rest.mfa.provider;

import io.github.rose.security.rest.mfa.config.MfaConfig;
import io.github.rose.security.util.SecurityUser;
import org.springframework.security.core.userdetails.User;

public interface MfaProvider<C extends MfaProviderConfig, A extends MfaConfig> {

    A generateTwoFaConfig(User user, C providerConfig);

    default void prepareVerificationCode(SecurityUser user, C providerConfig, A accountConfig) {}

    boolean checkVerificationCode(SecurityUser user, String code, C providerConfig, A accountConfig);

    default void check(String tenantId) {}

    MfaProviderType getType();
}
