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

import io.github.rose.security.rest.mfa.provider.MfaProviderType;
import io.github.rose.security.util.TokenPair;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/mfa")
public class MfaAuthController {

    private final MfaSettingService mfaSettingService;

    public MfaAuthController(MfaSettingService mfaSettingService) {
        this.mfaSettingService = mfaSettingService;
    }

    @PostMapping("/verification/send")
    @PreAuthorize("hasAuthority('PRE_VERIFICATION_TOKEN')")
    public void requestTwoFaVerificationCode() throws Exception {
        mfaSettingService.prepareVerificationCode();
    }

    @PostMapping("/verification/check")
    @PreAuthorize("hasAuthority('PRE_VERIFICATION_TOKEN')")
    public TokenPair checkTwoFaVerificationCode(@RequestParam String verificationCode) throws Exception {
        return mfaSettingService.checkVerificationCode(verificationCode);
    }

    @GetMapping("/providers")
    @PreAuthorize("hasAuthority('PRE_VERIFICATION_TOKEN')")
    public List<TwoFaProviderInfo> getAvailableTwoFaProviders() {
        return mfaSettingService.getAvailableTwoFaProviders();
    }

    public static class TwoFaProviderInfo {

        private MfaProviderType type;

        private boolean useByDefault;

        private String contact;

        private Integer minVerificationCodeSendPeriod;

        public TwoFaProviderInfo() {
        }

        public static TwoFaProviderInfo.Builder builder() {
            return new Builder();
        }

        public MfaProviderType getType() {
            return type;
        }

        public void setType(MfaProviderType type) {
            this.type = type;
        }

        public boolean isUseByDefault() {
            return useByDefault;
        }

        public void setUseByDefault(boolean useByDefault) {
            this.useByDefault = useByDefault;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public Integer getMinVerificationCodeSendPeriod() {
            return minVerificationCodeSendPeriod;
        }

        public void setMinVerificationCodeSendPeriod(Integer minVerificationCodeSendPeriod) {
            this.minVerificationCodeSendPeriod = minVerificationCodeSendPeriod;
        }

        //创建一个 Builder 类
        public static class Builder {
            private MfaProviderType type;
            private boolean useByDefault;
            private String contact;
            private Integer minVerificationCodeSendPeriod;

            public Builder type(MfaProviderType type) {
                this.type = type;
                return this;
            }

            public Builder useByDefault(boolean useByDefault) {
                this.useByDefault = useByDefault;
                return this;
            }

            public Builder contact(String contact) {
                this.contact = contact;
                return this;
            }

            public Builder minVerificationCodeSendPeriod(Integer minVerificationCodeSendPeriod) {
                this.minVerificationCodeSendPeriod = minVerificationCodeSendPeriod;
                return this;
            }

            public TwoFaProviderInfo build() {
                TwoFaProviderInfo info = new TwoFaProviderInfo();
                info.type = this.type;
                info.useByDefault = this.useByDefault;
                info.contact = this.contact;
                info.minVerificationCodeSendPeriod = this.minVerificationCodeSendPeriod;
                return info;
            }
        }

    }
}
