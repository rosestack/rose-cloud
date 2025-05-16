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
package io.github.rose.upms.domain.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.rose.mybatis.model.BaseEntity;
import io.github.rose.upms.model.enums.SystemSettingType;

@TableName(value = "sys_system_setting", autoResultMap = true)
public class SystemSetting extends BaseEntity {

    private static final long serialVersionUID = -7670322981725511892L;

    private SystemSettingType type;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private JsonNode content;

    public SystemSettingType getType() {
        return type;
    }

    public void setType(SystemSettingType type) {
        this.type = type;
    }

    public JsonNode getContent() {
        return content;
    }

    public void setContent(JsonNode content) {
        this.content = content;
    }
}
