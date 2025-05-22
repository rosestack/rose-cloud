package io.github.rose.core.cache;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class UnboundedCacheTest {

    @Test
    void testGetWhenKeyNotExists() {
        Function<String, Value> loadingFunction = key -> new Value(key);

        CacheConfig<String, Value> config = CacheConfig.<String, Value>builder()
            .initialSize(2)
            .maxSize(4)
            .loadingFunction(loadingFunction)
            .build();

        Cache<String, Value> cache = Cache.create(config, UnboundedCache.Factory.class);

        Value value = cache.get("key");
        assertNotNull(value);
        assertEquals("key", value.getValue());

        Value value1 = cache.get("key");
        assertNotNull(value);

        assertEquals(value1, value);
    }

    @Test
    void testCreateCacheWithDefaultConfig() {
        CacheConfig<Object, Object> config = CacheConfig.builder().build();

        UnboundedCache.Factory factory = new UnboundedCache.Factory();
        Cache<Object, Object> cache = factory.createCache(config);

        assertNotNull(cache);
        assertTrue(cache instanceof UnboundedCache);
    }
}
