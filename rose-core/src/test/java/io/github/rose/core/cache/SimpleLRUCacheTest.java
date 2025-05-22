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
class SimpleLRUCacheTest {
    @Test
    void testGetWhenKeyNotExists() {
        Function<String, Value> loadingFunction = key -> new Value(key);

        CacheConfig<String, Value> config = CacheConfig.<String, Value>builder()
            .initialSize(2)
            .maxSize(2)
            .loadingFunction(loadingFunction)
            .build();

        Cache<String, Value> cache = Cache.create(config, SimpleLRUCache.Factory.class);

        Value value = cache.get("key");
        assertNotNull(value);
        assertEquals("key", value.getValue());

        cache.get("key1");
        cache.get("key2");

        Value value2 = cache.get("key");
        assertNotEquals(value, value2);
    }

    @Test
    void testCreateCacheWithDefaultConfig() {
        CacheConfig<Object, Object> config = CacheConfig.builder().build();

        SimpleLRUCache.Factory factory = new SimpleLRUCache.Factory();
        Cache<Object, Object> cache = factory.createCache(config);

        assertNotNull(cache);
        assertTrue(cache instanceof FakeCache);
    }
}
