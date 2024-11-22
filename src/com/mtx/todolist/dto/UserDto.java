package com.mtx.todolist.dto;

import com.mtx.todolist.entity.Gender;
import com.mtx.todolist.entity.Role;
import jakarta.servlet.http.Part;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserDto {
    Integer id;
    String name;
    LocalDate birthday;
    LocalDate registeredDate;
    String email;
    Gender gender;
    Role role;
    String image;
}
