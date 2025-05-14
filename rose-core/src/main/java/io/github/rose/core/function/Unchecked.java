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
package io.github.rose.core.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.function.*;

/**
 * Improved interoperability between checked exceptions and Java 8.
 * <p>
 * Checked exceptions are one of Java's biggest flaws. Due to backwards-compatibility,
 * we're inheriting all the checked exception trouble back from JDK 1.0. This becomes even
 * more obvious when using lambda expressions, most of which are not allowed to throw
 * checked exceptions.
 * <p>
 * This library tries to ease some pain and wraps / unwraps a variety of API elements from
 * the JDK 8 to improve interoperability with checked exceptions.
 *
 * @author Lukas Eder
 */
public final class Unchecked {

    /**
     * A {@link Consumer} that wraps any {@link Throwable} in a {@link RuntimeException}.
     */
    public static final Consumer<Throwable> THROWABLE_TO_RUNTIME_EXCEPTION = Unchecked::uncheckedThrow;

    /**
     * A {@link Consumer} that rethrows all exceptions, including checked exceptions.
     */
    public static final Consumer<Throwable> RETHROW_ALL = Unchecked::sneakyThrow;

    /**
     * No instances
     */
    private Unchecked() {
    }

    @SuppressWarnings("unchecked")
    public static <E extends Throwable> void sneakyThrow(Throwable t) throws E {
        throw (E) t;
    }

    public static <E extends Throwable> void uncheckedThrow(Throwable t) throws E {
        if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        }

        if (t instanceof IOException) {
            throw new UncheckedIOException((IOException) t);
        }

        if (t instanceof InterruptedException) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(t);
        }

        throw new RuntimeException(t);
    }

    /**
     * Wrap a {@link CheckedRunnable} in a {@link Runnable}.
     * <p>
     * Example: <pre><code>
     * new Thread(Unchecked.runnable(() -> {
     *     throw new Exception("Cannot run this thread");
     * })).start();
     * </code></pre>
     */
    public static Runnable runnable(CheckedRunnable runnable) {
        return runnable(runnable, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedRunnable} in a {@link Runnable} with a custom handler for
     * checked exceptions.
     * <p>
     * Example: <pre><code>
     * new Thread(Unchecked.runnable(
     *     () -> {
     *         throw new Exception("Cannot run this thread");
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * )).start();
     * </code></pre>
     */
    public static Runnable runnable(CheckedRunnable runnable, Consumer<Throwable> handler) {
        return () -> {
            try {
                runnable.run();
            } catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedCallable <T>} in a {@link Callable<T>}.
     * <p>
     * Example: <pre><code>
     * Executors.newFixedThreadPool(1).submit(Unchecked.callable(() -> {
     *     throw new Exception("Cannot execute this task");
     * })).get();
     * </code></pre>
     */
    public static <T> Callable<T> callable(CheckedCallable<T> callable) {
        return callable(callable, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedCallable<T>} in a {@link Callable<T>} with a custom handler
     * for checked exceptions.
     * <p>
     * Example: <pre><code>
     * Executors.newFixedThreadPool(1).submit(Unchecked.callable(
     *     () -> {
     *         throw new Exception("Cannot execute this task");
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * )).get();
     * </code></pre>
     */
    public static <T> Callable<T> callable(CheckedCallable<T> callable, Consumer<Throwable> handler) {
        return () -> {
            try {
                return callable.call();
            } catch (Exception e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedComparator} in a {@link Comparator}.
     */
    public static <T> Comparator<T> comparator(CheckedComparator<T> comparator) {
        return comparator(comparator, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedComparator} in a {@link Comparator} with a custom handler for
     * checked exceptions.
     */
    public static <T> Comparator<T> comparator(CheckedComparator<T> comparator, Consumer<Throwable> handler) {
        return (t1, t2) -> {
            try {
                return comparator.compare(t1, t2);
            } catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedBiConsumer} in a {@link BiConsumer}.
     * <p>
     * Example: <pre><code>
     * map.forEach(Unchecked.biConsumer((k, v) -> {
     *     if (k == null || v == null)
     *         throw new Exception("No nulls allowed in map");
     * }));
     * </code></pre>
     */
    public static <T, U> BiConsumer<T, U> biConsumer(CheckedBiConsumer<T, U> consumer) {
        return biConsumer(consumer, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedBiConsumer} in a {@link BiConsumer} with a custom handler for
     * checked exceptions.
     * <p>
     * Example: <pre><code>
     * map.forEach(Unchecked.biConsumer(
     *     (k, v) -> {
     *         if (k == null || v == null)
     *             throw new Exception("No nulls allowed in map");
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <T, U> BiConsumer<T, U> biConsumer(CheckedBiConsumer<T, U> consumer, Consumer<Throwable> handler) {
        return (t, u) -> {
            try {
                consumer.accept(t, u);
            } catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedBiFunction} in a {@link BiFunction}.
     * <p>
     * Example: <pre><code>
     * map.computeIfPresent("key", Unchecked.biFunction((k, v) -> {
     *     if (k == null || v == null)
     *         throw new Exception("No nulls allowed in map");
     *
     *     return 42;
     * }));
     * </code></pre>
     */
    public static <T, U, R> BiFunction<T, U, R> biFunction(CheckedBiFunction<T, U, R> function) {
        return biFunction(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedBiFunction} in a {@link BiFunction} with a custom handler for
     * checked exceptions.
     * <p>
     * Example: <pre><code>
     * map.computeIfPresent("key", Unchecked.biFunction(
     *     (k, v) -> {
     *         if (k == null || v == null)
     *             throw new Exception("No nulls allowed in map");
     *
     *         return 42;
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <T, U, R> BiFunction<T, U, R> biFunction(
        CheckedBiFunction<T, U, R> function, Consumer<Throwable> handler) {
        return (t, u) -> {
            try {
                return function.apply(t, u);
            } catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedBiPredicate} in a {@link BiPredicate}.
     */
    public static <T, U> BiPredicate<T, U> biPredicate(CheckedBiPredicate<T, U> predicate) {
        return biPredicate(predicate, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedBiPredicate} in a {@link BiPredicate} with a custom handler
     * for checked exceptions.
     */
    public static <T, U> BiPredicate<T, U> biPredicate(
        CheckedBiPredicate<T, U> predicate, Consumer<Throwable> handler) {
        return (t, u) -> {
            try {
                return predicate.test(t, u);
            } catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedFunction} in a {@link Function}.
     * <p>
     * Example: <pre><code>
     * map.computeIfAbsent("key", Unchecked.function(k -> {
     *     if (k.length() > 10)
     *         throw new Exception("Only short strings allowed");
     *
     *     return 42;
     * }));
     * </code></pre>
     */
    public static <T, R> Function<T, R> function(CheckedFunction<T, R> function) {
        return function(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedFunction} in a {@link Function} with a custom handler for
     * checked exceptions.
     * <p>
     * Example: <pre><code>
     * map.forEach(Unchecked.function(
     *     k -> {
     *         if (k.length() > 10)
     *             throw new Exception("Only short strings allowed");
     *
     *         return 42;
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <T, R> Function<T, R> function(CheckedFunction<T, R> function, Consumer<Throwable> handler) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedPredicate} in a {@link Predicate}.
     * <p>
     * Example: <pre><code>
     * Stream.of("a", "b", "c").filter(Unchecked.predicate(s -> {
     *     if (s.length() > 10)
     *         throw new Exception("Only short strings allowed");
     *
     *     return true;
     * }));
     * </code></pre>
     */
    public static <T> Predicate<T> predicate(CheckedPredicate<T> predicate) {
        return predicate(predicate, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedPredicate} in a {@link Predicate} with a custom handler for
     * checked exceptions.
     * <p>
     * Example: <pre><code>
     * Stream.of("a", "b", "c").filter(Unchecked.predicate(
     *     s -> {
     *         if (s.length() > 10)
     *             throw new Exception("Only short strings allowed");
     *
     *         return true;
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <T> Predicate<T> predicate(CheckedPredicate<T> function, Consumer<Throwable> handler) {
        return t -> {
            try {
                return function.test(t);
            } catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedSupplier} in a {@link Supplier}.
     * <p>
     * Example: <pre><code>
     * ResultSet rs = statement.executeQuery();
     * Stream.generate(Unchecked.supplier(() -> rs.getObject(1)));
     * </code></pre>
     */
    public static <T> Supplier<T> supplier(CheckedSupplier<T> supplier) {
        return supplier(supplier, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedSupplier} in a {@link Supplier} with a custom handler for
     * checked exceptions.
     * <p>
     * Example: <pre><code>
     * ResultSet rs = statement.executeQuery();
     *
     * Stream.generate(Unchecked.supplier(
     *     () -> rs.getObject(1),
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <T> Supplier<T> supplier(CheckedSupplier<T> supplier, Consumer<Throwable> handler) {
        return () -> {
            try {
                return supplier.get();
            } catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedConsumer} in a {@link Consumer}.
     * <p>
     * Example: <pre><code>
     * Arrays.asList("a", "b").stream().forEach(Unchecked.consumer(s -> {
     *     if (s.length() > 10)
     *         throw new Exception("Only short strings allowed");
     * }));
     * </code></pre>
     */
    public static <T> Consumer<T> consumer(CheckedConsumer<T> consumer) {
        return consumer(consumer, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedConsumer} in a {@link Consumer} with a custom handler for
     * checked exceptions.
     * <p>
     * Example: <pre><code>
     * Arrays.asList("a", "b").stream().forEach(Unchecked.consumer(
     *     s -> {
     *         if (s.length() > 10)
     *             throw new Exception("Only short strings allowed");
     *     },
     *     e -> {
     *         throw new RuntimeException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <T> Consumer<T> consumer(CheckedConsumer<T> consumer, Consumer<Throwable> handler) {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }
}
