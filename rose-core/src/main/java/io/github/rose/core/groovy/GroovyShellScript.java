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
package io.github.rose.core.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.Script;
import io.github.rose.core.util.concurrent.TryLock;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class GroovyShellScript implements ExecutableScript {
    private static final ThreadLocal<Map<String, Object>> BINDING_THREAD_LOCAL = new InheritableThreadLocal<>();
    private static final Logger log = LoggerFactory.getLogger(GroovyShellScript.class);
    private final TryLock lock = new TryLock();
    private final String script;
    private Script groovyScript;

    public GroovyShellScript(String script) {
        this.script = script;
    }

    public String getScript() {
        return script;
    }

    @Override
    public <T> T execute(final Object[] args, final Class<T> clazz) throws Throwable {
        return execute(args, clazz, true);
    }

    @Override
    public void execute(final Object[] args) throws Throwable {
        execute(args, Void.class, true);
    }

    @Override
    public <T> T execute(final Object[] args, final Class<T> clazz, final boolean failOnError) throws Throwable {
        if (lock.tryLock()) {
            try {
                log.trace("Beginning to execute script [{}]", this);
                Map<String, Object> binding = BINDING_THREAD_LOCAL.get();
                if (groovyScript == null) {
                    groovyScript = ScriptingUtils.parseGroovyShellScript(binding, script);
                }
                if (binding != null && !binding.isEmpty()) {
                    log.trace("Setting binding [{}]", binding);
                    groovyScript.setBinding(new Binding(binding));
                }
                log.trace("Current binding [{}]", groovyScript.getBinding());
                Object result = ScriptingUtils.executeGroovyShellScript(groovyScript, clazz);
                log.debug("Groovy script [{}] returns result [{}]", this, result);
                return (T) result;
            } catch (final GroovyRuntimeException e) {
                log.error("Groovy script [{}] execution error", this, e);
            } finally {
                BINDING_THREAD_LOCAL.remove();
                if (groovyScript != null) {
                    groovyScript.setBinding(new Binding(new HashMap()));
                }
                log.trace("Completed script execution [{}]", this);
                lock.unlock();
            }
        }
        return null;
    }

    public Script getGroovyScript() {
        return groovyScript;
    }

    @Override
    public <T> T execute(final String methodName, final Class<T> clazz, final Object... args) throws Throwable {
        return execute(args, clazz);
    }

    @Override
    public void setBinding(final Map<String, Object> args) {
        BINDING_THREAD_LOCAL.set(new HashMap<>(args));
    }
}
