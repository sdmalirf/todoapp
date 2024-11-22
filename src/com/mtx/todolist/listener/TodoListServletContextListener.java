package com.mtx.todolist.listener;

import com.mtx.todolist.util.ConnectionPool;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class TodoListServletContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPool.closePool();
        System.out.println("Connection pool was closed");
        System.out.println("ServletContextListener destroyed");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("ServletContextListener started");
    }
}
