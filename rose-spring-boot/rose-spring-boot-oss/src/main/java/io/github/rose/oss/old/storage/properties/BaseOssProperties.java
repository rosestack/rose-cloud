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

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Levin
 */
public class BaseOssProperties implements java.io.Serializable {

    private Boolean enabled = false;

    @Value("${spring.application.name:'oss'}")
    private String bucket;

    private String mappingPath = "http://minio.battcn.com/";

    /**
     * 访问key
     **/
    private String accessKey;

    /**
     * 访问秘钥
     **/
    private String secretKey;

    /**
     * 本地文件临时目录
     */
    private String tmpDir;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getMappingPath() {
        return mappingPath;
    }

    public void setMappingPath(String mappingPath) {
        this.mappingPath = mappingPath;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTmpDir() {
        return tmpDir;
    }

    public void setTmpDir(String tmpDir) {
        this.tmpDir = tmpDir;
    }

    public enum StorageType {

        /**
         * MINIO
         */
        MINIO,
        ALIYUN,
        TENCENT,
        QINIU
    }
}
