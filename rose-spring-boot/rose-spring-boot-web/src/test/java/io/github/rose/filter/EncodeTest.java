package io.github.rose.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.owasp.encoder.Encode;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since
 */
class EncodeTest {
    @Test
    void test() {
        String url = "https://www.example.com/?param=" + Encode.forUriComponent("<script>alert(\"XSS Attack\");</script>");
        Assertions.assertNotNull(url);

        String userInput = "<script>alert(\"XSS Attack\");</script>";
        String escapedHtml = Encode.forHtml(userInput);
        Assertions.assertNotNull(escapedHtml);

    }
}
