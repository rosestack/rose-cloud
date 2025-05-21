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
package io.github.rose.core.util;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 简化{@code RestResponse<T>} 的访问操作,例子
 * <pre>
 * RestResponse<Integer> result = RestResponse.ok(0);
 * // 使用场景1: 链式操作: 断言然后消费
 * RetOps.of(result)
 * 		.assertCode(-1,r -> new RuntimeException("error "+r.getCode()))
 * 		.assertHasData(r -> new IllegalStateException("oops!"))
 * 		.accept(System.out::println);
 *
 * // 使用场景2: 读取原始值(data),这里返回的是Optional
 * RetOps.of(result).getData().orElse(null);
 *
 * // 使用场景3: 类型转换
 * RestResponse<String> s = RetOps.of(result)
 *        .assertHasData(r -> new IllegalStateException("nani"))
 *        .map(i -> Integer.toHexString(i))
 *        .peek();
 * </pre>
 */
public class RetOps<T> {
    /**
     * 状态码为成功
     */
    public static final Predicate<RestResponse<?>> CODE_SUCCESS = r -> 0 == r.getCode();

    /**
     * 有数据
     */
    public static final Predicate<RestResponse<?>> HAS_DATA = r -> ObjectUtils.isNotEmpty(r.getData());

    /**
     * 状态码为成功并且有数据
     */
    public static final Predicate<RestResponse<?>> SUCCESS_AND_HAS_DATA = CODE_SUCCESS.and(HAS_DATA);

    private final RestResponse<T> original;

    RetOps(RestResponse<T> original) {
        this.original = original;
    }

    public static <T> RetOps<T> of(RestResponse<T> original) {
        return new RetOps<>(Objects.requireNonNull(original));
    }

    /**
     * 观察原始值
     *
     * @return R
     */
    public RestResponse<T> peek() {
        return original;
    }

    /**
     * 有条件地读取{@code data}的值
     *
     * @param predicate 断言函数
     * @return 返回 Optional 包装的data,如果断言失败返回empty
     */
    public Optional<T> ofData(Predicate<? super RestResponse<?>> predicate) {
        return predicate.test(original) ? Optional.of(original.getData()) : Optional.empty();
    }

    /**
     * 对{@code code}的值进行相等性测试
     *
     * @param value 基准值
     * @return 返回ture表示相等
     */
    public boolean codeEquals(int value) {
        return original.getCode() == value;
    }

    /**
     * 对{@code code}的值进行相等性测试
     *
     * @param value 基准值
     * @return 返回ture表示不相等
     */
    public boolean codeNotEquals(int value) {
        return !codeEquals(value);
    }

    /**
     * 断言{@code code}的值
     *
     * @param expect 预期的值
     * @param func   用户函数,负责创建异常对象
     * @param <Ex>   异常类型
     * @return 返回实例，以便于继续进行链式操作
     * @throws Ex 断言失败时抛出
     */
    public <Ex extends Exception> RetOps<T> assertCode(int expect, Function<? super RestResponse<T>, ? extends Ex> func)
        throws Ex {
        if (codeNotEquals(expect)) {
            throw func.apply(original);
        }
        return this;
    }

    /**
     * 断言成功
     *
     * @param func 用户函数,负责创建异常对象
     * @param <Ex> 异常类型
     * @return 返回实例，以便于继续进行链式操作
     * @throws Ex 断言失败时抛出
     */
    public <Ex extends Exception> RetOps<T> assertSuccess(Function<? super RestResponse<T>, ? extends Ex> func)
        throws Ex {
        if (!CODE_SUCCESS.test(original)) {
            throw func.apply(original);
        }
        return this;
    }

    /**
     * 断言业务数据有值
     *
     * @param func 用户函数,负责创建异常对象
     * @param <Ex> 异常类型
     * @return 返回实例，以便于继续进行链式操作
     * @throws Ex 断言失败时抛出
     */
    public <Ex extends Exception> RetOps<T> assertHasData(Function<? super RestResponse<T>, ? extends Ex> func)
        throws Ex {
        if (!HAS_DATA.test(original)) {
            throw func.apply(original);
        }
        return this;
    }

    /**
     * 对业务数据(data)转换
     *
     * @param mapper 业务数据转换函数
     * @param <U>    数据类型
     * @return 返回新实例，以便于继续进行链式操作
     */
    public <U> RetOps<U> map(Function<? super T, ? extends U> mapper) {
        RestResponse<U> result =
            RestResponse.build(mapper.apply(original.getData()), original.getCode(), original.getMessage());
        return of(result);
    }

    /**
     * 消费数据,注意此方法保证数据可用
     *
     * @param consumer 消费函数
     */
    public void accept(Consumer<? super T> consumer) {
        consumer.accept(original.getData());
    }

    /**
     * 条件消费(错误代码表示成功)
     *
     * @param consumer 消费函数
     */
    public void acceptIfSuccess(Consumer<? super T> consumer) {
        acceptIf(CODE_SUCCESS, consumer);
    }

    public void acceptIfHasData(Consumer<? super T> consumer) {
        acceptIf(HAS_DATA, consumer);
    }

    /**
     * 条件消费
     *
     * @param predicate 断言函数
     * @param consumer  消费函数,断言函数返回{@code true}时被调用
     * @see RetOps#CODE_SUCCESS
     * @see RetOps#HAS_DATA
     */
    public void acceptIf(Predicate<? super RestResponse<T>> predicate, Consumer<? super T> consumer) {
        if (predicate.test(original)) {
            consumer.accept(original.getData());
        }
    }

    /**
     * 条件消费(错误代码匹配某个值)
     *
     * @param consumer 消费函数
     * @param codes    错误代码集合,匹配任意一个则调用消费函数
     */
    public void acceptIf(Consumer<? super T> consumer, int... codes) {
        acceptIf(
            o -> Arrays.stream(codes)
                .filter(c -> original.getCode() == c)
                .findFirst()
                .isPresent(),
            consumer);
    }
}
