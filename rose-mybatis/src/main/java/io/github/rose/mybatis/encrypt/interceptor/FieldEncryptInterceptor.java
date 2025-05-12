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
package io.github.rose.mybatis.encrypt.interceptor;

import io.github.rose.mybatis.encrypt.IEncryptor;
import io.github.rose.mybatis.encrypt.util.InterceptorHelper;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Intercepts({
    @Signature(
            type = Executor.class,
            method = "update",
            args = {MappedStatement.class, Object.class}),
    @Signature(
            type = Executor.class,
            method = "query",
            args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(
            type = Executor.class,
            method = "query",
            args = {
                MappedStatement.class,
                Object.class,
                RowBounds.class,
                ResultHandler.class,
                CacheKey.class,
                BoundSql.class
            }),
})
public class FieldEncryptInterceptor implements Interceptor {

    private IEncryptor encryptor;

    private String password;

    public FieldEncryptInterceptor(IEncryptor encryptor, String password) {
        this.encryptor = encryptor;
        this.password = password;
    }

    public IEncryptor getEncryptor() {
        return encryptor;
    }

    public String getPassword() {
        return password;
    }

    public Object intercept(Invocation invocation) throws Throwable {
        return InterceptorHelper.encrypt(invocation, encryptor, password);
    }

    public Object plugin(Object var1) {
        return var1 instanceof Executor ? Plugin.wrap(var1, this) : var1;
    }
}
