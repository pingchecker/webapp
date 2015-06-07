
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage title="Log in">
    <jsp:body>
        <h1>Log in</h1>

        <p>Please log in using your email and password.</p>

        <form method="post">
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>
            <label for="email">Email:</label>
            <input type="email" id="email" name="email">

            <label for="password">Password:</label>
            <input type="password" id="password" name="password">

            <input type="submit" value="Log in">
        </form>
    </jsp:body>
</t:genericpage>