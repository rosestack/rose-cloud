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
package io.github.rose.upms.domain.hr;

public class EmployeeType {
    private String id;

    // 人员类型的选项编号。
    // 人员类型内默认选项的编号是固定的，即 1 ~ 5 依次表示正式、实习、外包、劳务、顾问。
    // 新建的自定义选项的编号是由系统按顺序自动生成的。如果你是首次新建自定义选项，则选项值为 6。
    private String value;
    private String name;

    // 1：内置类型（正式、实习、外包、劳务、顾问为内置类型）
    // 2：自定义（由你新建的选项均为自定义类型）
    private Integer type;

    // 1：激活 2：未激活
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
