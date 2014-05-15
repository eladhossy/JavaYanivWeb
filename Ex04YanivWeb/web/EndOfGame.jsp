<%-- 
    Document   : EndOfGame
    Created on : 16/06/2012, 18:55:14
    Author     : Elad Hossy
--%>

<%@page import="yaniv.model.Player"%>
<%@page import="yaniv.model.GameModel"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
         <link href="styles/yanivEndGame1920x1080.css" rel="stylesheet" type="text/css" />
           <script type="text/javascript" src="scripts/jquery.js"></script>
    <script type="text/javascript" src="scripts/AjaxForEndGame.js"></script>
         <title>JSP Page</title>
        <% GameModel gameModel = (GameModel) getServletContext().getAttribute("gameModel");%>
    </head>
    <body>
        <table>
            <tr>
                <td>
                    <div><h1>Game was ended</h1></div>
                    <% 
if (gameModel.isAllPlayersLeft() == true) {out.println("<div>All Players Has Left The Game</div>");}                    
        else {
            out.println("<h3>Players Remaining:</h3>");
            for (Player player : gameModel.getPlayers()){
                out.println("<div>" + player.getName() + ": " + player.getScores() + " Scores.</div>");
            }
            out.println("<h3>And The Winners Are:</h3>");
            for (Player winnerPlayer : gameModel.getGameWinners()){
                out.println("<div>" +winnerPlayer.getName()  + ": "  + winnerPlayer.getScores() + " Scores.</div>");
            }
      }
                
                
            
        



%>  
                   
                        

            
                    
<form action="GameRestarter" method="post"><input  TYPE=hidden  NAME=restartMethod VALUE=restartGame><input type="submit" value="Restart Game       "></input></form>
<form action="GameRestarter" method="post"><input  TYPE=hidden  NAME=restartMethod VALUE=newGame><input type="submit"            value="Start a New Game"></input></form>
                </td>
            </tr>
        </table>
    </body>
</html>
