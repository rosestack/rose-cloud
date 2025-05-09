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
package io.github.rose.core.exception;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 业务异常，必须提供code码，便于统一维护
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class BusinessException extends RuntimeException {

    private static final ResultCode DEFAULT = ResultCode.INTERNAL_ERROR;

    private final Integer code;

    private Serializable data;

    public BusinessException() {
        this(DEFAULT);
    }

    public BusinessException(Integer code, String message) {
        this(code, message, null);
    }

    public BusinessException(String message) {
        this(DEFAULT.getCode(), message, null);
    }

    public BusinessException(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getName(), null);
    }

    public BusinessException(ResultCode resultCode, Serializable data) {
        this(resultCode.getCode(), resultCode.getName(), data);
    }

    public BusinessException(Integer code, String message, Serializable data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = DEFAULT.getCode();
    }

    public BusinessException(String message, Serializable data) {
        super(message);
        this.code = DEFAULT.getCode();
        this.data = data;
    }

    @Override
    public String getMessage() {
        return StringUtils.isBlank(super.getMessage()) ? DEFAULT.getName() : super.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    public Serializable getData() {
        return data;
    }
}
