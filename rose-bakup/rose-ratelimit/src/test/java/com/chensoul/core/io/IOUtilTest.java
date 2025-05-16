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
package cc.rose-group.github.iore.io;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

/**
 * 
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 
 */
class IOUtilTest {

    @Test
    void copy() throws IOException {
        URL url = new URL("https://wmsps.obs.cn-north-4.myhuaweicloud.com/test/client/capturepicture/capturepicture_4bb6b451-59f3-4378-adbe-784bef71c4c2.jpeg");
        File file = new File("test.jpeg");

        IOUtils.copy(url.openStream(), new FileWriter(file));
        file.delete();
    }
}
