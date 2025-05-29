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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Executable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public abstract class StandardAnnotationResolver<E extends AnnotatedElement, A extends Annotation>
        implements AnnotationResolver<E, A> {
    private final Class<A> annotationClass;

    @SuppressWarnings("unchecked")
    protected StandardAnnotationResolver() {
        Type[] actualTypeArguments = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        annotationClass = (Class<A>) Types.rawClassOf(actualTypeArguments[1]);
    }

    @Override
    public Optional<A> apply(E element) {
        return resolveAnnotation(element);
    }

    @Override
    public boolean test(E element) {
        return resolveAnnotation(element).isPresent();
    }

    private Optional<A> resolveAnnotation(E element) {
        Annotations.OnAnnotatedElement on;
        if (element instanceof Class<?>) {
            if (Annotation.class.isAssignableFrom((Class<?>) element)) {
                // do not return annotated annotations
                return Optional.empty();
            } else {
                on = Annotations.on(element);
            }
        } else if (element instanceof Executable) {
            on = Annotations.on(((Executable) element)).traversingOverriddenMembers();
        } else {
            return Optional.empty();
        }
        return on.fallingBackOnClasses().includingMetaAnnotations().find(annotationClass);
    }
}
