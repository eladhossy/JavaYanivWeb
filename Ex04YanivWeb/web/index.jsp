<%-- 
    Document   : index
    Created on : 19/07/2012, 09:34:13
    Author     : osus
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
      
        <% Object obj = getServletContext().getAttribute("leftHumanPlayersToEnterGame");
        if (obj == null)
                     {
            response.sendRedirect("InitGame.htm");
        }
               else{
            getServletContext().getRequestDispatcher("/JoinGame").forward(request, response);
               }
        %>
    </body>
</html>
