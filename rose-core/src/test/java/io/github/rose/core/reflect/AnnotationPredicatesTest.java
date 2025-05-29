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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Test;

public class AnnotationPredicatesTest {
    @Test
    public void elementAnnotatedWith() throws Exception {
        assertThat(AnnotationPredicates.elementAnnotatedWith(SomeAnnotation.class, false)
                        .test(AnnotatedElements.class))
                .isTrue();
        assertThat(AnnotationPredicates.elementAnnotatedWith(SomeAnnotation.class, false)
                        .test(AnnotatedElements.class.getDeclaredField("someField")))
                .isTrue();
        assertThat(AnnotationPredicates.elementAnnotatedWith(SomeAnnotation.class, false)
                        .test(AnnotatedElements.class.getDeclaredMethod("someMethod")))
                .isTrue();
        assertThat(AnnotationPredicates.elementAnnotatedWith(SomeMetaAnnotation.class, false)
                        .test(AnnotatedElements.class))
                .isFalse();
        assertThat(AnnotationPredicates.elementAnnotatedWith(SomeMetaAnnotation.class, false)
                        .test(AnnotatedElements.class.getDeclaredField("someField")))
                .isFalse();
        assertThat(AnnotationPredicates.elementAnnotatedWith(SomeMetaAnnotation.class, false)
                        .test(AnnotatedElements.class.getDeclaredMethod("someMethod")))
                .isFalse();
        assertThat(AnnotationPredicates.elementAnnotatedWith(SomeMetaAnnotation.class, true)
                        .test(AnnotatedElements.class))
                .isTrue();
        assertThat(AnnotationPredicates.elementAnnotatedWith(SomeMetaAnnotation.class, true)
                        .test(AnnotatedElements.class.getDeclaredField("someField")))
                .isTrue();
        assertThat(AnnotationPredicates.elementAnnotatedWith(SomeMetaAnnotation.class, true)
                        .test(AnnotatedElements.class.getDeclaredMethod("someMethod")))
                .isTrue();
    }

    @Test
    public void atLeastOneMethodAnnotatedWith() throws Exception {
        assertThat(AnnotationPredicates.atLeastOneMethodAnnotatedWith(SomeAnnotation.class, false)
                        .test(AnnotatedElements.class))
                .isTrue();
        assertThat(AnnotationPredicates.atLeastOneMethodAnnotatedWith(SomeMetaAnnotation.class, false)
                        .test(AnnotatedElements.class))
                .isFalse();
        assertThat(AnnotationPredicates.atLeastOneMethodAnnotatedWith(SomeMetaAnnotation.class, true)
                        .test(AnnotatedElements.class))
                .isTrue();
    }

    @Test
    public void atLeastOneFieldAnnotatedWith() throws Exception {
        assertThat(AnnotationPredicates.atLeastOneFieldAnnotatedWith(SomeAnnotation.class, false)
                        .test(AnnotatedElements.class))
                .isTrue();
        assertThat(AnnotationPredicates.atLeastOneFieldAnnotatedWith(SomeMetaAnnotation.class, false)
                        .test(AnnotatedElements.class))
                .isFalse();
        assertThat(AnnotationPredicates.atLeastOneFieldAnnotatedWith(SomeMetaAnnotation.class, true)
                        .test(AnnotatedElements.class))
                .isTrue();
    }

    @Test
    public void elementOrAncestorAnnotatedWith() throws Exception {
        assertThat(AnnotationPredicates.classOrAncestorAnnotatedWith(SomeAnnotation.class, false)
                        .test(ExtendingAnnotatedElements.class))
                .isTrue();
        assertThat(AnnotationPredicates.classOrAncestorAnnotatedWith(SomeMetaAnnotation.class, false)
                        .test(ExtendingAnnotatedElements.class))
                .isFalse();
        assertThat(AnnotationPredicates.classOrAncestorAnnotatedWith(SomeMetaAnnotation.class, true)
                        .test(ExtendingAnnotatedElements.class))
                .isTrue();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE})
    private @interface SomeMetaAnnotation {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
    @SomeMetaAnnotation
    private @interface SomeAnnotation {}

    @SomeAnnotation
    private static class AnnotatedElements {
        @SomeAnnotation
        private String someField;

        @SomeAnnotation
        private void someMethod() {}
    }

    private static class ExtendingAnnotatedElements extends AnnotatedElements {}
}
