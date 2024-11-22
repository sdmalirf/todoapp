package com.mtx.todolist.util;

import lombok.NoArgsConstructor;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ConnectionPool {
    
    private static BlockingQueue<Connection> pool;
    private static List<Connection> connections;
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final String DRIVER_KEY = "db.driver";
    private static final int DEFAULT_POOL_SIZE = 5;

    static {
        loadDriver();
        initPool();
    }

    public static Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closePool() {
        for (Connection connection : connections) {
            try {
                connection.close();
                System.out.println("Connection was closed");
            } catch (SQLException throwables) {
                throw new RuntimeException(throwables);
            }
        }
    }

    private static void initPool() {
        var poolSize = PropertiesUtil.get(POOL_SIZE_KEY);
        int size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        pool = new ArrayBlockingQueue<>(size);
        connections = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            var connection = open();
            var proxyConnection = (Connection)Proxy.newProxyInstance(ConnectionPool.class.getClassLoader(), new Class[]{Connection.class},
                    (proxy, method, args) -> method.getName().equals("close")
                            ? pool.add((Connection) proxy)
                            : method.invoke(connection, args));
            pool.add(proxyConnection);
            connections.add(connection);
        }
    }

    private static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY)
            );
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName(PropertiesUtil.get(DRIVER_KEY));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
