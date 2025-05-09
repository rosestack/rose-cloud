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

import io.github.rose.core.lambda.function.CheckedConsumer;
import io.github.rose.core.lambda.function.CheckedFunction;
import io.github.rose.core.lambda.function.CheckedSupplier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
public class Try {
    private static final Logger log = LoggerFactory.getLogger(Try.class);

    public static <T, R> Function<T, R> tryApply(
        final CheckedFunction<T, R> trueFunction, final CheckedFunction<Throwable, R> errorHandler) {
        return tryApply(Predicates.alwaysTrue(), trueFunction, null, errorHandler, null);
    }

    public static <T, R> Function<T, R> tryApply(
        final CheckedFunction<T, R> trueFunction,
        final CheckedFunction<Throwable, R> errorHandler,
        final CheckedConsumer<T> finalConsumer) {
        return tryApply(Predicates.alwaysTrue(), trueFunction, null, errorHandler, finalConsumer);
    }

    public static <T, R> Function<T, R> tryApply(
        final CheckedFunction<T, R> trueFunction, final CheckedConsumer<T> finalConsumer) {
        return tryApply(Predicates.alwaysTrue(), trueFunction, null, null, finalConsumer);
    }

    public static <T, R> Function<T, R> tryApply(
        final Predicate<T> condition, final CheckedFunction<T, R> trueFunction) {
        return tryApply(condition, trueFunction, null, null, null);
    }

    public static <T, R> Function<T, R> tryApply(
        final Predicate<T> condition,
        final CheckedFunction<T, R> trueFunction,
        final CheckedFunction<T, R> falseFunction) {
        return tryApply(condition, trueFunction, falseFunction, null, null);
    }

    public static <T, R> Function<T, R> tryApply(
        final boolean condition,
        final CheckedFunction<T, R> trueFunction,
        final CheckedFunction<T, R> falseFunction) {
        return tryApply(Predicates.of(condition), trueFunction, falseFunction, null, null);
    }

    public static <T, R> Function<T, R> tryApply(final boolean condition, final CheckedFunction<T, R> trueFunction) {
        return tryApply(Predicates.of(condition), trueFunction, null, null, null);
    }

    public static <T, R> Function<T, R> tryApply(
        final Predicate<T> condition,
        final CheckedFunction<T, R> trueFunction,
        final CheckedFunction<T, R> falseFunction,
        final CheckedFunction<Throwable, R> errorHandler,
        final CheckedConsumer<T> finalConsumer) {
        Objects.nonNull(condition);
        Objects.nonNull(trueFunction);
        return t -> {
            try {
                if (condition.test(t)) {
                    return trueFunction.apply(t);
                } else if (falseFunction != null) {
                    return falseFunction.apply(t);
                }
                return null;
            } catch (final Throwable e) {
                log.warn("tryApply error", e);
                if (errorHandler != null) {
                    return CheckedFunction.unchecked(errorHandler).apply(e);
                }
                Unchecked.uncheckedThrow(e);
                return null;
            } finally {
                if (finalConsumer != null) {
                    CheckedConsumer.unchecked(finalConsumer).accept(t);
                }
            }
        };
    }

    public static <R> Consumer<R> tryAccept(
        final CheckedConsumer<R> trueConsumer, final CheckedFunction<Throwable, R> errorHandler) {
        return tryAccept(Predicates.alwaysTrue(), trueConsumer, null, errorHandler, null);
    }

    public static <R> Consumer<R> tryAccept(final Predicate<R> condition, final CheckedConsumer<R> trueConsumer) {
        return tryAccept(condition, trueConsumer, null, null, null);
    }

    public static <R> Consumer<R> tryAccept(
        final Predicate<R> condition,
        final CheckedConsumer<R> trueConsumer,
        final CheckedConsumer<R> falseConsumer) {
        return tryAccept(condition, trueConsumer, falseConsumer, null, null);
    }

    public static <R> Consumer<R> tryAccept(
        final Predicate<R> condition,
        final CheckedConsumer<R> trueConsumer,
        final CheckedConsumer<R> falseConsumer,
        final CheckedFunction<Throwable, R> errorHandler) {
        return tryAccept(condition, trueConsumer, falseConsumer, errorHandler, null);
    }

    public static <R> Consumer<R> tryAccept(final boolean condition, final CheckedConsumer<R> trueConsumer) {
        return tryAccept(Predicates.of(condition), trueConsumer, null, null, null);
    }

    public static <R> Consumer<R> tryAccept(
        final boolean condition, final CheckedConsumer<R> trueConsumer, final CheckedConsumer<R> falseConsumer) {
        return tryAccept(Predicates.of(condition), trueConsumer, falseConsumer, null, null);
    }

    public static <R> Consumer<R> tryAccept(
        final boolean condition,
        final CheckedConsumer<R> trueConsumer,
        final CheckedConsumer<R> falseConsumer,
        final CheckedFunction<Throwable, R> errorHandler) {
        return tryAccept(Predicates.of(condition), trueConsumer, falseConsumer, errorHandler, null);
    }

    public static <R> Consumer<R> tryAccept(
        final Predicate<R> condition,
        final CheckedConsumer<R> trueConsumer,
        final CheckedConsumer<R> falseConsumer,
        final CheckedFunction<Throwable, R> errorHandler,
        final CheckedConsumer<R> finalConsumer) {
        Objects.nonNull(condition);
        Objects.nonNull(trueConsumer);
        return t -> {
            try {
                if (condition.test(t)) {
                    trueConsumer.accept(t);
                } else if (falseConsumer != null) {
                    falseConsumer.accept(t);
                }
            } catch (final Throwable e) {
                log.warn("tryAccept error", e);
                if (errorHandler != null) {
                    CheckedFunction.unchecked(errorHandler).apply(e);
                }
                Unchecked.uncheckedThrow(e);
            } finally {
                if (finalConsumer != null) {
                    CheckedConsumer.unchecked(finalConsumer).accept(t);
                }
            }
        };
    }

    public static <R> Supplier<R> tryGet(final boolean condition, final CheckedSupplier<R> trueSupplier) {
        return tryGet(condition, trueSupplier, null, null);
    }

    public static <R> Supplier<R> tryGet(
        final boolean condition, final CheckedSupplier<R> trueSupplier, final CheckedSupplier<R> falseSupplier) {
        return tryGet(condition, trueSupplier, falseSupplier, null);
    }

    public static <R> Supplier<R> tryGet(
        final boolean condition,
        final CheckedSupplier<R> trueSupplier,
        final CheckedSupplier<R> falseSupplier,
        final CheckedFunction<Throwable, R> errorHandler) {
        return tryGet(condition, trueSupplier, falseSupplier, errorHandler, null);
    }

    public static <R> Supplier<R> tryGet(
        final CheckedSupplier<R> trueSupplier, final CheckedFunction<Throwable, R> errorHandler) {
        return tryGet(true, trueSupplier, null, errorHandler, null);
    }

    public static <R> Supplier<R> tryGet(
        final boolean condition,
        final CheckedSupplier<R> trueSupplier,
        final CheckedSupplier<R> falseSupplier,
        final CheckedFunction<Throwable, R> errorHandler,
        final CheckedConsumer<R> finalConsumer) {
        Objects.nonNull(trueSupplier);

        return () -> {
            try {
                if (condition) {
                    return trueSupplier.get();
                } else if (falseSupplier != null) {
                    return falseSupplier.get();
                }
                return null;
            } catch (final Throwable e) {
                log.warn("tryGet error", e);
                if (errorHandler != null) {
                    return CheckedFunction.unchecked(errorHandler).apply(e);
                }
                Unchecked.uncheckedThrow(e);
                return null;
            } finally {
                if (finalConsumer != null) {
                    CheckedConsumer.unchecked(finalConsumer).accept(null);
                }
            }
        };
    }

    public static String throwIfBlank(final String value) throws Throwable {
        throwIf(StringUtils.isBlank(value), () -> new IllegalArgumentException("Value cannot be empty or blank"));
        return value;
    }

    public static <T> T throwIfNull(final T value, final CheckedSupplier<Throwable> handler) throws Throwable {
        throwIf(value == null, handler);
        return value;
    }

    public static void throwIf(final boolean condition, final CheckedSupplier<? extends Throwable> throwable)
        throws Throwable {
        if (condition) {
            throw throwable.get();
        }
    }
}
