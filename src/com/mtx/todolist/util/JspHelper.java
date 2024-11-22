package com.mtx.todolist.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JspHelper {
    private static final String JSP_PATH = "WEB-INF/jsp/%s.jsp";

    public String getPath(String jspName) {
        return String.format(JSP_PATH, jspName);
    }
}
