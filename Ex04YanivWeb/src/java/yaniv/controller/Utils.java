/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yaniv.controller;

import java.util.ArrayList;
import javax.servlet.ServletContext;
import org.apache.catalina.util.ParameterMap;
import yaniv.model.Card;
import yaniv.model.Deck;
import yaniv.model.GameModel;
import yaniv.model.Player;

/**
 *
 * @author osus
 */
public class Utils {
    public static void InitGame(GameModel.eGameModes gameMode, ArrayList<Player> players, Deck deck, ServletContext servletContext)
    {
        //save the settings for a game restart
        ParameterMap savedGameParams = new ParameterMap();
        savedGameParams.put("gameMode", gameMode);
        ArrayList<Player>savedPlayers = copyPlayers(players);
        savedGameParams.put("players", savedPlayers);
        
        if (deck != null)
        {
            Deck savedDeck = copyDeck (deck);
            savedGameParams.put("deck", savedDeck );
        }
        else
        {
            savedGameParams.put("deck", deck );
        }
        
        servletContext.setAttribute("savedMap", savedGameParams);
        
         //init of a new gameModel (=new game). null is for the game to create it's own deck.
        GameModel gameModel = new GameModel();
        gameModel.InitializeGame(gameMode, players, deck);
         //store the model in the servlet contex for all the other servlets to use
        servletContext.setAttribute("gameModel", gameModel);
        servletContext.setAttribute("version", 0);
        if (gameModel.getCurrentPlayer().getPlayerType().equals(Player.ePlayerType.COMPUTER))
                {
                     gameModel.playComputerTurn(); //'tell' the model to play the computer player turn.
                      incrementVersion(servletContext);
                }
      
    }

private static ArrayList<Player> copyPlayers (ArrayList<Player> players)
{
    ArrayList<Player>clonedPlayers = new ArrayList<Player>();
    for (Player player : players)
        {
            String name = player.getName();
            Player.ePlayerType playerType = player.getPlayerType();
            clonedPlayers.add(new Player(playerType, name));
        }
    return clonedPlayers;
}

    private static Deck copyDeck(Deck deck) 
    {
        Deck clonedDeck = new Deck();
        ArrayList<Card> deckCards = deck.getDeck();
        ArrayList<Card>clonedCards = clonedDeck.getDeck();
       for (Card card : deckCards)
       {
           clonedCards.add(card);
       }
        return clonedDeck;
    }


    public static void incrementVersion(ServletContext servletContext) //used to incrememnt the version at server's side
    {
        int version = (Integer)servletContext.getAttribute("version");
         version++;
         servletContext.setAttribute("version", version);
    }
    
}


