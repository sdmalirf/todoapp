<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Registration</title>
    <style>
        <%@include file="/WEB-INF/resources/css/registrations.css" %>
    </style>
</head>
<body>
<div class="registration">
    <div>
        <h1>Registration</h1>
    </div>
    <div>
        <form action="${pageContext.request.contextPath}/registration" method="post" enctype="multipart/form-data">
            <div class="input">
                <label for="name">Name:
                    <input type="text" name="name" id="name" required>
                </label>
            </div>
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
            <div class="input">
                <label for="birthday">Birthday:
                    <input type="date" name="birthday" id="birthday" required>
                </label>
            </div>
            <div class="input">
                <label for="gender">Gender:
                    <c:forEach var="gender" items="${requestScope.genders}">
                        <input class="gender" type="radio" name="gender" value="${gender}" id="gender">${gender}
                    </c:forEach>
                </label>
            </div>
            <div class="input">
                <label for="image">Image:
                    <input class="image" type="file" name="image" id="image">
                </label>
            </div>
            <button type="submit">Send</button>
        </form>
    </div>
</div>

<c:if test="${not empty requestScope.errors}">
    <div class="errors" style="color: red">
        <c:forEach var="error" items="${requestScope.errors}">
            <span>${error.message}</span><br>
        </c:forEach>
    </div>
</c:if>
</body>
</html>
