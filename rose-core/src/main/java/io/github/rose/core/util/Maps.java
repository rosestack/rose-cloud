package io.github.rose.core.util;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Maps {
    public static final float MIN_LOAD_FACTOR = 0.75f;

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
        Map map = new ConcurrentHashMap<>((length / 2) + 1, MIN_LOAD_FACTOR);
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
