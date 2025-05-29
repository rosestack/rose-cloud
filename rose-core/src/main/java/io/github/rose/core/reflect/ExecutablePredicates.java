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

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.function.Predicate;

public final class ExecutablePredicates {

    private ExecutablePredicates() {
        // no instantiation allowed
    }

    /**
     * Checks if a candidate executable is synthetic.
     *
     * @return the predicate.
     */
    public static <T extends Executable> Predicate<T> executableIsSynthetic() {
        return candidate -> candidate != null && candidate.isSynthetic();
    }

    /**
     * Checks if a candidate executable does belong to the specified class.
     *
     * @param reference the class to check against.
     * @return the predicate.
     */
    public static <T extends Executable> Predicate<T> executableBelongsToClass(Class<?> reference) {
        return candidate -> candidate != null && reference.equals(candidate.getDeclaringClass());
    }

    /**
     * Checks if a candidate executable does belong to a class assignable as the specified class.
     *
     * @param reference the class to check against.
     * @return the predicate.
     */
    public static <T extends Executable> Predicate<T> executableBelongsToClassAssignableTo(Class<?> reference) {
        return candidate -> candidate != null && reference.isAssignableFrom(candidate.getDeclaringClass());
    }

    /**
     * Checks if a candidate executable is equivalent to the specified reference executable.
     *
     * @param reference the executable to check equivalency against.
     * @return the predicate.
     */
    public static <T extends Executable> Predicate<T> executableIsEquivalentTo(T reference) {
        Predicate<T> predicate = candidate -> candidate != null
                && candidate.getName().equals(reference.getName())
                && executableHasSameParameterTypesAs(reference).test(candidate);
        if (reference instanceof Method) {
            predicate.and(candidate -> candidate instanceof Method
                    && ((Method) candidate).getReturnType().equals(((Method) reference).getReturnType()));
        }
        return predicate;
    }

    /**
     * Checks if a candidate executable has the same parameter type as the specified reference
     * executable.
     *
     * @param reference the executable to check parameters against.
     * @return the predicate.
     */
    public static <T extends Executable> Predicate<T> executableHasSameParameterTypesAs(T reference) {
        return candidate -> {
            if (candidate == null) {
                return false;
            }
            Class<?>[] candidateParameterTypes = candidate.getParameterTypes();
            Class<?>[] referenceParameterTypes = reference.getParameterTypes();
            if (candidateParameterTypes.length != referenceParameterTypes.length) {
                return false;
            }
            for (int i = 0; i < candidateParameterTypes.length; i++) {
                if (!candidateParameterTypes[i].equals(referenceParameterTypes[i])) {
                    return false;
                }
            }
            return true;
        };
    }
}
