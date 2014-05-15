<!--This is the page of the game-board-->

<%@page import="yaniv.controller.JSPUtils"%>
<%@page import="yaniv.model.Card"%>
<%@page import="yaniv.model.Card"%>
<%@page import="java.util.ArrayList"%>
<%@page import="yaniv.model.Player"%>
<%@page import="yaniv.model.GameModel"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
    <title>Yaniv Web</title>
    <link href="styles/yanivBoard1920x1080.css" rel="stylesheet" type="text/css" />
     <script type="text/javascript" src="scripts/jquery.js"></script>
    <script type="text/javascript" src="scripts/boardJS.js"></script>
     <script type="text/javascript" src="scripts/AjaxForBoard.js"></script>
    <style type="text/css">
    </style>
</head>


<body>
    <% GameModel gameModel = (GameModel)application.getAttribute("gameModel"); 
            Player currentPlayer = gameModel.getCurrentPlayer();
            JSPUtils.gameModel = gameModel;
            ArrayList<Player> players = gameModel.getPlayers();
            Player topPlayer = null;
            Player  buttomPlayer = null;
            Player rightPlayer = null;
            Player leftPlayer = null;
            
            buttomPlayer = players.get(0);
            if (players.size() == 2)
             {
                topPlayer = players.get(1);
             }
            if (players.size() > 2)
             {
                leftPlayer = players.get(1);
                topPlayer = players.get(2);
            }
            if (players.size() == 4)
           {
                rightPlayer = players.get(3);
            }
%>
         <form id="userChoiceForm" action="CheckCardSelection" method="post"></form>
    <table class=mainTbl cellpadding="0" cellspacing="0">
        <tr>
            <td>
                <table class="verticalCards leftCards"   cellpadding="0" cellspacing="0">
                      <% if (leftPlayer != null) {
                    out.write(JSPUtils.printVerticalPlayer(leftPlayer, (String)session.getAttribute("playerName"))); } %>
                </table>
            </td>
            <td>
                <table class="centerPanel"  cellpadding="0" cellspacing="0">
                    <tr>
                        <td>
                            <table class="horizontalCards topCards" width="100"   cellpadding="0" cellspacing="0">
                                <tr>
                                    <%=JSPUtils.printHorizontalPlayer(topPlayer , (String)session.getAttribute("playerName")) %>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div>
                                <img  class="yanivLogo" src="images/yanivCenter.png" alt="Alternate Text" />
                                <%= JSPUtils.printMessage() %>
                            </div>
                            <div>
                                <%= JSPUtils.PrintPileAndDeck(gameModel.getCenterPile()) %>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <table class="horizontalCards buttomCards" width="100"   cellpadding="0" cellspacing="0">
                                <tr>
                                    <%=JSPUtils.printHorizontalPlayer(buttomPlayer, (String)session.getAttribute("playerName")) %>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td>
                <table class="verticalCards  rightCards" cellpadding="0" cellspacing="0">
                    <% if (rightPlayer != null) {
                    out.write(JSPUtils.printVerticalPlayer(rightPlayer, (String)session.getAttribute("playerName"))); } %>
                </table>
            </td>
        </tr>
    </table>
           
</body>




                      





</html>
