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
package io.github.rose.feign.core;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.github.rose.core.SecurityConstants;
import io.github.rose.feign.annotation.NoToken;
import java.lang.reflect.Method;
import org.springframework.core.Ordered;

public class FeignInnerRequestInterceptor implements RequestInterceptor, Ordered {

    /**
     * Called for every request. Add data using methods on the supplied
     * {@link RequestTemplate}.
     *
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {
        Method method = template.methodMetadata().method();
        NoToken noToken = method.getAnnotation(NoToken.class);
        if (noToken != null) {
            template.header(SecurityConstants.FROM, SecurityConstants.FROM_IN);
        }
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
