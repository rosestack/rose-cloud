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
package io.github.rose.core.function.pipeline;

/**
 * @param <I>
 * @param <O>
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
public class Pipeline<I, O> {

    private final Handler<I, O> currentHandler;

    public Pipeline(Handler<I, O> currentHandler) {
        this.currentHandler = currentHandler;
    }

    public <K> Pipeline<I, K> addHandler(Handler<O, K> newHandler) {
        return new Pipeline<>(input -> newHandler.process(currentHandler.process(input)));
    }

    public O execute(I input) {
        return currentHandler.process(input);
    }
}
