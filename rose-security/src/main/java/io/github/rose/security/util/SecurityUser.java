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
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
public class SecurityUser extends User {

    private static final long serialVersionUID = -797397440703066079L;

    private List<String> tenants;

    public SecurityUser(
        String username,
        String password,
        List<String> tenants,
        boolean enabled,
        boolean accountNonExpired,
        boolean credentialsNonExpired,
        boolean accountNonLocked,
        Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.tenants = tenants;
    }

    public SecurityUser(
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        List<String> tenants) {
        super(username, password, authorities);
        this.tenants = tenants;
    }

    public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(username, password, authorities, new ArrayList<>());
    }
}
