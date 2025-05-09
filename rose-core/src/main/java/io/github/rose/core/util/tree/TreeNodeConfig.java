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
package io.github.rose.core.util.tree;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 树配置属性相关
 *
 * @author liangbaikai
 */
@Data
@Accessors(chain = true)
public class TreeNodeConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 默认属性配置对象
     */
    public static TreeNodeConfig DEFAULT_CONFIG = new TreeNodeConfig();

    // 属性名配置字段
    private String idKey = "id";

    private String parentIdKey = "parentId";

    private String weightKey = "weight";

    private String nameKey = "name";

    private String childrenKey = "children";

    private Boolean reversed = false;

    // 可以配置递归深度 从0开始计算 默认此配置为空,即不限制
    private Integer deep;
}
