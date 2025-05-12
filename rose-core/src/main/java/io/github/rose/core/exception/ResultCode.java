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

    /**
     * 成功状态码
     **/
    SUCCESS(0, "SUCCESS"),

    /**
     * 参数错误
     **/
    BAD_REQUEST(1001, "PARAM_INVALID"),
    DATA_DUPLICATE(1010, "DATA_DUPLICATE"),
    NOT_FOUND(1011, "DATA_NOT_FOUND"),
    TOO_MANY_REQUESTS(1012, "TOO_MANY_REQUESTS"),

    /**
     * 权限相关
     **/
    FORBIDDEN(4000, "AUTHORIZED_FAIL"),
    UNAUTHORIZED(4001, "UNAUTHORIZED"),
    INVALID_CLIENT(4003, "INVALID_CLIENT"),
    INVALID_GRANT(4004, "INVALID_GRANT"),
    INVALID_TOKEN(4005, "INVALID_TOKEN"),

    USER_LOCKED(4010, "USER_LOCKED"),
    USER_LOGIN_ERROR(4011, "USER_LOGIN_ERROR"),
    USER_NOT_LOGIN(4012, "USER_NOT_LOGIN"),
    USER_PASSWORD_DECODE_FAIL(4013, "USER_PASSWORD_DECODE_FAIL"),
    USER_NOT_FOUND(4014, "USER_NOT_FOUND"),

    CAPTCHA_EXPIRED(4020, "CAPTCHA_EXPIRED"),
    CAPTCHA_WRONG(4021, "CAPTCHA_WRONG"),
    CAPTCHA_MISSING_PARAM_CODE(4022, "CAPTCHA_MISSING_PARAM_CODE"),
    CAPTCHA_MISSING_PARAM_KEY(4023, "CAPTCHA_MISSING_PARAM_KEY"),
    CAPTCHA_MISSING_PARAM_SMS(4024, "CAPTCHA_MISSING_PARAM_SMS"),

    INTERNAL_ERROR(5000, "INTERNAL_ERROR"),
    INTERFACE_INNER_INVOKE_ERROR(5001, "INTERFACE_INNER_INVOKE_ERROR"),
    INTERFACE_OUTER_INVOKE_ERROR(5002, "INTERFACE_OUTER_INVOKE_ERROR"),
    INTERFACE_REQUEST_TIMEOUT(5003, "INTERFACE_REQUEST_TIMEOUT"),
    INTERFACE_EXCEED_LOAD(5004, "INTERFACE_EXCEED_LOAD");

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
