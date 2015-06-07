<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage title="Targets">
    <jsp:attribute name="bodyEnd">
        <script>
            $(document).ready(function () {
                $(".category-name").click(function () {
                    $(this).parent().children(".category-items").slideToggle(300);
                });
                $(".service").each(function (service) {
                    var id = $(this).attr("data-target-id");
                    var serviceElement = $(this);
                    $.getJSON("/api/v1/target/" + id + "/pings?limit=1", function (data) {
                        if (data.length > 0) {
                            if (data[0].latency >= 0) {
                                serviceElement.children(".service-state")
                                        .addClass("online")
                                        .attr("title", "Last checked: " + data[0].timestamp + "\nLatency: "
                                        + data[0].latency);

                            } else {
                                serviceElement.children(".service-state")
                                        .addClass("offline")
                                        .attr("title", "Last checked: " + data[0].timestamp + "\nError: "
                                        + data[0].latency);
                                ;
                            }
                        }
                    });
                });
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <c:forEach var="category" items="${categories}">
            <t:category category="${category}"/>
        </c:forEach>
        <div style="text-align: center;">
            <a href="/admin/targets/add" style="color: dodgerblue;">Add target</a> |
            <a href="/admin/categories/add" style="color: dodgerblue;">Add category</a>
        </div>
    </jsp:body>
</t:genericpage>