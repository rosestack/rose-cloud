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

import io.github.rose.core.util.StringPool;
import io.github.rose.core.util.date.TimeUtils;
import io.github.rose.oss.enums.PolicyType;
import io.github.rose.oss.model.BladeFile;
import io.github.rose.oss.model.OssFile;
import io.github.rose.oss.props.OssProperties;
import io.github.rose.oss.rule.OssRule;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * MinIOTemplate
 *
 * @author Chill
 */
public class MinioTemplate implements OssTemplate {

    /**
     * MinIO客户端
     */
    private final MinioClient client;

    /**
     * 存储桶命名规则
     */
    private final OssRule ossRule;

    /**
     * 配置类
     */
    private final OssProperties ossProperties;

    public MinioTemplate(MinioClient client, OssRule ossRule, OssProperties ossProperties) {
        this.client = client;
        this.ossRule = ossRule;
        this.ossProperties = ossProperties;
    }

    /**
     * 获取存储桶策略
     *
     * @param bucketName 存储桶名称
     * @param policyType 策略枚举
     * @return String
     */
    public static String getPolicyType(String bucketName, PolicyType policyType) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("    \"Statement\": [\n");
        builder.append("        {\n");
        builder.append("            \"Action\": [\n");

        switch (policyType) {
            case WRITE:
                builder.append("                \"s3:GetBucketLocation\",\n");
                builder.append("                \"s3:ListBucketMultipartUploads\"\n");
                break;
            case READ_WRITE:
                builder.append("                \"s3:GetBucketLocation\",\n");
                builder.append("                \"s3:ListBucket\",\n");
                builder.append("                \"s3:ListBucketMultipartUploads\"\n");
                break;
            default:
                builder.append("                \"s3:GetBucketLocation\"\n");
                break;
        }

        builder.append("            ],\n");
        builder.append("            \"Effect\": \"Allow\",\n");
        builder.append("            \"Principal\": \"*\",\n");
        builder.append("            \"Resource\": \"arn:aws:s3:::");
        builder.append(bucketName);
        builder.append("\"\n");
        builder.append("        },\n");
        if (PolicyType.READ.equals(policyType)) {
            builder.append("        {\n");
            builder.append("            \"Action\": [\n");
            builder.append("                \"s3:ListBucket\"\n");
            builder.append("            ],\n");
            builder.append("            \"Effect\": \"Deny\",\n");
            builder.append("            \"Principal\": \"*\",\n");
            builder.append("            \"Resource\": \"arn:aws:s3:::");
            builder.append(bucketName);
            builder.append("\"\n");
            builder.append("        },\n");
        }
        builder.append("        {\n");
        builder.append("            \"Action\": ");

        switch (policyType) {
            case WRITE:
                builder.append("[\n");
                builder.append("                \"s3:AbortMultipartUpload\",\n");
                builder.append("                \"s3:DeleteObject\",\n");
                builder.append("                \"s3:ListMultipartUploadParts\",\n");
                builder.append("                \"s3:PutObject\"\n");
                builder.append("            ],\n");
                break;
            case READ_WRITE:
                builder.append("[\n");
                builder.append("                \"s3:AbortMultipartUpload\",\n");
                builder.append("                \"s3:DeleteObject\",\n");
                builder.append("                \"s3:GetObject\",\n");
                builder.append("                \"s3:ListMultipartUploadParts\",\n");
                builder.append("                \"s3:PutObject\"\n");
                builder.append("            ],\n");
                break;
            default:
                builder.append("\"s3:GetObject\",\n");
                break;
        }

        builder.append("            \"Effect\": \"Allow\",\n");
        builder.append("            \"Principal\": \"*\",\n");
        builder.append("            \"Resource\": \"arn:aws:s3:::");
        builder.append(bucketName);
        builder.append("/*\"\n");
        builder.append("        }\n");
        builder.append("    ],\n");
        builder.append("    \"Version\": \"2012-10-17\"\n");
        builder.append("}\n");
        return builder.toString();
    }

    public MinioClient getClient() {
        return client;
    }

    public OssRule getOssRule() {
        return ossRule;
    }

    public OssProperties getOssProperties() {
        return ossProperties;
    }

    @Override
    public void makeBucket(String bucketName) {
        try {
            if (!client.bucketExists(
                BucketExistsArgs.builder().bucket(getBucketName(bucketName)).build())) {
                client.makeBucket(
                    MakeBucketArgs.builder().bucket(getBucketName(bucketName)).build());
                client.setBucketPolicy(SetBucketPolicyArgs.builder()
                    .bucket(getBucketName(bucketName))
                    .config(getPolicyType(getBucketName(bucketName), PolicyType.READ))
                    .build());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Bucket getBucket() {
        return getBucket(getBucketName());
    }

    public Bucket getBucket(String bucketName) {
        Optional<Bucket> bucketOptional = null;
        try {
            bucketOptional = client.listBuckets().stream()
                .filter(bucket -> bucket.name().equals(getBucketName(bucketName)))
                .findFirst();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bucketOptional.orElse(null);
    }

    public List<Bucket> listBuckets() {
        try {
            return client.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeBucket(String bucketName) {
        try {
            client.removeBucket(
                RemoveBucketArgs.builder().bucket(getBucketName(bucketName)).build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean bucketExists(String bucketName) {
        try {
            return client.bucketExists(
                BucketExistsArgs.builder().bucket(getBucketName(bucketName)).build());
        } catch (ErrorResponseException e) {
            throw new RuntimeException(e);
        } catch (InsufficientDataException e) {
            throw new RuntimeException(e);
        } catch (InternalException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (ServerException e) {
            throw new RuntimeException(e);
        } catch (XmlParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName) {
        copyFile(bucketName, fileName, destBucketName, fileName);
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName) {
        try {
            client.copyObject(CopyObjectArgs.builder()
                .source(CopySource.builder()
                    .bucket(getBucketName(bucketName))
                    .object(fileName)
                    .build())
                .bucket(getBucketName(destBucketName))
                .object(destFileName)
                .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OssFile statFile(String fileName) {
        return statFile(ossProperties.getBucketName(), fileName);
    }

    @Override
    public OssFile statFile(String bucketName, String fileName) {
        StatObjectResponse stat = null;
        try {
            stat = client.statObject(StatObjectArgs.builder()
                .bucket(getBucketName(bucketName))
                .object(fileName)
                .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        OssFile ossFile = new OssFile();
        ossFile.setName(StringUtils.isEmpty(stat.object()) ? fileName : stat.object());
        ossFile.setLink(fileLink(ossFile.getName()));
        ossFile.setHash(String.valueOf(stat.hashCode()));
        ossFile.setLength(stat.size());
        ossFile.setPutTime(TimeUtils.toDate(stat.lastModified().toLocalDateTime()));
        ossFile.setContentType(stat.contentType());
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
        return getEndpoint()
            .concat(StringPool.SLASH)
            .concat(getBucketName())
            .concat(StringPool.SLASH)
            .concat(fileName);
    }

    @Override
    public String fileLink(String bucketName, String fileName) {
        return getEndpoint()
            .concat(StringPool.SLASH)
            .concat(getBucketName(bucketName))
            .concat(StringPool.SLASH)
            .concat(fileName);
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
            return putFile(bucketName, file.getOriginalFilename(), file.getInputStream());
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
        return putFile(bucketName, fileName, stream, "application/octet-stream");
    }

    public BladeFile putFile(String bucketName, String fileName, InputStream stream, String contentType) {
        makeBucket(bucketName);
        String originalName = fileName;
        fileName = getFileName(fileName);
        try {
            client.putObject(PutObjectArgs.builder().bucket(getBucketName(bucketName)).object(fileName).stream(
                    stream, stream.available(), -1)
                .contentType(contentType)
                .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        BladeFile file = new BladeFile();
        file.setOriginalName(originalName);
        file.setName(fileName);
        file.setDomain(getOssHost(bucketName));
        file.setLink(fileLink(bucketName, fileName));
        return file;
    }

    @Override
    public void removeFile(String fileName) {
        removeFile(ossProperties.getBucketName(), fileName);
    }

    @Override
    public void removeFile(String bucketName, String fileName) {
        try {
            client.removeObject(RemoveObjectArgs.builder()
                .bucket(getBucketName(bucketName))
                .object(fileName)
                .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeFiles(List<String> fileNames) {
        removeFiles(ossProperties.getBucketName(), fileNames);
    }

    @Override
    public void removeFiles(String bucketName, List<String> fileNames) {
        Stream<DeleteObject> stream = fileNames.stream().map(DeleteObject::new);
        client.removeObjects(RemoveObjectsArgs.builder()
            .bucket(getBucketName(bucketName))
            .objects(stream::iterator)
            .build());
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
     * 获取文件外链
     *
     * @param bucketName bucket名称
     * @param fileName   文件名称
     * @param expires    过期时间
     * @return url
     */
    public String getPresignedObjectUrl(String bucketName, String fileName, Integer expires) {
        try {
            return client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(getBucketName(bucketName))
                .object(fileName)
                .expiry(expires)
                .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取存储桶策略
     *
     * @param policyType 策略枚举
     * @return String
     */
    public String getPolicyType(PolicyType policyType) {
        return getPolicyType(getBucketName(), policyType);
    }

    /**
     * 获取域名
     *
     * @param bucketName 存储桶名称
     * @return String
     */
    public String getOssHost(String bucketName) {
        return getEndpoint() + StringPool.SLASH + getBucketName(bucketName);
    }

    /**
     * 获取域名
     *
     * @return String
     */
    public String getOssHost() {
        return getOssHost(ossProperties.getBucketName());
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
