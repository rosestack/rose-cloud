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
package io.github.rose.mybatis.extension.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 基础业务接口
 *
 * @param <T>
 */
public interface BaseService<T> extends IService<T> {

    /**
     * 判断字段是否重复
     *
     * @param field      字段
     * @param value      字段值
     * @param excludedId 排除的id
     * @return boolean
     */
    boolean isFieldDuplicate(SFunction<T, ?> field, Object value, Long excludedId);
}
