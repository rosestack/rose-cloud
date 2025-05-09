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
package io.github.rose.security.rest.mfa.provider;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class SmsMfaProviderConfig extends OtpBasedMfaProviderConfig {

    @NotBlank(message = "is required")
    @Pattern(regexp = ".*\\$\\{code}.*", message = "must contain verification code")
    private String template;

    @Override
    public MfaProviderType getProviderType() {
        return MfaProviderType.SMS;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public String toString() {
        return "SmsMfaProviderConfig{" +
            "template='" + template + '\'' +
            '}';
    }
}
