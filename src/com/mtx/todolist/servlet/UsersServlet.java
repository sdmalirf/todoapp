package com.mtx.todolist.servlet;

import com.mtx.todolist.service.UserService;
import com.mtx.todolist.util.JspHelper;
import com.mtx.todolist.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(UrlPath.USERS)
public class UsersServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var action = req.getParameter("action");
        if (action != null && action.equals("delete")) {
            var delete = userService.delete(Integer.valueOf(req.getParameter("id")));
            if (delete) {
                req.setAttribute("delete", "User " + req.getParameter("email") + " was deleted success");
            }
        }
        var users = userService.getAll();
        req.setAttribute("users", users);
        req.getRequestDispatcher(JspHelper.getPath(UrlPath.USERS))
                .forward(req, resp);
    }
}
