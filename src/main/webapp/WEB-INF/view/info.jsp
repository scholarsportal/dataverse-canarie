<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<link href="assets/style.css" rel="stylesheet">
<title>Info</title>
</head>
<body>

<%@include file='header.jsp'%>

<h3>Info</h3>
<table border="1" cellpadding="5">
<tbody>
    <c:forEach items="${infoMap}" var="mapElement">
        <tr>
            <td>${mapElement.key}</td>
            <td>${mapElement.value}</td>
        </tr>
    </c:forEach>
</tbody>
</table>
</body>
</html>