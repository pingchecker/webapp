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

<t:genericpage title="Account settings">
    <jsp:attribute name="bodyEnd">
        <script>
            function checkPasswords() {
                if($("#newpassword").val() == $("#newpassword2").val()) {
                    $("#newpassword2").css("box-shadow", "0px 0px 4px 0px green");
                } else {
                    $("#newpassword2").css("box-shadow", "0px 0px 4px 0px red");
                }
            }

            function passwordsMatch() {
                return $("#newpassword").val() == $("#newpassword2").val();
            }
        </script>
    </jsp:attribute>
    <jsp:body>
        <h1>Settings</h1>

        <h2>Change password</h2>

        <p>Here, you can change your old password to new password. Fill the form and hit change password button.</p>

        <form method="post">
            <c:if test="${not empty passwordError}">
                <p class="error">${passwordError}</p>
            </c:if>
            <c:if test="${not empty passwordChanged}">
                <p class="success">${passwordChanged}</p>
            </c:if>
            <label for="oldpassword">Old password:</label>
            <input type="password" id="oldpassword" name="oldpassword">

            <label for="newpassword">New password:</label>
            <input type="password" id="newpassword" name="newpassword" onkeyup="checkPasswords()">

            <label for="newpassword2">New password:</label>
            <input type="password" id="newpassword2" name="newpassword2" onkeyup="checkPasswords()">

            <input type="submit" value="Change password" onclick="passwordsMatch()">
        </form>

        <h2>Change email address</h2>

        <p>You currently cannot change email address.</p>

        <form method="post">
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>
            <label for="oldemail">Old email address:</label>
            <input disabled="disabled" type="email" id="oldemail" name="oldemail">

            <label for="newemail">New email address:</label>
            <input disabled="disabled" type="email" id="newemail" name="newemail">

            <input disabled="disabled" type="submit" value="Change email address">
        </form>

        <h2>Access tokens</h2>

        <p>Here you can find and manage your access tokens used for accessing our API.</p>

        <p><i style="color: #686868">No access tokens.</i></p>
    </jsp:body>
</t:genericpage>