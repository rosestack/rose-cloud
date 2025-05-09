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
import com.fasterxml.jackson.databind.JsonNode;
import io.github.rose.mybatis.model.BaseEntity;
import io.github.rose.security.util.Authority;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String accountId;

    private String enName;

    private String nickname;

    private String tenantEmail;

    private Boolean mobileVisible;

    private String avatar;

    // 0：保密 1：男 2：女 3：其他
    private Integer gender;

    private String departmentId;

    private Integer status;

    private Authority authority;

    private String lastLoginIp;

    private LocalDateTime lastLoginTime;

    private LocalDateTime joinTime;

    private JsonNode extra;
}
