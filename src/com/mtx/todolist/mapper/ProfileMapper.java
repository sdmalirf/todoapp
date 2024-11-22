package com.mtx.todolist.mapper;

import com.mtx.todolist.dto.Profile;
import com.mtx.todolist.entity.Gender;
import com.mtx.todolist.entity.User;
import com.mtx.todolist.util.LocalDateFormatter;
import com.mtx.todolist.util.PasswordUtil;
import com.mtx.todolist.util.PropertiesUtil;

import java.io.File;

public class ProfileMapper implements Mapper<Profile, User> {
    private static final ProfileMapper INSTANCE = new ProfileMapper();
    private static final String IMAGE_FOLDER = "users";
    private static final String DEFAULT_MALE_IMAGE = PropertiesUtil.get("image.male.default.url");
    private static final String DEFAULT_FEMALE_IMAGE = PropertiesUtil.get("image.female.default.url");

    @Override
    public User mapFrom(Profile profile) {
        try {
            return User.builder()
                    .id(Integer.valueOf(profile.getId()))
                    .name(profile.getName())
                    .email(profile.getEmail())
                    .password(PasswordUtil.getSaltedHash(profile.getPassword()))
                    .birthday(LocalDateFormatter.format(profile.getBirthday()))
                    .gender(Gender.valueOf(profile.getGender()))
                    .image(!profile.getImage().getSubmittedFileName().isEmpty()
                            ? IMAGE_FOLDER + File.separator + profile.getEmail() + profile.getImage().getSubmittedFileName()
                            : (!profile.getImagePath().equals(DEFAULT_MALE_IMAGE) && !profile.getImagePath().equals(DEFAULT_FEMALE_IMAGE) && !profile.getImagePath().isEmpty())
                            ? profile.getImagePath()
                            : Gender.MALE.name().equals(profile.getGender())
                            ? DEFAULT_MALE_IMAGE
                            : DEFAULT_FEMALE_IMAGE)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ProfileMapper getInstance() {
        return INSTANCE;
    }
}
