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
package cc.chensoul.jpa.support;

import java.util.List;

/**
 * 校验失败异常
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class ValidationException extends RuntimeException {
    /**
     * 校验结果
     */
    private List<ValidateResult> result;

    public ValidationException(List<ValidateResult> list) {
        this.result = list;
    }

    public List<ValidateResult> getResult() {
        return this.result;
    }
}
