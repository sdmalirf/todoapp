<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Login</title>
    <style>
        <%@include file="/WEB-INF/resources/css/login.css"%>
    </style>
</head>
<body>
<div>
    <div class="h1">
        <h1>Login</h1>
    </div>
    <div>
        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="input">
                <label for="email">Email:
                    <input type="email" name="email" id="email" required>
                </label>
            </div>
            <div class="input">
                <label for="password">Password:
                    <input type="password" name="password" id="password" required>
                </label>
            </div>
            <button type="submit">Login</button>
            <a href="${pageContext.request.contextPath}/registration">
                <button type="button">Register</button>
            </a>
        </form>
    </div>
    <div class="error">
        <c:if test="${param.error != null}">
            <span>Email or password is not correct</span>
        </c:if>
    </div>
</div>
</body>
</html>
