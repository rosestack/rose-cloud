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
package io.github.rose.mybatis.tenant.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.toolkit.SqlParserUtils;
import io.github.rose.mybatis.tenant.util.TenantContextHolder;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class DefaultTenantLineHandler implements TenantLineHandler {

    private final Set<String> ignoredTables = new HashSet<>();

    public DefaultTenantLineHandler(Set<String> ignoredTables) {
        // 不同 DB 下，大小写的习惯不同，所以需要都添加进去
        ignoredTables.forEach(table -> {
            this.ignoredTables.add(table.toLowerCase(Locale.getDefault()));
            this.ignoredTables.add(table.toUpperCase(Locale.getDefault()));
        });
    }

    @Override
    public Expression getTenantId() {
        return new StringValue(TenantContextHolder.getTenantId());
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return TenantContextHolder.isIgnored() // 情况一，全局忽略多租户
            || ignoredTables.contains(SqlParserUtils.removeWrapperSymbol(tableName)); // 情况二，忽略多租户的表
    }
}
