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
package io.github.rose.feign;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;

public class RetryMetricsErrorDecoder extends MetricsErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        metrics(methodKey);

        FeignException exception = feign.FeignException.errorStatus(methodKey, response);
        int status = response.status();
        if (status >= 400) {
            return new RetryableException(
                    response.status(),
                    exception.getMessage(),
                    response.request().httpMethod(),
                    exception,
                    null,
                    response.request());
        }
        return exception;
    }
}
