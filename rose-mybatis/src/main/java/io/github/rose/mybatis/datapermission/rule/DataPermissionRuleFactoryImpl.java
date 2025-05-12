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
package io.github.rose.mybatis.datapermission.rule;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import io.github.rose.mybatis.datapermission.annotation.DataPermission;
import io.github.rose.mybatis.datapermission.aop.DataPermissionContextHolder;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 默认的 DataPermissionRuleFactoryImpl 实现类 支持通过 {@link DataPermissionContextHolder} 过滤数据权限
 */
public class DataPermissionRuleFactoryImpl implements DataPermissionRuleFactory {

    /**
     * 数据权限规则数组
     */
    private final List<DataPermissionRule> rules;

    public DataPermissionRuleFactoryImpl(List<DataPermissionRule> rules) {
        this.rules = rules;
    }

    @Override
    public List<DataPermissionRule> getDataPermissionRules() {
        return rules;
    }

    @Override // mappedStatementId 参数，暂时没有用。以后，可以基于 mappedStatementId + DataPermission
    // 进行缓存
    public List<DataPermissionRule> getDataPermissionRule(String mappedStatementId) {
        // 1. 无数据权限
        if (CollectionUtils.isEmpty(rules)) {
            return Collections.emptyList();
        }
        // 2. 未配置，则默认开启
        DataPermission dataPermission = DataPermissionContextHolder.get();
        if (dataPermission == null) {
            return rules;
        }
        // 3. 已配置，但禁用
        if (!dataPermission.enable()) {
            return Collections.emptyList();
        }

        // 4. 已配置，只选择部分规则
        if (ObjectUtils.isNotEmpty(dataPermission.includeRules())) {
            return rules.stream()
                    .filter(rule -> ArrayUtils.contains(dataPermission.includeRules(), rule.getClass()))
                    .collect(Collectors.toList()); // 一般规则不会太多，所以不采用 HashSet 查询
        }
        // 5. 已配置，只排除部分规则
        if (ObjectUtils.isNotEmpty(dataPermission.excludeRules())) {
            return rules.stream()
                    .filter(rule -> !ArrayUtils.contains(dataPermission.excludeRules(), rule.getClass()))
                    .collect(Collectors.toList()); // 一般规则不会太多，所以不采用 HashSet 查询
        }
        // 6. 已配置，全部规则
        return rules;
    }
}
