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
package io.github.rose.core.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(JsonUtilsTest.class);

    @Test
    public void convertValueTest() {
        Person person = new Person("a", 18, LocalDateTime.now(), new Date());
        Person person1 = JsonUtils.convertValue(person, Person.class);
        log.info("person1: {}", JsonUtils.toString(person1));

        Assertions.assertEquals(person.getName(), person1.getName());
        Assertions.assertNotEquals(person, person1);

        Person person2 = JsonUtils.convertValue(person, new TypeReference<Person>() {
        });
        Assertions.assertEquals(person.getName(), person2.getName());
        Assertions.assertNotEquals(person, person2);

        Person person3 = JsonUtils.convertValue(person, SimpleType.constructUnsafe(Person.class));
        Assertions.assertEquals(person.getName(), person3.getName());
        Assertions.assertNotEquals(person, person3);
    }

    @Test
    public void allowUnquotedFieldMapperTest() {
        String data = "{data: 123}";
        JsonNode actualResult = JsonUtils.readTree(data, true);

        ObjectNode expectedResult = JsonUtils.newObjectNode();
        expectedResult.put("data", 123);
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertThrows(IllegalArgumentException.class, () -> JsonUtils.readTree(data));
    }

    @Test
    public void toPrettyStringTest() {
        String data = "{\n  \"data\" : \"123\"\n}";
        JsonNode actualResult = JsonUtils.readTree(data, true);
        String toPrettyString = JsonUtils.toPrettyString(actualResult);
        Assertions.assertEquals(data, toPrettyString);
    }

    @Test
    public void toFlatMapTest() {
        String data = "{\"data\":\"123\"}";
        JsonNode actualResult = JsonUtils.readTree(data);

        Map<String, String> flatMap = JsonUtils.toFlatMap(actualResult);
        Assertions.assertEquals(data, JsonUtils.toString(flatMap));
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
    public void toPlainStringTest(String original) {
        String serialized = JsonUtils.toString(original);
        Assertions.assertNotNull(serialized);
        Assertions.assertEquals(original, JsonUtils.toPlainString(serialized));
    }

    @Test
    public void optionalMappingJDK8ModuleTest() {
        // To address the issue: Java 8 optional type `java.util.Optional` not supported
        // by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jdk8"
        // to enable handling
        assertThat(JsonUtils.toString(Optional.of("hello"))).isEqualTo("\"hello\"");
        assertThat(JsonUtils.toString(Collections.singletonList(Optional.of("abc"))))
            .isEqualTo("[\"abc\"]");
        assertThat(JsonUtils.toString(new HashSet<>(Collections.singletonList(Optional.empty()))))
            .isEqualTo("[null]");
    }

    @Test
    public void testNoEmptyConstruction() {
        Person person = new Person("a", 18, LocalDateTime.now(), new Date());
        String json = JsonUtils.toString(person);
        Person person1 = JsonUtils.readValue(json, Person.class);
        Assertions.assertEquals(person.getName(), person1.getName());
    }

    @Test
    public void testEnum() {
        String json = JsonUtils.toString(Gender.FEMALE);
        log.info("json: {}", json);
        Gender gender = JsonUtils.readValue(json, Gender.class);
        Assertions.assertEquals(Gender.FEMALE, gender);
    }

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    public static class Person {
        private final String name;
        private final Integer age;
        private final LocalDateTime birthday;
        private final Date createTime;

        public Person(String name, Integer age, LocalDateTime birthday, Date createTime) {
            this.name = name;
            this.age = age;
            this.birthday = birthday;
            this.createTime = createTime;
        }

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }

        public LocalDateTime getBirthday() {
            return birthday;
        }

        public Date getCreateTime() {
            return createTime;
        }
    }
}
