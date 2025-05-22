/*
 * Copyright © 2025 rosestack.github.io
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
package io.github.rose.mybatis.extension.query;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.MPJWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import java.util.Collection;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * 拓展 MyBatis Plus Join QueryWrapper 类，主要增加如下功能：
 * <p>
 * 1. 拼接条件的方法，增加 xxxIfPresent 方法，用于判断值不存在的时候，不要拼接到条件中。
 *
 * @param <T> 数据类型
 */
public class MPJLambdaWrapperX<T> extends MPJLambdaWrapper<T> {

    public MPJLambdaWrapperX<T> likeIfPresent(SFunction<T, ?> column, String val) {
        MPJWrappers.lambdaJoin().like(column, val);
        if (StringUtils.hasText(val)) {
            return (MPJLambdaWrapperX<T>) super.like(column, val);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> inIfPresent(SFunction<T, ?> column, Collection<?> values) {
        if (ObjectUtils.isNotEmpty(values)) {
            return (MPJLambdaWrapperX<T>) super.in(column, values);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> inIfPresent(SFunction<T, ?> column, Object... values) {
        if (ObjectUtils.isNotEmpty(values)) {
            return (MPJLambdaWrapperX<T>) super.in(column, values);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> eqIfPresent(SFunction<T, ?> column, Object val) {
        if (ObjectUtils.isNotEmpty(val)) {
            return (MPJLambdaWrapperX<T>) super.eq(column, val);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> neIfPresent(SFunction<T, ?> column, Object val) {
        if (ObjectUtils.isNotEmpty(val)) {
            return (MPJLambdaWrapperX<T>) super.ne(column, val);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> gtIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (MPJLambdaWrapperX<T>) super.gt(column, val);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> geIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (MPJLambdaWrapperX<T>) super.ge(column, val);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> ltIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (MPJLambdaWrapperX<T>) super.lt(column, val);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> leIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (MPJLambdaWrapperX<T>) super.le(column, val);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> betweenIfPresent(SFunction<T, ?> column, Object val1, Object val2) {
        if (val1 != null && val2 != null) {
            return (MPJLambdaWrapperX<T>) super.between(column, val1, val2);
        }
        if (val1 != null) {
            return (MPJLambdaWrapperX<T>) ge(column, val1);
        }
        if (val2 != null) {
            return (MPJLambdaWrapperX<T>) le(column, val2);
        }
        return this;
    }

    public MPJLambdaWrapperX<T> betweenIfPresent(SFunction<T, ?> column, Object[] values) {
        Object val1 = ArrayUtils.get(values, 0);
        Object val2 = ArrayUtils.get(values, 1);
        return betweenIfPresent(column, val1, val2);
    }
}
