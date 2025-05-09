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
package io.github.rose.upms.client;

import io.github.rose.core.util.RestResponse;
import io.github.rose.upms.ServiceNameConstants;
import io.github.rose.upms.domain.app.OAuth2Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = ServiceNameConstants.SYSTEM_SERVICE)
public interface RemoteClientService {

    @GetMapping("/OAuth2Client/list")
    RestResponse<List<OAuth2Client>> getClientList(@SpringQueryMap OAuth2Client OAuth2Client);

    @GetMapping("/client/getClientByCode/{code}")
    RestResponse<OAuth2Client> getClientByCode(@PathVariable("code") String code);
}
