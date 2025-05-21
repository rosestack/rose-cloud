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
/**
 *
 */
package io.github.rose.core.reflect;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.util.List;

/**
 * Unit test for {@link TypeOf}
 */
public class TypeOfTest {

    /**
     * Test method for {@link TypeOf#getType()}.
     */
    @Test
    public void result() {
        TypeOf<List<String>> typeOf = new TypeOf<List<String>>() {
        };
        Assertions.assertThat(typeOf.getType().toString()).isEqualTo("java.util.List<java.lang.String>");
        Assertions.assertThat(typeOf.getRawType()).isEqualTo(List.class);

        TypeOf<Long> typeOf2 = new TypeOf<Long>() {
        };
        Assertions.assertThat(typeOf2.getType()).isEqualTo(Long.class);
        Assertions.assertThat(typeOf2.getRawType()).isEqualTo(Long.class);

    }

    /**
     * Test method for {@link TypeOf#getType()}.
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void getTypeWithoutParameterized() {
        StringWriter stringWriter = new StringWriter();
        try {
            new TypeOf() {
            };
            Assertions.fail("Should throw a SeedException");
        } catch (Exception e) {
            Assertions.assertThat(e.getMessage()).contains("Missing generic parameter");
        }
    }
}
