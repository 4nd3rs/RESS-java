<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="header.jspf"%>

<div class="container">
    <div class="row">
        <div class="span8">
            <%@include file="fragments/carousel.jspf"%>
            <%@include file="fragments/content.jspf"%>
            <%@include file="fragments/detection/device.jspf"%>
            <%@include file="fragments/detection/feature.jspf"%>
            <%@include file="fragments/detection/ress.jspf"%>

        </div>

        <div class="span4">
            <%@include file="fragments/archive.jspf"%>
            <c:if test="${ressCapabilities.width >= 768 || ressCapabilities.connection == 'WiFi' || ressCapabilities.connection == 'Ethernet'}">
                <div <%--class="max-768"--%>>
                    <h2>Social</h2>
                    <%@include file="fragments/twitter-search.jspf"%>
                    <%@include file="fragments/facebook.jspf"%>
                </div>
            </c:if>
        </div>
    </div>
</div><!-- /container -->

<%@include file="footer.jspf"%>