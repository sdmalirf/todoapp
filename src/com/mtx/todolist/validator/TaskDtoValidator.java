package com.mtx.todolist.validator;

import com.mtx.todolist.dto.TaskDto;
import com.mtx.todolist.util.LocalDateFormatter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class TaskDtoValidator implements Validator<TaskDto>{

    private static final TaskDtoValidator INSTANCE = new TaskDtoValidator();

    @Override
    public ValidationResult validate(TaskDto taskDto) {
        ValidationResult validationResult = new ValidationResult();

        if (taskDto.getTitle().isBlank()) {
            validationResult.add(Error.of("title.invalid", "Title can not be empty"));
        }


        if (!LocalDateFormatter.isValid(taskDto.getStartDate())) {
            validationResult.add(Error.of("startDate.invalid", "Start date is invalid"));
        }

        if (!LocalDateFormatter.isValid(taskDto.getEndDate())) {
            validationResult.add(Error.of("endDate.invalid", "End date is invalid"));
        }

        if (LocalDateFormatter.isValid(taskDto.getEndDate()) && LocalDateFormatter.isValid(taskDto.getStartDate())) {
            var startDate = LocalDateFormatter.format(taskDto.getStartDate());
            var endDate = LocalDateFormatter.format(taskDto.getEndDate());
            if (endDate.isBefore(startDate)) {
                validationResult.add(Error.of("endDate.invalid", "End date cannot be before start date"));
            }

            if (endDate.isBefore(LocalDate.now())) {
                validationResult.add(Error.of("endDate.invalid", "End date cannot be before today"));
            }
        }

        return validationResult;
    }

    public static TaskDtoValidator getInstance() {
        return INSTANCE;
    }

}
