<%-- 
    Document   : WaitingForPlayers
    Created on : 19/07/2012, 10:51:58
    Author     : osus
--%>

<%@page import="yaniv.model.Player"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.apache.catalina.util.ParameterMap"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="styles/yanivInput1920x1080.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="scripts/jquery.js"></script>
         <script type="text/javascript" src="scripts/AjaxForWaitingForPlayers.js"></script>
        <title>JSP Page</title>
    </head>
    <body>
         <table class="banner" width="100%" border="0"  cellspacing="0">
        <tr>
            <td>
                 <img  src="images/yanivLogoSmallBlack.png" alt="Alternate Text" />
            </td>
            <td>
                 <img  src="images/evilJoker.gif" alt="Alternate Text" />
            </td>
        </tr>
    </table>
        
        
        
        <%= "<h1>hello " + request.getSession().getAttribute("playerName") + "</h1><br />" %>
        <% int leftHumanPlayersToEnterGame; 
        ParameterMap XMLgameParams = (ParameterMap)getServletContext().getAttribute("XMLgameParams");
         if (XMLgameParams == null) {
             leftHumanPlayersToEnterGame = (Integer)getServletContext().getAttribute("leftHumanPlayersToEnterGame") ;
         }
               else {
            ArrayList<Player>humanPlayers= (ArrayList<Player>)XMLgameParams.get("humanPlayers");
            leftHumanPlayersToEnterGame = humanPlayers.size();
               }


%>
        <%= "<h2>waiting for another <span id='count'>" + leftHumanPlayersToEnterGame + "</span> players to enter the game</h2>" %>
    </body>
</html>
