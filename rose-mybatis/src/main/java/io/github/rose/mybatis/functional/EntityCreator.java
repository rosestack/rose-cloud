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
package io.github.rose.mybatis.functional;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.rose.core.validation.Insert;
import io.vavr.control.Try;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <code>
 * Customer customer = new Customer();
 * customer.setGrade(dto.getGrade());
 * customer.setUsername(dto.getUsername());
 * EntityOperations
 * .doCreate(getBaseMapper())
 * .create(() -> customer)
 * .update(e -> e.init())
 * .execute();
 * </code>
 */
public class EntityCreator<T> extends BaseEntityOperation implements Create<T>, UpdateHandler<T>, Executor<T> {
    private static final Logger log = LoggerFactory.getLogger(EntityCreator.class);

    private final BaseMapper<T> baseMapper;

    private T entity;

    private Consumer<T> successHook = t -> log.info("save success");

    private Consumer<? super Throwable> errorHook = e -> log.error("save error", e);

    public EntityCreator(BaseMapper<T> baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public UpdateHandler<T> create(Supplier<T> supplier) {
        this.entity = supplier.get();
        return this;
    }

    @Override
    public Executor<T> update(Consumer<T> consumer) {
        Objects.requireNonNull(entity, "entity is null");
        consumer.accept(this.entity);
        return this;
    }

    @Override
    public Optional<T> execute() {
        doValidate(this.entity, Insert.class);
        T save = Try.of(() -> {
                    baseMapper.insert(entity);
                    return this.entity;
                })
                .onSuccess(successHook)
                .onFailure(errorHook)
                .getOrNull();
        return Optional.ofNullable(save);
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
