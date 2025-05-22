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
package cc.rosestack.github.iore.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.*;

//@NotThreadSafe
public class CurrentInstanceTest {

    @BeforeAll
    public static void clearExistingThreadLocals() {
        // Ensure no previous test left some thread locals hanging
        CurrentInstance.clearAll();
    }

    @Test
    public void testInitiallyCleared() throws Exception {
        assertCleared();
    }

    @Test
    public void testClearedAfterRemove() throws Exception {
        CurrentInstance.set(CurrentInstanceTest.class, this);
        Assertions.assertEquals(this, CurrentInstance.get(CurrentInstanceTest.class));
        CurrentInstance.set(CurrentInstanceTest.class, null);

        assertCleared();
    }

    @Test
    public void testClearedWithClearAll() throws Exception {
        CurrentInstance.set(CurrentInstanceTest.class, this);
        Assertions.assertEquals(this,
            CurrentInstance.get(CurrentInstanceTest.class));
        CurrentInstance.clearAll();

        assertCleared();
    }

    private void assertCleared() throws SecurityException, NoSuchFieldException,
        IllegalAccessException {
        Assertions.assertNull(getInternalCurrentInstanceVariable().get());
    }

    @SuppressWarnings("unchecked")
    private ThreadLocal<Map<Class<?>, CurrentInstance>> getInternalCurrentInstanceVariable()
        throws SecurityException, NoSuchFieldException,
        IllegalAccessException {
        Field f = CurrentInstance.class.getDeclaredField("instances");
        f.setAccessible(true);
        return (ThreadLocal<Map<Class<?>, CurrentInstance>>) f.get(null);
    }

    @Test
    public void nonInheritableThreadLocals()
        throws InterruptedException, ExecutionException {
        CurrentInstance.clearAll();
        CurrentInstance.set(CurrentInstanceTest.class, this);

        Assertions.assertNotNull(CurrentInstance.get(CurrentInstanceTest.class));

        Callable<Void> runnable = () -> {
            Assertions.assertNull(CurrentInstance.get(CurrentInstanceTest.class));
            return null;
        };
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Void> future = service.submit(runnable);
        future.get();
    }
}
