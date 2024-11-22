<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>My tasks</title>
    <style>
        <%@include file="/WEB-INF/resources/css/tasks.css"%>
    </style>
</head>
<body>
<%@include file="header.jsp" %>
<div>
    <h1>Your tasks</h1>
    <table title="Tasks">
        <tr>
            <th>№</th>
            <th>Title</th>
            <th>Priority</th>
            <th>Status</th>
            <th>Start date</th>
            <th>End date</th>
            <th>Completed date</th>
            <th>Description</th>
            <th colspan="2"></th>
        </tr>
        <c:forEach var="task" items="${requestScope.tasks}" varStatus="loop">
            <form class="delete" action="${pageContext.request.contextPath}/tasks" method="post"
                  id="delete + ${task.id}">
                <input type="hidden" name="action" value="delete">
            </form>
            <form class="edit" action="${pageContext.request.contextPath}/tasks" method="post" id="edit + ${task.id}">
                <input type="hidden" name="action" value="edit">
            </form>
            <input name="startDate" value="${task.startDate}" type="hidden" form="edit + ${task.id}"/>
            <tr style="background-color:${task.status == 'RUNNING' ? '#D8E6F3' : task.status == 'COMPLETED' ? 'lightgreen' : 'pink'}">
                <td>${loop.index + 1}</td>
                <td>
                    <c:if test="${task.status == 'RUNNING'}">
                        <input type="text" name="title" value="${task.title}" form="edit + ${task.id}" required/>
                    </c:if>
                    <c:if test="${task.status != 'RUNNING'}">
                        ${task.title}
                    </c:if>
                </td>
                <td>
                    <c:if test="${task.status == 'RUNNING'}">
                        <select size="1" name="priority" form="edit + ${task.id}">
                            <c:forEach var="priority" items="${requestScope.priority}">
                                <c:if test="${task.priority != priority}">
                                    <option value="${priority}">${priority}</option>
                                </c:if>
                            </c:forEach>
                            <option value="${task.priority}" selected>${task.priority}</option>
                        </select>
                    </c:if>
                    <c:if test="${task.status != 'RUNNING'}">
                        ${task.priority}
                    </c:if>
                </td>
                <td>
                    <c:if test="${task.status == 'RUNNING'}">
                        <select size="1" name="status" form="edit + ${task.id}">
                            <c:forEach var="status" items="${requestScope.status}">
                                <c:if test="${task.status != status}">
                                    <option value="${status}">${status}</option>
                                </c:if>
                            </c:forEach>
                            <option value="${task.status}" selected>${task.status}</option>
                        </select>
                    </c:if>
                    <c:if test="${task.status != 'RUNNING'}">
                        ${task.status}
                    </c:if>
                </td>
                <td>${task.startDate}</td>
                <td>
                    <c:if test="${task.status == 'RUNNING'}">
                        <input type="date" name="endDate" value="${task.endDate}" form="edit + ${task.id}" required/>
                    </c:if>
                    <c:if test="${task.status != 'RUNNING'}">
                        ${task.endDate}
                    </c:if>
                </td>
                <td>${task.completedDate}</td>
                <td>
                    <c:if test="${task.status == 'RUNNING'}">
                        <textarea type="text" name="description" form="edit + ${task.id}">${task.description}</textarea>
                    </c:if>
                    <c:if test="${task.status != 'RUNNING'}">
                        ${task.description}
                    </c:if>
                </td>
                <td>
                    <input type="hidden" name="id" value="${task.id}" form="delete + ${task.id}">
                    <button type="submit" form="delete + ${task.id}">delete</button>
                </td>
                <td>
                    <c:if test="${task.status == 'RUNNING'}">
                        <input type="hidden" name="id" value="${task.id}" form="edit + ${task.id}"/>
                        <button type="submit" form="edit + ${task.id}">save changes</button>
                    </c:if>
                    <c:if test="${task.status != 'RUNNING'}">
                        No edit
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="10"></td>
        </tr>
    </table>
</div>
<div>
    <form action="${pageContext.request.contextPath}/tasks" method="post" id="add">
        <input type="hidden" name="action" value="create">
        <div class="addform">
            <div class="add">
                <label for="title">Title:
                    <input type="text" name="title" placeholder="enter task name" form="add" id="title" required/>
                </label>
            </div>
            <div class="add">
                <label for="priority">Priority:
                    <select size="1" name="priority" form="add" id="priority">
                        <c:forEach var="priority" items="${requestScope.priority}">
                            <option value="${priority}">${priority}</option>
                        </c:forEach>
                    </select>
                </label>
            </div>
            <div class="add">
                <label for="start">Start date:
                    <input type="date" name="startDate" form="add" id="start" required/>
                </label>
            </div>
            <div class="add">
                <label for="end">End date:
                    <input type="date" name="endDate" form="add" id="end" required/>
                </label>
            </div>
            <div class="add">
                <label for="description">Description:
                    <input type="text" name="description" placeholder="enter task description" form="add" id="description"/>
                </label>
            </div>
            <div>
                <button class="addtask" type="submit" form="add">Add new task</button>
            </div>
        </div>
    </form>
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
