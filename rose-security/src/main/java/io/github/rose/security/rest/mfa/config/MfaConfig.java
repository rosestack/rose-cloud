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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.rose.security.rest.mfa.provider.MfaProviderType;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "providerType")
@JsonSubTypes({
    @Type(name = "SMS", value = SmsMfaConfig.class),
    @Type(name = "EMAIL", value = EmailMfaConfig.class),
    @Type(name = "BACKUP_CODE", value = BackupCodeMfaConfig.class)
})
public abstract class MfaConfig implements Serializable {

    @JsonIgnore
    protected transient boolean serializeHiddenFields;

    private boolean useByDefault;

    public boolean isSerializeHiddenFields() {
        return serializeHiddenFields;
    }

    public void setSerializeHiddenFields(boolean serializeHiddenFields) {
        this.serializeHiddenFields = serializeHiddenFields;
    }

    public boolean isUseByDefault() {
        return useByDefault;
    }

    public void setUseByDefault(boolean useByDefault) {
        this.useByDefault = useByDefault;
    }

    @JsonIgnore
    public abstract MfaProviderType getProviderType();
}
