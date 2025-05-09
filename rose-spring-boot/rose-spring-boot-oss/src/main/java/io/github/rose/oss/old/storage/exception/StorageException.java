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
package io.github.rose.oss.old.storage.exception;

import io.github.rose.oss.old.storage.properties.BaseOssProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Levin
 */
public class StorageException extends RuntimeException {

    @Getter
    @Setter
    private BaseOssProperties.StorageType storageType;

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(BaseOssProperties.StorageType storageType, String message) {
        super(message);
        this.setStorageType(storageType);
    }

    public StorageException(BaseOssProperties.StorageType storageType, String message, Throwable cause) {
        super(message, cause);
        this.setStorageType(storageType);
    }
}
