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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static io.github.rose.core.util.NanoIdUtils.DEFAULT_ALPHABET;
import static io.github.rose.core.util.NanoIdUtils.DEFAULT_SIZE;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
class NanoIdUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(NanoIdUtilsTest.class);

    @Test
    public void testNanoId() {
        Assertions.assertEquals(DEFAULT_SIZE, NanoIdUtils.randomNanoId().length());
        Assertions.assertEquals(10, NanoIdUtils.randomNanoId(10).length());
        Assertions.assertEquals(10, NanoIdUtils.randomNanoId(new Random(), DEFAULT_ALPHABET, 10).length());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            NanoIdUtils.randomNanoId(null, DEFAULT_ALPHABET, 10);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            NanoIdUtils.randomNanoId(new Random(), null, 10);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            NanoIdUtils.randomNanoId(new Random(), new char[]{}, -1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            NanoIdUtils.randomNanoId(new Random(), new char[256], -1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            NanoIdUtils.randomNanoId(new Random(), DEFAULT_ALPHABET, -1);
        });
    }
}
