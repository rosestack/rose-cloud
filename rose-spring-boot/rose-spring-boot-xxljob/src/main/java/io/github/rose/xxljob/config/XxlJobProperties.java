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

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * XXL-Job 配置类
 */
@ConfigurationProperties("xxl.job")
@Validated
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public AdminProperties getAdmin() {
        return admin;
    }

    public void setAdmin(AdminProperties admin) {
        this.admin = admin;
    }

    public ExecutorProperties getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorProperties executor) {
        this.executor = executor;
    }

    public ClientProperties getClient() {
        return client;
    }

    public void setClient(ClientProperties client) {
        this.client = client;
    }

    /**
     * XXL-Job 调度器配置类
     */
    public static class AdminProperties {
        /**
         * 调度器地址
         */
        private String addresses;

        public String getAddresses() {
            return addresses;
        }

        public void setAddresses(String addresses) {
            this.addresses = addresses;
        }
    }

    /**
     * XXL-Job 执行器配置类
     */
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

        public static String getDefaultLogPath() {
            return DEFAULT_LOG_PATH;
        }

        public static void setDefaultLogPath(String defaultLogPath) {
            DEFAULT_LOG_PATH = defaultLogPath;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getLogPath() {
            return logPath;
        }

        public void setLogPath(String logPath) {
            this.logPath = logPath;
        }

        public Integer getLogRetentionDays() {
            return logRetentionDays;
        }

        public void setLogRetentionDays(Integer logRetentionDays) {
            this.logRetentionDays = logRetentionDays;
        }
    }

    @Valid
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

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAlarmEmail() {
            return alarmEmail;
        }

        public void setAlarmEmail(String alarmEmail) {
            this.alarmEmail = alarmEmail;
        }

        public String getScheduleType() {
            return scheduleType;
        }

        public void setScheduleType(String scheduleType) {
            this.scheduleType = scheduleType;
        }

        public String getGlueType() {
            return glueType;
        }

        public void setGlueType(String glueType) {
            this.glueType = glueType;
        }

        public String getExecutorRouteStrategy() {
            return executorRouteStrategy;
        }

        public void setExecutorRouteStrategy(String executorRouteStrategy) {
            this.executorRouteStrategy = executorRouteStrategy;
        }

        public String getMisfireStrategy() {
            return misfireStrategy;
        }

        public void setMisfireStrategy(String misfireStrategy) {
            this.misfireStrategy = misfireStrategy;
        }

        public String getExecutorBlockStrategy() {
            return executorBlockStrategy;
        }

        public void setExecutorBlockStrategy(String executorBlockStrategy) {
            this.executorBlockStrategy = executorBlockStrategy;
        }

        public int getExecutorTimeout() {
            return executorTimeout;
        }

        public void setExecutorTimeout(int executorTimeout) {
            this.executorTimeout = executorTimeout;
        }

        public int getExecutorFailRetryCount() {
            return executorFailRetryCount;
        }

        public void setExecutorFailRetryCount(int executorFailRetryCount) {
            this.executorFailRetryCount = executorFailRetryCount;
        }
    }
}
