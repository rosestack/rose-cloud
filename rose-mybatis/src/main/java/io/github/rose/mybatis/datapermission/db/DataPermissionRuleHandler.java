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
package io.github.rose.mybatis.datapermission.db;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import io.github.rose.mybatis.datapermission.rule.DataPermissionRule;
import io.github.rose.mybatis.datapermission.rule.DataPermissionRuleFactory;
import io.github.rose.mybatis.util.MyBatisUtils;
import java.util.List;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.schema.Table;

/**
 * 基于 {@link DataPermissionRule} 的数据权限处理器
 * <p>
 * 它的底层，是基于 MyBatis Plus 的
 * <a href="https://baomidou.com/plugins/data-permission/">数据权限插件</a> 核心原理：它会在 SQL 执行前拦截
 * SQL 语句，并根据用户权限动态添加权限相关的 SQL 片段。这样，只有用户有权限访问的数据才会被查询出来
 */
public class DataPermissionRuleHandler implements MultiDataPermissionHandler {

    private final DataPermissionRuleFactory ruleFactory;

    public DataPermissionRuleHandler(DataPermissionRuleFactory ruleFactory) {
        this.ruleFactory = ruleFactory;
    }

    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {
        // 获得 Mapper 对应的数据权限的规则
        List<DataPermissionRule> rules = ruleFactory.getDataPermissionRule(mappedStatementId);
        if (CollectionUtils.isEmpty(rules)) {
            return null;
        }

        // 生成条件
        Expression allExpression = null;
        for (DataPermissionRule rule : rules) {
            // 判断表名是否匹配
            String tableName = MyBatisUtils.getTableName(table);
            if (!rule.getTableNames().contains(tableName)) {
                continue;
            }

            // 单条规则的条件
            Expression oneExpress = rule.getExpression(tableName, table.getAlias());
            if (oneExpress == null) {
                continue;
            }
            // 拼接到 allExpression 中
            allExpression = allExpression == null ? oneExpress : new AndExpression(allExpression, oneExpress);
        }
        return allExpression;
    }
}
