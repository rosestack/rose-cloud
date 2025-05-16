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

import com.fasterxml.jackson.annotation.JsonGetter;
import io.github.rose.security.rest.mfa.provider.MfaProviderType;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import javax.validation.constraints.NotBlank;

public class BackupCodeMfaConfig extends MfaConfig {

    @NotBlank
    private String codes;

    @Override
    public MfaProviderType getProviderType() {
        return MfaProviderType.BACKUP_CODE;
    }

    @JsonGetter("codes")
    public Set<String> getCodesForJson() {
        if (serializeHiddenFields) {
            return new TreeSet<>(Arrays.asList(codes.split(",")));
        } else {
            return null;
        }
    }

    @JsonGetter
    private Integer getCodesLeft() {
        if (codes != null) {
            return getCodesForJson().size();
        } else {
            return null;
        }
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes(String codes) {
        this.codes = codes;
    }

    @Override
    public String toString() {
        return "BackupCodeMfaConfig{" + "codes='"
                + codes + '\'' + ", serializeHiddenFields="
                + serializeHiddenFields + '}';
    }
}
