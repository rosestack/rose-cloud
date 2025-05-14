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
package io.github.rose.core.jackson.sensitive;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public enum SensitiveType {

    /**
     * 自定义
     */
    CUSTOM,
    /**
     * 用户名, 刘*华, 徐*
     */
    CHINESE_NAME,
    /**
     * 身份证号, 110110********1234
     */
    ID_CARD,
    /**
     * 银行卡, 622202************1234
     */
    BANK_CARD,
    /**
     * 中国大陆车牌，包含普通车辆、新能源车辆
     */
    CAR_LICENSE,
    /**
     * 座机号, ****1234
     */
    TEL,
    /**
     * 手机号, 176****1234
     */
    PHONE,
    /**
     * 地址, 北京********
     */
    ADDRESS,
    /**
     * 电子邮件, s*****o@xx.com
     */
    EMAIL,

    /**
     * 密码, 永远是 ******, 与长度无关
     */
    SECRET,
    IPV4,
    IPV6,
    FIRST_MASK
}
