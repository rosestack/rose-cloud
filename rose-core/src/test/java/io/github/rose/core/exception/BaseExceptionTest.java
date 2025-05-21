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
package io.github.rose.core.exception;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BaseExceptionTest {
    @Test
    public void createNewTotoException1() {
        assertThrows(TotoException.class, () -> {
            throw BaseException.createNew(TotoException.class, TotoErrorCode.CARAMBAR_MODE)
                    .put("key1", "value1")
                    .put("key2", "value2");
        });
    }

    @Test
    public void wrap_should_work_fine_with_descendant() {
        assertThrows(TotoException.class, () -> {
            try {
                throw new NullPointerException();
            } catch (Exception exception) {
                throw BaseException.wrap(TotoException.class, exception, TotoErrorCode.JOKE_MODE)
                        .put("Error Code", "this is how we do it !");
            }
        });
    }

    @Test
    public void wrap_should_work_fine_with_change_of_error_code() {
        assertThrows(BaseException.class, () -> {
            try {
                throw new TotoException(TotoErrorCode.JOKE_MODE);
            } catch (TotoException e) {
                throw BaseException.wrap(TotoException.class, e, TotoErrorCode.CARAMBAR_MODE);
            }
        });
    }

    @Test
    public void wrap_should_work_fine() {
        assertThrows(BaseException.class, () -> {
            try {
                throw new TotoException(TotoErrorCode.JOKE_MODE);
            } catch (TotoException exception) {
                throw BaseException.wrap(TotoException.class, exception, TotoErrorCode.JOKE_MODE)
                        .put("Error Code", "this is how we do it !");
            }
        });
    }

    @Test
    public void multiple_causes_should_be_visible() {
        StringWriter stringWriter = new StringWriter();
        BaseException.wrap(TotoException.class, BaseException.wrap(TotoException.class, new RuntimeException("yop"),
                TotoErrorCode.CARAMBAR_MODE), TotoErrorCode.JOKE_MODE).printStackTrace(new PrintWriter(stringWriter));
        String text = stringWriter.toString();

        assertThat(text).contains("Caused by: java.lang.RuntimeException: yop");
        assertThat(text).contains("Caused by: io.github.rose.core.exception.BaseExceptionTest$TotoException: " +
                "Carambar mode");
        assertThat(text).contains("io.github.rose.core.exception.BaseExceptionTest$TotoException: Joke mode");
    }

    @Test
    public void infoShouldBeLoaded() {
        BaseException seedException = BaseException.createNew(TotoException.class, TotoErrorCode.CARAMBAR_MODE)
                .put("who", "World")
                .put("tld", "com");
        String text = seedException.toString();
        assertThat(text).contains("Hello World!");
        assertThat(text).contains("Some fix");
        assertThat(text).contains("http://some.url.com");
    }

    private enum TotoErrorCode implements ErrorCode {
        JOKE_MODE, CARAMBAR_MODE;
    }

    private static class TotoException extends BaseException {
        private static final long serialVersionUID = 1L;

        private TotoException(ErrorCode errorCode) {
            super(errorCode);
        }

        private TotoException(ErrorCode errorCode, Throwable throwable) {
            super(errorCode, throwable);
        }
    }
}
