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
package io.github.rose.core.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Capture a generic type and resolve it.
 * <p>
 * For example:
 * </p>
 * <pre>
 *   new TypeOf&lt;Repository&lt;AggregateRoot&lt;Long&gt;,Long&gt;&gt;() {}
 * </pre>
 *
 * @param <T> Parameterized type to capture.
 */
public abstract class TypeOf<T> {
    private final Type type;
    private final Class<? super T> rawType;

    protected TypeOf() {
        Type superclass = getClass().getGenericSuperclass();
        if (!(superclass instanceof ParameterizedType)) {
            throw new IllegalStateException("Missing generic parameter");
        }
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
        Class<?> clazz = type instanceof Class<?> ? (Class<?>) type : (Class<?>) ((ParameterizedType) type)
                .getRawType();
        @SuppressWarnings("unchecked")
        Class<? super T> clazz2 = (Class<? super T>) clazz;
        this.rawType = clazz2;

    }

    /**
     * Returns the raw type with the generic types from this reference.
     *
     * @return parameterized type
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Returns the raw type from this reference.
     *
     * @return the rawType
     */
    public Class<? super T> getRawType() {
        return rawType;
    }
}
