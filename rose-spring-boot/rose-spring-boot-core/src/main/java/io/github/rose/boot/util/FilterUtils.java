package io.github.rose.boot.util;

import org.springframework.boot.web.servlet.FilterRegistrationBean;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
public class FilterUtils {
    public static <T extends Filter> FilterRegistrationBean<T> createFilterBean(T filter, Integer order) {
        FilterRegistrationBean<T> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName(filter.getClass().getSimpleName());
        registrationBean.setOrder(order);
        return registrationBean;
    }
}
