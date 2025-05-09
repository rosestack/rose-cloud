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
package io.github.rose.springdoc.pig.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties("swagger")
public class SwaggerProperties {

    /**
     * 是否开启swagger
     */
    private Boolean enabled = true;

    /**
     * swagger会解析的包路径
     **/
    private String basePackage = "";

    /**
     * swagger会解析的url规则
     **/
    private List<String> basePath = new ArrayList<>();

    /**
     * 在basePath基础上需要排除的url规则
     **/
    private List<String> excludePath = new ArrayList<>();

    /**
     * 需要排除的服务
     */
    private List<String> ignoreProviders = new ArrayList<>();

    /**
     * 标题
     **/
    private String title = "";

    /**
     * 网关
     */
    private String gateway;

    private Security security = new Security();

    @Data
    public static class Security {

        private Boolean enabled = true;

        /**
         * 获取token
         */
        private String tokenUrl;

        /**
         * 作用域
         */
        private String scope;
    }
}
