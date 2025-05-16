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

/**
 * 业务异常，http 状态返回码为 200
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class BusinessException extends RuntimeException {

    private static final ResultCode DEFAULT = ResultCode.INTERNAL_ERROR;

    private final Integer code;

    public BusinessException() {
        this(DEFAULT);
    }

    public BusinessException(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getName());
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        this(DEFAULT.getCode(), message);
    }

    @Override
    public String getMessage() {
        return StringUtils.isBlank(super.getMessage()) ? DEFAULT.getName() : super.getMessage();
    }

    public Integer getCode() {
        return code;
    }
}
