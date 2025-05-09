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
package io.github.rose.xxljob.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class XxlJobGroup implements Serializable {

    private int id;

    private String appname;

    private String title;

    private int addressType; // 执行器地址类型：0=自动注册、1=手动录入

    private String addressList; // 执行器地址列表，多地址逗号分隔(手动录入)

    private Date updateTime;

    private List<String> registryList; // 执行器地址列表(系统注册)

    public List<String> getRegistryList() {
        if (addressList != null && addressList.trim().length() > 0) {
            registryList = new ArrayList<String>(Arrays.asList(addressList.split(",")));
        }
        return registryList;
    }
}
