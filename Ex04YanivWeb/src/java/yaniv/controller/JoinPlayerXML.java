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
import yaniv.model.Deck;
import yaniv.model.GameModel;
import yaniv.model.Player;

/**
 *
 * @author osus
 */
@WebServlet(name = "JoinPlayerXML", urlPatterns = {"/JoinPlayerXML"})
public class JoinPlayerXML extends HttpServlet {

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
          String chosenName = request.getParameter("chooseNameRadio");
          request.getSession().setAttribute("playerName", chosenName);
          request.getSession().setAttribute("clientVersion", 0);
          ParameterMap XMLgameParams = (ParameterMap)getServletContext().getAttribute("XMLgameParams");
          ArrayList<Player>players = (ArrayList<Player>)XMLgameParams.get("players");
          ArrayList<Player>humanPlayers = (ArrayList<Player>)XMLgameParams.get("humanPlayers");
          GameModel.eGameModes gameMode = (GameModel.eGameModes)XMLgameParams.get("gameMode");
          Deck deck = (Deck)XMLgameParams.get("deck");
          
          Player humanPlayerToRemove = null;
          for (Player player : humanPlayers)
          {
              if (player.getName().equals(chosenName))
              {
                  humanPlayerToRemove = player;
              }
          }
          humanPlayers.remove(humanPlayerToRemove);
          
          
                if (humanPlayers.size()  == 0) //no more human players left to join
                {
                   //save the settings for a game restart
                    ParameterMap savedGameParams = new ParameterMap();
                    savedGameParams.put("gameMode", gameMode);
                    savedGameParams.put("players", players);
                    savedGameParams.put("deck", deck);
                    getServletContext().setAttribute("savedMap", savedGameParams);
                    Utils.InitGame(gameMode, players, deck, getServletContext());
                    getServletContext().getRequestDispatcher("/YanivWeb.jsp").forward(request, response);
                }
                else
                {
                    getServletContext().getRequestDispatcher("/WaitingForPlayers.jsp").forward(request, response);
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
