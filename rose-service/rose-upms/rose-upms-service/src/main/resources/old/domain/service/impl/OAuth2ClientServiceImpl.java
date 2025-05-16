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

import cc.chensoul.rose.upms.old.domain.mapper.OAuth2ClientMapper;
import cc.chensoul.rose.upms.old.domain.service.OAuth2ClientService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.rose.core.exception.BusinessException;
import io.github.rose.upms.domain.app.OAuth2Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2ClientServiceImpl extends ServiceImpl<OAuth2ClientMapper, OAuth2Client>
    implements OAuth2ClientService {

    private final PasswordEncoder passwordEncoder;

    private final CacheManager cacheManager;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Cacheable(value = CacheConstants.CLIENT, key = "#clientId", unless = "#result == null")
    public OAuth2Client getByClientId(String clientId) {
        return getOne(Wrappers.<OAuth2Client>lambdaQuery()
            .eq(OAuth2Client::getClientId, clientId)
            .last("limit 1"));
    }

    @Override
    public Boolean removeClientDetailsById(Long id) {
        OAuth2Client OAuth2Client = this.getById(id);
        if (Objects.isNull(OAuth2Client)) {
            return false;
        }
        redisTemplate.opsForHash().delete(CacheConstants.OAUTH_CLIENT_DETAIL, OAuth2Client.getClientId());
        cacheManager.getCache(CacheConstants.CLIENT).clear();
        return this.removeById(id);
    }

    @Override
    public Boolean updateClientDetailsById(OAuth2Client OAuth2Client) {
        OAuth2Client oldOAuth2Client = baseMapper.selectById(OAuth2Client.getId());

        if (Objects.nonNull(oldOAuth2Client) && !Objects.equals(oldOAuth2Client.getId(), OAuth2Client.getId())) {
            throw new BusinessException("该Client编码已存在");
        }

        Boolean updated = this.updateById(OAuth2Client);
        if (updated) {
            log.info("更新Client -> {}", OAuth2Client);
            redisTemplate.opsForHash().delete(CacheConstants.OAUTH_CLIENT_DETAIL, OAuth2Client.getClientId());
            cacheManager.getCache(CacheConstants.CLIENT).clear();
        }

        return updated;
    }

    @Override
    public boolean saveClientDetails(OAuth2Client oAuth2Client) {
        OAuth2Client oldOAuth2Client = baseMapper.selectOne(
            new LambdaQueryWrapper<OAuth2Client>().eq(OAuth2Client::getClientId, oAuth2Client.getClientId()));
        if (oldOAuth2Client != null) {
            throw new BusinessException("该Client已存在");
        }
        oAuth2Client.setClientSecret(passwordEncoder.encode(oAuth2Client.getClientSecret()));
        boolean saved = this.save(oAuth2Client);
        if (saved) {
            log.info("缓存Client -> {}", oAuth2Client);
            cacheManager.getCache(CacheConstants.CLIENT).clear();
        }
        return saved;
    }
}
