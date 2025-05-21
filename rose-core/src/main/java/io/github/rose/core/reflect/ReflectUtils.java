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

import io.github.rose.core.exception.RoseErrorCode;
import io.github.rose.core.exception.RoseException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

public final class ReflectUtils {
    private ReflectUtils() {
        // no instantiation allowed
    }

    /**
     * Makes an {@link AccessibleObject} accessible by invoking
     * {@link AccessibleObject#setAccessible(boolean)}
     * in a {@link PrivilegedAction}.
     *
     * @param accessibleObject the object to make accessible.
     * @param <T>              the type of object.
     * @return the object made accessible.
     */
    public static <T extends AccessibleObject> T makeAccessible(T accessibleObject) {
        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            accessibleObject.setAccessible(true);
            return null;
        });
        return accessibleObject;
    }

    /**
     * Invokes the specified method while wrapping checked exception in a {@link RoseException}.
     *
     * @param method the method.
     * @param self   the instance to invoke the method on.
     * @param args   the method arguments.
     * @param <T>    the type of the return value.
     * @return the method returned value.
     */
    @SuppressWarnings("unchecked")
    public static <T> T invoke(Method method, Object self, Object... args) {
        try {
            return (T) method.invoke(self, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw RoseException.wrap(e, RoseErrorCode.UNABLE_TO_INVOKE_METHOD)
                .put("method", method.toGenericString());
        }
    }

    /**
     * Sets the specified value into the specified field while wrapping checked exception in a {@link
     * RoseException}.
     *
     * @param field the field.
     * @param self  the instance to set the field on.
     * @param value the value to set.
     */
    public static void setValue(Field field, Object self, Object value) {
        try {
            field.set(self, value);
        } catch (IllegalAccessException e) {
            throw RoseException.wrap(e, RoseErrorCode.UNABLE_TO_SET_FIELD)
                .put("field", field.toGenericString());
        }
    }

    /**
     * Sets the specified field value while wrapping checked exception in a {@link RoseException}.
     *
     * @param field the field.
     * @param self  the instance to get the value from.
     * @param <T>   the type of the field value.
     * @return the field value.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValue(Field field, Object self) {
        try {
            return (T) field.get(self);
        } catch (IllegalAccessException e) {
            throw RoseException.wrap(e, RoseErrorCode.UNABLE_TO_GET_FIELD)
                .put("field", field.toGenericString());
        }
    }
}
