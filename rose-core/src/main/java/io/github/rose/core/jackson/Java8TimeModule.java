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

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import io.github.rose.core.util.date.DatePattern;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
public class Java8TimeModule extends SimpleModule {

    public Java8TimeModule() {
        super(PackageVersion.VERSION);

        this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DatePattern.NORM_DATETIME_FORMATTER));
        this.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
        this.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME));
        this.addSerializer(Instant.class, InstantSerializer.INSTANCE);
        this.addSerializer(Duration.class, DurationSerializer.INSTANCE);

        this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DatePattern.NORM_DATETIME_FORMATTER));
        this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));
        this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME));
        this.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
        this.addDeserializer(Duration.class, DurationDeserializer.INSTANCE);
    }
}
