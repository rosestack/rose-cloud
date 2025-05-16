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

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public abstract class BaseEntityOperation implements EntityOperation {

    static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public <T> void doValidate(T t, Class<? extends Default> group) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t, group, Default.class);
        if (!isEmpty(constraintViolations)) {
            List<ValidateResult> results = constraintViolations.stream()
                .map(cv -> new ValidateResult(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toList());
            throw new ValidationException(results);
        }
    }
}
