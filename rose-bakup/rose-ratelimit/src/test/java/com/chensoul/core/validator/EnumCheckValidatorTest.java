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
package cc.rose-group.github.iore.validation;

import cc.rose-group.github.iore.domain.CodeNameAware;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * EnumCheckValidator 测试
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
class EnumCheckValidatorTest {
    private static Validator validator;

    @BeforeAll
    public static void init() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void givenWrongSex_whenNotValidated_thenValidationError() {
        UserDto userDto = new UserDto("someName", "something@gmail.com", 2);

        Set<ConstraintViolation<UserDto>> constraintViolations = validator.validate(userDto);

        Assertions.assertTrue(constraintViolations.size() > 0);
    }

    @AllArgsConstructor
    @Getter
    public enum SexEnum implements CodeNameAware {
        F(0, "男"),
        M(1, "女");

        private Integer code;
        private String name;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class UserDto {
        @NotNull
        private String username;

        @NotNull
        private String email;

        @EnumCheck(enumClass = SexEnum.class, message = "性别不正确")
        private Integer sex;
    }

}
