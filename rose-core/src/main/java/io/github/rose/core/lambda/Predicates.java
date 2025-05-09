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
package io.github.rose.core.lambda;

import java.util.function.Predicate;

public final class Predicates {

    public static Predicate[] EMPTY_PREDICATE_ARRAY = new Predicate[0];

    public static <T> Predicate<T>[] emptyArray() {
        return (Predicate<T>[]) EMPTY_PREDICATE_ARRAY;
    }

    public static <T> Predicate<T> alwaysTrue() {
        return e -> Boolean.TRUE;
    }

    public static <T> Predicate<T> alwaysFalse() {
        return e -> Boolean.FALSE;
    }

    public static <T> Predicate<T> of(boolean condition) {
        return e -> condition;
    }

    /**
     * a composed predicate that represents a short-circuiting logical AND of
     * {@link Predicate predicates}
     *
     * @param predicates {@link Predicate predicates}
     * @param <T>        the type to test
     * @return non-null
     */
    public static <T> Predicate<? super T> and(Predicate<? super T>... predicates) {
        int length = predicates == null ? 0 : predicates.length;
        if (length == 0) {
            return alwaysTrue();
        } else if (length == 1) {
            return predicates[0];
        } else {
            Predicate<T> andPredicate = alwaysTrue();
            for (Predicate<? super T> p : predicates) {
                andPredicate = andPredicate.and(p);
            }
            return andPredicate;
        }
    }

    public static <T> Predicate<? super T> not(Predicate<? super T>... predicates) {
        if (predicates == null || predicates.length == 0) {
            // 如果没有传入任何Predicate，返回一个总是返回true的Predicate
            return e -> true;
        } else if (predicates.length == 1) {
            // 如果只有一个Predicate，直接对其取反
            return predicates[0].negate();
        } else {
            // 如果有多个Predicate，对它们全部取反
            Predicate<T> notPredicate = alwaysTrue();
            for (Predicate<? super T> p : predicates) {
                notPredicate = notPredicate.and(p.negate());
            }
            return notPredicate;
        }
    }

    /**
     * a composed predicate that represents a short-circuiting logical OR of
     * {@link Predicate predicates}
     *
     * @param predicates {@link Predicate predicates}
     * @param <T>        the detected type
     * @return non-null
     */
    public static <T> Predicate<? super T> or(Predicate<? super T>... predicates) {
        int length = predicates == null ? 0 : predicates.length;
        if (length == 0) {
            return alwaysTrue();
        } else if (length == 1) {
            return predicates[0];
        } else {
            Predicate<T> orPredicate = alwaysFalse();
            for (Predicate<? super T> p : predicates) {
                orPredicate = orPredicate.or(p);
            }
            return orPredicate;
        }
    }
}
