package com.mtx.todolist.filter;

import com.mtx.todolist.dto.UserDto;
import com.mtx.todolist.entity.Role;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

import static com.mtx.todolist.util.UrlPath.*;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {

    private static final Set<String> PUBLIC_PATH = Set.of(REGISTRATION, LOGIN, IMAGES);
    private static final Set<String> ADMIN_PATH = Set.of(USERS);
    private static final Set<String> USER_PATH = Set.of(TASKS, LOGOUT, PROFILE);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var uri = ((HttpServletRequest) servletRequest).getRequestURI();

        if (isPublicPath(uri)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }

        else if (isAdminPath(uri) && isUserLoggedInAsAdmin(servletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }

        else if (isUserPath(uri) && isUserLoggedIn(servletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }

        else {
            var prevPage = ((HttpServletRequest) servletRequest).getHeader("referer");
            ((HttpServletResponse) servletResponse)
                    .sendRedirect((prevPage != null && isUserLoggedIn(servletRequest)) ? prevPage : LOGIN);
        }
    }

    private boolean isUserLoggedIn(ServletRequest servletRequest) {
        var user = (UserDto) ((HttpServletRequest) servletRequest).getSession().getAttribute("user");
        return user != null;
    }

    private boolean isUserPath(String uri) {
        return USER_PATH.stream().anyMatch(uri::startsWith);
    }

    private boolean isAdminPath(String uri) {
        return ADMIN_PATH.stream().anyMatch(uri::startsWith);
    }

    private boolean isUserLoggedInAsAdmin(ServletRequest servletRequest) {
        var user = (UserDto) ((HttpServletRequest) servletRequest).getSession().getAttribute("user");
        return user != null && user.getRole() == Role.ADMIN;
    }

    private boolean isPublicPath(String uri) {
        return PUBLIC_PATH.stream().anyMatch(uri::startsWith);
    }
}
