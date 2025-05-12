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

import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * 响应结果
 *
 * @author Levin
 */
public class StorageResponse implements java.io.Serializable {

    private static final String SEPARATOR = "/";

    private String fileId;

    private String etag;

    private String originName;

    private String targetName;

    private long size;

    /**
     * 文件存储的名字
     */
    private String md5;

    /**
     * 文件的完整路径
     */
    private String fullUrl;

    private String mappingPath;

    private String bucket;

    /**
     * 对应存储的扩展字段
     */
    private Map<String, Object> extend;

    public StorageResponse(
            String fileId,
            String etag,
            String originName,
            String targetName,
            String mappingPath,
            String bucket,
            long size,
            String md5,
            String fullUrl,
            Map<String, Object> extend) {
        this.fileId = fileId;
        this.etag = etag;
        this.originName = originName;
        this.targetName = targetName;
        this.size = size;
        this.md5 = md5;
        this.bucket = bucket;
        this.extend = extend;
        this.mappingPath = mappingPath;
        if (StringUtils.isBlank(fullUrl)) {
            this.fullUrl = buildFullUrl(mappingPath, targetName);
        } else {
            this.fullUrl = fullUrl;
        }
    }

    public static String buildFullUrl(String mappingPath, String targetName) {
        if (mappingPath.endsWith(SEPARATOR) && targetName.startsWith(SEPARATOR)) {
            mappingPath = mappingPath.substring(0, mappingPath.length() - 1);
        }
        return StringUtils.join(mappingPath, targetName);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getMappingPath() {
        return mappingPath;
    }

    public void setMappingPath(String mappingPath) {
        this.mappingPath = mappingPath;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }

    public static class Builder {
        private String fileId;
        private String etag;
        private String originName;
        private String targetName;
        private long size;
        private String md5;
        private String fullUrl;
        private String mappingPath;
        private String bucket;
        private Map<String, Object> extend;

        public Builder fileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder etag(String etag) {
            this.etag = etag;
            return this;
        }

        public Builder originName(String originName) {
            this.originName = originName;
            return this;
        }

        public Builder targetName(String targetName) {
            this.targetName = targetName;
            return this;
        }

        public Builder size(long size) {
            this.size = size;
            return this;
        }

        public Builder md5(String md5) {
            this.md5 = md5;
            return this;
        }

        public Builder fullUrl(String fullUrl) {
            this.fullUrl = fullUrl;
            return this;
        }

        public Builder mappingPath(String mappingPath) {
            this.mappingPath = mappingPath;
            return this;
        }

        public Builder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public Builder extend(Map<String, Object> extend) {
            this.extend = extend;
            return this;
        }

        public StorageResponse build() {
            return new StorageResponse(
                    fileId, etag, originName, targetName, mappingPath, bucket, size, md5, fullUrl, extend);
        }
    }
}
