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
package io.github.rose.core.benchmarks;

import io.github.rose.core.reflect.Classes;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class ClassesBenchmark {
    @Benchmark
    public void findHierarchyMethods() {
        Classes.from(SubClass.class)
            .traversingSuperclasses()
            .traversingInterfaces()
            .classes()
            .toArray();
    }

    @Benchmark
    public void findHierarchyFields() {
        Classes.from(SubClass.class)
            .traversingSuperclasses()
            .fields()
            .toArray();
    }

    @Benchmark
    public void findSpecificFieldByFilter() {
        Classes.from(SubClass.class)
            .traversingSuperclasses()
            .fields()
            .filter(field -> "baseClassField".equals(field.getName()))
            .findFirst()
            .get();
    }

    @Benchmark
    public void findSpecificFieldByName() throws NoSuchFieldException {
        Classes.from(SubClass.class)
            .traversingSuperclasses()
            .field("baseClassField")
            .get();
    }

    interface SomeInterface {
        void someInterfaceMethod();
    }

    interface SomeOtherInterface {
        void someOtherInterfaceMethod();
    }

    static class BaseClass implements SomeInterface {
        private String baseClassField;

        public void someInterfaceMethod() {
        }
    }

    static class SubClass extends BaseClass implements SomeOtherInterface {
        private String subClassField;

        public void someInterfaceMethod() {
        }

        public void someOtherInterfaceMethod() {
        }
    }
}
