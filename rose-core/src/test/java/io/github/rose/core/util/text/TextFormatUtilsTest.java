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
package io.github.rose.core.util.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link TextFormatUtils} Test
 *
 * @since 1.0.0
 */
public class TextFormatUtilsTest {

    @Test
    public void testFormat() {
        String message = TextFormatUtils.format("A,{},C,{},E", "B", "D");
        assertEquals("A,B,C,D,E", message);

        message = TextFormatUtils.format("A,{},C,{},E", "B");
        assertEquals("A,B,C,{},E", message);

        message = TextFormatUtils.format("A,{},C,{},E");
        assertEquals("A,{},C,{},E", message);

        message = TextFormatUtils.format("A,{},C,{},E", 1, 2, 3);
        assertEquals("A,1,C,2,E", message);
    }

    @Test
    public void testSubVariables() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("v1", "abc");
        variables.put("v2", "def");

        String text = "This is a test for ${v1} and ${v2}";
        String expect = "This is a test for abc and def";

        Assertions.assertEquals(expect, TextFormatUtils.substituteVariables(text, variables));
    }
}
