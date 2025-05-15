package io.github.rose.filter;

import org.junit.jupiter.api.Test;
import org.owasp.encoder.Encode;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
class EncodeTest {
    @Test
    public void test() {
        String url = "https://www.example.com/?param=" + Encode.forUriComponent("<script>alert(\"XSS Attack\");</script>");
        System.out.println(url);

        String userInput = "<script>alert(\"XSS Attack\");</script>";
        String escapedHtml = Encode.forHtml(userInput);
        System.out.println(escapedHtml);
    }
}
