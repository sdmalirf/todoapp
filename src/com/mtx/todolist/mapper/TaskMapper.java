package com.mtx.todolist.mapper;

import com.mtx.todolist.dto.TaskDto;
import com.mtx.todolist.entity.Priority;
import com.mtx.todolist.entity.Status;
import com.mtx.todolist.entity.Task;
import com.mtx.todolist.util.LocalDateFormatter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class TaskMapper implements Mapper<TaskDto, Task>{

    private static final TaskMapper INSTANCE = new TaskMapper();

    @Override
    public Task mapFrom(TaskDto taskDto) {
        return Task.builder()
                .id(taskDto.getId() == null ? null : Long.valueOf(taskDto.getId()))
                .userId(Integer.valueOf(taskDto.getUserId()))
                .title(taskDto.getTitle())
                .startDate(LocalDateFormatter.format(taskDto.getStartDate()))
                .endDate(LocalDateFormatter.format(taskDto.getEndDate()))
                .status(Status.valueOf(taskDto.getStatus()))
                .priority(Priority.valueOf(taskDto.getPriority()))
                .description(taskDto.getDescription())
                .build();
    }

    public static TaskMapper getInstance() {
        return INSTANCE;
    }
}
