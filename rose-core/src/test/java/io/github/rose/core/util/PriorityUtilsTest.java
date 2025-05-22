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

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import javax.annotation.Priority;
import java.util.List;

public class PriorityUtilsTest {

    @Test
    public void testPriorityOf() {
        Assertions.assertThat(PriorityUtils.priorityOf(NoPriority.class)).isEqualTo(0);
        Assertions.assertThat(PriorityUtils.priorityOf(ChildOfNoPriority.class)).isEqualTo(0);
        Assertions.assertThat(PriorityUtils.priorityOf(Priority10.class)).isEqualTo(10);
        Assertions.assertThat(PriorityUtils.priorityOf(ChildOf10Priority.class)).isEqualTo(10);
    }

    @Test
    public void testPriorityOfObject() {
        Assertions.assertThat(PriorityUtils.priorityOfObject(new NoPriority())).isEqualTo(0);
        Assertions.assertThat(PriorityUtils.priorityOfObject(new ChildOfNoPriority())).isEqualTo(0);
        Assertions.assertThat(PriorityUtils.priorityOfObject(new Priority10())).isEqualTo(10);
        Assertions.assertThat(PriorityUtils.priorityOfObject(new ChildOf10Priority())).isEqualTo(10);
    }

    @Test
    public void testSortByPriority() {
        List<Class<? extends Object>> classes =
            Lists.list(Priority10.class, Negative10Priority.class, NoPriority.class);

        Assertions.assertThat(classes)
            .containsExactly(Priority10.class, Negative10Priority.class, NoPriority.class);

        PriorityUtils.sortByPriority(classes);

        Assertions.assertThat(classes)
            .containsExactly( Priority10.class, NoPriority.class, Negative10Priority.class);
    }

    @Test
    public void testSortByObjectPriority() {
        Priority10 p10 = new Priority10();
        Negative10Priority pMinus10 = new Negative10Priority();
        NoPriority pNone = new NoPriority();

        List<Object> objects = Lists.list(p10, pMinus10, pNone);

        Assertions.assertThat(objects).containsExactly(p10, pMinus10, pNone);

        PriorityUtils.sortByObjectPriority(objects);
        Assertions.assertThat(objects).containsExactly(p10, pNone, pMinus10);
    }

    public class NoPriority {
        // Fixture Class
    }


    @Priority(10)
    public class Priority10 {
        // Fixture class
    }

    @Priority(-10)
    public class Negative10Priority {
        // Fixture Class
    }

    public class ChildOfNoPriority extends NoPriority {
        // Fixture class
    }

    public class ChildOf10Priority extends Priority10 {
        // Fixture class
    }
}
