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

<t:genericpage title="${service.name}">
    <jsp:attribute name="bodyEnd">
        <!-- We need Chart.js on this page. -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/1.0.2/Chart.min.js"></script>
        <script>
            $(".serverStatus").hover(function () {
                $(".lastChecked").css("left", "0px");
            });

            var data = {
                labels: [${graphLabels}],
                datasets: [
                    {
                        label: "Latency",
                        fillColor: "rgba(220,220,220,0.2)",
                        strokeColor: "rgba(220,220,220,1)",
                        pointColor: "rgba(220,220,220,1)",
                        pointStrokeColor: "#fff",
                        pointHighlightFill: "#fff",
                        pointHighlightStroke: "rgba(220,220,220,1)",
                        data: [${graphData}]
                    }
                ]
            };

            // Make graph of device width.
            $("#graph").css("width", $("#graphHolder").width());

            var ctx = document.getElementById("graph").getContext("2d");
            var myLineChart = new Chart(ctx).Line(data);
        </script>
    </jsp:attribute>
    <jsp:body>
        <h1>${service.name} /
            <small style="color: #888">${service.category.name}</small>
        </h1>
        <c:choose>
            <c:when test="${lastPing != null}">
                <c:choose>
                    <c:when test="${lastPing.ping >= 0}">
                        <b class="online serverStatus">Online (latency ${lastPing.ping} ms)</b>
                        <small class="lastChecked" title="Last checked">${lastPing.timestamp}</small>
                        <br>
                    </c:when>
                    <c:otherwise>
                        <b class="offline serverStatus">Offline (error ${lastPing.ping})</b>
                        <small class="lastChecked" title="Last checked">${lastPing.timestamp}</small>
                        <br>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <b>Status: <i>unchecked</i></b><br>
            </c:otherwise>
        </c:choose>

        <h2>Configuration</h2>

        <p>Here, you can change configuration of this check target. You can save changes by hitting Save configuration
            button.</p>

        <form method="post">
            <c:if test="${not empty success}">
                <p class="success">${success}</p>
            </c:if>

            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>

            <label for="service_name">Name:</label>
            <input type="text" id="service_name" name="service_name" value="${service.name}" required>

            <label for="service_address">Target address:</label>
            <input type="text" id="service_address" name="service_address" value="${service.address}" required>

            <label for="check_type">Check type:</label>
            <select id="check_type" name="check_type" value="${service.type}" required>
                <c:forEach var="check_type" items="${checkTypes}">
                    <c:choose>
                        <c:when test="${check_type == service.type}">
                            <option value="${check_type}" selected="selected">${check_type}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${check_type}">${check_type}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>

            <label for="check_interval">Check interval (minutes):</label>
            <input type="text" id="check_interval" name="check_interval" value="${service.checkInterval}" required>

            <input type="submit" value="Save configuration">
        </form>

        <h2>Notifications</h2>

        <p>No notifications configured.</p>

        <h2>Latency graph</h2>

        <!-- <div class="graph_controls">
        <span>7 days</span>
        <span>30 days</span>
        <span>365 days</span>
        </div> -->
        <div id="graphHolder" class="graph">
            <canvas id="graph" height="120" style="width: 560px; height: 120px;"></canvas>
        </div>
    </jsp:body>
</t:genericpage>
