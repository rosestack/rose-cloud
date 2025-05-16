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
package old.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.rose.upms.domain.app.OAuth2Client;

/**
 * <p>
 * 客户端 服务类
 * </p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface OAuth2ClientService extends IService<OAuth2Client> {

    OAuth2Client getByClientId(String clientId);

    Boolean removeClientDetailsById(Long id);

    Boolean updateClientDetailsById(OAuth2Client OAuth2Client);

    boolean saveClientDetails(OAuth2Client OAuth2Client);
}
