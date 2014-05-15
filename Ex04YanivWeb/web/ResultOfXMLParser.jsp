<%-- 
    Document   : ResultOfXMLParser
    Created on : 21/07/2012, 09:48:17
    Author     : osus
--%>

<%@page import="org.apache.catalina.util.ParameterMap"%>
<%@page import="yaniv.model.GameModel"%>
<%@page import="yaniv.model.GameModel.eGameModes"%>
<%@page import="yaniv.model.Player"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
           <script type="text/javascript" src="scripts/jquery.js"></script>   
        <script type="text/javascript" src="scripts/AjaxForInitGame.js"></script>
          <link href="styles/yanivXMLResults1920x1080.css" rel="stylesheet" type="text/css" />
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
        <h1>Here are the results of the xml file parser:</h1>
        <h2>Players:</h2>
        <% ParameterMap<String, Object> XMLgameParams = (ParameterMap<String, Object>)application.getAttribute("XMLgameParams");
        ArrayList<Player>players = (ArrayList<Player>)XMLgameParams.get("players");
        ArrayList<Player>humanPlayers = (ArrayList<Player>)XMLgameParams.get("humanPlayers");
        GameModel.eGameModes gameMode = (eGameModes)XMLgameParams.get("gameMode");
        
 
        out.print("<table 'style=border:5px solid black'><tr><td>Name</td><td>Type</td></tr>");
        for (Player player : players){
            out.print("<tr>");
                      out.print("<td>" + player.getName() + "</td>");
                      out.print("<td>" + player.getPlayerType() + "</td>");
             out.print("</tr>");
        }
        out.print("</table>");
        out.print("<br />");
        out.print("Please choose which human player are you:"); 
        out.print("<form action='FirstPlayerChoice' method='post'>");          
        boolean firstHuman = true;
        for (Player player : humanPlayers){
                if (firstHuman){
                    out.print("<input type='radio' name='firstPlayerChooseRadio' value='" + player.getName() +  "' checked='checked'/>" + player.getName() + "<br />");
                    firstHuman = false;
                }
                 else{
                    out.print("<input type='radio' name='firstPlayerChooseRadio' value='" + player.getName() +  "'/>" + player.getName() + "<br />");
                  }
         }
        out.print("<input type='submit' />");
        out.print("</form>");
         
                 
        
out.print("<h2>Game Mode: </h2>" + gameMode.name() + " " + gameMode.getValue());        
%>              
        
        
        
    </body>
</html>
