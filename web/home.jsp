<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<html>
    <head>
        <link rel="stylesheet" type="text/css" href="styles/main.css" />
        <link rel="stylesheet" type="text/css" href="styles/home.css" />
        <link rel="stylesheet" type="text/css" href="styles/header.css" />
        <link rel="stylesheet" type="text/css" href="styles/footer.css" />
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <title>Home Page</title>
    </head>
    
    <body>
        <div id="header"><c:import url="header.jsp" /></div>
        
        <c:if test="${user == null}">
            <c:redirect url = "/login.jsp"/>
        </c:if>
        
        <c:if test="${users == null}">
            <c:redirect url = "/membership?action=get_users"/>
        </c:if>
        
        <c:if test="${tweets == null}">
            <c:redirect url = "/tweet?action=get_tweets"/>
        </c:if>
        
        <div class="flex-container">
            
        </div>
    </body>
</html>
