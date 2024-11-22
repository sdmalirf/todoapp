package com.mtx.todolist.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TaskDto {
    String id;
    String userId;
    String title;
    String startDate;
    String endDate;
    String completedDate;
    String status;
    String priority;
    String description;
}
