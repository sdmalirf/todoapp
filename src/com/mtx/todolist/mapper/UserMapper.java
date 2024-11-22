package com.mtx.todolist.mapper;

import com.mtx.todolist.dto.UserDto;
import com.mtx.todolist.entity.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserMapper implements Mapper<User, UserDto> {

    private static final UserMapper INSTANCE = new UserMapper();

    @Override
    public UserDto mapFrom(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .registeredDate(user.getRegisteredDate())
                .role(user.getRole())
                .gender(user.getGender())
                .image(user.getImage())
                .build();
    }

    public static UserMapper getInstance() {
        return INSTANCE;
    }
}
