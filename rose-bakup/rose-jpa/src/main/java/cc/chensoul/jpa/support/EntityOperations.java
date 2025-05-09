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
package cc.chensoul.jpa.support;

import org.springframework.data.repository.CrudRepository;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@SuppressWarnings("unchecked")
public abstract class EntityOperations {

    public static <T, ID> EntityUpdater<T, ID> doUpdate(CrudRepository<T, ID> repository) {
        return new EntityUpdater<>(repository);
    }

    public static <T, ID> EntityCreator<T, ID> doCreate(CrudRepository<T, ID> repository) {
        return new EntityCreator(repository);
    }
}
