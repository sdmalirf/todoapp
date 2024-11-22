package com.mtx.todolist.service;

import com.mtx.todolist.dao.TaskDao;
import com.mtx.todolist.dto.TaskDto;
import com.mtx.todolist.entity.Status;
import com.mtx.todolist.entity.Task;
import com.mtx.todolist.exception.ValidationException;
import com.mtx.todolist.mapper.TaskMapper;
import com.mtx.todolist.validator.TaskDtoValidator;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class TaskService {

    private static final TaskService INSTANCE = new TaskService();

    private final TaskDao taskDao = TaskDao.getInstance();
    private final TaskDtoValidator taskDtoValidator = TaskDtoValidator.getInstance();
    private final TaskMapper taskMapper = TaskMapper.getInstance();

    public Long create(TaskDto taskDto) {
        var validationResult = taskDtoValidator.validate(taskDto);

        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }

        var task = taskMapper.mapFrom(taskDto);
        var saveTask = taskDao.save(task);
        return saveTask.getId();
    }

    public List<Task> getAllByUserId(Integer userId) {
        List<Task> tasks = taskDao.findAllByUserId(userId);
        var tasksForUpdate = tasks.stream()
                .filter(task -> LocalDate.now().isAfter(task.getEndDate()) && task.getStatus() != Status.COMPLETED)
                .collect(Collectors.toList());
        if (!tasksForUpdate.isEmpty()) {
            tasksForUpdate
            .forEach(task -> {
                task.setStatus(Status.FAILED);
                taskDao.update(task);
            });
            return taskDao.findAllByUserId(userId);
        }

        return tasks;
    }

    public boolean delete(Long id) {
        return taskDao.delete(id);
    }

    public Optional<Task> get(Long id) {
        return taskDao.findById(id);
    }

    public void update(TaskDto taskDto) {
        var validationResult = taskDtoValidator.validate(taskDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }

        var task = taskMapper.mapFrom(taskDto);
        if (task.getStatus() == Status.COMPLETED) {
            task.setCompletedDate(LocalDate.now());
        }
        taskDao.update(task);
    }

    public static TaskService getInstance() {
        return INSTANCE;
    }
}
