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
package io.github.rose.oss.old.storage;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * @author Levin
 */
public class FileUtils {

    private static final String SEPARATOR = "/";

    /**
     * 根据旧的名称生成新的名称
     *
     * @param originName originName
     * @return 生成结果
     */
    public static String randomName(String originName) {
        final String uuid = UUID.randomUUID().toString();
        if (StringUtils.isBlank(originName)) {
            return uuid;
        }
        final String extension = FileNameUtils.getExtension(originName);
        return uuid + "." + extension;
    }

    public static String targetName(boolean random, String prefix, String originName) {
        return buildTargetName(random, prefix, originName).replaceAll("//", "/");
    }

    private static String buildTargetName(boolean random, String prefix, String originName) {
        if (!random) {
            return StringUtils.join(SEPARATOR, originName);
        }
        final String name = randomName(originName);
        if (StringUtils.isNotBlank(name)) {
            return StringUtils.isBlank(prefix) ? name : StringUtils.join(prefix, SEPARATOR, name);
        }
        return StringUtils.isBlank(prefix) ? name : StringUtils.join(prefix, name);
    }
}
