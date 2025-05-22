/*
 * Copyright © 2025 rosestack.github.io
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
import lombok.Getter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.Serializable;

@Getter
public abstract class AbstractCachedEntityService<K extends Serializable, V extends Serializable, E>
    extends AbstractEntityService {

    protected final TransactionalCache<K, V> cache;

    public AbstractCachedEntityService(ApplicationEventPublisher eventPublisher, TransactionalCache<K, V> cache) {
        super(eventPublisher);
        this.cache = cache;
    }

    public void publishEvictEvent(E event) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            eventPublisher.publishEvent(event);
        } else {
            handleEvictEvent(event);
        }
    }

    public abstract void handleEvictEvent(E event);
}
