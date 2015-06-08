<%--

    ====
        pingchecker - Tool to periodically check services availability
        Copyright (c) 2015, Matej Kormuth <http://www.github.com/dobrakmato>
        All rights reserved.

        Redistribution and use in source and binary forms, with or without modification,
        are permitted provided that the following conditions are met:

        1. Redistributions of source code must retain the above copyright notice, this
        list of conditions and the following disclaimer.

        2. Redistributions in binary form must reproduce the above copyright notice,
        this list of conditions and the following disclaimer in the documentation and/or
        other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
        ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
        WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
        DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
        ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
        (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
        LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
        ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
        (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
        SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
    ====

    Pingchecker.eu webapp - Tool to periodically check services availability
    Copyright (c) 2015, Matej Kormuth <http://www.github.com/dobrakmato>
    All rights reserved.

    Redistribution and use in source and binary forms, with or without modification,
    are permitted provided that the following conditions are met:

    1. Redistributions of source code must retain the above copyright notice, this
    list of conditions and the following disclaimer.

    2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation and/or
    other materials provided with the distribution.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
    ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
    WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
    DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
    ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
    (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
    LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
    ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
    SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--%>
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