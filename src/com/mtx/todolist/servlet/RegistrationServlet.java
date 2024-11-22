package com.mtx.todolist.servlet;

import com.mtx.todolist.dto.CreateUserDto;
import com.mtx.todolist.entity.Gender;
import com.mtx.todolist.entity.Role;
import com.mtx.todolist.exception.ValidationException;
import com.mtx.todolist.service.UserService;
import com.mtx.todolist.util.JspHelper;
import com.mtx.todolist.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@MultipartConfig(fileSizeThreshold = 1024 * 1024)
@WebServlet(UrlPath.REGISTRATION)
public class RegistrationServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("genders", List.of(Gender.MALE.name(), Gender.FEMALE.name()));
        req.getRequestDispatcher(JspHelper.getPath("registration"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var createUserDto = CreateUserDto.builder()
                .name(req.getParameter("name"))
                .email(req.getParameter("email"))
                .password(req.getParameter("password"))
                .birthday(req.getParameter("birthday"))
                .gender(req.getParameter("gender"))
                .role(Role.USER.name())
                .image(req.getPart("image"))
                .build();

        try {
            userService.create(createUserDto);
            resp.sendRedirect(UrlPath.LOGIN);
        } catch (ValidationException e) {
            req.setAttribute("errors", e.getErrors());
            doGet(req, resp);
        }
    }
}
