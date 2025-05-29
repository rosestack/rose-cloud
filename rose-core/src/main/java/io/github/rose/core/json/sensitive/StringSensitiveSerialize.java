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
package io.github.rose.core.json.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import io.github.rose.core.spring.expression.SpringExpressionResolver;
import java.io.IOException;
import java.util.Objects;
import org.apache.commons.lang3.ObjectUtils;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class StringSensitiveSerialize extends JsonSerializer<String> implements ContextualSerializer {

    private FieldSensitive fieldSensitive;

    public StringSensitiveSerialize(FieldSensitive fieldSensitive) {
        this.fieldSensitive = fieldSensitive;
    }

    public StringSensitiveSerialize() {}

    private String handler(FieldSensitive fieldSensitive, String origin) {
        Object disable = SpringExpressionResolver.getInstance().resolve(fieldSensitive.disabled());
        if (Boolean.TRUE.equals(disable)) {
            return origin;
        }

        ObjectUtils.requireNonEmpty(fieldSensitive.type(), "Sensitive type enum should not be null.");
        switch (fieldSensitive.type()) {
            case CHINESE_NAME:
                return Sensitives.chineseName(origin);
            case ID_CARD:
                return Sensitives.idCardNum(origin);
            case BANK_CARD:
                return Sensitives.bankCard(origin);
            case CAR_LICENSE:
                return Sensitives.carLicense(origin);
            case TEL:
                return Sensitives.tel(origin);
            case PHONE:
                return Sensitives.phone(origin);
            case ADDRESS:
                return Sensitives.address(origin);
            case EMAIL:
                return Sensitives.email(origin);
            case SECRET:
                return Sensitives.secret(origin);
            case IPV4:
                return Sensitives.ipv4(origin);
            case IPV6:
                return Sensitives.ipv6(origin);
            case FIRST_MASK:
                return Sensitives.deSensitive(origin, 1, 0, fieldSensitive.mask());
            case CUSTOM:
                return Sensitives.deSensitive(
                        origin, fieldSensitive.prefixKeep(), fieldSensitive.suffixKeep(), fieldSensitive.mask());
            default:
                throw new IllegalArgumentException("Unknown sensitive type enum " + fieldSensitive.type());
        }
    }

    @Override
    public void serialize(
            final String origin, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeString(handler(fieldSensitive, origin));
    }

    @Override
    public JsonSerializer<?> createContextual(
            final SerializerProvider serializerProvider, final BeanProperty beanProperty) throws JsonMappingException {
        FieldSensitive annotation = beanProperty.getAnnotation(FieldSensitive.class);
        if (Objects.nonNull(annotation)
                && Objects.equals(String.class, beanProperty.getType().getRawClass())) {
            return new StringSensitiveSerialize(annotation);
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }
}
