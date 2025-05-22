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


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TextUtilsTest {
    @Test
    public void leftPad() throws Exception {
        String multiLineString = "abcdefghijklmnopqrstuvwxyz\nabcdefghijklmnopqrstuvwxyz\nabcdefghijklmnopqrstuvwxyz";
        String padded = TextUtils.leftPad(multiLineString, "***", 1);
        boolean first = true;
        for (String s : padded.split("\\n")) {
            if (first) {
                assertThat(s).isEqualTo("abcdefghijklmnopqrstuvwxyz");
                first = false;
            } else {
                assertThat(s).isEqualTo("***abcdefghijklmnopqrstuvwxyz");
            }
        }
    }
}
