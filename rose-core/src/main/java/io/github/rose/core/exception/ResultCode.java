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

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 只定义常见的、需要显示给用户的信息描述信息
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
public enum ResultCode {
    SUCCESS(0, "OK"),

    /**
     * 权限相关
     **/
    FORBIDDEN(400, "AUTHORIZED_FAIL"),
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    INVALID_CLIENT(403, "INVALID_CLIENT"),
    INVALID_GRANT(404, "INVALID_GRANT"),
    INVALID_TOKEN(405, "INVALID_TOKEN"),
    TOO_MANY_REQUESTS(429, "TOO_MANY_REQUESTS"),

    INTERNAL_ERROR(500, "INTERNAL_ERROR"),
    INTERFACE_INNER_INVOKE_ERROR(501, "INTERFACE_INNER_INVOKE_ERROR"),
    INTERFACE_OUTER_INVOKE_ERROR(502, "INTERFACE_OUTER_INVOKE_ERROR"),
    INTERFACE_REQUEST_TIMEOUT(503, "INTERFACE_REQUEST_TIMEOUT"),
    INTERFACE_EXCEED_LOAD(504, "INTERFACE_EXCEED_LOAD");

    private static Map<Integer, ResultCode> reverseLookup = Arrays.asList(ResultCode.class.getEnumConstants()).stream()
        .collect(Collectors.toMap(ResultCode::getCode, t -> t));

    Integer code;

    String name;

    ResultCode(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ResultCode fromCode(final Integer code) {
        return reverseLookup.getOrDefault(code, SUCCESS);
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
