/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yaniv.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.util.ParameterMap;
import yaniv.model.GameModel;
import yaniv.model.GameModel.eGameModes;
import yaniv.model.Player;

/**
 *
 * @author osus
 */
@WebServlet(name = "JoinGame", urlPatterns = {"/JoinGame"})
public class JoinGame extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
        out.print("<script type='text/javascript' src='scripts/jquery.js'></script>");
        out.print("<script type='text/javascript' src='scripts/joinGameJS.js'></script>");
        out.print("<link href='styles/yanivInput1920x1080.css' rel='stylesheet' type='text/css' />");
        out.print("<table class='banner' width='100%' border='0'  cellspacing='0'><tr><td><img  src='images/yanivLogoSmallBlack.png' alt='Alternate Text' /></td><td><img  src='images/evilJoker.gif' alt='Alternate Text' /></td></tr></table>");  
        ParameterMap XMLgameParams = (ParameterMap)getServletContext().getAttribute("XMLgameParams");
        int leftHumanPlayersToEnterGame =(Integer) getServletContext().getAttribute("leftHumanPlayersToEnterGame");
        if (leftHumanPlayersToEnterGame == 0)
        {
            out.print("The game is in progress. Please try again later...");
        }
        else
        {
          if (XMLgameParams == null) //the game has already been initialized MANUALLY
            {
                
                out.print("<h2>The game is already been initialized. Write  your name and  click submit to join</h2>");
                out.print("<form id='joinGameForm' action='JoinPlayer' method='post' >");
                out.print("Name: <input id='inputName' type='text' name='playerName'><span id='msg' class='error'></span>");
                
                out.print("</form>"); 
                out.print("<Button id='submitButton'>Join Game!</Button>"); 
            }
            else
            {
            out.print("<h2>The game is already been initialized Using XML. Choose  your name:</h2>");
            out.print("<form action='JoinPlayerXML' method='post' >");
            ArrayList<Player>humanPlayers = (ArrayList<Player>)XMLgameParams.get("humanPlayers");
            boolean firstHuman = true;
            for (Player player : humanPlayers)
            {
                if (firstHuman)
                {
                    out.print("<input type='radio' name='chooseNameRadio' value='" + player.getName() +  "' checked='checked'/>" + player.getName() + "<br />");
                    firstHuman = false;
                }
                 else
                {
                    out.print("<input type='radio' name='chooseNameRadio' value='" + player.getName() +  "'/>" + player.getName() + "<br />");
                }
         }
                out.print("<input type='submit'>"); 
                out.print("</form> <br />"); 
         }
        
         out.print("<h2>Game Details: </h2>");
         out.print("<ol>");
         out.print("<li>Players:");
         out.print("<ol>");
         
         ArrayList<Player> players; 
         GameModel.eGameModes gameMode;
        if (XMLgameParams == null) 
        {
            players = (ArrayList<Player>)getServletContext().getAttribute("gamePlayers");
            gameMode = (eGameModes)getServletContext().getAttribute("gameMode");  
         }
        else
        {
            players = (ArrayList<Player>)XMLgameParams.get("players");
            gameMode = (eGameModes)XMLgameParams.get("gameMode");
        }

        for (Player player : players)
        {
            out.print("<li><span class='names'>" + player.getName() + "</span>  Type: " + player.getPlayerType() + "</li>");
        }
 
             out.print("</ol></li>");   
             out.print("<li>Game Mode:");
            out.print(gameMode);                
            out.print(gameMode.getValue()); 
            
}   
            
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
