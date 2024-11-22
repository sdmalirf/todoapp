<%@ page contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>EditUser</title>
    <style>
        <%@include file="/WEB-INF/resources/css/profile.css" %>
    </style>
</head>
<body>
<%@include file="header.jsp" %>
<div class="profile">
    <div>
        <h1>Edit profile</h1>
    </div>
    <div>
        <form action="${pageContext.request.contextPath}/profile" method="post" enctype="multipart/form-data">
            <input type="hidden" name="id" value="${requestScope.user.id}">
            <div class="input">
                <label for="name">Name:
                    <input type="text" name="name" id="name" value="${requestScope.user.name}" required>
                </label>
            </div>
            <div class="input">
                <label for="email">Email:
                    <input type="email" name="email" id="email" value="${requestScope.user.email}" required>
                </label>
            </div>
            <div class="input">
                <label for="password">Password:
                    <input type="password" name="password" id="password" placeholder="enter new password or old password" required>
                </label>
            </div>
            <div class="input">
                <label for="birthday">Birthday:
                    <input type="date" name="birthday" id="birthday" value="${requestScope.user.birthday}" required>
                </label>
            </div>
            <div class="input">
                <label for="gender">Gender:
                    <c:forEach var="gender" items="${requestScope.genders}">
                        <input class="gender" type="radio" name="gender" value="${gender}" id="gender"
                            <c:if test="${requestScope.user.gender == gender}">checked</c:if>>${gender}
                    </c:forEach>
                </label>
            </div>
            <input type="hidden" name="imagepath" value="${requestScope.user.image}">
            <div class="input">
                <label for="image">New image(The file name must be without spaces and in Latin):
                    <input class="image" type="file" name="image" id="image">
                </label>
            </div>
            <button class="save" type="submit">Save</button>
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
