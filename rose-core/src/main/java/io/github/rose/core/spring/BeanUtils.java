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
package io.github.rose.core.spring;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.Converter;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * 获取所有的属性值为空属性名数组
     *
     * @param source 对象
     * @return 属性值
     */
    public static String[] getNullPropertyNames(Object source) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();
        List<String> nullPropertyNames = new ArrayList<>();
        for (PropertyDescriptor pd : pds) {
            String propertyName = pd.getName();
            if (beanWrapper.getPropertyValue(propertyName) == null) {
                nullPropertyNames.add(propertyName);
            }
        }
        return nullPropertyNames.toArray(new String[nullPropertyNames.size()]);
    }

    /**
     * 单对象基于对象创建拷贝
     *
     * @param source 数据来源实体
     * @param desc   转换后的对象
     * @return desc
     */
    public static <T, V> V copy(T source, V desc) {
        if (ObjectUtils.isEmpty(source)) {
            return null;
        }
        if (ObjectUtils.isEmpty(desc)) {
            return null;
        }
        BeanCopier beanCopier = BeanCopierCache.INSTANCE.get(source.getClass(), desc.getClass(), null);
        beanCopier.copy(source, desc, null);
        return desc;
    }

    /**
     * bean拷贝到map
     *
     * @param bean 数据来源实体
     * @return map对象
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, Object> copyToMap(T bean) {
        if (ObjectUtils.isEmpty(bean)) {
            return null;
        }
        return BeanMap.create(bean);
    }

    /**
     * map拷贝到bean
     *
     * @param map  数据来源
     * @param bean bean对象
     * @return bean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        if (ObjectUtils.isEmpty(map)) {
            return null;
        }
        if (ObjectUtils.isEmpty(bean)) {
            return null;
        }
        BeanMap.create(bean).putAll(map);
        return bean;
    }

    /**
     * BeanCopier属性缓存<br>
     * 缓存用于防止多次反射造成的性能问题
     */
    public enum BeanCopierCache {

        /**
         * BeanCopier属性缓存单例
         */
        INSTANCE;

        private final ConcurrentReferenceHashMap<String, BeanCopier> cache = new ConcurrentReferenceHashMap<>();

        /**
         * 获得类与转换器生成的key在{@link BeanCopier}的Map中对应的元素
         *
         * @param srcClass    源Bean的类
         * @param targetClass 目标Bean的类
         * @param converter   转换器
         * @return Map中对应的BeanCopier
         */
        public BeanCopier get(Class<?> srcClass, Class<?> targetClass, Converter converter) {
            final String key = genKey(srcClass, targetClass, converter);
            return cache.getOrDefault(key, BeanCopier.create(srcClass, targetClass, converter != null));
        }

        /**
         * 获得类与转换器生成的key
         *
         * @param srcClass    源Bean的类
         * @param targetClass 目标Bean的类
         * @param converter   转换器
         * @return 属性名和Map映射的key
         */
        private String genKey(Class<?> srcClass, Class<?> targetClass, Converter converter) {
            final StringBuilder key =
                new StringBuilder().append(srcClass.getName()).append('#').append(targetClass.getName());
            if (null != converter) {
                key.append('#').append(converter.getClass().getName());
            }
            return key.toString();
        }
    }
}
