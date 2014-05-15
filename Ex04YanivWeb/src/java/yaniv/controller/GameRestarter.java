/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yaniv.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.util.ParameterMap;
import org.omg.PortableInterceptor.ForwardRequest;
import yaniv.model.Deck;
import yaniv.model.GameModel;
import yaniv.model.Player;

/**
 *
 * @author Elad Hossy
 * when a game is over, the user can decided wether to restart it or start a new game.
 * each decision is a button, and after the user press it, this servlet is called.
 */
@WebServlet(name = "GameRestarter", urlPatterns = {"/GameRestarter"})
public class GameRestarter extends HttpServlet {

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
         String restartMethod = request.getParameter("restartMethod");

         if (restartMethod.equals("newGame")) //if the user wants a new game
         {
            getServletContext().setAttribute("version", -1);
            getServletContext().setAttribute("leftHumanPlayersToEnterGame", null);
           response.sendRedirect("index.jsp");
         }
         else //the user wants to restart the game
         {
            //store the saved map that the initializer saved, to another attribute in the servlet context, so the initializer will know to take the saved parameters
             ParameterMap  savedMap = (ParameterMap)getServletContext().getAttribute("savedMap"); 
          
             //  getServletContext().setAttribute("savedSettings", savedMap);
             GameModel.eGameModes gameMode = (GameModel.eGameModes)savedMap.get("gameMode");
            ArrayList<Player>players = (ArrayList<Player>) savedMap.get("players");
            Deck deck = (Deck)savedMap.get("deck");
            Utils.InitGame(gameMode, players, deck, getServletContext());
 
            getServletContext().getRequestDispatcher("/YanivWeb.jsp").forward(request, response);
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
