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
package old.controller;

import cc.chensoul.rose.upms.old.domain.service.LogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.rose.core.util.RestResponse;
import io.github.rose.upms.domain.system.AuditLog;
import io.github.rose.upms.model.LogAddRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    @GetMapping
    public RestResponse<IPage<AuditLog>> page(Page<AuditLog> page, LogAddRequest logAddRequest) {
        return RestResponse.ok(logService.page(
            page,
            Wrappers.<AuditLog>lambdaQuery()
                .ge(
                    Objects.nonNull(logAddRequest.getBeginTime()),
                    AuditLog::getCreateTime,
                    logAddRequest.getBeginTime())
                .le(
                    Objects.nonNull(logAddRequest.getEndTime()),
                    AuditLog::getCreateTime,
                    logAddRequest.getEndTime())));
    }

    @PostMapping
    public RestResponse<Void> saveLog(@RequestBody AuditLog auditLog) {
        logService.save(auditLog);
        return RestResponse.ok();
    }
}
