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
package io.github.rose.oss.enums;


/**
 * minio策略配置
 *
 * @author SCMOX
 */
public enum PolicyType {

    /**
     * 只读
     */
    READ("read", "只读"),

    /**
     * 只写
     */
    WRITE("write", "只写"),

    /**
     * 读写
     */
    READ_WRITE("read_write", "读写");

    /**
     * 类型
     */
    private final String type;

    /**
     * 描述
     */
    private final String policy;

    PolicyType(String type, String policy) {
        this.type = type;
        this.policy = policy;
    }

    public String getType() {
        return type;
    }

    public String getPolicy() {
        return policy;
    }
}
