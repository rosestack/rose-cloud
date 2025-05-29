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
package io.github.rose.core.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;
import javax.annotation.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.OrderUtils;

public final class PriorityUtils {
    private static final Logger log = LoggerFactory.getLogger(PriorityUtils.class);

    private PriorityUtils() {
        // no instantiation allowed
    }

    /***
     * A specialization of {@link #sortByPriority(List, ToIntFunction)} for a list of classes.
     * @param <T> Class definition type
     * @param someClasses the list of classes to sort.
     */
    public static <T extends Class<?>> void sortByPriority(List<T> someClasses) {
        sortByPriority(someClasses, PriorityUtils::priorityOf);
    }

    /**
     * Sort classes by <strong>descending</strong> order of their priority, meaning the class with
     * the higher priority will
     * be the first element of the sorted list. The priority is determined according to the
     * provided priority extractor.
     *
     * @param <T>               Ensure type consistency
     * @param someClasses       the list of classes to sort.
     * @param priorityExtractor a function that extract a priority from an item.
     */
    public static <T> void sortByPriority(List<T> someClasses, ToIntFunction<T> priorityExtractor) {
        someClasses.sort(Collections.reverseOrder(Comparator.comparingInt(priorityExtractor)));
    }

    /**
     * A specialization of {@link #sortByPriority(List, ToIntFunction)} for a list of objects.
     *
     * @param <T>         Ensure type consistency
     * @param someObjects the list of objects to sort.
     */
    public static <T extends Object> void sortByObjectPriority(List<T> someObjects) {
        sortByPriority(someObjects, PriorityUtils::priorityOfObject);
    }

    /**
     * Retrieves the priority of a class by using the value of the {@link Priority} annotation
     * present on the class or on
     * its superclasses. If no annotation is found, the returned priority is 0.
     *
     * @param someClass the class to extract priority from.
     * @return the priority.
     */
    public static int priorityOf(Class<?> someClass) {
        Integer order = 0;
        while (someClass != null) {
            order = OrderUtils.getOrder(someClass);
            if (order != null) {
                break;
            }
            someClass = someClass.getSuperclass();
        }
        if (order == null) {
            order = 0;
        }
        return order;
    }

    /**
     * Calls {@link #priorityOf(Class)} on the class of the specified object.
     *
     * @param object the object from which to extract class from which to extract priority.
     * @return the priority.
     */
    public static int priorityOfObject(Object object) {
        if (object instanceof Ordered) {
            return ((Ordered) object).getOrder();
        }

        return priorityOf(object.getClass());
    }
}
