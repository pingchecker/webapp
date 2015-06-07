<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Add target">
    <jsp:body>
        <h1>Add target</h1>

        <h2>Create new target</h2>

        <p>To create new target, please fill the form and then hit Create target button. You will be taken to target's
            details page.</p>

        <form method="post">
            <c:if test="${not empty errors}">
                <p class="error">${errors}</p>
            </c:if>

            <label for="name">Name:</label>
            <input type="text" id="name" name="name" value="${name}" required>

            <label for="category">Category:</label>
            <select id="category" name="category" value="${category}" required>
                <c:forEach var="category1" items="${categories}">
                    <c:choose>
                        <c:when test="${category1 == category}">
                            <option value="${category1.id}" selected="selected">${category1.name}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${category1.id}">${category1.name}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>

            <label for="address">Target address:</label>
            <input type="text" id="address" name="address" value="${address}" required>

            <label for="check_type">Check type:</label>
            <select id="check_type" name="check_type" value="${checkType}" required>
                <c:forEach var="check_type" items="${checkTypes}">
                    <c:choose>
                        <c:when test="${check_type == checkType}">
                            <option value="${check_type}" selected="selected">${check_type}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${check_type}">${check_type}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>

            <label for="check_interval">Check interval (minutes):</label>
            <input type="text" id="check_interval" name="check_interval" value="${checkInterval}" required>

            <input type="submit" value="Create target">
        </form>


    </jsp:body>
</t:genericpage>