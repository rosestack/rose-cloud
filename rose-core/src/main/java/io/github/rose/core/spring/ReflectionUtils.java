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
package io.github.rose.core.spring;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
public class ReflectionUtils extends org.springframework.util.ReflectionUtils {
    public static Type getTypeArgument(Type type) {
        return getTypeArgument(type, 0);
    }

    public static Type getTypeArgument(Type type, int index) {
        Type[] typeArguments = getTypeArguments(type);
        return null != typeArguments && typeArguments.length > index ? typeArguments[index] : null;
    }

    public static Type[] getTypeArguments(Type type) {
        if (null == type) {
            return null;
        } else {
            ParameterizedType parameterizedType = toParameterizedType(type);
            return null == parameterizedType ? null : parameterizedType.getActualTypeArguments();
        }
    }

    public static ParameterizedType toParameterizedType(Type type) {
        return toParameterizedType(type, 0);
    }

    public static ParameterizedType toParameterizedType(Type type, int interfaceIndex) {
        if (type instanceof ParameterizedType) {
            return (ParameterizedType) type;
        } else {
            if (type instanceof Class) {
                ParameterizedType[] generics = getGenerics((Class) type);
                if (generics.length > interfaceIndex) {
                    return generics[interfaceIndex];
                }
            }

            return null;
        }
    }

    public static ParameterizedType[] getGenerics(Class<?> clazz) {
        List<ParameterizedType> result = new ArrayList();
        Type genericSuper = clazz.getGenericSuperclass();
        if (null != genericSuper && !Object.class.equals(genericSuper)) {
            ParameterizedType parameterizedType = toParameterizedType(genericSuper);
            if (null != parameterizedType) {
                result.add(parameterizedType);
            }
        }

        Type[] genericInterfaces = clazz.getGenericInterfaces();
        if (ArrayUtils.isNotEmpty(genericInterfaces)) {
            for (Type genericInterface : genericInterfaces) {
                ParameterizedType parameterizedType = toParameterizedType(genericInterface);
                if (null != parameterizedType) {
                    result.add(parameterizedType);
                }
            }
        }

        return (ParameterizedType[]) result.toArray(new ParameterizedType[0]);
    }
}
