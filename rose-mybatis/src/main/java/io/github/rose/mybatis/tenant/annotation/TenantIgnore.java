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
package io.github.rose.mybatis.tenant.annotation;

import java.lang.annotation.*;

/**
 * 忽略租户，标记指定方法不进行租户的自动过滤
 * <p>
 * 注意，只有 DB 的场景会过滤，其它场景暂时不过滤： 1、Redis 场景：因为是基于 Key 实现多租户的能力，所以忽略没有意义，不像 DB 是一个 column 实现的
 * 2、MQ 场景：有点难以抉择，目前可以通过 Consumer 手动在消费的方法上，添加 @TenantIgnore 进行忽略
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TenantIgnore {}
