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
package io.github.rose.oss.old.storage.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static io.github.rose.oss.old.storage.OssOperation.OSS_CONFIG_PREFIX_TENCENT;

/**
 * @author Levin
 * @since 2018-09-17 11:09
 **/
@ConfigurationProperties(prefix = OSS_CONFIG_PREFIX_TENCENT)
public class TencentOssProperties extends BaseOssProperties {

    /**
     * AppId
     */
    private String appId;

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 绑定的域名
     */
    private String domain;

    /**
     * 所属地区
     */
    private String region;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
