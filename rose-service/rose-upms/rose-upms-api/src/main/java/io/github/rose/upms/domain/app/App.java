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
package io.github.rose.upms.domain.app;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.rose.mybatis.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName
public class App extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    private boolean status;
}
