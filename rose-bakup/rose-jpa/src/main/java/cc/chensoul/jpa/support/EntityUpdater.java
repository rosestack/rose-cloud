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
package cc.chensoul.jpa.support;

import cc.rosestack.github.iore.exception.BusinessException;
import cc.rosestack.github.iore.exception.ResultCode;
import cc.rosestack.github.iore.validation.Update;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class EntityUpdater<T, ID> extends BaseEntityOperation implements Loader<T, ID>, UpdateHandler<T>, Executor<T> {

    private final CrudRepository<T, ID> repository;
    private T entity;
    private Consumer<T> successHook = t -> log.info("update success");
    private Consumer<? super Throwable> errorHook = e -> log.error("update error", e);

    public EntityUpdater(CrudRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<T> execute() {
        doValidate(this.entity, Update.class);
        T save = Try.of(() -> repository.save(entity))
            .onSuccess(successHook)
            .onFailure(errorHook).getOrNull();
        return Optional.ofNullable(save);
    }

    @Override
    public UpdateHandler<T> loadById(ID id) {
        Objects.requireNonNull(id, "id is null");
        Optional<T> loadEntity = repository.findById(id);
        this.entity = loadEntity.orElseThrow(() -> new BusinessException(ResultCode.INTERNAL_ERROR.getName()));
        return this;
    }

    @Override
    public UpdateHandler<T> load(Supplier<T> t) {
        this.entity = t.get();
        return this;
    }

    @Override
    public Executor<T> update(Consumer<T> consumer) {
        Objects.requireNonNull(entity, "entity is null");
        consumer.accept(this.entity);
        return this;
    }

    @Override
    public Executor<T> onSuccess(Consumer<T> consumer) {
        this.successHook = consumer;
        return this;
    }

    @Override
    public Executor<T> onError(Consumer<? super Throwable> consumer) {
        this.errorHook = consumer;
        return this;
    }

}
