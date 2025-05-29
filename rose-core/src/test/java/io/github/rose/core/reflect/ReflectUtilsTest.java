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

import static io.github.rose.core.reflect.ReflectUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ReflectUtilsTest {
    private String simpleField;

    @Test
    public void testMakeAccessible() throws Exception {
        makeAccessible(ReflectUtilsTest.class.getDeclaredField("simpleField"));
    }

    @Test
    public void testSetValue() throws Exception {
        setValue(makeAccessible(ReflectUtilsTest.class.getDeclaredField("simpleField")), this, "test");
        assertThat(simpleField).isEqualTo("test");
    }

    @Test
    public void testGetValue() throws Exception {
        simpleField = "someValue";
        assertThat((String) getValue(makeAccessible(ReflectUtilsTest.class.getDeclaredField("simpleField")), this))
                .isEqualTo("someValue");
    }

    @Test
    public void testInvoke() throws Exception {
        assertThat((String) invoke(
                        makeAccessible(
                                ReflectUtilsTest.class.getDeclaredMethod("someMethod", String.class, String.class)),
                        this,
                        "a",
                        "b"))
                .isEqualTo("ab");
    }

    private String someMethod(String arg1, String arg2) {
        return arg1 + arg2;
    }
}
