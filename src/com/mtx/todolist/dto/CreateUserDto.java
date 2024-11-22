package com.mtx.todolist.dto;

import jakarta.servlet.http.Part;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateUserDto {
    String name;
    String birthday;
    String email;
    String password;
    String gender;
    String role;
    Part image;
}
