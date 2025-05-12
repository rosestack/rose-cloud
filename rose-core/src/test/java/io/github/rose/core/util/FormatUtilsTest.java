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
package io.github.rose.core.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * {@link FormatUtils} Test
 *
 * @since 1.0.0
 */
public class FormatUtilsTest {

    @Test
    public void testFormat() {
        String message = FormatUtils.format("A,{},C,{},E", "B", "D");
        assertEquals("A,B,C,D,E", message);

        message = FormatUtils.format("A,{},C,{},E", "B");
        assertEquals("A,B,C,{},E", message);

        message = FormatUtils.format("A,{},C,{},E");
        assertEquals("A,{},C,{},E", message);

        message = FormatUtils.format("A,{},C,{},E", 1, 2, 3);
        assertEquals("A,1,C,2,E", message);
    }
}
