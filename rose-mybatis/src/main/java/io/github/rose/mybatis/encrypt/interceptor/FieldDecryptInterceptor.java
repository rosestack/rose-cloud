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

import io.github.rose.mybatis.encrypt.FieldSetProperty;
import io.github.rose.mybatis.encrypt.IEncryptor;
import io.github.rose.mybatis.encrypt.IFieldBinder;
import io.github.rose.mybatis.encrypt.annotation.FieldBind;
import io.github.rose.mybatis.encrypt.annotation.FieldEncrypt;
import io.github.rose.mybatis.encrypt.util.FieldSetPropertyHelper;
import io.github.rose.mybatis.encrypt.util.InterceptorHelper;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Statement;
import java.util.Properties;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Intercepts({
    @Signature(
        type = ResultSetHandler.class,
        method = "handleResultSets",
        args = {Statement.class})
})
public class FieldDecryptInterceptor implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(FieldDecryptInterceptor.class);
    
    private IEncryptor encryptor;

    private IFieldBinder fieldBinder;

    private String password;

    public FieldDecryptInterceptor(IEncryptor encryptor, IFieldBinder fieldBinder, String password) {
        this.encryptor = encryptor;
        this.fieldBinder = fieldBinder;
        this.password = password;

        FieldSetPropertyHelper.init(null != encryptor, null != fieldBinder);
    }

    public Object intercept(Invocation invocation) throws Throwable {
        return InterceptorHelper.decrypt(invocation, (metaObject, fieldSetProperty) -> {
            decrypt(this.encryptor, this.fieldBinder, this.password, metaObject, fieldSetProperty);
        });
    }

    public void decrypt(
        IEncryptor encryptor,
        IFieldBinder fieldBinder,
        String password,
        MetaObject metaObject,
        FieldSetProperty fieldSetProperty) {
        String fieldName = fieldSetProperty.getFieldName();
        Object value = metaObject.getValue(fieldName);
        if (null != value) {
            if (null != encryptor && value instanceof String) {
                try {
                    FieldEncrypt fieldEncrypt = fieldSetProperty.getFieldEncrypt();
                    if (null != fieldEncrypt) {
                        value = InterceptorHelper.getEncryptor(encryptor, fieldEncrypt.encryptor())
                            .decrypt(fieldEncrypt.algorithm(), password, (String) value, null);
                    }
                } catch (Exception e) {
                    log.error("field decrypt", e);
                }
            }

            if (null != fieldBinder) {
                FieldBind fieldBind = fieldSetProperty.getFieldBind();
                if (null != fieldBind) {
                    fieldBinder.setMetaObject(fieldBind, value, metaObject);
                }
            }
            metaObject.setValue(fieldName, value);
        }
    }

    public Object plugin(Object var1) {
        return var1 instanceof ResultSetHandler ? Plugin.wrap(var1, this) : var1;
    }

    public void setProperties(Properties properties) {
    }
}
