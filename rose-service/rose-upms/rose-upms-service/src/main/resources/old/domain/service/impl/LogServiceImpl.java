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
package old.domain.service.impl;

import cc.chensoul.rose.upms.old.domain.mapper.AuditLogMapper;
import cc.chensoul.rose.upms.old.domain.service.LogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.rose.upms.domain.system.AuditLog;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl extends ServiceImpl<AuditLogMapper, AuditLog> implements LogService {
}
