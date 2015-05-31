<%@tag description="Service template" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="service" type="eu.matejkormuth.pingchecker.beans.Target" %>

<div id="service-${service.id}" class="service" onclick="window.location = '/admin/target/${service.id}'">
    <span class="service-state"></span>
    <span class="service-name">${service.name}</span>
    <span class="service-availability">100%</span>
</div>