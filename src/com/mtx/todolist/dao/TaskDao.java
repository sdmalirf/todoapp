package com.mtx.todolist.dao;

import com.mtx.todolist.entity.Priority;
import com.mtx.todolist.entity.Status;
import com.mtx.todolist.entity.Task;
import com.mtx.todolist.util.ConnectionPool;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class TaskDao implements Dao<Long, Task> {

    private final static TaskDao INSTANCE = new TaskDao();

    private static final String FIND_ALL_BY_USER_ID_SQL = """
            SELECT id, user_id, title, start_date, end_date, completed_date, status, priority, description
            FROM task
            WHERE user_id = ?
            ORDER BY start_date
            """;
    private static final String SAVE_SQL = """
            INSERT INTO task (user_id, title, start_date, end_date, completed_date, status, priority, description)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT id, user_id, title, start_date, end_date, completed_date, status, priority, description
            FROM task
            WHERE id = ?
            """;
    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM task
            WHERE id = ?
            """;
    private static final String UPDATE_SQL = """
            UPDATE task
            SET title = ?,
                end_date = ?,
                completed_date = ?,
                status = ?,
                priority = ?,
                description = ?
            WHERE id = ?
            """;

    @SneakyThrows
    public List<Task> findAllByUserId(Integer userId) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_BY_USER_ID_SQL)) {
            preparedStatement.setObject(1, userId);
            var resultSet = preparedStatement.executeQuery();

            List<Task> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(build(resultSet));
            }
            return result;
        }
    }

    @Override
    public List<Task> findAll() {
        return null;
    }

    @SneakyThrows
    @Override
    public Optional<Task> findById(Long id) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setObject(1, id);
            var resultSet = preparedStatement.executeQuery();
            Task task = null;
            if (resultSet.next()) {
                task = build(resultSet);
            }

            return Optional.ofNullable(task);
        }
    }

    @SneakyThrows
    @Override
    public boolean delete(Long id) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            preparedStatement.setObject(1, id);

            return preparedStatement.executeUpdate() == 1;
        }
    }

    @SneakyThrows
    @Override
    public Task save(Task task) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, task.getUserId());
            preparedStatement.setObject(2, task.getTitle());
            preparedStatement.setObject(3, task.getStartDate());
            preparedStatement.setObject(4, task.getEndDate());
            preparedStatement.setObject(5, task.getCompletedDate());
            preparedStatement.setObject(6, task.getStatus().name());
            preparedStatement.setObject(7, task.getPriority().name());
            preparedStatement.setObject(8, task.getDescription());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            task.setId(generatedKeys.getObject("id", Long.class));

            return task;
        }
    }

    @SneakyThrows
    @Override
    public int update(Task task) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setObject(1, task.getTitle());
            preparedStatement.setObject(2, task.getEndDate());
            preparedStatement.setObject(3, task.getCompletedDate());
            preparedStatement.setObject(4, task.getStatus().name());
            preparedStatement.setObject(5, task.getPriority().name());
            preparedStatement.setObject(6, task.getDescription());
            preparedStatement.setObject(7, task.getId());

            preparedStatement.executeUpdate();
        }
        return 0;
    }

    public static TaskDao getInstance() {
        return INSTANCE;
    }

    private Task build(ResultSet resultSet) throws SQLException {
        return Task.builder()
                .id(resultSet.getObject("id", Long.class))
                .userId(resultSet.getObject("user_id", Integer.class))
                .title(resultSet.getObject("title", String.class))
                .startDate(resultSet.getObject("start_date", Date.class).toLocalDate())
                .endDate(resultSet.getObject("end_date", Date.class).toLocalDate())
                .completedDate(resultSet.getObject("completed_date") != null
                        ? resultSet.getObject("completed_date", Date.class).toLocalDate()
                        : null)
                .status(Status.find(resultSet.getObject("status", String.class)).orElse(null))
                .priority(Priority.find(resultSet.getObject("priority", String.class)).orElse(null))
                .description(resultSet.getObject("description", String.class))
                .build();
    }
}
