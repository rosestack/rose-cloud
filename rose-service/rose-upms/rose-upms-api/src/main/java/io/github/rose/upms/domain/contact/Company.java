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
package io.github.rose.upms.domain.contact;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.rose.mybatis.model.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实体类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class Company extends TenantEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private String description;
}
