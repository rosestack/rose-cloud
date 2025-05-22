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
package cc.rosestack.github.iore.ratelimit;

import lombok.Data;

@Data
public class RateLimitContext {
    private String userId;    // 用户ID
    private String userLevel; // 用户等级
    private String requestResource;   // 请求资源
    private String requestMethod;   // 请求方法
    private String ip;        // IP地址
}
