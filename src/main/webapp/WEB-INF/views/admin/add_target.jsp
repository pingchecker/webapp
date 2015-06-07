<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Add target">
    <jsp:attribute name="bodyEnd">
        <script>

        </script>
    </jsp:attribute>
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
            <input type="text" id="category" name="category" value="${category}" required>

            <label for="address">Target address:</label>
            <input type="text" id="address" name="address" value="${address}" required>

            <label for="check_type">Check type:</label>
            <input type="text" id="check_type" name="check_type" value="${checkType}" required>

            <label for="check_interval">Check interval (minutes):</label>
            <input type="text" id="check_interval" name="check_interval" value="${checkInterval}" required>

            <input type="submit" value="Create target">
        </form>


    </jsp:body>
</t:genericpage>