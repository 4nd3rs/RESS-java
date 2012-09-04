<!DOCTYPE html><html>
<%@ page import="com.scientiamobile.wurflcloud.device.AbstractDevice" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=yes">
    <%@include file="ress.jspf" %>
</head>
<body>

DeviceWidth: ${wurflCapabilities.max_image_width}<br/>
Ress width: ${ressCapabilities.width}            <br/>
Ress imagewidth: ${ressCapabilities.imageVersion}  <br/>
Feature width: ${featureCapabilities.width}      <br/>

<table>
    <tr class="green">
        <td colspan=2 class="title">wurflCapabilities:</td>
    </tr>
    <c:forEach var="capab" items="${wurflCapabilities}" varStatus="stat">
        <tr<c:if test="${stat.index %2 == 1}"> class="yellow"</c:if>>
            <td class="titleSmall">${capab.key}</td>
            <td>${capab.value}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>


