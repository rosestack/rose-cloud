package io.github.rose.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since
 */
class LocaleTest {
    @Test
    void test() {
        Locale locale1 = Locale.getDefault(Locale.Category.FORMAT);
        Locale locale2 = Locale.getDefault();
        Assertions.assertEquals(locale1, locale2);
    }
}
