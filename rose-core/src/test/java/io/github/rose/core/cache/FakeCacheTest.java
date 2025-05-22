package io.github.rose.core.cache;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
class FakeCacheTest {
    @Test
    void testGetWhenKeyNotExists() {
        Function<String, Value> loadingFunction = key -> new Value(key);

        CacheConfig<String, Value> config = CacheConfig.<String, Value>builder()
            .loadingFunction(loadingFunction)
            .build();

        Cache<String, Value> cache = Cache.create(config, FakeCache.Factory.class);

        Value value = cache.get("key");
        assertNotNull(value);
        assertEquals("key", value.getValue());

        Value value1 = cache.get("key");
        assertNotNull(value1);
        assertEquals("key", value1.getValue());

        assertNotEquals(value, value1);
    }

    @Test
    void testCreateCacheWithDefaultConfig() {
        CacheConfig<Object, Object> config = CacheConfig.builder().build();

        FakeCache.Factory factory = new FakeCache.Factory();
        Cache<Object, Object> cache = factory.createCache(config);

        assertNotNull(cache);
        assertTrue(cache instanceof FakeCache);
    }
}
