/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yaniv.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yaniv.model.GameModel;
import yaniv.model.Player;

/**
 *
 * @author Elad Hossy
 * each time a round is over, the user get a message with a button, 
 * when he click it, this servlet is called to prepare a new round.
 */
@WebServlet(name = "NewRound", urlPatterns = {"/NewRound"})
public class NewRound extends HttpServlet {

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
            Utils.incrementVersion(getServletContext());
            GameModel gameModel = (GameModel) getServletContext().getAttribute("gameModel");
            gameModel.prepareANewRound();
            Player currentPlayer = gameModel.getCurrentPlayer();
             if (currentPlayer.getPlayerType().equals(Player.ePlayerType.COMPUTER))
                {
                     gameModel.playComputerTurn(); //'tell' the model to play the computer player turn.
                      Utils.incrementVersion(getServletContext());
                    if (gameModel.getGameStatus() == GameModel.eGameSatus.ENDGAME) //if the game is ended as a result
                    {
                        getServletContext().getRequestDispatcher("/EndOfGame.jsp").forward(request, response);
                    }
                    else
                    {
                        getServletContext().getRequestDispatcher("/YanivWeb.jsp").forward(request, response);
                    }
                }
                else
                {
                    getServletContext().getRequestDispatcher("/YanivWeb.jsp").forward(request, response);
                }
            //getServletContext().getRequestDispatcher("/YanivWeb.jsp").forward(request, response);
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
