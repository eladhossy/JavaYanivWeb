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
import org.apache.catalina.Session;
import org.apache.catalina.util.ParameterMap;
import yaniv.model.Deck;
import yaniv.model.GameModel;
import yaniv.model.Player;

/**
 *
 * @author Elad Hossy
 * the first servlet to be called in the application.
 * this servlet is called from the index page, with the parameters for the game, given by the user.
 */ 

@WebServlet(name = "initializer", urlPatterns = {"/initializer"})
public class initializer extends HttpServlet {

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

       GameModel gameModel = new GameModel();
        Map<String, String[]>  gameParameters;
        ParameterMap savedGameParameters; 
       int numOfPlayers;
       int numOfHumanPlayers;
       int leftHumanPlayersToEnterGame;
       GameModel.eGameModes gameMode;
       ArrayList<Player> players = new ArrayList<Player>();
      
        
        try {
            ParameterMap<String, Object> XMLgameParams = (ParameterMap<String, Object>)getServletContext().getAttribute("XMLgameParams");
            if (XMLgameParams == null) //game was NOT initialized using XML file
            {
                if (getServletContext().getAttribute("savedSettings") == null) 
                    //if there aren't saved setting - get new settings from the request
                {
                    gameParameters   = request.getParameterMap();
                }
                else
                {
                    gameParameters = (ParameterMap)(getServletContext().getAttribute("savedSettings"));
                    getServletContext().setAttribute("savedSettings", null);
                }
                //save the settings for if the user will want to restart the game
                //savedGameParameters =(ParameterMap) ((ParameterMap)gameParameters).clone(); 
                //getServletContext().setAttribute("savedMap", savedGameParameters);
         
         
         
                //all the next lines will get the parameters from the map (taken from either the request or the context, for a new game initialization.
                String firstPlayerName = gameParameters.get("firstPlayerName")[0];
                numOfPlayers  = Integer.parseInt(gameParameters.get("numberOfPlayersRadio")[0]);
                numOfHumanPlayers = Integer.parseInt(gameParameters.get("numberOfHumanPlayersRadio")[0]);
                leftHumanPlayersToEnterGame = numOfHumanPlayers - 1;

                //Add the first human player (that initializes the game)
                players.add(new Player(Player.ePlayerType.HUMAN,firstPlayerName));
                request.getSession().setAttribute("playerName", firstPlayerName);
                request.getSession().setAttribute("clientVersion", 0);

                //Add the computer players:
                int numOFComputerPlayers = numOfPlayers-numOfHumanPlayers;
                for (int i = 0 ; i < numOFComputerPlayers ; i++)
                {
                    players.add(new Player(Player.ePlayerType.COMPUTER, "Computer" + i));
                }

        //get the game mode from the request:
                String gameModeText = gameParameters.get("gameModeRadio")[0];
                
                if (gameModeText.equals("singleLimit"))
                {
                    gameMode = GameModel.eGameModes.SINGLE_LIMIT;
                }
                else if (gameModeText.equals("gameLimit"))
                {
                    gameMode = GameModel.eGameModes.GAME_LIMIT;
                }
                else
                {
                    gameMode = GameModel.eGameModes.TURNS;
                }
                int endGameValue = Integer.parseInt(gameParameters.get("endValueText")[0]);
                gameMode.setValue(endGameValue);
            }
            
            else //game was initialized using an XML file
            {
                players = (ArrayList<Player>)XMLgameParams.get("players");
                ArrayList<Player> humanPlayers = (ArrayList<Player>)XMLgameParams.get("humanPlayers");
                gameMode = (GameModel.eGameModes)XMLgameParams.get("gameMode");
                Deck deck = (Deck)XMLgameParams.get("deck");
                String firstPlayerName = (String)XMLgameParams.get("firstPlayerName");
                request.getSession().setAttribute("playerName", firstPlayerName);
                //first user already choose his name (from the xml), so - remove it
                Player firstPlayer = null;
                for (Player player : humanPlayers)
                {
                    if (player.getName().equals(firstPlayerName))
                    {
                        firstPlayer = player;
                    }
                }
                humanPlayers.remove(firstPlayer);
                
                leftHumanPlayersToEnterGame = humanPlayers.size();
            }
         
         
  if (leftHumanPlayersToEnterGame > 0)  //can't initialize yet, need to wait for other players to join
  {
          getServletContext().setAttribute("gamePlayers", players);
          getServletContext().setAttribute("leftHumanPlayersToEnterGame", leftHumanPlayersToEnterGame);
          getServletContext().setAttribute("gameMode", gameMode);
          getServletContext().getRequestDispatcher("/WaitingForPlayers.jsp").forward(request, response);
  }
  else
  {//save the settings for a game restart
//      ParameterMap savedGameParams = new ParameterMap();
//      savedGameParams.put("gameMode", gameMode);
//      savedGameParams.put("players", players);
//      savedGameParams.put("deck", null);
//     getServletContext().setAttribute("savedMap", savedGameParams);
     Utils.InitGame(gameMode, players, null, getServletContext());
     //init of a new gameModel (=new game). null is for the game to create it's own deck.
//       gameModel.InitializeGame(gameMode, players, null);
//       //store the model in the servlet contex for all the other servlets to use
//       getServletContext().setAttribute("gameModel", gameModel);
//       getServletContext().setAttribute("XMLgameParams", null);
//      getServletContext().setAttribute("version", 0);
       //forward the user to the game board
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
