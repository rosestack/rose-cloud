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
package io.github.rose.core.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * <p>
 * ExceptionHandleAsyncTaskExecutor class.
 * </p>
 */
public class ExceptionHandleAsyncTaskExecutor implements AsyncTaskExecutor, InitializingBean, DisposableBean {

    static final String EXCEPTION_MESSAGE = "Caught async exception";

    private final Logger log = LoggerFactory.getLogger(ExceptionHandleAsyncTaskExecutor.class);

    private final AsyncTaskExecutor executor;

    /**
     * <p>
     * Constructor for ExceptionHandleAsyncTaskExecutor.
     * </p>
     *
     * @param executor a {@link AsyncTaskExecutor} object.
     */
    public ExceptionHandleAsyncTaskExecutor(AsyncTaskExecutor executor) {
        this.executor = executor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Runnable task) {
        executor.execute(createWrappedRunnable(task));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Runnable task, long startTimeout) {
        executor.execute(createWrappedRunnable(task), startTimeout);
    }

    private <T> Callable<T> createCallable(Callable<T> task) {
        return () -> {
            try {
                return task.call();
            } catch (Exception e) {
                handle(e);
                throw e;
            }
        };
    }

    private Runnable createWrappedRunnable(Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Exception e) {
                handle(e);
            }
        };
    }

    /**
     * <p>
     * handle.
     * </p>
     *
     * @param e a {@link Exception} object.
     */
    protected void handle(Exception e) {
        log.error(EXCEPTION_MESSAGE, e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<?> submit(Runnable task) {
        return executor.submit(createWrappedRunnable(task));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(createCallable(task));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() throws Exception {
        if (executor instanceof DisposableBean) {
            DisposableBean bean = (DisposableBean) executor;
            bean.destroy();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (executor instanceof InitializingBean) {
            InitializingBean bean = (InitializingBean) executor;
            bean.afterPropertiesSet();
        }
    }
}
