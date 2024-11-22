package com.mtx.todolist.dto;

import jakarta.servlet.http.Part;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Profile {
    String id;
    String name;
    String password;
    String birthday;
    String email;
    String gender;
    String imagePath;
    Part image;
}
