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
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

public final class ClassPredicates {
    private ClassPredicates() {
        // no instantiation allowed
    }

    /**
     * Checks if a candidate class is equal to the specified class.
     *
     * @param reference the class to check for.
     * @return the predicate.
     */
    public static Predicate<Class<?>> classIs(final Class<?> reference) {
        return candidate -> candidate != null && candidate.equals(reference);
    }

    /**
     * Check if a candidate class is assignable to the specified class.
     *
     * @param ancestor the class to check for.
     * @return the predicate.
     */
    public static Predicate<Class<?>> classIsAssignableFrom(Class<?> ancestor) {
        return candidate -> candidate != null && ancestor.isAssignableFrom(candidate);
    }

    /**
     * Check if a candidate class is strictly a descendant of the specified class (not the
     * specified class itself).
     *
     * @param ancestor the ancestor class to check for.
     * @return the predicate.
     */
    public static Predicate<Class<?>> classIsDescendantOf(Class<?> ancestor) {
        return candidate -> candidate != null && candidate != ancestor && ancestor.isAssignableFrom(candidate);
    }

    /**
     * Checks if a candidate class is an interface.
     *
     * @return the predicate.
     */
    public static Predicate<Class<?>> classImplements(Class<?> anInterface) {
        if (!anInterface.isInterface()) {
            throw new IllegalArgumentException("Class " + anInterface.getName() + " is not an interface");
        }
        return candidate -> candidate != null && !candidate.isInterface() && anInterface.isAssignableFrom(candidate);
    }

    /**
     * Checks if a candidate class is an interface.
     *
     * @return the predicate.
     */
    public static Predicate<Class<?>> classIsInterface() {
        return candidate -> candidate != null && candidate.isInterface();
    }

    /**
     * Checks if a candidate class is an annotation.
     *
     * @return the predicate.
     */
    public static Predicate<Class<?>> classIsAnnotation() {
        return candidate -> candidate != null && candidate.isAnnotation();
    }

    /**
     * Checks if a candidate class has the specified modifier.
     *
     * @param modifier the modifier to check for.
     * @return the predicate.
     */
    public static Predicate<Class<?>> classModifierIs(final int modifier) {
        return candidate -> (candidate.getModifiers() & modifier) != 0;
    }

    /**
     * Checks if a candidate class has the specified modifier.
     *
     * @param modifier the modifier to check for.
     * @return the predicate.
     */
    public static <T extends Executable> Predicate<T> executableModifierIs(final int modifier) {
        return candidate -> (candidate.getModifiers() & modifier) != 0;
    }

    /**
     * Checks if a candidate class implements at least one interface.
     *
     * @return the predicate.
     */
    public static Predicate<Class<?>> atLeastOneInterfaceImplemented() {
        return candidate -> candidate != null && candidate.getInterfaces().length > 0;
    }

    /**
     * Checks if a candidate class has at least one public constructor.
     *
     * @return the predicate.
     */
    public static Predicate<Class<?>> atLeastOneConstructorIsPublic() {
        return candidate -> candidate != null
                && Classes.from(candidate).constructors().anyMatch(executableModifierIs(Modifier.PUBLIC));
    }
}
