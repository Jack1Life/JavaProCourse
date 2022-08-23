<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Prog.kiev.ua</title>
</head>
<body>
    <div align="center">
        <h1>Your login is: ${login}, your roles are:</h1>
        <c:forEach var="s" items="${roles}">
            <h3><c:out value="${s}" /></h3>
        </c:forEach>

        <c:if test="${admin || moderator}">
            <c:url value="/admin" var="adminUrl" />
            <p>
                <a href="${adminUrl}"> Click </a>
                <c:if test="${admin}">
                    for admin page
                </c:if>
                <c:if test="${moderator}">
                    for moderator page
                </c:if>
            </p>
        </c:if>

        <c:url value="/update" var="updateUrl" />
        <form action="${updateUrl}" method="POST">
            E-mail:<br/><input type="text" name="email" value="${email}" /><br/>
            Phone:<br/><input type="text" name="phone" value="${phone}" /><br/>
            Address:<br/><input type="text" name="address" value="${address}" /><br/>
            <input type="submit" value="Update" />
        </form>

        <c:url value="/logout" var="logoutUrl" />
        <p>Click to logout: <a href="${logoutUrl}">LOGOUT</a></p>
    </div>
</body>
</html>
