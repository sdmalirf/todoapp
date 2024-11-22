package com.mtx.todolist.validator;

import com.mtx.todolist.dto.Profile;
import com.mtx.todolist.util.LocalDateFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileValidator implements Validator<Profile> {
    private static final ProfileValidator INSTANCE = new ProfileValidator();
    private static final int MIN_PASSWORD_LENGTH = 8;

    @Override
    public ValidationResult validate(Profile profile) {
        ValidationResult validationResult = new ValidationResult();

        if (profile.getName().isBlank()) {
            validationResult.add(Error.of("name.invalid", "Name cannot be empty"));
        }

        if (profile.getPassword().isBlank()) {
            validationResult.add(Error.of("password.invalid", "Password cannot be empty"));
        } else if (profile.getPassword().length() < MIN_PASSWORD_LENGTH) {
            validationResult.add(Error.of("password.invalid", "Password must be at least 8 characters long"));
        } else if (profile.getPassword().contains(" ")) {
            validationResult.add(Error.of("password.invalid", "Password cannot contain spaces"));
        }

        if (!LocalDateFormatter.isValid(profile.getBirthday())) {
            validationResult.add(Error.of("birthday.invalid","Birthday is invalid"));
        }

        return validationResult;
    }

    public static ProfileValidator getInstance() {
        return INSTANCE;
    }
}
