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
package io.github.rose.logging;


public class Logging {

    private final Logstash logstash = new Logstash();

    private final Loki loki = new Loki();

    private boolean useJsonFormat = false;

    public Logstash getLogstash() {
        return logstash;
    }

    public Loki getLoki() {
        return loki;
    }

    public boolean isUseJsonFormat() {
        return useJsonFormat;
    }

    public void setUseJsonFormat(boolean useJsonFormat) {
        this.useJsonFormat = useJsonFormat;
    }

    public static class Loki {

        private String url = "http://localhost:3100/loki/api/v1/push";

        private String labelPattern = "application=${appName},host=${HOSTNAME},level=%level";

        private String messagePattern = "%level %logger{36} %thread | %msg %ex";

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLabelPattern() {
            return labelPattern;
        }

        public void setLabelPattern(String labelPattern) {
            this.labelPattern = labelPattern;
        }

        public String getMessagePattern() {
            return messagePattern;
        }

        public void setMessagePattern(String messagePattern) {
            this.messagePattern = messagePattern;
        }
    }

    public static class Logstash {

        private boolean enabled = false;

        private String host = "localhost";

        private int port = 5000;

        private int ringBufferSize = 512;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getRingBufferSize() {
            return ringBufferSize;
        }

        public void setRingBufferSize(int ringBufferSize) {
            this.ringBufferSize = ringBufferSize;
        }
    }
}
