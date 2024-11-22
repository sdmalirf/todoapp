package com.mtx.todolist.servlet;

import com.mtx.todolist.dto.TaskDto;
import com.mtx.todolist.dto.UserDto;
import com.mtx.todolist.entity.Priority;
import com.mtx.todolist.entity.Status;
import com.mtx.todolist.exception.ValidationException;
import com.mtx.todolist.service.TaskService;
import com.mtx.todolist.util.JspHelper;
import com.mtx.todolist.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@WebServlet(UrlPath.TASKS)
public class TasksServlet extends HttpServlet {

    private final TaskService taskService = TaskService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var user = (UserDto) req.getSession().getAttribute("user");
        var tasks = taskService.getAllByUserId(user.getId());
        req.setAttribute("tasks", tasks);
        req.setAttribute("priority", Arrays.stream(Priority.values())
                .map(Enum::name)
                .collect(Collectors.toSet()));
        req.setAttribute("status", Arrays.stream(Status.values())
                .map(Enum::name)
                .collect(Collectors.toSet()));
        req.getRequestDispatcher(JspHelper.getPath("tasks"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var action = req.getParameter("action");
        if (action != null && action.equals("create")) {
            var taskDto = TaskDto.builder()
                    .userId(String.valueOf(((UserDto)req.getSession().getAttribute("user")).getId()))
                    .title(req.getParameter("title"))
                    .priority(req.getParameter("priority"))
                    .status(Status.RUNNING.name())
                    .startDate(req.getParameter("startDate"))
                    .endDate(req.getParameter("endDate"))
                    .description(req.getParameter("description"))
                    .build();
            try {
                taskService.create(taskDto);
                resp.sendRedirect("/tasks");
                return;
            } catch (ValidationException e) {
                req.setAttribute("errors", e.getErrors());
                doGet(req, resp);
            }
        }

        if (action != null && action.equals("delete")) {
            taskService.delete(Long.valueOf(req.getParameter("id")));
            resp.sendRedirect("/tasks");
            return;
        }

        if (action != null && action.equals("edit")) {
            var taskDto = TaskDto.builder()
                    .id(req.getParameter("id"))
                    .userId(String.valueOf(((UserDto)req.getSession().getAttribute("user")).getId()))
                    .title(req.getParameter("title"))
                    .priority(req.getParameter("priority"))
                    .status(req.getParameter("status"))
                    .startDate(req.getParameter("startDate"))
                    .endDate(req.getParameter("endDate"))
                    .completedDate(null)
                    .description(req.getParameter("description"))
                    .build();
            try {
                taskService.update(taskDto);
                resp.sendRedirect("/tasks");
            } catch (ValidationException e) {
                req.setAttribute("errors", e.getErrors());
                doGet(req, resp);
            }
        }
    }
}
