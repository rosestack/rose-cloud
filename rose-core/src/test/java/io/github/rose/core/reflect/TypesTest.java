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
package io.github.rose.core.reflect;

import org.junit.jupiter.api.Test;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
class TypesTest {
    /**
     * 测试simpleNameOf方法
     * 验证能否正确获取类型的简单名称
     */
    @Test
    void testSimpleNameOf() {
        // 测试基本类型
        assertEquals("String", Types.simpleNameOf(String.class));
        assertEquals("Integer", Types.simpleNameOf(Integer.class));

        // 测试集合类型
        assertEquals("List", Types.simpleNameOf(List.class));
        assertEquals("Map", Types.simpleNameOf(Map.class));

        // 测试参数化类型
        TypeOf<List<String>> typeOf = new TypeOf<List<String>>() {
        };
        assertEquals("List<String>", Types.simpleNameOf(typeOf.getType()));

        // 测试匿名内部类
        Map<String, Object> map = new HashMap<>();
        assertEquals("HashMap", Types.simpleNameOf(map.getClass()));

        // 测试数组类型
        assertEquals("String[]", Types.simpleNameOf(String[].class));
    }

    /**
     * 测试canonicalNameOf方法
     * 验证能否正确获取类型的规范名称
     */
    @Test
    void testCanonicalNameOf() {
        // 测试基本类型
        assertEquals("java.lang.String", Types.canonicalNameOf(String.class));
        assertEquals("java.lang.Integer", Types.canonicalNameOf(Integer.class));

        // 测试参数化类型
        TypeOf<List<String>> typeOf = new ListTypeOf();
        assertEquals("java.util.List<java.lang.String>", Types.canonicalNameOf(typeOf.getType()));

        // 测试集合实现类
        Map<String, Object> map = new HashMap<>();
        assertEquals("java.util.HashMap", Types.canonicalNameOf(map.getClass()));

        // 测试数组类型
        assertEquals("java.lang.String[]", Types.canonicalNameOf(String[].class));
    }

    /**
     * 测试nameOf方法
     * 验证能否正确获取类型的全限定名称
     */
    @Test
    void testNameOf() {
        // 测试基本类型
        assertEquals("java.lang.String", Types.nameOf(String.class));
        assertEquals("java.lang.Integer", Types.nameOf(Integer.class));

        // 测试参数化类型
        TypeOf<List<String>> typeOf = new ListTypeOf();
        assertEquals("java.util.List<java.lang.String>", Types.nameOf(typeOf.getType()));

        // 测试集合实现类
        Map<String, Object> map = new HashMap<>();
        assertEquals("java.util.HashMap", Types.nameOf(map.getClass()));
    }

    /**
     * 测试rawClassOf方法
     * 验证能否正确获取类型的原始类
     */
    @Test
    void testRawClassOf() {
        // 测试Class类型
        assertEquals(String.class, Types.rawClassOf(String.class));

        // 测试参数化类型
        ParameterizedType parameterizedType = mock(ParameterizedType.class);
        when(parameterizedType.getRawType()).thenReturn(List.class);
        assertEquals(List.class, Types.rawClassOf(parameterizedType));

        // 测试泛型数组类型
        GenericArrayType genericArrayType = mock(GenericArrayType.class);
        when(genericArrayType.getGenericComponentType()).thenReturn(Integer.class);
        assertEquals(Integer[].class, Types.rawClassOf(genericArrayType));

        // 测试TypeVariable类型
        List<String> list = new ArrayList<>();
        TypeVariable<? extends Class<? extends List>> typeVariable = list.getClass().getTypeParameters()[0];
        assertEquals(Object.class, Types.rawClassOf(typeVariable));
    }

    /**
     * 测试getTypeArgument方法
     * 验证能否正确获取类型的第一个泛型参数
     */
    @Test
    void testGetTypeArgument() {

    }

    /**
     * 测试getTypeArguments方法
     * 验证能否正确获取类型的所有泛型参数
     */
    @Test
    void testGetTypeArguments() {

    }

    /**
     * 测试toParameterizedType方法
     * 验证能否正确将类型转换为ParameterizedType
     */
    @Test
    void testToParameterizedType() {
    }

    /**
     * 测试带interfaceIndex参数的toParameterizedType方法
     * 验证能否正确获取指定索引处的泛型接口
     */
    @Test
    void testToParameterizedTypeWithInterfaceIndex() {

    }

    /**
     * 测试getGenerics方法
     * 验证能否正确获取类的泛型信息
     */
    @Test
    void testGetGenerics() {

    }

    /**
     * 测试buildTypeName私有方法
     * 验证类型名称构建的正确性
     */
    @Test
    void testBuildTypeName() throws Exception {
        // 使用反射访问私有方法
        java.lang.reflect.Method method = Types.class.getDeclaredMethod(
            "buildTypeName", Type.class, StringBuilder.class, boolean.class, boolean.class);
        method.setAccessible(true);

        // 测试简单类名构建
        StringBuilder sb = new StringBuilder();
        method.invoke(null, String.class, sb, true, false);
        assertEquals("String", sb.toString());

        // 测试完整类名构建
        sb = new StringBuilder();
        method.invoke(null, String.class, sb, false, true);
        assertEquals("java.lang.String", sb.toString());

        // 测试参数化类型构建
        ParameterizedType parameterizedType = mock(ParameterizedType.class);
        when(parameterizedType.getRawType()).thenReturn(List.class);
        when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[]{String.class});

        sb = new StringBuilder();
        method.invoke(null, parameterizedType, sb, true, false);
        assertEquals("List<String>", sb.toString());
    }

    /**
     * 测试buildGenericTypeNames私有方法
     * 验证泛型参数名称构建的正确性
     */
    @Test
    void testBuildGenericTypeNames() throws Exception {
        // 使用反射访问私有方法
        java.lang.reflect.Method method = Types.class.getDeclaredMethod(
            "buildGenericTypeNames", Type[].class, StringBuilder.class, boolean.class, boolean.class);
        method.setAccessible(true);

        // 测试单个泛型参数
        StringBuilder sb = new StringBuilder();
        method.invoke(null, new Type[]{String.class}, sb, true, false);
        assertEquals("String", sb.toString());

        // 测试多个泛型参数
        sb = new StringBuilder();
        method.invoke(null, new Type[]{String.class, Integer.class}, sb, true, false);
        assertEquals("String, Integer", sb.toString());
    }

    // 用于模拟TypeOf对象的内部实现
    private static class TypeOfImpl<T> implements Type {
        private final Type type;

        TypeOfImpl(Type type) {
            this.type = type;
        }

        @Override
        public String getTypeName() {
            return type.getTypeName();
        }
    }

    // 创建一个具体的TypeOf子类用于测试
    private static class ListTypeOf extends TypeOf<List<String>> {
    }
}
