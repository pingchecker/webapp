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