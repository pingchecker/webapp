<%--

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

<t:genericpage>
    <jsp:body>
        <h1>${service.name} /
            <small style="color: #888">${service.category.name}</small>
        </h1>
        <c:choose>
            <c:when test="${lastPing != null}">
                <c:choose>
                    <c:when test="${lastPing.ping >= 0}">
                        <b style="color: #31be3a">Online (latency ${lastPing.ping} ms)</b>
                        <small style="color: #ccc" title="Last checked">${lastPing.timestamp}</small>
                        <br>
                    </c:when>
                    <c:otherwise>
                        <b style="color: #c90022">Offline (error ${lastPing.ping})</b>
                        <small style="color: #ccc" title="Last checked">${lastPing.timestamp}</small>
                        <br>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <b>Status: <i>unchecked</i></b><br>
            </c:otherwise>
        </c:choose>

        <h2>Configuration</h2>
        <table class="target_table">
            <tr>
                <td>Target address:</td>
                <td>${service.address}</td>
            </tr>
            <tr>
                <td>Check type:</td>
                <td>${service.type}</td>
            </tr>
            <tr>
                <td>Check interval:</td>
                <td>${service.checkInterval} minutes</td>
            </tr>
        </table>

        <h2>Notifications</h2>

        <p>No notifications configured.</p>

        <h2>Graph</h2>

        <div class="graph_controls">
            <span>7 days</span>
            <span>30 days</span>
            <span>365 days</span>
        </div>
        <div class="graph">
            <canvas id="seven_days_lat" width="100%" height="120" style="width: 560px; height: 120px;"></canvas>
        </div>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/1.0.2/Chart.min.js"></script>
    </jsp:body>
</t:genericpage>
