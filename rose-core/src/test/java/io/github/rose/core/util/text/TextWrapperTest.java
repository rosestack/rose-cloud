/*
 * Copyright © 2025 rose-group.github.io
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

public class TextWrapperTest {
    @Test
    public void simpleWrap() throws Exception {
        String longString = "Nec vox accusatoris ulla licet subditicii in his malorum quaerebatur acervis ut saltem " +
                "specie tenus crimina praescriptis legum committerentur, quod aliquotiens fecere principes saevi: sed" +
                " quicquid Caesaris implacabilitati sedisset, id velut fas iusque perpensum confestim urgebatur " +
                "impleri.";
        String wrapped = new TextWrapper().wrap(longString);
        for (String line : wrapped.split("\n")) {
            assertThat(line.length()).isLessThanOrEqualTo(120);
        }
    }

    @Test
    public void wrapPreserveShortStrings() throws Exception {
        String stringWithNewLine = "Nec vox accusatoris\n";
        assertThat(new TextWrapper().wrap(stringWithNewLine)).isEqualTo(stringWithNewLine);
        String stringWithoutNewLine = "Nec vox accusatoris";
        assertThat(new TextWrapper().wrap(stringWithoutNewLine)).isEqualTo(stringWithoutNewLine);
    }

    @Test
    public void longWordWrapStrictMode() throws Exception {
        String longWord = "abcdefghijklmnopqrstuvwxyz----abcdefghijklmnopqrstuvwxyz----abcdefghijklmnopqrstuvwxyz" +
                "----abcdefghijklmnopqrstuvwxyz----abcdefghijklmnopqrstuvwxyz----";
        String wrapped = new TextWrapper(TextWrapper.DEFAULT_WIDTH, TextWrapper.DEFAULT_CONTINUATION, true).wrap
                (longWord);
        for (String line : wrapped.split("\n")) {
            assertThat(line.length()).isLessThanOrEqualTo(80);
        }
        assertThat(wrapped).contains("—");
    }

    @Test
    public void longWordWrapLaxMode() throws Exception {
        String longWord = "abcdefghijklmnopqrstuvwxyz----abcdefghijklmnopqrstuvwxyz----abcdefghijklmnopqrstuvwxyz" +
                "----abcdefghijklmnopqrstuvwxyz----abcdefghijklmnopqrstuvwxyz----";
        String wrapped = new TextWrapper().wrap(longWord);
        for (String line : wrapped.split("\n")) {
            assertThat(line.length()).isEqualTo(longWord.length());
        }
        assertThat(wrapped).doesNotContain("—");
    }
}
