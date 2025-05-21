/*
 * Copyright © 2013-2019, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
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
