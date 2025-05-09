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
package io.github.rose.mybatis;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Set;

@ConfigurationProperties(prefix = "mybatis-plus.tenant")
public class TenantProperties {

    private boolean enabled = true;

    private String tenantIdColumn = "tenant_id";

    /**
     * 全路径：com.xxx.user.mybatis-mapper.SysUserMapper.findList
     */
    private Set<String> ignoredMappedStatements = Collections.emptySet();

    /**
     * 需要忽略多租户的请求
     * <p>
     * 默认情况下，每个请求需要带上 tenant-id 的请求头。但是，部分请求是无需带上的，例如说短信回调、支付回调等 Open API！
     */
    private Set<String> ignoreUrls = Collections.emptySet();

    /**
     * 需要忽略多租户的表
     * <p>
     * 即默认所有表都开启多租户的功能，所以记得添加对应的 tenant_id 字段哟
     */
    private Set<String> ignoredTables = Collections.emptySet();

    /**
     * 需要忽略多租户的 Spring Cache 缓存
     * <p>
     * 即默认所有缓存都开启多租户的功能，所以记得添加对应的 tenant_id 字段哟
     */
    private Set<String> ignoredCaches = Collections.emptySet();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getTenantIdColumn() {
        return tenantIdColumn;
    }

    public void setTenantIdColumn(String tenantIdColumn) {
        this.tenantIdColumn = tenantIdColumn;
    }

    public Set<String> getIgnoredMappedStatements() {
        return ignoredMappedStatements;
    }

    public void setIgnoredMappedStatements(Set<String> ignoredMappedStatements) {
        this.ignoredMappedStatements = ignoredMappedStatements;
    }

    public Set<String> getIgnoreUrls() {
        return ignoreUrls;
    }

    public void setIgnoreUrls(Set<String> ignoreUrls) {
        this.ignoreUrls = ignoreUrls;
    }

    public Set<String> getIgnoredTables() {
        return ignoredTables;
    }

    public void setIgnoredTables(Set<String> ignoredTables) {
        this.ignoredTables = ignoredTables;
    }

    public Set<String> getIgnoredCaches() {
        return ignoredCaches;
    }

    public void setIgnoredCaches(Set<String> ignoredCaches) {
        this.ignoredCaches = ignoredCaches;
    }
}
