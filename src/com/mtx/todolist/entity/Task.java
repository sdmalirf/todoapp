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
public class Task {
    private Long id;
    private Integer userId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate completedDate;
    private Status status;
    private Priority priority;
    private String description;
}

