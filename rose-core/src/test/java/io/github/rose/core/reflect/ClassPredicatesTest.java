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
package io.github.rose.core.reflect;

import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ClassPredicatesTest {
    @Test
    public void classIs() throws Exception {
        assertThat(ClassPredicates.classIs(SomeAnnotation.class).test(SomeAnnotation.class)).isTrue();
        assertThat(ClassPredicates.classIs(SomeAnnotation.class).test(SomeMetaAnnotation.class)).isFalse();
    }

    @Test
    public void classIsAssignable() throws Exception {
        assertThat(ClassPredicates.classIsAssignableFrom(Number.class).test(Integer.class)).isTrue();
        assertThat(ClassPredicates.classIsAssignableFrom(Number.class).test(String.class)).isFalse();
    }

    @Test
    public void classIsInterface() throws Exception {
        assertThat(ClassPredicates.classIsInterface().test(Collection.class)).isTrue();
        assertThat(ClassPredicates.classIsInterface().test(ArrayList.class)).isFalse();
    }

    @Test
    public void classIsAnnotation() throws Exception {
        assertThat(ClassPredicates.classIsAnnotation().test(SomeAnnotation.class)).isTrue();
        assertThat(ClassPredicates.classIsInterface().test(String.class)).isFalse();
    }

    @Test
    public void classHasModifierIs() throws Exception {
        assertThat(ClassPredicates.classModifierIs(Modifier.ABSTRACT).test(AbstractCollection.class)).isTrue();
        assertThat(ClassPredicates.classModifierIs(Modifier.ABSTRACT).test(ArrayList.class)).isFalse();
    }

    @Test
    public void atLeastOneInterfaceImplemented() throws Exception {
        assertThat(ClassPredicates.atLeastOneInterfaceImplemented().test(ArrayList.class)).isTrue();
        assertThat(ClassPredicates.atLeastOneInterfaceImplemented().test(Object.class)).isFalse();
    }

    @Test
    public void atLeastOneConstructorIsPublic() throws Exception {
        assertThat(ClassPredicates.atLeastOneConstructorIsPublic().test(ClassPredicates.class)).isFalse();
        assertThat(ClassPredicates.atLeastOneConstructorIsPublic().test(String.class)).isTrue();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE})
    private @interface SomeMetaAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
    @SomeMetaAnnotation
    private @interface SomeAnnotation {
    }

    @SomeAnnotation
    private static class AnnotatedElements {
        @SomeAnnotation
        private String someField;

        @SomeAnnotation
        private void someMethod() {

        }
    }
}
