package io.github.rose.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
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
