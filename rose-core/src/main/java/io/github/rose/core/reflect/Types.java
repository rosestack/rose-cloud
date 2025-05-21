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
package io.github.rose.core.reflect;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public final class Types {

    private Types() {
        // no instantiation allowed
    }

    public static String simpleNameOf(Type type) {
        return buildTypeName(type, new StringBuilder(), true, false).toString();
    }

    public static String canonicalNameOf(Type type) {
        return buildTypeName(type, new StringBuilder(), false, true).toString();
    }

    public static String nameOf(Type type) {
        return buildTypeName(type, new StringBuilder(), false, false).toString();
    }

    /**
     * Returns the raw class of the specified type.
     *
     * @param type the type.
     * @return the raw class.
     */
    public static Class<?> rawClassOf(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            return (Class<?>) rawType;
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(rawClassOf(componentType), 0).getClass();
        } else if (type instanceof TypeVariable) {
            return Object.class;
        } else {
            throw new IllegalArgumentException("Unsupported type " + type.getTypeName());
        }
    }

    public static Type getTypeArgument(Type type) {
        Type[] typeArguments = getTypeArguments(type);

        return typeArguments.length > 1 ? typeArguments[0] : null;
    }

    public static Type[] getTypeArguments(Type type) {
        if (null == type) {
            return new Type[]{};
        }
        ParameterizedType parameterizedType = toParameterizedType(type);
        return null == parameterizedType ? new Type[]{} : parameterizedType.getActualTypeArguments();
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
        List<ParameterizedType> result = new ArrayList<>();
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

        return result.toArray(new ParameterizedType[0]);
    }

    private static StringBuilder buildTypeName(Type type, StringBuilder sb, boolean simpleName,
                                               boolean canonicalName) {
        if (type instanceof ParameterizedType) {
            buildTypeName(((ParameterizedType) type).getRawType(), sb, simpleName, canonicalName);
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            sb.append("<");
            buildGenericTypeNames(actualTypeArguments, sb, simpleName, canonicalName);
            sb.append(">");
        } else if (type instanceof Class) {
            sb.append(simpleName ? ((Class) type).getSimpleName()
                : (canonicalName ? ((Class) type).getCanonicalName() : ((Class) type).getName()));
        } else if (type instanceof WildcardType) {
            sb.append("?");
            Type[] lowerBounds = ((WildcardType) type).getLowerBounds();
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if (lowerBounds.length > 0) {
                sb.append(" super ");
                buildGenericTypeNames(lowerBounds, sb, simpleName, canonicalName);
            } else if (upperBounds.length > 0) {
                if (upperBounds.length > 1 || !upperBounds[0].equals(Object.class)) {
                    sb.append(" extends ");
                    buildGenericTypeNames(upperBounds, sb, simpleName, canonicalName);
                }
            }
        }
        return sb;
    }

    private static void buildGenericTypeNames(Type[] actualTypeArguments, StringBuilder sb,
                                              boolean simpleName, boolean canonicalName) {
        for (int i = 0; i < actualTypeArguments.length; i++) {
            Type typeArgument = actualTypeArguments[i];
            buildTypeName(typeArgument, sb, simpleName, canonicalName);
            if (i < actualTypeArguments.length - 1) {
                sb.append(", ");
            }
        }
    }
}
