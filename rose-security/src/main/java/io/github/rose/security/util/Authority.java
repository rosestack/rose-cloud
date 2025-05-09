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
package io.github.rose.security.util;

import lombok.Getter;

@Getter
public enum Authority {
    SUPER_ADMIN(1),
    TENANT_ADMIN(2),
    COMPANY_ADMIN(3),
    CUSTOMER_USER(4),
    NORMAL_USER(5),
    REFRESH_TOKEN(10),
    PRE_VERIFICATION_TOKEN(11);

    private final int code;

    Authority(int code) {
        this.code = code;
    }

    public static Authority parse(String value) {
        Authority authority = null;
        if (value != null && !value.isEmpty()) {
            for (Authority current : Authority.values()) {
                if (current.name().equalsIgnoreCase(value)) {
                    authority = current;
                    break;
                }
            }
        }
        return authority;
    }
}
