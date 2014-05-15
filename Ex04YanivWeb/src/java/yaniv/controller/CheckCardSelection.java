/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yaniv.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yaniv.model.Card;
import yaniv.model.GameModel;
import yaniv.model.Player;

/**
 *
 * @author Elad Hossy
 * after a user choose the cards to dump, he presses either the deck or the pile.
 * then, this servlet is called, to decided wether the selection is legal.
 * if so, the model is updated.
 */
@WebServlet(name = "CheckCardSelection", urlPatterns = {"/CheckCardSelection"})
public class CheckCardSelection extends HttpServlet {

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
   
    enum eCardPosition //sent as parameter to the 'key to card' converter.
    {
        PLAYER,
        PILE
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
         
        try {
            //here, it is sure that all initializaions has completed, so it is safe to remove init Data, for next games to come
            getServletContext().setAttribute("XMLgameParams", null); 
            getServletContext().setAttribute("leftHumanPlayersToEnterGame", null);

             GameModel gameModel = (GameModel) getServletContext().getAttribute("gameModel");
            Map map = request.getParameterMap();
            Set keys = map.keySet();
            String valueString;
            
            ArrayList<Card> userChosenCards = new ArrayList<Card>(); //holds the selected cards to dump.
            Card cardToDrawFromPile = null;//holds the card to draw from the pile
            boolean isLegal;
            String keyString;
            String[] jokers;
            
            for (Object key : keys) //going through all the keys
            {
                keyString = key.toString();
                if (!(keyString.equals("cardSource"))) // if the current key is a selected card value (not the card source parameter)
                {
                    if (keyString.equals("JOKER_JOKER")) // since there are more than one joker
                    {
                        jokers =(String[]) map.get("JOKER_JOKER");
                        for (String joker : jokers) //for each joker, it will find the reference, and add it to userChosenCards.
                        {
                             userChosenCards.add(converKeyToCard(keyString,eCardPosition.PLAYER, userChosenCards));
                        }
                    }
                    else
                    {
                             userChosenCards.add(converKeyToCard(keyString,eCardPosition.PLAYER, userChosenCards) ); //convert the key to a card object and add it to the selected cards.
                    }
                }
                else //the current key is the card source parameter
                {
                    valueString =((String[]) map.get(keyString))[0];
                    if (!(valueString.equals("DECK"))) //if the source for a new card is a card from the pile
                    {
                        cardToDrawFromPile = converKeyToCard(valueString,eCardPosition.PILE,null);
                    }
                }
            }
           
            if (!gameModel.getCurrentPlayer().getName().equals(request.getSession().getAttribute("playerName")))
            {
                isLegal = false;
            }
            else
            {
                isLegal = GameModel.checkValidityOfChoice(userChosenCards); //call the model with the user selection to check if it is valid.
            }
            
            if (isLegal)
            {
                gameModel.UpdateHumanPlayerAfterTurn(userChosenCards, cardToDrawFromPile);
                Utils.incrementVersion(getServletContext());
                gameModel.nextPlayerTurn(null);
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
            }   
            else
            {
                getServletContext().getRequestDispatcher("/YanivWeb.jsp").forward(request, response);
            }
            
        } finally {            
            out.close();
        }
    }
    
    
    // key to card converter. 
    //each card the user chose is recieved here as a string. this converter will convert it to a card object
    //in order to send it later to the model for manipulations.
    private Card converKeyToCard(String keyString, eCardPosition cardPosition, ArrayList<Card> chosenCards)
    {
        GameModel gameModel = (GameModel) getServletContext().getAttribute("gameModel");
        ArrayList<Card> playerCards = gameModel.getCurrentPlayer().getCards();
        Card res = null;
        
        String[] arr;
        String suit;
        String rank;
         
        arr = keyString.split("_");
        rank = arr[0];
        suit = arr[1];

        Card.eCardRank cardRank = Card.eCardRank.valueOf(rank);
        Card.eCardSuit cardSuit = Card.eCardSuit.valueOf(suit);
        
        ArrayList<Card> cardsToSearchIn;
        if (cardPosition.equals(eCardPosition.PLAYER))
        {
            cardsToSearchIn = gameModel.getCurrentPlayer().getCards();
        }
        else //position = PILE
        {
            cardsToSearchIn = (ArrayList<Card>)gameModel.getCenterPile().peek();
        }
            
        for (Card card : cardsToSearchIn)
        {
            if (card.getRank().equals(cardRank) && card.getSuit().equals(cardSuit))
            {
                if (chosenCards !=null)
                {
                    if (chosenCards.indexOf(card) == -1)
                   {
                    res = card;
                    }
                }
                else
                {
                    res = card;
                }
            }
        }
        
        return res;
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
