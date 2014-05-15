/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yaniv.controller;

import java.util.ArrayList;
import java.util.Stack;
import yaniv.model.Card;
import yaniv.model.GameModel;
import yaniv.model.Player;


/**
 *
 * @author Elad Hossy
 * a utilities class for the yanivWeb.jsp.
 */
public class JSPUtils 
{
     public enum eCardPrintState //the function that prints the cards will use this enum to decide which pic to choose.
    {
        HIDDEN,SHOWN, DISABLED; 
    }
    public static GameModel gameModel;
 
    public static eCardPrintState cardPrintState;
 

    public static String printHorizontalPlayer(Player player, String sessionPlayer) //will print everything about a horizontal player
    {
             String res = "";
             eCardPrintState cardPrintState = decideCardState(player, sessionPlayer);
             String addedClasses = decideAddedClasses(cardPrintState);
             res = res + printHorizontalCards(player.getCards(), cardPrintState, addedClasses);
             res = res + printPlayerPanel(player, sessionPlayer);
             return res;
    }
    
     private static String printHorizontalCards(ArrayList<Card> cards, eCardPrintState cardPrintState, String addedClasses) //will print horizontal player's cards.
    {
        String res = "";
        for (Card card : cards) 
         {
                 res = res + "<td>";
                 res = res + printCard(card, cardPrintState, addedClasses);
                 res = res + "</td>";
          }
        return res;
    }
    
     public static String printVerticalPlayer(Player player, String sessionPlayer) //will print everything about a vertical player
    {
             String res = "";
             eCardPrintState cardPrintState = decideCardState(player, sessionPlayer);
             String addedClasses = decideAddedClasses(cardPrintState);
             res = res + printVerticalCards(player.getCards(), cardPrintState, addedClasses);
             res = res + "<tr>";
             res = res + printPlayerPanel(player, sessionPlayer);
             res = res + "</tr>";
             return res;
    }
    
    private static String printVerticalCards(ArrayList<Card> cards, eCardPrintState cardPrintState, String addedClasses)  //will print vertical player's cards.
    {
        String res = "";
        for (Card card : cards)
                {
                    res = res + "<tr>";
                    res = res + "<td>";
                    res = res + printCard(card, cardPrintState, addedClasses);
                    res = res + "</td>";
                    res = res + "</tr>";
                }
        return res;

    }

    public static String PrintPileAndDeck(Stack pile) //print the pile and deck
    {
        String res = "";
        res = res + "<table class='horizontalCards ' width='100' style='display: block' />";
        res = res + "<tr>";
        res = res + "<td>";
       res = res + printCard(null, eCardPrintState.HIDDEN, "deck");
        res = res + "</td>";
        ArrayList<Card> pileTopCards = (ArrayList<Card>)pile.peek();
        res = res + printPileCards(pileTopCards, eCardPrintState.SHOWN, "pile");
       
        res = res + "</tr>";
        res = res + "</table>";
        
        return res;

    }
    
    public static String printMessage() //all the messages to the user, are printed here
    {
        Player currentPlayer = gameModel.getCurrentPlayer();
        String res = "";
        if (gameModel.getGameStatus() == GameModel.eGameSatus.ENDROUND)
        {
           res = res + "<span class='message' >Round Was Ended.</br> Winners are:</br> " + printWinners() + "</span>";
           res = res + "<form class='message' action='NewRound' method='post'><input type='submit' value='press to continue'/></form></span>";
        }

        return res;
    }
    
    
    //below are all the private sub-routines that the public methods above use. each sub-task is warpped with a private func, since a lot of the tasks are repeated, 
    //so the code is reused.
    private static String printCard(Card card, eCardPrintState cardPrintState, String addedClasses) //prints a single card.
    {
        String res = "";
        if (cardPrintState == eCardPrintState.SHOWN)
        {
              res = res + "<img id='" + card.getRank() + "_" + card.getSuit() + "' class='cardImg " + addedClasses + "' src='images/" + card.getRank() + "_" + card.getSuit() + ".png' />";
        }
        else if (cardPrintState == eCardPrintState.HIDDEN)
        {
              res = res + "<img class='cardImg " + addedClasses + "' src='images/CARD_BACK.png' />";
        }
        else //DISABLED
        {
              res = res + "<img class='cardImg " + addedClasses + "' src='images/CARD_BACK_DISABLED.png' />";
        }
        return res;
    }
    
    private static eCardPrintState decideCardState(Player player, String sessionPlayer) 
    {
        eCardPrintState res;
        Player currentPlayer = gameModel.getCurrentPlayer();
        if ( player.getName().equals(sessionPlayer)  &&  currentPlayer.getPlayerType().equals(Player.ePlayerType.HUMAN)          )
        {
            res = eCardPrintState.SHOWN;
        }
        else
        {
            res = eCardPrintState.DISABLED;
        }
        return res;
    }
    
    private static String printPileCards(ArrayList<Card> cards, eCardPrintState cardPrintState, String addedClasses)  //prints the pile cards
    {
        String res = "";
        int indexOfCard;
        for (Card card : cards) 
         {
                 res = res + "<td>";
                 indexOfCard = cards.indexOf(card);
                 if (indexOfCard == 0 || indexOfCard == (cards.size()-1))
                 {
                      res = res + printCard(card, cardPrintState, addedClasses);
                 }
                 else
                  {
                      res = res + printCard(card, cardPrintState,"disabledPile");
                   }
                         
                 res = res + "</td>";
          }
        return res;
    }
    
    private static String decideAddedClasses(eCardPrintState cardPrintState)  //each card will have attached classes.
    {
        if (cardPrintState.equals(eCardPrintState.SHOWN))
        {
            return "currentPlayerCards enabled";
        }
        
        return "";
    }

    private static String printWinners()  //prints the winners for the round.
    {
        String res = "";
        ArrayList<Player> winners = gameModel.getRoundWinners();
        for (Player player : winners)
        {
            res = res + player.getName() + ": " + player.TotalHandValue() + " scores in hand.";
        }
        return res;
    }

    private static String printPlayerPanel(Player player, String sessionPlayer)  //prints each player details and action buttons.
    {
        String res = "";
        res = res + "<td>";
        if (player.equals(gameModel.getCurrentPlayer()))
        {
            res = res + "<table class='playerPanel sessionPlayer'><tr><td>";
        }
        else
        {
            res = res + "<table class='playerPanel'><tr><td>";
        }
        res = res + "<table>";
        res = res  + "<tr><td>" + player.getName() + "</td></tr>";
        res = res  + "<tr><td>Scores: </td></tr>";
   
       
        if (player.getPlayerType().equals(Player.ePlayerType.HUMAN) && player.getName().equals(sessionPlayer))
        {
            res = res   + "<tr><td>hand: </td></tr>";
        }
        res = res + "</table></td>";
        res = res + "<td><table>";
        res = res + "<tr><td>" + player.getPlayerType().toString() + "</td></tr>";
        res = res + "<tr><td>" + player.getScores() + "</td></tr>";
        
        if (player.getPlayerType().equals(Player.ePlayerType.HUMAN) && player.getName().equals(sessionPlayer))
        {
             res = res  + "<tr><td>" + player.TotalHandValue() + "</td></tr>";
        }
        res = res + "</table></td>";
        res = res + "<td><table>";

        if (player.getPlayerCondition() == Player.ePlayerCondition.YANIV)
        {
            res = res + "<img src='images/yanivShout.png' alt="+ player.getPlayerCondition() + " />";
        }
        else if (player.getPlayerCondition() == Player.ePlayerCondition.ASSAF)
        {
            res = res + "<img src='images/assafShout.png' alt="+ player.getPlayerCondition() + " />";
        }
        else
        {
            if (player.getPlayerType().equals(Player.ePlayerType.HUMAN))
            {
                if (player.getName().equals(sessionPlayer))
                {
                    res = res  + "<tr><td><form id='quitForm' action='HumanQuitGame'  method='post'><input type='submit' value='Quit'></input></form></td></tr>";
                    if (player.TotalHandValue() <= 7 && player.equals(gameModel.getCurrentPlayer()))
                    {
                        res = res  + "<tr><td><form action='HumanCallYaniv'  method='post'><input type='submit' value='Yaniv!'></input></form></td></tr>";
                    }
                }
            }
        }

       res = res + "</table></td>";
       res = res + "</table></td>";
        
        return res;
    }
    
   
}
