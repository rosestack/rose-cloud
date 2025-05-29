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

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since
 */
class MapsTest {

    @Test
    void of() {
        Map<String, String> map = Maps.of("key", "value");
        Assertions.assertEquals(1, map.size());
        Assertions.assertEquals("value", map.get("key"));
    }

    @Test
    void of1() {
        Map<String, String> map = Maps.of("key", "value", "key2", "value2");
        Assertions.assertEquals(2, map.size());
        Assertions.assertEquals("value", map.get("key"));
        Assertions.assertEquals("value2", map.get("key2"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Maps.of("key", "value", "other");
        });
    }
}
