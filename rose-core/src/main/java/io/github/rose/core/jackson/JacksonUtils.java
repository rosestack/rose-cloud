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
package io.github.rose.core.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import io.github.rose.core.util.StringPool;
import io.github.rose.core.validation.Views;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class JacksonUtils {
    public static final ObjectMapper OBJECT_MAPPER = getObjectMapperWithJavaTimeModule();
    public static final ObjectMapper PRETTY_SORTED_JSON_MAPPER = JsonMapper.builder()
        .addModule(new Java8TimeModule())
        .addModule(new Jdk8Module())
        .enable(SerializationFeature.INDENT_OUTPUT)
        .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
        .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
        .build();
    public static final ObjectMapper IGNORE_UNKNOWN_PROPERTIES_JSON_MAPPER = JsonMapper.builder()
        .addModule(new Java8TimeModule())
        .addModule(new Jdk8Module())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .build();
    private static final Logger log = LoggerFactory.getLogger(JacksonUtils.class);
    public static ObjectMapper ALLOW_UNQUOTED_FIELD_NAMES_MAPPER = JsonMapper.builder()
        .addModule(new Java8TimeModule())
        .addModule(new Jdk8Module())
        .configure(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature(), false)
        .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
        .build();

    public static ObjectMapper getObjectMapperWithJavaTimeModule() {
        return JsonMapper.builder()
            .addModule(new Java8TimeModule())
            .addModule(new Jdk8Module())
            .build();
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        try {
            return fromValue != null ? OBJECT_MAPPER.convertValue(fromValue, toValueType) : null;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "The given object value cannot be converted to " + toValueType + ": " + fromValue, e);
        }
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        try {
            return fromValue != null ? OBJECT_MAPPER.convertValue(fromValue, toValueTypeRef) : null;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "The given object value cannot be converted to " + toValueTypeRef + ": " + fromValue, e);
        }
    }

    public static <T> T fromString(String string, Class<T> clazz) {
        try {
            return string != null ? OBJECT_MAPPER.readValue(string, clazz) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException(
                "The given string value cannot be transformed to Json object: " + string, e);
        }
    }

    public static <T> T fromString(String string, TypeReference<T> valueTypeRef) {
        try {
            return string != null ? OBJECT_MAPPER.readValue(string, valueTypeRef) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException(
                "The given string value cannot be transformed to Json object: " + string, e);
        }
    }

    public static <T> T fromString(String string, JavaType javaType) {
        try {
            return string != null ? OBJECT_MAPPER.readValue(string, javaType) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException(
                "The given String value cannot be transformed to Json object: " + string, e);
        }
    }

    public static <T> T fromString(String string, Class<T> clazz, boolean ignoreUnknownFields) {
        try {
            return string != null ? IGNORE_UNKNOWN_PROPERTIES_JSON_MAPPER.readValue(string, clazz) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException(
                "The given string value cannot be transformed to Json object: " + string, e);
        }
    }

    public static <T> T fromBytes(byte[] bytes, Class<T> clazz) {
        try {
            return bytes != null ? OBJECT_MAPPER.readValue(bytes, clazz) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException(
                "The given byte[] value cannot be transformed to Json object:" + Arrays.toString(bytes), e);
        }
    }

    public static <T> T fromBytes(byte[] bytes, TypeReference<T> valueTypeRef) {
        try {
            return bytes != null ? OBJECT_MAPPER.readValue(bytes, valueTypeRef) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException(
                "The given string value cannot be transformed to Json object: " + Arrays.toString(bytes), e);
        }
    }

    public static JsonNode fromBytes(byte[] bytes) {
        try {
            return OBJECT_MAPPER.readTree(bytes);
        } catch (IOException e) {
            throw new IllegalArgumentException(
                "The given byte[] value cannot be transformed to Json object: " + Arrays.toString(bytes), e);
        }
    }

    public static byte[] toBytes(Object value) {
        try {
            return value != null ? OBJECT_MAPPER.writeValueAsBytes(value) : null;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                "The given Json object value cannot be transformed to a String: " + value, e);
        }
    }

    public static String toString(Object value) {
        try {
            return value != null ? OBJECT_MAPPER.writeValueAsString(value) : null;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                "The given Json object value cannot be transformed to a String: " + value, e);
        }
    }

    public static String toStringWithView(Object value, Class<Views.Public> serializationView)
        throws JsonProcessingException {
        return value == null
            ? ""
            : OBJECT_MAPPER.writerWithView(serializationView).writeValueAsString(value);
    }

    public static String toPrettyString(Object o) {
        try {
            return PRETTY_SORTED_JSON_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toPlainText(String data) {
        if (data == null) {
            return null;
        }
        if (data.startsWith("\"") && data.endsWith("\"") && data.length() >= 2) {
            final String dataBefore = data;
            try {
                data = fromString(data, String.class);
            } catch (Exception ignored) {
            }
            log.trace("Trimming double quotes. Before trim: [{}], after trim: [{}]", dataBefore, data);
        }
        return data;
    }

    public static <T> T treeToValue(JsonNode node, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.treeToValue(node, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't convert value: " + node.toString(), e);
        }
    }

    public static <T> T readValue(String file, CollectionType clazz) {
        try {
            return OBJECT_MAPPER.readValue(file, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't read file: " + file, e);
        }
    }

    public static <T> T readValue(String object, TypeReference<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(object, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't read object: " + object, e);
        }
    }

    public static <T> T readValue(File file, TypeReference<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(file, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't read file: " + file, e);
        }
    }

    public static <T> T readValue(File file, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(file, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't read file: " + file, e);
        }
    }

    public static <T> T readValue(Reader reader, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(reader, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't read reader: " + reader, e);
        }
    }

    public static JsonNode toJsonNode(String value) {
        return toJsonNode(value, OBJECT_MAPPER);
    }

    public static JsonNode toJsonNode(String value, ObjectMapper mapper) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return mapper.readTree(value);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static JsonNode toJsonNode(Path file) {
        try {
            return OBJECT_MAPPER.readTree(Files.readAllBytes(file));
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't read file: " + file, e);
        }
    }

    public static JsonNode toJsonNode(File value) {
        try {
            return value != null ? OBJECT_MAPPER.readTree(value) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException(
                "The given File object value: " + value + " cannot be transformed to a JsonNode", e);
        }
    }

    public static JsonNode toJsonNode(InputStream value) {
        try {
            return value != null ? OBJECT_MAPPER.readTree(value) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException(
                "The given InputStream value: " + value + " cannot be transformed to a JsonNode", e);
        }
    }

    public static ObjectNode newObjectNode() {
        return newObjectNode(OBJECT_MAPPER);
    }

    public static ObjectNode newObjectNode(ObjectMapper mapper) {
        return mapper.createObjectNode();
    }

    public static ArrayNode newArrayNode() {
        return newArrayNode(OBJECT_MAPPER);
    }

    public static ArrayNode newArrayNode(ObjectMapper mapper) {
        return mapper.createArrayNode();
    }

    public static <T> T clone(T value) {
        @SuppressWarnings("unchecked")
        Class<T> valueClass = (Class<T>) value.getClass();
        return fromString(toString(value), valueClass);
    }

    public static <T> JsonNode valueToTree(T value) {
        return OBJECT_MAPPER.valueToTree(value);
    }

    public static <T> byte[] writeValueAsBytes(T value) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                "The given Json object value cannot be transformed to a String: " + value, e);
        }
    }

    public static JsonNode getSafely(JsonNode node, String... path) {
        if (node == null) {
            return null;
        }
        for (String p : path) {
            if (!node.has(p)) {
                return null;
            } else {
                node = node.get(p);
            }
        }
        return node;
    }

    public static ObjectNode asObject(JsonNode node) {
        return node != null && node.isObject() ? ((ObjectNode) node) : newObjectNode();
    }

    public static Map<String, String> toFlatMap(JsonNode node) {
        HashMap<String, String> map = new HashMap<>();
        toFlatMap(node, "", map);
        return map;
    }

    public static <T> T fromReader(Reader reader, Class<T> clazz) {
        try {
            return reader != null ? OBJECT_MAPPER.readValue(reader, clazz) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid request payload", e);
        }
    }

    public static <T> void writeValue(Writer writer, T value) {
        try {
            OBJECT_MAPPER.writeValue(writer, value);
        } catch (IOException e) {
            throw new IllegalArgumentException("The given writer value: " + writer + "cannot be wrote", e);
        }
    }

    public static JavaType constructCollectionType(Class collectionClass, Class<?> elementClass) {
        return OBJECT_MAPPER.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    private static void toFlatMap(JsonNode node, String currentPath, Map<String, String> map) {
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            currentPath = currentPath.isEmpty() ? "" : currentPath + ".";
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                toFlatMap(entry.getValue(), currentPath + entry.getKey(), map);
            }
        } else if (node.isValueNode()) {
            map.put(currentPath, node.asText());
        }
    }

    public static void replaceAll(JsonNode root, String pathPrefix, BiFunction<String, String, String> processor) {
        Queue<JsonNodeProcessingTask> tasks = new LinkedList<>();
        tasks.add(new JsonNodeProcessingTask(pathPrefix, root));
        while (!tasks.isEmpty()) {
            JsonNodeProcessingTask task = tasks.poll();
            JsonNode node = task.getNode();
            if (node == null) {
                continue;
            }
            String currentPath = StringUtils.isBlank(task.getPath()) ? "" : (task.getPath() + ".");
            if (node.isObject()) {
                ObjectNode on = (ObjectNode) node;
                for (Iterator<String> it = on.fieldNames(); it.hasNext(); ) {
                    String childName = it.next();
                    JsonNode childValue = on.get(childName);
                    if (childValue.isTextual()) {
                        on.put(childName, processor.apply(currentPath + childName, childValue.asText()));
                    } else if (childValue.isObject() || childValue.isArray()) {
                        tasks.add(new JsonNodeProcessingTask(currentPath + childName, childValue));
                    }
                }
            } else if (node.isArray()) {
                ArrayNode childArray = (ArrayNode) node;
                for (int i = 0; i < childArray.size(); i++) {
                    JsonNode element = childArray.get(i);
                    if (element.isObject()) {
                        tasks.add(new JsonNodeProcessingTask(currentPath + "." + i, element));
                    } else if (element.isTextual()) {
                        childArray.set(i, processor.apply(currentPath + "." + i, element.asText()));
                    }
                }
            }
        }
    }

    public static void replaceAllByMapping(
        JsonNode jsonNode,
        Map<String, String> mapping,
        Map<String, String> templateParams,
        BiFunction<String, String, String> processor) {
        replaceByMapping(jsonNode, mapping, templateParams, (name, value) -> {
            if (value.isTextual()) {
                return new TextNode(processor.apply(name, value.asText()));
            } else if (value.isArray()) {
                ArrayNode array = (ArrayNode) value;
                for (int i = 0; i < array.size(); i++) {
                    String arrayElementName = name.replace("$index", Integer.toString(i));
                    array.set(i, processor.apply(arrayElementName, array.get(i).asText()));
                }
                return array;
            }
            return value;
        });
    }

    public static void replaceByMapping(
        JsonNode jsonNode,
        Map<String, String> mapping,
        Map<String, String> templateParams,
        BiFunction<String, JsonNode, JsonNode> processor) {
        for (Map.Entry<String, String> entry : mapping.entrySet()) {
            String expression = entry.getValue();
            Queue<JsonPathProcessingTask> tasks = new LinkedList<>();
            tasks.add(new JsonPathProcessingTask(entry.getKey().split("\\."), templateParams, jsonNode));
            while (!tasks.isEmpty()) {
                JsonPathProcessingTask task = tasks.poll();
                String token = task.currentToken();
                JsonNode node = task.getNode();
                if (node == null) {
                    continue;
                }
                if (token.equals("*") || token.startsWith("$")) {
                    String variableName = token.startsWith("$") ? token.substring(1) : null;
                    if (node.isArray()) {
                        ArrayNode childArray = (ArrayNode) node;
                        for (JsonNode element : childArray) {
                            tasks.add(task.next(element));
                        }
                    } else if (node.isObject()) {
                        ObjectNode on = (ObjectNode) node;
                        for (Iterator<Map.Entry<String, JsonNode>> it = on.fields(); it.hasNext(); ) {
                            Map.Entry<String, JsonNode> kv = it.next();
                            if (variableName != null) {
                                tasks.add(task.next(kv.getValue(), variableName, kv.getKey()));
                            } else {
                                tasks.add(task.next(kv.getValue()));
                            }
                        }
                    }
                } else {
                    String variableName = null;
                    String variableValue = null;
                    if (token.contains("[$")) {
                        variableName = StringUtils.substringBetween(token, "[$", "]");
                        token = StringUtils.substringBefore(token, "[$");
                    }
                    if (node.has(token)) {
                        JsonNode value = node.get(token);
                        if (variableName != null
                            && value.has(variableName)
                            && value.get(variableName).isTextual()) {
                            variableValue = value.get(variableName).asText();
                        }
                        if (task.isLast()) {
                            String name = expression;
                            for (Map.Entry<String, String> replacement :
                                task.getVariables().entrySet()) {
                                name = name.replace(
                                    "$" + replacement.getKey(),
                                    StringUtils.defaultString(replacement.getValue(), StringPool.EMPTY));
                            }
                            ((ObjectNode) node).set(token, processor.apply(name, value));
                        } else {
                            if (StringUtils.isNotEmpty(variableName)) {
                                tasks.add(task.next(value, variableName, variableValue));
                            } else {
                                tasks.add(task.next(value));
                            }
                        }
                    }
                }
            }
        }
    }

    public static class JsonNodeProcessingTask {
        private final String path;
        private final JsonNode node;

        public JsonNodeProcessingTask(String path, JsonNode node) {
            this.path = path;
            this.node = node;
        }

        public String getPath() {
            return path;
        }

        public JsonNode getNode() {
            return node;
        }
    }

    public static class JsonPathProcessingTask {

        private final String[] tokens;

        private final Map<String, String> variables;

        private final JsonNode node;

        public JsonPathProcessingTask(String[] tokens, Map<String, String> variables, JsonNode node) {
            this.tokens = tokens;
            this.variables = variables;
            this.node = node;
        }

        public boolean isLast() {
            return tokens.length == 1;
        }

        public String currentToken() {
            return tokens[0];
        }

        public JsonPathProcessingTask next(JsonNode next) {
            return new JsonPathProcessingTask(Arrays.copyOfRange(tokens, 1, tokens.length), variables, next);
        }

        public JsonPathProcessingTask next(JsonNode next, String key, String value) {
            Map<String, String> variables = new HashMap<>(this.variables);
            variables.put(key, value);
            return new JsonPathProcessingTask(Arrays.copyOfRange(tokens, 1, tokens.length), variables, next);
        }

        @Override
        public String toString() {
            return "JsonPathProcessingTask{" + "tokens=" + Arrays.toString(tokens) + ", variables=" + variables
                + ", node=" + node.toString().substring(0, 20) + '}';
        }

        public String[] getTokens() {
            return tokens;
        }

        public Map<String, String> getVariables() {
            return variables;
        }

        public JsonNode getNode() {
            return node;
        }
    }
}
