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

import cc.chensoul.rose.upms.old.domain.service.OAuth2ClientService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.rose.core.util.RestResponse;
import io.github.rose.upms.domain.app.OAuth2Client;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 客户端 前端控制器
 * </p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 2020-04-17
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final OAuth2ClientService OAuth2ClientService;

    @GetMapping("/page")
    public RestResponse<IPage<OAuth2Client>> page(Page<OAuth2Client> page, OAuth2Client oAuth2Client) {
        return RestResponse.ok(OAuth2ClientService.page(page, Wrappers.query(oAuth2Client)));
    }

    @GetMapping
    public RestResponse<List<OAuth2Client>> list(OAuth2Client oAuth2Client) {
        return RestResponse.ok(
            OAuth2ClientService.list(Wrappers.<OAuth2Client>lambdaQuery().orderByDesc(OAuth2Client::getStatus)));
    }

    @GetMapping("/{id}")
    public RestResponse<OAuth2Client> getById(@PathVariable("id") Long id) {
        return RestResponse.ok(OAuth2ClientService.getById(id));
    }

    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_client_add')")
    public RestResponse<Boolean> save(@RequestBody OAuth2Client OAuth2Client) {
        return RestResponse.ok(OAuth2ClientService.save(OAuth2Client));
    }

    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_client_edit')")
    public RestResponse<Boolean> updateById(@RequestBody OAuth2Client OAuth2Client) {
        return RestResponse.ok(OAuth2ClientService.updateClientDetailsById(OAuth2Client));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_client_del')")
    public RestResponse<Boolean> removeById(@PathVariable("id") Long id) {
        return RestResponse.ok(OAuth2ClientService.removeClientDetailsById(id));
    }

    @GetMapping("/clientId/{clientId}")
    public RestResponse<OAuth2Client> getByClientId(@PathVariable("clientId") String clientId) {
        return RestResponse.ok(OAuth2ClientService.getByClientId(clientId));
    }
}
