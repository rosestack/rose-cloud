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
package io.github.rose.mybatis;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import io.github.rose.mybatis.datapermission.aop.DataPermissionAnnotationAdvisor;
import io.github.rose.mybatis.datapermission.db.DataPermissionRuleHandler;
import io.github.rose.mybatis.datapermission.rule.DataPermissionRule;
import io.github.rose.mybatis.datapermission.rule.DataPermissionRuleFactory;
import io.github.rose.mybatis.datapermission.rule.DataPermissionRuleFactoryImpl;
import io.github.rose.mybatis.util.MyBatisUtils;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 数据权限的自动配置类
 */
@AutoConfiguration
public class MybatisDataPermissionConfiguration {

    @Bean
    public DataPermissionRuleFactory dataPermissionRuleFactory(List<DataPermissionRule> rules) {
        return new DataPermissionRuleFactoryImpl(rules);
    }

    @Bean
    public DataPermissionRuleHandler dataPermissionRuleHandler(
            MybatisPlusInterceptor interceptor, DataPermissionRuleFactory ruleFactory) {
        // 创建 DataPermissionInterceptor 拦截器
        DataPermissionRuleHandler handler = new DataPermissionRuleHandler(ruleFactory);
        DataPermissionInterceptor inner = new DataPermissionInterceptor(handler);
        // 需要加在首个，主要是为了在分页插件前面。这个是 MyBatis Plus 的规定
        MyBatisUtils.addInterceptor(interceptor, inner, 0);
        return handler;
    }

    @Bean
    public DataPermissionAnnotationAdvisor dataPermissionAnnotationAdvisor() {
        return new DataPermissionAnnotationAdvisor();
    }
}
