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
package io.github.rose.core.util.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextUtils {
    private static final Pattern LINE_START_PATTERN = Pattern.compile("^.*", Pattern.MULTILINE);

    private TextUtils() {
    }

    /**
     * Inserts the specified string at the beginning of each newline of the specified text.
     *
     * @param text          the text to pad.
     * @param padding       the padding.
     * @param linesToIgnore the number of lines to ignore before starting the padding.
     * @return the padded text.
     */
    public static String leftPad(String text, String padding, int linesToIgnore) {
        StringBuilder result = new StringBuilder();
        Matcher matcher = LINE_START_PATTERN.matcher(text);

        while (matcher.find()) {
            if (linesToIgnore > 0) {
                linesToIgnore--;
            } else {
                result.append(padding);
            }
            result.append(matcher.group()).append("\n");
        }
        return result.toString();
    }
}
