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
package io.github.rose.iot.model;

import lombok.Data;

@Data
public class ThingModeProperties {

    private static final long serialVersionUID = 1L;

    private Long productId;

    private String name;

    private String identifier;

    // r、rw
    private String accessMode;

    private boolean required;

    /**
     * { "type": "double", "specs": { "min": "1", "max": "100", "unit": "L/min",
     * "unitName": "升每分钟", "step": "1" } }
     * <p>
     * { "type": "enum", "specs": { "1": "a", "2": "b" } }
     * <p>
     * { "type": "bool", "specs": { "0": "f", "1": "y" } }
     * <p>
     * { "type": "array", "specs": { "size": "10", "item": { "type": "int" } }
     */
    private String dataType;

    private String description;
}
