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
package io.github.rose.upms.domain.tenant;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.rose.mybatis.model.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 租户套餐
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
public class TenantProfile extends TenantEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    private boolean isDefault;

    private LocalDateTime expireTime;

    /**
     * 套餐限制数量： 最大应用数 最大资源数 最大用户数 最大角色数
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private JsonNode content;
}
