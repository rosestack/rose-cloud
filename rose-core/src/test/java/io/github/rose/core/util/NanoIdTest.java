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

import static io.github.rose.core.util.NanoId.DEFAULT_ALPHABET;
import static io.github.rose.core.util.NanoId.DEFAULT_SIZE;

import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since
 */
class NanoIdTest {
    @Test
    void testNanoId() {
        Assertions.assertEquals(DEFAULT_SIZE, NanoId.randomNanoId().length());
        Assertions.assertEquals(10, NanoId.randomNanoId(10).length());
        Assertions.assertEquals(
                10, NanoId.randomNanoId(new Random(), DEFAULT_ALPHABET, 10).length());

        Random random = new Random();
        Assertions.assertThrows(IllegalArgumentException.class, () -> NanoId.randomNanoId(null, DEFAULT_ALPHABET, 10));

        Assertions.assertThrows(IllegalArgumentException.class, () -> NanoId.randomNanoId(random, null, 10));

        Assertions.assertThrows(IllegalArgumentException.class, () -> NanoId.randomNanoId(random, new char[] {}, -1));

        Assertions.assertThrows(IllegalArgumentException.class, () -> NanoId.randomNanoId(random, new char[256], -1));

        Assertions.assertThrows(
                IllegalArgumentException.class, () -> NanoId.randomNanoId(random, DEFAULT_ALPHABET, -1));
    }
}
