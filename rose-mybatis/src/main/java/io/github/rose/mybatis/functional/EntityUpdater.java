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
import io.github.rose.core.validation.Update;
import io.github.rose.core.validation.ValidateException;
import io.vavr.control.Try;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class EntityUpdater<T> extends BaseEntityOperation implements Loader<T>, UpdateHandler<T>, Executor<T> {
    private static final Logger log = LoggerFactory.getLogger(EntityUpdater.class);

    private final BaseMapper<T> baseMapper;

    private T entity;

    private Consumer<T> successHook = t -> log.info("update success");

    private Consumer<? super Throwable> errorHook = e -> log.error("update error", e);

    public EntityUpdater(BaseMapper<T> baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public Optional<T> execute() {
        doValidate(this.entity, Update.class);
        T save = Try.of(() -> {
                    baseMapper.updateById(entity);
                    return this.entity;
                })
                .onSuccess(successHook)
                .onFailure(errorHook)
                .getOrNull();
        return Optional.ofNullable(save);
    }

    @Override
    public UpdateHandler<T> loadById(Serializable id) {
        Objects.requireNonNull(id, "id is null");
        T t = baseMapper.selectById(id);
        if (Objects.isNull(t)) {
            throw new ValidateException("entity not found");
        } else {
            this.entity = t;
        }
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
