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
package io.github.rose.oss;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import io.github.rose.core.util.StringPool;
import io.github.rose.oss.model.BladeFile;
import io.github.rose.oss.model.OssFile;
import io.github.rose.oss.props.OssProperties;
import io.github.rose.oss.rule.OssRule;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * QiniuTemplate
 *
 * @author Chill
 */
public class QiniuTemplate implements OssTemplate {

    private final Auth auth;

    private final UploadManager uploadManager;

    private final BucketManager bucketManager;

    private final OssProperties ossProperties;

    private final OssRule ossRule;

    public QiniuTemplate(Auth auth, UploadManager uploadManager, BucketManager bucketManager, OssProperties ossProperties, OssRule ossRule) {
        this.auth = auth;
        this.uploadManager = uploadManager;
        this.bucketManager = bucketManager;
        this.ossProperties = ossProperties;
        this.ossRule = ossRule;
    }

    @Override
    public void makeBucket(String bucketName) {
        try {
            if (!ArrayUtils.contains(bucketManager.buckets(), getBucketName(bucketName))) {
                bucketManager.createBucket(
                    getBucketName(bucketName), Zone.autoZone().getRegion());
            }
        } catch (QiniuException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeBucket(String bucketName) {
    }

    @Override
    public boolean bucketExists(String bucketName) {
        try {
            return ArrayUtils.contains(bucketManager.buckets(), getBucketName(bucketName));
        } catch (QiniuException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName) {
        try {
            bucketManager.copy(getBucketName(bucketName), fileName, getBucketName(destBucketName), fileName);
        } catch (QiniuException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName) {
        try {
            bucketManager.copy(getBucketName(bucketName), fileName, getBucketName(destBucketName), destFileName);
        } catch (QiniuException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OssFile statFile(String fileName) {
        return statFile(ossProperties.getBucketName(), fileName);
    }

    @Override
    public OssFile statFile(String bucketName, String fileName) {
        FileInfo stat = null;
        try {
            stat = bucketManager.stat(getBucketName(bucketName), fileName);
        } catch (QiniuException e) {
            throw new RuntimeException(e);
        }
        OssFile ossFile = new OssFile();
        ossFile.setName(StringUtils.isEmpty(stat.key) ? fileName : stat.key);
        ossFile.setLink(fileLink(ossFile.getName()));
        ossFile.setHash(stat.hash);
        ossFile.setLength(stat.fsize);
        ossFile.setPutTime(new Date(stat.putTime / 10000));
        ossFile.setContentType(stat.mimeType);
        return ossFile;
    }

    @Override
    public String filePath(String fileName) {
        return getBucketName().concat(StringPool.SLASH).concat(fileName);
    }

    @Override
    public String filePath(String bucketName, String fileName) {
        return getBucketName(bucketName).concat(StringPool.SLASH).concat(fileName);
    }

    @Override
    public String fileLink(String fileName) {
        return getEndpoint().concat(StringPool.SLASH).concat(fileName);
    }

    @Override
    public String fileLink(String bucketName, String fileName) {
        return getEndpoint().concat(StringPool.SLASH).concat(fileName);
    }

    @Override
    public BladeFile putFile(MultipartFile file) {
        return putFile(ossProperties.getBucketName(), file.getOriginalFilename(), file);
    }

    @Override
    public BladeFile putFile(String fileName, MultipartFile file) {
        return putFile(ossProperties.getBucketName(), fileName, file);
    }

    @Override
    public BladeFile putFile(String bucketName, String fileName, MultipartFile file) {
        try {
            return putFile(bucketName, fileName, file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BladeFile putFile(String fileName, InputStream stream) {
        return putFile(ossProperties.getBucketName(), fileName, stream);
    }

    @Override
    public BladeFile putFile(String bucketName, String fileName, InputStream stream) {
        return put(bucketName, stream, fileName, false);
    }

    public BladeFile put(String bucketName, InputStream stream, String key, boolean cover) {
        makeBucket(bucketName);
        String originalName = key;
        key = getFileName(key);
        try {
            // 覆盖上传
            if (cover) {
                uploadManager.put(stream, key, getUploadToken(bucketName, key), null, null);
            } else {
                Response response = uploadManager.put(stream, key, getUploadToken(bucketName), null, null);

                int retry = 0;
                int retryCount = 5;
                while (response.needRetry() && retry < retryCount) {
                    response = uploadManager.put(stream, key, getUploadToken(bucketName), null, null);
                    retry++;
                }
            }
        } catch (QiniuException e) {
            throw new RuntimeException(e);
        }
        BladeFile file = new BladeFile();
        file.setOriginalName(originalName);
        file.setName(key);
        file.setDomain(getOssHost());
        file.setLink(fileLink(bucketName, key));
        return file;
    }

    @Override
    public void removeFile(String fileName) {
        try {
            bucketManager.delete(getBucketName(), fileName);
        } catch (QiniuException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeFile(String bucketName, String fileName) {
        try {
            bucketManager.delete(getBucketName(bucketName), fileName);
        } catch (QiniuException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeFiles(List<String> fileNames) {
        fileNames.forEach(this::removeFile);
    }

    @Override
    public void removeFiles(String bucketName, List<String> fileNames) {
        fileNames.forEach(fileName -> removeFile(getBucketName(bucketName), fileName));
    }

    /**
     * 根据规则生成存储桶名称规则
     *
     * @return String
     */
    private String getBucketName() {
        return getBucketName(ossProperties.getBucketName());
    }

    /**
     * 根据规则生成存储桶名称规则
     *
     * @param bucketName 存储桶名称
     * @return String
     */
    private String getBucketName(String bucketName) {
        return ossRule.bucketName(bucketName);
    }

    /**
     * 根据规则生成文件名称规则
     *
     * @param originalFilename 原始文件名
     * @return string
     */
    private String getFileName(String originalFilename) {
        return ossRule.fileName(originalFilename);
    }

    /**
     * 获取上传凭证，普通上传
     *
     * @param bucketName 存储桶名称
     * @return string
     */
    public String getUploadToken(String bucketName) {
        return auth.uploadToken(getBucketName(bucketName));
    }

    /**
     * 获取上传凭证，覆盖上传
     *
     * @param bucketName 存储桶名称
     * @param key        key
     * @return string
     */
    private String getUploadToken(String bucketName, String key) {
        return auth.uploadToken(getBucketName(bucketName), key);
    }

    /**
     * 获取域名
     *
     * @return String
     */
    public String getOssHost() {
        return getEndpoint();
    }

    /**
     * 获取服务地址
     *
     * @return String
     */
    public String getEndpoint() {
        if (StringUtils.isBlank(ossProperties.getTransformEndpoint())) {
            return ossProperties.getEndpoint();
        }
        return ossProperties.getTransformEndpoint();
    }
}
