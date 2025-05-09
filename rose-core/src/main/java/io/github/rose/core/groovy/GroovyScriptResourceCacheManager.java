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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.rose.core.lambda.function.CheckedConsumer;
import io.github.rose.core.spring.expression.SpringExpressionResolver;
import io.github.rose.core.util.concurrent.TryLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class GroovyScriptResourceCacheManager implements ScriptResourceCacheManager<String, ExecutableScript> {
    private final TryLock lock = new TryLock();
    private final Cache<String, ExecutableScript> cache;
    Logger log = LoggerFactory.getLogger(GroovyScriptResourceCacheManager.class);

    public GroovyScriptResourceCacheManager(final int initialCapacity, int cacheSize, final Duration duration) {
        this.cache = newCacheBuilder(initialCapacity, cacheSize, duration).build();
    }

    private Caffeine newCacheBuilder(final int initialCapacity, int cacheSize, final Duration duration) {
        Caffeine builder =
            Caffeine.newBuilder().initialCapacity(initialCapacity).maximumSize(cacheSize);
        if (duration != null) {
            builder.expireAfterWrite(duration);
        }
        builder.removalListener((key, value, cause) -> {
            log.trace("Removing cached value [{}] linked to cache key [{}]; removal cause is [{}]", value, key, cause);
            CheckedConsumer.unchecked(__ -> {
                    if (value instanceof AutoCloseable) {
                        AutoCloseable closeable = (AutoCloseable) value;
                        Objects.requireNonNull(closeable).close();
                    }
                })
                .accept(value);
        });
        return builder;
    }

    @Override
    public ExecutableScript get(final String key) {
        return lock.tryLock(() -> cache.getIfPresent(key));
    }

    @Override
    public boolean containsKey(final String key) {
        return get(key) != null;
    }

    @Override
    public ScriptResourceCacheManager<String, ExecutableScript> put(final String key, final ExecutableScript value) {
        return lock.tryLock(() -> {
            this.cache.put(key, value);
            return this;
        });
    }

    @Override
    public ScriptResourceCacheManager<String, ExecutableScript> remove(final String key) {
        return lock.tryLock(() -> {
            this.cache.invalidate(key);
            return this;
        });
    }

    @Override
    public Set<String> getKeys() {
        return lock.tryLock(() -> cache.asMap().keySet());
    }

    @Override
    public void close() {
        lock.tryLock(__ -> cache.invalidateAll());
    }

    @Override
    public boolean isEmpty() {
        return lock.tryLock(() -> cache.asMap().isEmpty());
    }

    @Override
    public ExecutableScript resolveScriptableResource(final String scriptResource, final String... keys) {

        String cacheKey = ScriptResourceCacheManager.computeKey(keys);
        log.trace("Constructed cache key [{}] for keys [{}] mapped as groovy script", cacheKey, keys);
        ExecutableScript script = null;
        if (containsKey(cacheKey)) {
            script = get(cacheKey);
            log.trace("Located cached groovy script [{}] for key [{}]", script, cacheKey);
        } else {
            try {
                if (ScriptingUtils.isExternalGroovyScript(scriptResource)) {
                    String scriptPath =
                        (String) SpringExpressionResolver.getInstance().resolve(scriptResource);
                    Resource resource = new DefaultResourceLoader().getResource(scriptPath);
                    script = new WatchableGroovyScript(resource);
                } else {
                    String resourceToUse = scriptResource;
                    if (ScriptingUtils.isInlineGroovyScript(resourceToUse)) {
                        Matcher matcher = ScriptingUtils.getMatcherForInlineGroovyScript(resourceToUse);
                        if (matcher.find()) {
                            resourceToUse = matcher.group(1);
                        }
                    }
                    script = new GroovyShellScript(resourceToUse);
                }
                log.trace("Groovy script [{}] for key [{}] is not cached", scriptResource, cacheKey);
                put(cacheKey, script);
                log.trace("Cached groovy script [{}] for key [{}]", script, cacheKey);
            } catch (final Exception e) {
                log.error("Failed to resolve groovy script [{}] for key [{}]", scriptResource, cacheKey, e);
            }
        }
        return script;
    }
}
