package com.mtx.todolist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Integer id;
    private String name;
    private LocalDate birthday;
    private LocalDate registeredDate;
    private String email;
    private String password;
    private Gender gender;
    private Role role;
    private String image;
}
