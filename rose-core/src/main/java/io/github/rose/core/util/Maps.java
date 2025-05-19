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

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Maps {
    public static Map of() {
        return Collections.unmodifiableMap(new ConcurrentHashMap());
    }

    public static Map of(Object key, Object value) {
        if (key == null || value == null) {
            throw new NullPointerException("Key or value cannot be null");
        }
        return Collections.singletonMap(key, value);
    }

    @SafeVarargs
    public static Map of(Object... values) {
        int length = values.length;
        if (length % 2 != 0) {
            throw new IllegalArgumentException("Keys and values must be provided in pairs");
        }
        Map map = new ConcurrentHashMap<>((length / 2) + 1);
        for (int i = 0; i < length; i += 2) {
            Object key = values[i];
            Object value = values[i + 1];
            if (key == null || value == null) {
                throw new NullPointerException("Key or value cannot be null");
            }
            map.put(key, value);
        }
        return Collections.unmodifiableMap(map);
    }
}
