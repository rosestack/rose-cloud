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
package io.github.rose.mybatis.encrypt.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.rose.core.spring.ReflectionUtils;
import io.github.rose.mybatis.encrypt.FieldSetProperty;
import io.github.rose.mybatis.encrypt.IEncryptor;
import io.github.rose.mybatis.encrypt.annotation.FieldEncrypt;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class InterceptorHelper {
    private static final Logger log = LoggerFactory.getLogger(InterceptorHelper.class);

    private static Map<Class<? extends IEncryptor>, IEncryptor> encryptorMap;

    public static Object encrypt(Invocation invocation, IEncryptor encryptor, String password) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];

        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (SqlCommandType.UPDATE == sqlCommandType
            || SqlCommandType.INSERT == sqlCommandType
            || SqlCommandType.SELECT == sqlCommandType) {
            Object paramMap = args[1];
            Configuration configuration = mappedStatement.getConfiguration();
            if (paramMap instanceof Map) {
                for (Map.Entry entry : ((Map<?, ?>) paramMap).entrySet()) {
                    if (entry.getValue() == null) {
                        continue;
                    }
                    if (((String) entry.getKey()).startsWith("param")) {
                        continue;
                    }

                    if (entry.getValue() instanceof ArrayList) {
                        for (Object var : (ArrayList) entry.getValue()) {
                            if (encryptValue(configuration, encryptor, password, var)) {
                            }
                        }
                    } else if (entry.getValue() instanceof QueryWrapper) {
                        Object entity = ((QueryWrapper<?>) entry.getValue()).getEntity();
                        if (entity == null) {
                            continue;
                        }
                        encryptValue(configuration, encryptor, password, entity);
                    } else if (!encryptValue(configuration, encryptor, password, entry.getValue())) {
                        continue;
                    }
                    return invocation.proceed();
                }
            } else {
                if (paramMap != null) {
                    encryptValue(configuration, encryptor, password, paramMap);
                }
            }
        }
        return invocation.proceed();
    }

    public static boolean encryptValue(
        Configuration configuration, IEncryptor encryptor, String password, Object object) {
        return FieldSetPropertyHelper.foreachValue(configuration, object, (metaObject, fieldSetProperty) -> {
            FieldEncrypt fieldEncrypt = fieldSetProperty.getFieldEncrypt();
            if (null != fieldEncrypt) {
                Object objectValue = metaObject.getValue(fieldSetProperty.getFieldName());
                if (null != objectValue) {
                    try {
                        String value = getEncryptor(encryptor, fieldEncrypt.encryptor())
                            .encrypt(fieldEncrypt.algorithm(), password, (String) objectValue, null);
                        metaObject.setValue(fieldSetProperty.getFieldName(), value);
                    } catch (Exception e) {
                        log.error("field encrypt", e.getMessage());
                    }
                }
            }
        });
    }

    public static IEncryptor getEncryptor(IEncryptor encryptor, Class<? extends IEncryptor> customEncryptor) {
        IEncryptor result = encryptor;
        if (IEncryptor.class != customEncryptor) {
            if (null == encryptorMap) {
                encryptorMap = new HashMap();
            }

            result = encryptorMap.get(customEncryptor);
            if (null == result) {
                try {
                    result = customEncryptor.newInstance();
                    encryptorMap.put(customEncryptor, result);
                } catch (Exception var4) {
                    log.error("fieldEncrypt encryptor newInstance error", var4);
                }
            }
        }
        return result;
    }

    public static Object decrypt(Invocation invocation, BiConsumer<MetaObject, FieldSetProperty> consumer)
        throws Throwable {
        List result = (List) invocation.proceed();
        if (result.isEmpty()) {
            return result;
        } else {
            DefaultResultSetHandler defaultResultSetHandler = (DefaultResultSetHandler) invocation.getTarget();
            Field field = defaultResultSetHandler.getClass().getDeclaredField("mappedStatement");
            ReflectionUtils.makeAccessible(field);
            MappedStatement mappedStatement = (MappedStatement) field.get(defaultResultSetHandler);
            Configuration configuration = mappedStatement.getConfiguration();
            Iterator iterator = result.iterator();

            while (iterator.hasNext()) {
                Object value = iterator.next();
                if (null != value && !FieldSetPropertyHelper.foreachValue(configuration, value, consumer)) {
                    break;
                }
            }
            return result;
        }
    }
}
