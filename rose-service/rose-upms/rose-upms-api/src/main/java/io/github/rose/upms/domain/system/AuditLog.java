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

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.rose.mybatis.model.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName
public class AuditLog extends TenantEntity implements Serializable {

    private static final long serialVersionUID = 1129753896999673095L;

    private String name;

    private String ip;

    private String userAgent;

    private String requestUri;

    private String requestParam;

    private String requestPayload;

    private String requestMethod;

    private String response;

    private Long costTime;

    private boolean status;

    private String exception;

    private String traceId;
}
