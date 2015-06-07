<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage title="Error">
    <jsp:attribute name="bodyEnd">
        <c:if test="${not empty exception}">
            <script>
                $(document).ready(function() {
                    $(".main").css("transition", "300ms ease all");
                    $(".main").css("max-width", "100%");
                });
            </script>
        </c:if>
    </jsp:attribute>
    <jsp:body>
        <h1>${error}</h1>

        <p>We are sorry for this situation.</p>

        <div class="infoBox" style="max-width:100%;">
            <pre>${message}</pre>
            <c:if test="${not empty exception}">
                <pre>${exception}</pre>
            </c:if>
        </div>
    </jsp:body>
</t:genericpage>