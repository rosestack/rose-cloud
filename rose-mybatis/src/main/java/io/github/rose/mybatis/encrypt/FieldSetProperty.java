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
package io.github.rose.mybatis.encrypt;

import io.github.rose.mybatis.encrypt.annotation.FieldBind;
import io.github.rose.mybatis.encrypt.annotation.FieldEncrypt;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class FieldSetProperty {

    private String fieldName;

    private FieldEncrypt fieldEncrypt;

    private FieldBind fieldBind;

    public FieldSetProperty() {}

    public FieldSetProperty(String fieldName, FieldEncrypt fieldEncrypt, FieldBind fieldBind) {
        this.fieldName = fieldName;
        this.fieldEncrypt = fieldEncrypt;
        this.fieldBind = fieldBind;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public FieldEncrypt getFieldEncrypt() {
        return fieldEncrypt;
    }

    public void setFieldEncrypt(FieldEncrypt fieldEncrypt) {
        this.fieldEncrypt = fieldEncrypt;
    }

    public FieldBind getFieldBind() {
        return fieldBind;
    }

    public void setFieldBind(FieldBind fieldBind) {
        this.fieldBind = fieldBind;
    }
}
