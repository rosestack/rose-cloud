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
package io.github.rose.mybatis;

import io.github.rose.mybatis.encrypt.DefaultEncryptor;
import io.github.rose.mybatis.encrypt.IEncryptor;
import io.github.rose.mybatis.encrypt.IFieldBinder;
import io.github.rose.mybatis.encrypt.interceptor.FieldDecryptInterceptor;
import io.github.rose.mybatis.encrypt.interceptor.FieldEncryptInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Configuration
@ConditionalOnProperty(prefix = "mybatis-plus.encryptor", name = "password")
public class MybatisEncryptorConfiguration {

    @Value("${mybatis-plus.encryptor.password:-123456654321}")
    private String password;

    @Bean
    @ConditionalOnMissingBean
    public FieldEncryptInterceptor fieldEncryptInterceptor(IEncryptor encryptor) {
        return new FieldEncryptInterceptor(encryptor, password);
    }

    @Bean
    @ConditionalOnMissingBean
    public FieldDecryptInterceptor fieldDecryptInterceptor(
        @Autowired(required = false) IEncryptor encryptor, @Autowired(required = false) IFieldBinder fieldBinder) {
        return new FieldDecryptInterceptor(encryptor, fieldBinder, password);
    }

    @Bean
    @ConditionalOnMissingBean
    public IEncryptor encryptor() {
        return new DefaultEncryptor();
    }
}
