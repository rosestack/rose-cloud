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
package io.github.rose.mybatis.extension.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.rose.mybatis.model.BaseEntity;
import org.springframework.validation.annotation.Validated;

/**
 * 业务封装基础类
 *
 * @param <M> mybatis-mapper
 * @param <T> domain
 */
@Validated
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T>
    implements BaseService<T> {

    @Override
    public boolean isFieldDuplicate(SFunction<T, ?> field, Object value, Long excludedId) {
        LambdaQueryWrapper<T> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(field, value);
        if (excludedId != null) {
            queryWrapper.ne(T::getId, excludedId);
        }
        return baseMapper.selectCount(queryWrapper) > 0;
    }
}
