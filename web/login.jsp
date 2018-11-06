<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html>
    <head>
        <link rel="stylesheet" type="text/css" href="styles/header.css" />
        <link rel="stylesheet" type="text/css" href="styles/footer.css" />
        <link rel="stylesheet" type="text/css" href="styles/main.css" />
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <title>Resource Not Found Page</title>
    </head>
    
    <body>
        <c:import url="/header.jsp" />
            <h1>404 Error - Requested Page could not be found!</h1>
        <c:import url="/footer.jsp" />
    </body>
</html>
