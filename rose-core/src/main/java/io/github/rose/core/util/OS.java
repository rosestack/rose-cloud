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
package io.github.rose.core.util;

import java.util.Locale;

/**
 * Operating system family.
 * <p>
 * Technically Solaris is UNIX, but for test purposes we are classifying it as a separate family.
 *
 * @since 9.2 infinispan
 */
public enum OS {
    UNIX,
    WINDOWS,
    SOLARIS,
    LINUX,
    MAC_OS;

    public static OS getCurrentOs() {
        String os = System.getProperty("os.name").toLowerCase(Locale.CHINA);
        if (os.contains("win")) {
            return WINDOWS;
        } else if (os.contains("sunos")) {
            return SOLARIS;
        } else if (os.contains("linux")) {
            return LINUX;
        } else if (os.toLowerCase(Locale.CHINA).contains("mac os")) {
            return MAC_OS;
        } else {
            return UNIX;
        }
    }
}
