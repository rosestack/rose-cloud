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
package io.github.rose.mybatis.encrypt.util;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.github.rose.mybatis.encrypt.FieldSetProperty;
import io.github.rose.mybatis.encrypt.annotation.FieldBind;
import io.github.rose.mybatis.encrypt.annotation.FieldEncrypt;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiConsumer;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class FieldSetPropertyHelper {

    private static boolean hasFieldEncrypt = false;

    private static boolean hasFieldBind = false;

    private static Map<Class<?>, List<FieldSetProperty>> clazzMap;

    private static Set<Class<?>> excludeClazzSet;

    private FieldSetPropertyHelper() {}

    public static void init(boolean var1, boolean var2) {
        hasFieldEncrypt = var1;
        hasFieldBind = var2;
        clazzMap = new ConcurrentHashMap<>();
        excludeClazzSet = new CopyOnWriteArraySet<>();
    }

    public static List<FieldSetProperty> getFieldSetPropertyList(Class<?> clazz) {
        if (excludeClazzSet.contains(clazz)) {
            return new ArrayList<>();
        }

        List<FieldSetProperty> fieldSetPropertyList = clazzMap.get(clazz);
        if (fieldSetPropertyList != null) {
            return fieldSetPropertyList;
        }

        if (clazz.isAssignableFrom(HashMap.class)) {
            excludeClazzSet.add(clazz);
        } else {
            List<FieldSetProperty> finalFieldSetPropertyList = new ArrayList<>();

            for (Field field : FieldUtils.getAllFields(clazz)) {
                FieldEncrypt fieldEncrypt = null;
                if (hasFieldEncrypt) {
                    fieldEncrypt = field.getAnnotation(FieldEncrypt.class);
                    if (null != fieldEncrypt && !field.getType().isAssignableFrom(String.class)) {
                        throw new RuntimeException("annotation `@FieldEncrypt` only string types are supported.");
                    }
                }

                FieldBind fieldBind = null;
                if (hasFieldBind) {
                    fieldBind = field.getAnnotation(FieldBind.class);
                }
                if (fieldBind != null || fieldEncrypt != null) {
                    finalFieldSetPropertyList.add(new FieldSetProperty(field.getName(), fieldEncrypt, fieldBind));
                }
            }

            fieldSetPropertyList = finalFieldSetPropertyList;

            if (fieldSetPropertyList.isEmpty()) {
                excludeClazzSet.add(clazz);
            } else {
                clazzMap.put(clazz, fieldSetPropertyList);
            }
        }

        return fieldSetPropertyList;
    }

    public static boolean foreachValue(
            Configuration configuration, Object value, BiConsumer<MetaObject, FieldSetProperty> consumer) {
        if (value == null) {
            return Boolean.FALSE;
        }
        List<FieldSetProperty> fieldSetPropertyList = getFieldSetPropertyList(value.getClass());
        if (!CollectionUtils.isEmpty(fieldSetPropertyList)) {
            MetaObject metaObject = configuration.newMetaObject(value);
            fieldSetPropertyList.parallelStream()
                    .forEach(fieldSetProperty -> consumer.accept(metaObject, fieldSetProperty));
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
