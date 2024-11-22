package com.mtx.todolist.servlet;

import com.mtx.todolist.dto.Profile;
import com.mtx.todolist.dto.UserDto;
import com.mtx.todolist.entity.Gender;
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
@WebServlet(UrlPath.PROFILE)
public class ProfileServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var user = (UserDto) req.getSession().getAttribute("user");
        req.setAttribute("genders", List.of(Gender.MALE.name(), Gender.FEMALE.name()));
        var optionalUserDto = userService.getById(user.getId());
        optionalUserDto.ifPresentOrElse(userDto -> req.setAttribute("user", userDto),
                () -> req.setAttribute("errors", "No found such user"));
        req.getRequestDispatcher(JspHelper.getPath(UrlPath.PROFILE)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var profile = Profile.builder()
                .id(req.getParameter("id"))
                .name(req.getParameter("name"))
                .email(req.getParameter("email"))
                .password(req.getParameter("password"))
                .birthday(req.getParameter("birthday"))
                .gender(req.getParameter("gender"))
                .imagePath(req.getParameter("imagepath"))
                .image(req.getPart("image"))
                .build();
        try {
            userService.update(profile);
            resp.sendRedirect(UrlPath.LOGOUT);
        } catch (ValidationException e) {
            req.setAttribute("errors", e.getErrors());
            doGet(req, resp);
        }
    }
}
