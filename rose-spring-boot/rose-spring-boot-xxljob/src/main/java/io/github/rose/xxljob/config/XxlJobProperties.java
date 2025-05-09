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
package io.github.rose.xxljob.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * XXL-Job 配置类
 */
@ConfigurationProperties("xxl.job")
@Validated
@Data
public class XxlJobProperties {

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 控制器配置
     */
    private AdminProperties admin = new AdminProperties();

    /**
     * 执行器配置
     */
    private ExecutorProperties executor = new ExecutorProperties();

    private ClientProperties client = new ClientProperties();

    /**
     * XXL-Job 调度器配置类
     */
    @Data
    public static class AdminProperties {
        /**
         * 调度器地址
         */
        private String addresses;
    }

    /**
     * XXL-Job 执行器配置类
     */
    @Data
    @Valid
    public static class ExecutorProperties {
        /**
         * 默认端口
         * <p>
         * 这里使用 -1 表示随机
         */
        private static final Integer DEFAULT_PORT = -1;

        /**
         * 默认日志保留天数
         * <p>
         * 如果想永久保留，则设置为 -1
         */
        private static final Integer DEFAULT_LOG_RETENTION_DAYS = 30;

        private static String DEFAULT_LOG_PATH = "/data/logs/xxl-job/";

        private Boolean enabled = true;

        /**
         * 执行器的 IP
         */
        private String ip;

        /**
         * 执行器的 Port
         */
        private Integer port = DEFAULT_PORT;

        /**
         * 日志地址
         */
        private String logPath = DEFAULT_LOG_PATH;

        /**
         * 日志保留天数
         */
        private Integer logRetentionDays = DEFAULT_LOG_RETENTION_DAYS;
    }

    @Valid
    @Data
    public static class ClientProperties {

        private Boolean enabled = false;

        @NotBlank(message = "XxlJob用户名不能为空")
        private String username;

        @NotBlank(message = "XxlJob密码不能为空")
        private String password;

        private String author = "admin";

        private String alarmEmail;

        private String scheduleType = "CRON";

        private String glueType = "BEAN";

        private String executorRouteStrategy = "ROUND";

        private String misfireStrategy = "DO_NOTHING";

        private String executorBlockStrategy = "SERIAL_EXECUTION";

        private int executorTimeout = 0;

        private int executorFailRetryCount = 0;
    }
}
