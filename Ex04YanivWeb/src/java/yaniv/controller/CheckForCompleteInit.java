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
import yaniv.model.Player;

/**
 *
 * @author osus
 */
@WebServlet(name = "CheckForCompleteInit", urlPatterns = {"/CheckForCompleteInit"})
public class CheckForCompleteInit extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        int leftHumanPlayersToEnterGame;
        try {
           ParameterMap XMLgameParams = (ParameterMap)getServletContext().getAttribute("XMLgameParams");
         if (XMLgameParams == null) {
             leftHumanPlayersToEnterGame = (Integer)getServletContext().getAttribute("leftHumanPlayersToEnterGame") ;
         }
               else {
            ArrayList<Player>humanPlayers= (ArrayList<Player>)XMLgameParams.get("humanPlayers");
            leftHumanPlayersToEnterGame = humanPlayers.size();
               }
         
         if (leftHumanPlayersToEnterGame== 0)
         {
           out.print("{\"complete\":\"true\",\"left\":\"0\"}");
         }
         else
         {
             out.print("{\"complete\":\"false\", \"left\":\"" + leftHumanPlayersToEnterGame + "\"}");
         }
          out.flush();
            
            
            
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
