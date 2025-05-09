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
package old.cache;

import cc.chensoul.rose.redis.cache.TransactionalCache;
import cc.chensoul.rose.upms.old.domain.model.event.UserCacheEvictEvent;
import io.github.rose.upms.domain.contact.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;

// @Service
public class UserCachedEntityService extends AbstractCachedEntityService<String, User, UserCacheEvictEvent> {

    public UserCachedEntityService(ApplicationEventPublisher eventPublisher, TransactionalCache<String, User> cache) {
        super(eventPublisher, cache);
    }

    @Override
    @TransactionalEventListener(classes = UserCacheEvictEvent.class)
    public void handleEvictEvent(UserCacheEvictEvent event) {
        List<String> keys = new ArrayList<>(2);
        keys.add(event.getNewPhone());
        if (StringUtils.isNotEmpty(event.getOldPhone()) && !event.getNewPhone().equals(event.getOldPhone())) {
            keys.add(event.getOldPhone());
        }
        cache.evict(keys);
    }
}
