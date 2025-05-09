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
package io.github.rose.oss.old.storage;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import io.github.rose.oss.old.storage.domain.DownloadResponse;
import io.github.rose.oss.old.storage.domain.StorageItem;
import io.github.rose.oss.old.storage.domain.StorageRequest;
import io.github.rose.oss.old.storage.domain.StorageResponse;
import io.github.rose.oss.old.storage.exception.StorageException;
import io.github.rose.oss.old.storage.properties.BaseOssProperties;
import io.github.rose.oss.old.storage.properties.TencentOssProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Levin
 */
public class TencentOssOperation implements OssOperation {
    private static final Logger log = LoggerFactory.getLogger(TencentOssOperation.class);
    private final COSClient client;

    private final TencentOssProperties properties;

    public TencentOssOperation(COSClient client, TencentOssProperties properties) {
        this.client = client;
        this.properties = properties;
    }

    @Override
    public DownloadResponse download(String fileName) {
        return download(properties.getBucket(), fileName);
    }

    @Override
    public DownloadResponse download(String bucketName, String fileName) {
        final String path = StringUtils.defaultIfBlank(
            this.properties.getTmpDir(), this.getClass().getResource("/").getPath());
        final File file = new File(path + File.separator + fileName);
        log.debug("[文件目录] - [{}]", file.getPath());
        download(bucketName, fileName, file);

        DownloadResponse downloadResponse = new DownloadResponse();
        downloadResponse.setFile(file);
        try {
            downloadResponse.setInputStream(new BufferedInputStream(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        downloadResponse.setLocalFilePath(file.getPath());

        return downloadResponse;
    }

    @Override
    public void download(String bucketName, String fileName, File file) {
        final String bucket = StringUtils.defaultString(bucketName, properties.getBucket());
        this.client.getObject(new GetObjectRequest(bucket, fileName), file);
    }

    @Override
    public void download(String fileName, File file) {
        download(properties.getBucket(), fileName, file);
    }

    @Override
    public List<StorageItem> list() {
        return null;
    }

    @Override
    public void rename(String oldName, String newName) {
    }

    @Override
    public void rename(String bucketName, String oldName, String newName) {
    }

    @Override
    public StorageResponse upload(String fileName, byte[] content) {
        return upload(properties.getBucket(), fileName, content);
    }

    @Override
    public StorageResponse upload(String bucketName, String fileName, InputStream content) {
        // 腾讯云必需要以"/"开头
        if (!fileName.startsWith(File.separator)) {
            fileName = File.separator + fileName;
        }
        ObjectMetadata objectMetadata = new ObjectMetadata();
        try {
            objectMetadata.setContentLength(content.available());
            PutObjectRequest request = new PutObjectRequest(properties.getBucket(), fileName, content, objectMetadata);
            PutObjectResult result = client.putObject(request);
            if (StringUtils.isEmpty(result.getETag())) {
                throw new StorageException(BaseOssProperties.StorageType.TENCENT, "文件上传失败,ETag为空");
            }
            return StorageResponse.builder()
                .originName(fileName)
                .targetName(fileName)
                .size(objectMetadata.getContentLength())
                .fullUrl(properties.getMappingPath() + fileName)
                .build();
        } catch (IOException e) {
            log.error("[文件上传异常]", e);
            throw new StorageException(BaseOssProperties.StorageType.TENCENT, "文件上传失败," + e.getLocalizedMessage());
        }
    }

    @Override
    public StorageResponse upload(String bucketName, String fileName, byte[] content) {
        // 腾讯云必需要以"/"开头
        if (!fileName.startsWith(File.separator)) {
            fileName = File.separator + fileName;
        }
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 设置输入流长度为 500
        objectMetadata.setContentLength(content.length);
        PutObjectRequest request = new PutObjectRequest(
            properties.getBucket(), fileName, new ByteArrayInputStream(content), objectMetadata);
        PutObjectResult result = client.putObject(request);
        if (StringUtils.isEmpty(result.getETag())) {
            throw new StorageException(BaseOssProperties.StorageType.TENCENT, "文件上传失败,ETag为空");
        }
        return StorageResponse.builder()
            .originName(fileName)
            .targetName(fileName)
            .size(objectMetadata.getContentLength())
            .fullUrl(properties.getMappingPath() + fileName)
            .build();
    }

    @Override
    public StorageResponse upload(StorageRequest request) {
        return null;
    }

    @Override
    public void remove(String fileName) {
    }

    @Override
    public void remove(String bucketName, String fileName) {
    }

    @Override
    public void remove(String bucketName, Path path) {
    }
}
