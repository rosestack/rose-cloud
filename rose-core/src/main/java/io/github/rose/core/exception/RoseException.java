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
package io.github.rose.core.exception;

/**
 * This is the class for all Shed exceptions.
 */
public class RoseException extends BaseException {
    protected RoseException(ErrorCode errorCode) {
        super(errorCode);
    }

    protected RoseException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    /**
     * Create a new SeedException from an {@link ErrorCode}.
     *
     * @param errorCode the error code to set.
     * @return the created SeedException.
     */
    public static RoseException createNew(ErrorCode errorCode) {
        return new RoseException(errorCode);
    }

    /**
     * Wrap a SeedException with an {@link ErrorCode} around an existing {@link Throwable}.
     *
     * @param throwable the existing throwable to wrap.
     * @param errorCode the error code to set.
     * @return the created SeedException.
     */
    public static RoseException wrap(Throwable throwable, ErrorCode errorCode) {
        return new RoseException(errorCode, throwable);
    }
}
