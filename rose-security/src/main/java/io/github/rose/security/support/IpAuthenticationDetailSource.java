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
package io.github.rose.security.support;

import io.github.rose.core.spring.WebUtils;
import io.github.rose.core.util.NetUtils;
import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public class IpAuthenticationDetailSource
    implements AuthenticationDetailsSource<
    HttpServletRequest, IpAuthenticationDetailSource.RestAuthenticationDetail> {

    @Override
    public RestAuthenticationDetail buildDetails(HttpServletRequest request) {
        return new RestAuthenticationDetail(WebUtils.getClientIp(request), NetUtils.getLocalAddress());
    }

    public static class RestAuthenticationDetail implements Serializable {

        private final String serverAddress;

        private final String clientAddress;

        public RestAuthenticationDetail(String serverAddress, String clientAddress) {
            this.serverAddress = serverAddress;
            this.clientAddress = clientAddress;
        }

        public String getServerAddress() {
            return serverAddress;
        }

        public String getClientAddress() {
            return clientAddress;
        }
    }
}
