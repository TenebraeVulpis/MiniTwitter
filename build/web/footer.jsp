<%-- 
    Document   : footer.jsp
    Created on : Sep 24, 2015, 6:47:16 PM
    Author     : xl
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div id="footer_bar" >
    <c:set var="date" scope="session" value="<%= new java.util.Date() %>"/>
    <div class="info">
        <span class="info copyright_info">&copy; Copyright 2018 Nicolas Wilhoit & Matthew King/span>
        <span class="info date_info"><fmt:formatDate dateStyle="FULL" value="${sessionScope.date}"/></span>
    </div>
</div>
