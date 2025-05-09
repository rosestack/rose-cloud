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
package io.github.rose.core.groovy;

import java.util.Map;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface ExecutableScript extends AutoCloseable {

    /**
     * Execute t.
     *
     * @param <T>   the type parameter
     * @param args  the args
     * @param clazz the clazz
     * @return the t
     */
    <T> T execute(Object[] args, Class<T> clazz) throws Throwable;

    /**
     * Execute.
     *
     * @param args the args
     */
    void execute(Object[] args) throws Throwable;

    /**
     * Execute t.
     *
     * @param <T>         the type parameter
     * @param args        the args
     * @param clazz       the clazz
     * @param failOnError the fail on error
     * @return the t the throwable
     */
    <T> T execute(Object[] args, Class<T> clazz, boolean failOnError) throws Throwable;

    /**
     * Execute t.
     *
     * @param <T>        the type parameter
     * @param methodName the method name
     * @param clazz      the clazz
     * @param args       the args
     * @return the t
     */
    <T> T execute(String methodName, Class<T> clazz, Object... args) throws Throwable;

    /**
     * Sets binding.
     *
     * @param args the args
     */
    default void setBinding(final Map<String, Object> args) {
    }

    @Override
    default void close() {
    }
}
