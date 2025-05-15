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
package io.github.rose.oss.old.storage.domain;

import io.github.rose.oss.old.storage.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * 请求参数
 *
 * @author Levin
 */
public class StorageRequest implements java.io.Serializable {

    /**
     * 如果为空则取应用配置的
     */
    private String bucket;

    /**
     * 原始文件名称
     */
    private String originName;

    /**
     * 如果为 true 则会随机生成文件名
     */
    private boolean randomName = true;

    private PrefixRule rule;

    private String prefix;

    /**
     * content 与 inputStream 二选一
     */
    private byte[] content;

    /**
     * inputStream 与 content 二选一
     */
    private InputStream inputStream;

    private String contentType;

    private Object tenantId;

    private Object userId;

    public static Builder builder() {
        return new Builder();
    }

    /**
     * 获取目标名字
     *
     * @return 目标名称
     * @throws RuntimeException RuntimeException
     */
    public String buildTargetName() {
        if (StringUtils.isBlank(this.getOriginName())) {
            throw new RuntimeException("originName 不能为空");
        }
        final PrefixRule rule = this.getRule();
        if (rule == null) {
            return FileUtils.targetName(this.isRandomName(), this.getPrefix(), this.getOriginName());
        }
        String prefix;
        switch (rule) {
            case now_date_mouth:
                prefix = LocalDateTime.now(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("yyyyMM", Locale.getDefault()));
            case now_date_mouth_day:
                prefix = LocalDateTime.now(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd", Locale.getDefault()));
            case tenant_now_date_mouth_day: {
                if (tenantId == null || userId == null) {
                    throw new RuntimeException("tenantId or userId not null");
                }
                prefix = tenantId + "/" + userId + LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyyMMdd", Locale.getDefault()));
            }
            case none:
                prefix = this.getPrefix();
            default:
                prefix = this.getPrefix();
        }
        return FileUtils.targetName(this.isRandomName(), prefix, this.getOriginName());
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public boolean isRandomName() {
        return randomName;
    }

    public void setRandomName(boolean randomName) {
        this.randomName = randomName;
    }

    public PrefixRule getRule() {
        return rule;
    }

    public void setRule(PrefixRule rule) {
        this.rule = rule;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Object getTenantId() {
        return tenantId;
    }

    public void setTenantId(Object tenantId) {
        this.tenantId = tenantId;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public enum PrefixRule {

        /**
         * 无规则 默认提取 prefix 否则自动生成前缀
         */
        none,
        /**
         * 当前日期+月份
         */
        now_date_mouth,

        /**
         * 当前年月日
         */
        now_date_mouth_day,

        /**
         * 租户当前日期策略
         */
        tenant_now_date_mouth_day
    }

    public static class Builder {
        private String bucket;
        private String originName;
        private boolean randomName = true;
        private PrefixRule rule;
        private String prefix;
        private byte[] content;
        private InputStream inputStream;
        private String contentType;
        private Object tenantId;
        private Object userId;

        public Builder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public Builder originName(String originName) {
            this.originName = originName;
            return this;
        }

        public Builder randomName(boolean randomName) {
            this.randomName = randomName;
            return this;
        }

        public Builder rule(PrefixRule rule) {
            this.rule = rule;
            return this;
        }

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder content(byte[] content) {
            this.content = content;
            return this;
        }

        public Builder inputStream(InputStream inputStream) {
            this.inputStream = inputStream;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder tenantId(Object tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder userId(Object userId) {
            this.userId = userId;
            return this;
        }

        public StorageRequest build() {
            StorageRequest request = new StorageRequest();
            request.setBucket(bucket);
            request.setOriginName(originName);
            request.setRandomName(randomName);
            request.setRule(rule);
            request.setPrefix(prefix);
            request.setContent(content);
            request.setInputStream(inputStream);
            request.setContentType(contentType);
            request.setTenantId(tenantId);
            request.setUserId(userId);
            return request;
        }
    }
}
