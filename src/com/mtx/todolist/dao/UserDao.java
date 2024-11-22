package com.mtx.todolist.dao;

import com.mtx.todolist.entity.Gender;
import com.mtx.todolist.entity.Role;
import com.mtx.todolist.entity.User;
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
public class UserDao implements Dao<Integer, User> {

    private static final UserDao INSTANCE = new UserDao();

    private static final String SAVE_SQL = """
            INSERT  INTO users (name, birthday, registered_date, email, password, gender, role, image)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?);
            """;

    private static final String UPDATE_SQL = """
            UPDATE users
            SET name = ?,
                birthday = ?,
                email = ?,
                password = ?,
                gender = ?,
                image = ?
            WHERE id = ?;
            """;

    private static final String DELETE_SQL = """
            DELETE FROM users
            WHERE id = ?;
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, name, birthday, registered_date, email, password, gender, role, image
            FROM users
            WHERE id = ?;
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id, name, birthday, registered_date, email, password, gender, role, image
            FROM users;
            """;

    private static final String FIND_BY_EMAIL_SQL = """
            SELECT id, name, birthday, registered_date, email, password, gender, role, image
            FROM users
            WHERE email = ?;
            """;

    @SneakyThrows
    @Override
    public List<User> findAll() {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(build(resultSet));
            }

            return users;
        }
    }

    @SneakyThrows
    @Override
    public Optional<User> findById(Integer id) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setObject(1, id);

            var resultSet = preparedStatement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = build(resultSet);
            }

            return Optional.ofNullable(user);
        }
    }

    @SneakyThrows
    public Optional<User> findByEmail(String email) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_SQL)){
            preparedStatement.setObject(1, email);

            var resultSet = preparedStatement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = build(resultSet);
            }

            return Optional.ofNullable(user);
        }
    }

    @SneakyThrows
    @Override
    public boolean delete(Integer id) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setObject(1, id);

            return preparedStatement.executeUpdate() == 1;
        }
    }

    @SneakyThrows
    @Override
    public User save(User user) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, user.getName());
            preparedStatement.setObject(2, user.getBirthday());
            preparedStatement.setObject(3, user.getRegisteredDate());
            preparedStatement.setObject(4, user.getEmail());
            preparedStatement.setObject(5, user.getPassword());
            preparedStatement.setObject(6, user.getGender().name());
            preparedStatement.setObject(7, user.getRole().name());
            preparedStatement.setObject(8, user.getImage());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            user.setId(generatedKeys.getObject("id", Integer.class));

            return user;
        }
    }

    @SneakyThrows
    @Override
    public int update(User user) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setObject(1, user.getName());
            preparedStatement.setObject(2, user.getBirthday());
            preparedStatement.setObject(3, user.getEmail());
            preparedStatement.setObject(4, user.getPassword());
            preparedStatement.setObject(5, user.getGender().name());
            preparedStatement.setObject(6, user.getImage());
            preparedStatement.setObject(7, user.getId());

            return preparedStatement.executeUpdate();
        }
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }

    private User build(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getObject("id", Integer.class))
                .name(resultSet.getObject("name", String.class))
                .birthday(resultSet.getObject("birthday", Date.class).toLocalDate())
                .registeredDate(resultSet.getObject("registered_date", Date.class).toLocalDate())
                .email(resultSet.getObject("email", String.class))
                .password(resultSet.getObject("password", String.class))
                .gender(Gender.find(resultSet.getObject("gender", String.class)).orElse(null))
                .role(Role.find(resultSet.getObject("role", String.class)).orElse(null))
                .image(resultSet.getObject("image", String.class))
                .build();
    }
}
