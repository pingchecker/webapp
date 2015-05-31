<%@tag description="Category template" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ attribute name="category" type="eu.matejkormuth.pingchecker.beans.Category" %>

<div id="category-${category.id}" class="category">
    <div class="category-name">${category.name} <span class="expander">&#x25BC;</span></div>
    <div class="category-items">
        <c:forEach var="service" items="${category.targets}">
            <t:service service="${service}"/>
        </c:forEach>
    </div>
</div>