<%@tag description="Overall Page template" pageEncoding="UTF-8"
       import="eu.matejkormuth.pingchecker.webapp.AppConstants" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html class="no-js" lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Pingchecker.eu</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css">
</head>
<body>
<!--[if lt IE 8]>
<p class="browserupgrade">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade
    your browser</a> to improve your experience.</p>
<![endif]-->

<div class="main">
    <jsp:doBody/>
</div>

<!-- Standard footer. -->
<small class="footer">
    <c:choose>
        <c:when test="${sessionScope.user != null}">
            <a href="/admin/targets">Targets</a> |
            <a href="/admin/settings">Settings</a> |
            <a href="/logout">Log out</a> |
        </c:when>
        <c:otherwise>
            <a href="/login">Log in</a> |
        </c:otherwise>
    </c:choose>
    <a href="/">About us</a>
</small>
<small class="footer">&copy; Pingchecker.eu 2015 | Version: <%= AppConstants.VERSION %> <%= AppConstants.COMMIT %>
</small>

<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
<script>
    (function (b, o, i, l, e, r) {
        b.GoogleAnalyticsObject = l;
        b[l] || (b[l] =
                function () {
                    (b[l].q = b[l].q || []).push(arguments)
                });
        b[l].l = +new Date;
        e = o.createElement(i);
        r = o.getElementsByTagName(i)[0];
        e.src = 'https://www.google-analytics.com/analytics.js';
        r.parentNode.insertBefore(e, r)
    }(window, document, 'script', 'ga'));
    ga('create', 'UA-XXXXX-X', 'auto');
    ga('send', 'pageview');
</script>
</body>
</html>