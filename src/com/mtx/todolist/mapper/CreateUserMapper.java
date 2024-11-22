package com.mtx.todolist.mapper;

import com.mtx.todolist.dto.CreateUserDto;
import com.mtx.todolist.entity.Gender;
import com.mtx.todolist.entity.Role;
import com.mtx.todolist.entity.User;
import com.mtx.todolist.util.LocalDateFormatter;
import com.mtx.todolist.util.PasswordUtil;
import com.mtx.todolist.util.PropertiesUtil;
import lombok.NoArgsConstructor;

import java.io.File;
import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateUserMapper implements Mapper<CreateUserDto, User> {
    private static final CreateUserMapper INSTANCE = new CreateUserMapper();
    private static final String IMAGE_FOLDER = "users";
    private static final String DEFAULT_MALE_IMAGE = PropertiesUtil.get("image.male.default.url");
    private static final String DEFAULT_FEMALE_IMAGE = PropertiesUtil.get("image.female.default.url");

    @Override
    public User mapFrom(CreateUserDto createUserDto) {
        try {
            return User.builder()
                    .name(createUserDto.getName())
                    .email(createUserDto.getEmail())
                    .birthday(LocalDateFormatter.format(createUserDto.getBirthday()))
                    .registeredDate(LocalDate.now())
                    .password(PasswordUtil.getSaltedHash(createUserDto.getPassword()))
                    .gender(Gender.valueOf(createUserDto.getGender()))
                    .role(Role.valueOf(createUserDto.getRole()))
                    .image(!createUserDto.getImage().getSubmittedFileName().isEmpty()
                            ? IMAGE_FOLDER + File.separator + createUserDto.getImage().getSubmittedFileName()
                            : Gender.MALE.name().equals(createUserDto.getGender())
                            ? DEFAULT_MALE_IMAGE
                            : DEFAULT_FEMALE_IMAGE)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static CreateUserMapper getInstance() {
        return INSTANCE;
    }
}
