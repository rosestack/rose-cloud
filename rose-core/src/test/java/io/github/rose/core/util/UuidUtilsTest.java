package io.github.rose.core.util;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.rose.core.json.JsonUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UuidUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(UuidUtilsTest.class);

    @Test
    public void testGetUUID() {
        UUID uuid = UuidUtils.getUUID();
        log.info("uuid: {}", uuid);
        // Encode for URL
        String urlSafeId = UuidUtils.uuidToBase64(uuid);
        log.info("URL Safe ID:   {}", urlSafeId); // e.g., AZZK6H62dLudJ60SgK7tTQ

        // Decode from URL
        UUID decodedUuid = UuidUtils.base64ToUuid(urlSafeId);
        log.info("Decoded UUID:  {}", decodedUuid);

        log.info("Match: {}", uuid.equals(decodedUuid));
        Assertions.assertEquals(uuid, decodedUuid);
    }

    @Test
    public void testUuidInMap() {
        Map<String, Object> map = new HashMap<>();
        UUID uuid = UuidUtils.getUUID();
        map.put("key", uuid);
        log.info("Map: " + map);
        String json = JsonUtils.toString(map);
        log.info("Json: " + json);
        Map<String, Object> map2 = JsonUtils.readValue(json, new TypeReference<Map<String, Object>>() {
        });
        log.info("Map2: " + map2);
    }
}
