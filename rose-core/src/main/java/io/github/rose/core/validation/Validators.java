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
package io.github.rose.core.validation;

import io.github.rose.core.exception.BusinessException;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class Validators {

    public static final Pattern PROPERTY_PATTERN = Pattern.compile("^[\\p{L}0-9_-]+$"); // Unicode
    // letters,
    // numbers,
    // '_'
    // and
    // '-'
    // allowed

    public static void checkNotBlank(String val, String errorMessage) {
        if (StringUtils.isBlank(val)) {
            throw new BusinessException(errorMessage);
        }
    }

    public static void checkNotBlank(String val, Function<String, String> errorMessageFunction) {
        if (StringUtils.isBlank(val)) {
            throw new BusinessException(errorMessageFunction.apply(val));
        }
    }

    public static void checkPositiveNumber(long val, String errorMessage) {
        if (val <= 0) {
            throw new BusinessException(errorMessage);
        }
    }

    public static boolean isValidProperty(String key) {
        return StringUtils.isEmpty(key) || PROPERTY_PATTERN.matcher(key).matches();
    }

    public static <T> T checkNotNull(T reference) {
        return checkNotNull(reference, "请求的记录不存在");
    }

    public static <T> T checkNotNull(T reference, String notFoundMessage) {
        if (reference == null) {
            throw new BusinessException(notFoundMessage);
        }
        return reference;
    }

    public static <T> T checkNotNull(Optional<T> reference) {
        return checkNotNull(reference, "请求的记录不存在");
    }

    public static <T> T checkNotNull(Optional<T> reference, String notFoundMessage) {
        if (reference.isPresent()) {
            return reference.get();
        } else {
            throw new BusinessException(notFoundMessage);
        }
    }
}
