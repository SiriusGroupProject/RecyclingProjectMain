<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

<%
    HttpSession session1 = request.getSession();
    String username = request.getParameter("username");
    session1.setAttribute("username", username);
    String password = request.getParameter("password");
    session1.setAttribute("password", password);
    response.sendRedirect("/dashboard");
%>
</body>
</html>
