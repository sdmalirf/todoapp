package com.mtx.todolist.validator;

import com.mtx.todolist.dto.CreateUserDto;
import com.mtx.todolist.entity.Gender;
import com.mtx.todolist.util.LocalDateFormatter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateUserValidator implements Validator<CreateUserDto> {
    private static final CreateUserValidator INSTANCE = new CreateUserValidator();
    private static final int MIN_PASSWORD_LENGTH = 8;

    @Override
    public ValidationResult validate(CreateUserDto createUserDto) {
        ValidationResult validationResult = new ValidationResult();

        if (createUserDto.getName().isBlank()) {
            validationResult.add(Error.of("name.invalid", "Name cannot be empty"));
        }

        if (createUserDto.getPassword().isBlank()) {
            validationResult.add(Error.of("password.invalid", "Password cannot be empty"));
        } else if (createUserDto.getPassword().length() < MIN_PASSWORD_LENGTH) {
            validationResult.add(Error.of("password.invalid", "Password must be at least 8 characters long"));
        } else if (createUserDto.getPassword().contains(" ")) {
            validationResult.add(Error.of("password.invalid", "Password cannot contain spaces"));
        }

        if (!LocalDateFormatter.isValid(createUserDto.getBirthday())) {
            validationResult.add(Error.of("birthday.invalid","Birthday is invalid"));
        }

        if (Gender.find(createUserDto.getGender()).isEmpty()) {
            validationResult.add(Error.of("gender.invalid", "Gender is invalid"));
        }

        return validationResult;
    }

    public static CreateUserValidator getInstance() {
        return INSTANCE;
    }
}
