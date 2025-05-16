package io.github.rose.core.spi;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ServiceFinder is a {@link ServiceLoader} replacement which understands multiple classpaths.
 *
 * @author Tristan Tarrant
 * @author Brett Meyer
 * @since 6.0
 */
public class ServiceFinder {
    private static final Logger log = LoggerFactory.getLogger(ServiceFinder.class);

    public static <T> Collection<T> load(Class<T> contract, ClassLoader... loaders) {
        Map<String, T> services = new LinkedHashMap<>();

        if (loaders.length == 0) {
            try {
                ServiceLoader<T> loadedServices = ServiceLoader.load(contract);
                addServices(loadedServices, services);
            } catch (Exception e) {
                // Ignore
            }
        } else {
            for (ClassLoader loader : loaders) {
                if (loader == null) throw new NullPointerException();
                try {
                    ServiceLoader<T> loadedServices = ServiceLoader.load(contract, loader);
                    addServices(loadedServices, services);
                } catch (Exception e) {
                    // Ignore
                }
            }
        }

        if (services.isEmpty()) {
            log.info("No service impls found: %s", contract.getSimpleName());
        }
        return services.values();
    }

    private static <T> void addServices(ServiceLoader<T> loadedServices, Map<String, T> services) {
        Iterator<T> i = loadedServices.iterator();
        while (i.hasNext()) {
            try {
                T service = i.next();
                if (services.putIfAbsent(service.getClass().getName(), service) == null) {
                    log.info("Loading service impl: %s", service.getClass().getName());
                } else {
                    log.info(
                            "Ignoring already loaded service: %s",
                            service.getClass().getName());
                }
            } catch (ServiceConfigurationError e) {
                log.warn("Skipping service impl", e);
            }
        }
    }
}
