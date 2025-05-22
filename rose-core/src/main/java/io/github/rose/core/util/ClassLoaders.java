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
package io.github.rose.core.util;

public final class ClassLoaders {
    private ClassLoaders() {
        // no instantiation allowed
    }

    /**
     * Find the most complete class loader by trying the current thread context class loader, then
     * the classloader of the
     * given class if any, then the class loader that loaded SEED core, then the system class loader.
     *
     * @param target the class to get the class loader from if no current thread context class
     *               loader is present. May be null.
     * @return the most complete class loader it found.
     */
    public static ClassLoader findMostCompleteClassLoader(Class<?> target) {
        // Try the most complete class loader we can get
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // Then fallback to the class loader from a specific class given
        if (classLoader == null && target != null) {
            classLoader = target.getClassLoader();
        }

        // Then fallback to the class loader that loaded this class
        if (classLoader == null) {
            classLoader = ClassLoaders.class.getClassLoader();
        }

        // Then fallback to the system class loader
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }

        // Throw an exception if no classloader was found at all
        if (classLoader == null) {
            throw new RuntimeException("Unable to find a classloader");
        }

        return classLoader;
    }

    /**
     * Find the most complete class loader by trying the current thread context class loader, then
     * the class loader
     * that loaded Shed, then the system class loader.
     *
     * @return the most complete class loader found.
     */
    public static ClassLoader findMostCompleteClassLoader() {
        return ClassLoaders.findMostCompleteClassLoader(null);
    }
}
