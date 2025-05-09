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
package io.github.rose.core;

/**
 * Constants for Spring Security authorities.
 */
public interface SecurityConstants {

    /**
     * 内部
     */
    String FROM_IN = "Y";

    /**
     * 标志
     */
    String FROM = "from";

    String ADMIN = "ROLE_ADMIN";

    String USER = "ROLE_USER";

    String ANONYMOUS = "ROLE_ANONYMOUS";

    /**
     * 角色前缀
     */
    String ROLE = "ROLE_";

    /**
     * OAUTH2 令牌类型 https://oauth.net/2/bearer-tokens/
     */
    String OAUTH2_TOKEN_TYPE = "bearer ";

    /**
     * sys_client 表的字段，code 指 client_id
     */
    String CLIENT_FIELDS = "code, CONCAT('{noop}',secret) as client_secret, resource_ids, scope, "
        + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
        + "refresh_token_validity, additional_information, autoapprove";

    /**
     * JdbcClientDetailsService 查询语句
     */
    String BASE_FIND_STATEMENT = "select " + CLIENT_FIELDS + " from sys_client";

    /**
     * 默认的查询语句
     */
    String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by code";

    /**
     * 按条件client_id 查询
     */
    String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where code = ?";

    /**
     * 资源服务器默认bean名称
     */
    String RESOURCE_SERVER_CONFIGURER = "resourceServerConfigurerAdapter";

    /**
     * 刷新模式
     */
    String REFRESH_TOKEN = "refresh_token";

    /**
     * 授权码模式
     */
    String AUTHORIZATION_CODE = "authorization_code";

    /**
     * 客户端模式
     */
    String CLIENT_CREDENTIALS = "client_credentials";

    /**
     * 密码模式
     */
    String PASSWORD = "password";

    /**
     * 简化模式
     */
    String IMPLICIT = "implicit";

    String GRANT_TYPE = "grant_type";

    String JWT_USER_ID = "user_id";

    String JWT_NAME = "name";

    String JWT_USERNAME = "user_name";

    String JWT_AUTHORITIES = "authorities";

    String JWT_CLIENT_ID = "client_id";

    String JWT_SCOPE = "scope";

    String JWT_AUTHORITY = "authority";
}
