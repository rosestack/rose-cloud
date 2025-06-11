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
package io.github.rose.core.json.sensitive;

import io.github.rose.core.util.StringPool;
import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class Sensitives {

    public static final String MASK = "******";

    public static final String IPV4_MASK = ".*.*.*";

    public static final String IPV6_MASK = ":*:*:*:*:*:*:*";

    public static String deSensitive(String origin, int prefixKeep, int suffixKeep, String mask) {
        if (StringUtils.isBlank(origin)) {
            return StringPool.EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = origin.length(); i < n; i++) {
            if (i < prefixKeep) {
                sb.append(origin.charAt(i));
                continue;
            }
            if (i > (n - suffixKeep - 1)) {
                sb.append(origin.charAt(i));
                continue;
            }
            sb.append(mask);
        }
        return sb.toString();
    }

    /**
     * 【中文姓名】只显示最后一个汉字，其他隐藏为星号，比如：**梦
     *
     * @param fullName 姓名
     * @return 结果
     */
    public static String chineseName(String fullName) {
        return deSensitive(fullName, 0, 1, StringPool.ASTERISK);
    }

    /**
     * 【身份证号】显示前六位, 四位，其他隐藏。共计18位或者15位，比如：340304*******1234
     *
     * @param id 身份证号码
     * @return 结果
     */
    public static String idCardNum(String id) {
        return deSensitive(id, 6, 4, StringPool.ASTERISK);
    }

    /**
     * 【固定电话】后四位，其他隐藏，比如 ****1234
     *
     * @param num 固定电话
     * @return 结果
     */
    public static String tel(String num) {
        return deSensitive(num, 0, 4, StringPool.ASTERISK);
    }

    /**
     * 【手机号码】前三位，后四位，其他隐藏，比如135****6810
     *
     * @param num 手机号码
     * @return 结果
     */
    public static String phone(String num) {
        return deSensitive(num, 3, 4, StringPool.ASTERISK);
    }

    /**
     * 【地址】只显示到地区，不显示详细地址，比如：北京市海淀区****
     *
     * @param address 地址
     * @return 结果
     */
    public static String address(String address) {
        return deSensitive(address, 6, 0, StringPool.ASTERISK);
    }

    /**
     * 【电子邮箱 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示，比如：d**@126.com
     *
     * @param email 电子邮箱
     * @return 结果
     */
    public static String email(String email) {
        if (email == null) {
            return null;
        }
        int index = email.indexOf("@");
        if (index <= 1) {
            return email;
        }
        String preEmail = deSensitive(email.substring(0, index), 1, 0, StringPool.ASTERISK);
        return preEmail + email.substring(index);
    }

    /**
     * 【银行卡号】前六位，后四位，其他用星号隐藏每位1个星号，比如：622260**********1234
     *
     * @param cardNum 银行卡号
     * @return 结果
     */
    public static String bankCard(String cardNum) {
        return deSensitive(cardNum, 6, 4, StringPool.ASTERISK);
    }

    public static String carLicense(String carLicense) {
        return deSensitive(carLicense, 2, 1, StringPool.ASTERISK);
    }

    /**
     * 【密码】密码的全部字符都用*代替，比如：******
     *
     * @param secret 密码
     * @return 结果
     */
    public static String secret(String secret) {
        if (StringUtils.isBlank(secret)) {
            return StringUtils.EMPTY;
        }
        return MASK;
    }

    public static String ipv4(String ipv4) {
        return StringUtils.substringBefore(ipv4, StringPool.DOT) + IPV4_MASK;
    }

    public static String ipv6(String ipv6) {
        return StringUtils.substringBefore(ipv6, StringPool.DOT) + IPV6_MASK;
    }
}
