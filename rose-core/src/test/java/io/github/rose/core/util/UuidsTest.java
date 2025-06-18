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

import io.github.rose.core.json.JsonUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class UuidsTest {
    private static final Logger log = LoggerFactory.getLogger(UuidsTest.class);

    @Test
    void testGetUUID() {
        UUID uuid = Uuids.getUUID();
        log.info("uuid: {}", uuid);
        // Encode for URL
        String urlSafeId = Uuids.uuidToBase64(uuid);
        log.info("URL Safe ID:   {}", urlSafeId); // e.g., AZZK6H62dLudJ60SgK7tTQ

        // Decode from URL
        UUID decodedUuid = Uuids.base64ToUuid(urlSafeId);
        log.info("Decoded UUID:  {}", decodedUuid);

        log.info("Match: {}", uuid.equals(decodedUuid));
        Assertions.assertEquals(uuid, decodedUuid);
    }

    @Test
    void testUuidInMap() {
        Map<String, Object> map = new HashMap<>();
        UUID uuid = Uuids.getUUID();
        map.put("key", uuid);
        log.info("Map: " + map);
        String json = JsonUtils.toJson(map);
        log.info("Json: " + json);
        Map<String, Object> map2 = JsonUtils.toMap(json);
        log.info("Map2: " + map2);
    }
}
