<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    <%@include file="/WEB-INF/resources/css/header.css"%>
</style>
<div class="header">
    <c:if test="${not empty sessionScope.user}">
        <span class="user">Hello, ${sessionScope.user.name}</span>
        <img class="img" src="${pageContext.request.contextPath}/images/${sessionScope.user.image}"  alt="User image">
        <a class="profile" href="${pageContext.request.contextPath}/profile">
            <button type="button">Profile</button>
        </a>
        <a class="tasks" href="${pageContext.request.contextPath}/tasks">
            <button type="button">Tasks</button>
        </a>
        <c:if test="${sessionScope.user.role == 'ADMIN'}">
            <a class="users" href="${pageContext.request.contextPath}/users">
                <button type="button">Users</button>
            </a>
        </c:if>
        <a class="logout" href="${pageContext.request.contextPath}/logout">
            <button type="submit">Logout</button>
        </a>
    </c:if>
</div>
