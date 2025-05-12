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
package io.github.rose.core.jackson;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class JacksonUtilsTest {

    @Test
    public void allowUnquotedFieldMapperTest() {
        String data = "{data: 123}";
        JsonNode actualResult = JacksonUtils.toJsonNode(data, JacksonUtils.ALLOW_UNQUOTED_FIELD_NAMES_MAPPER); // should
        // be:
        // {"data":
        // 123}
        ObjectNode expectedResult = JacksonUtils.newObjectNode();
        expectedResult.put("data", 123); // {"data": 123}
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertThrows(IllegalArgumentException.class, () -> JacksonUtils.toJsonNode(data)); // syntax
        // exception
        // due
        // to
        // missing
        // quotes
        // in
        // the
        // field
        // name!
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "",
                "false",
                "\"",
                "\"\"",
                "\"This is a string with double quotes\"",
                "Path: /home/developer/test.txt",
                "First line\nSecond line\n\nFourth line",
                "Before\rAfter",
                "Tab\tSeparated\tValues",
                "Test\bbackspace",
                "[]",
                "[1, 2, 3]",
                "{\"key\": \"value\"}",
                "{\n\"temperature\": 25.5,\n\"humidity\": 50.2\n\"}",
                "Expression: (a + b) * c",
                "世界",
                "Україна",
                "\u1F1FA\u1F1E6",
                "🇺🇦"
            })
    public void toPlainTextTest(String original) {
        String serialized = JacksonUtils.toString(original);
        Assertions.assertNotNull(serialized);
        Assertions.assertEquals(original, JacksonUtils.toPlainText(serialized));
    }

    @Test
    public void optionalMappingJDK8ModuleTest() {
        // To address the issue: Java 8 optional type `java.util.Optional` not supported
        // by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jdk8"
        // to enable handling
        assertThat(JacksonUtils.toString(Optional.of("hello"))).isEqualTo("\"hello\"");
        assertThat(JacksonUtils.toString(Collections.singletonList(Optional.of("abc"))))
                .isEqualTo("[\"abc\"]");
        assertThat(JacksonUtils.toString(new HashSet<>(Collections.singletonList(Optional.empty()))))
                .isEqualTo("[null]");
    }
}
