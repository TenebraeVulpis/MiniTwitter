<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="header_bar">
    <c:if test="${user != null}">
        <ul>
            <li><a href="home.jsp">Home</a></li>
            <li><a href="notifications.jsp">Notifications</a></li>
            <li><a href="signup.jsp">Profile</a></li>
            <li id="logout_list">
                <form method="post" action="membership">
                    <input type="hidden" name="action" value="logout">
                    <input type="submit" name="logout" value="Sign Out" 
                        class="logout_button"/>
                </form>
            </li>
        </ul>
    </c:if>
</div>