/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yaniv.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
//import yaniv.view.YanivFrame;

/**This class is the main-class of the model part of the program
 *
 * @author Elad Hossy
 */
public class GameModel 
{
    public enum eGameSatus
    {
        ENDROUND, ENDGAME, NORMAL
    }
    public enum eGameModes
   {
       /** 3 game modes, according to the demand. */
       SINGLE_LIMIT, GAME_LIMIT, TURNS;
       
       /** the value for the mode (for example, 3 turnes) */
       int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
   }
       
    public enum eCardSource
    {
        /** each player can decide from where to take a new card: the deck, or the first or last of a group of card at
        *  the top of the pile. if there is only one card at the top, it considered PILE_FIRST
        */
        DECK, PILE_FIRST, PILE_LAST
    }

    final static int ASSAF_PENALTY = 30;
    private iModelListener modelListener;
    private eGameModes gameMode;
    ArrayList<Player> players;
    ArrayList<Player>roundWinners;
    ArrayList<Player>gameWinners;
    private boolean allPlayersLeft = false;

    public boolean isAllPlayersLeft() {
        return allPlayersLeft;
    }

    public ArrayList<Player> getGameWinners() {
        return gameWinners;
    }

    public void setGameWinners(ArrayList<Player> gameWinners) {
        this.gameWinners = gameWinners;
    }

    public ArrayList<Player> getRoundWinners() {
        return roundWinners;
    }

    public void setRoundWinners(ArrayList<Player> roundWinners) {
        this.roundWinners = roundWinners;
    }
    int roundNumber = 1;
    Deck deck;
    private  Stack centerPile = new Stack();
    Player currentPlayer;
    private eGameSatus gameStatus = eGameSatus.NORMAL;

    public eGameSatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(eGameSatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Stack getCenterPile() {
        return centerPile;
    }

    public Deck getDeck() {
        return deck;
    }

    public eGameModes getGameMode() {
        return gameMode;
    }

    public iModelListener getModelListener() {
        return modelListener;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getRoundNumber() {
        return roundNumber;
    }
    
    public void nextPlayerTurn(Player playerToPlay)
    {
        if (playerToPlay != null)
        {
            currentPlayer = playerToPlay;
        }
        else
        {
            int currentPlayerIndex = players.indexOf(currentPlayer);
            currentPlayer = players.get((currentPlayerIndex + 1) % players.size());
        }
    }
        
      public void playComputerTurn()
      {
          Player.PlayerDecision playerDecision  = currentPlayer.Play(deck,centerPile);
            if (playerDecision.calledYaiv == true)
              {
                userCalledYaniv(currentPlayer);
              }
              else
              {
                  nextPlayerTurn(null);
                   //modelListener.ComputerPlayerEndedTurn(currentPlayer, playerDecision.cardSource);
              }
      }
        
//        if (currentPlayer.getPlayerType().equals(Player.ePlayerType.COMPUTER))
//        {
//           Player.PlayerDecision playerDecision  = currentPlayer.Play(deck,centerPile);
//              if (playerDecision.calledYaiv == true)
//              {
//                userCalledYaniv(currentPlayer);
//              }
//              else
//              {
//                  nextPlayerTurn(null);
//                   //modelListener.ComputerPlayerEndedTurn(currentPlayer, playerDecision.cardSource);
//              }
//        }
//        else
//        {
//           // modelListener.HumanPlayerTurn(currentPlayer);
//        }
        
    

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    
    public void InitializeGame(eGameModes gameMode, ArrayList<Player>players, Deck deck)
    {
        this.gameMode = gameMode;
        this.players = players;
        this.currentPlayer = players.get(0);
        
        if(deck != null)
        {
            this.deck = deck;
        }
        else
        {
            this.deck = new Deck();
            this.deck.InitializeDeck();
        }
        
        dealCardsToPlayers();
    }

    public void setModelListener(iModelListener modelListener)
    {
        this.modelListener = modelListener;
    }

    public GameState getGameState()
    {
        return new GameState(gameMode, players, roundNumber, currentPlayer, deck, centerPile);
    }

   private void dealCardsToPlayers() 
    {
        for (Player player : this.players)
        {
            for (int i = 0 ; i < 5 ; i++)
            {
                    player.getCards().add(deck.pullCardFromTop());
            }
            player.sortCards();
        }
        
        ArrayList<Card> topCardOfPile = new ArrayList<Card>();
        topCardOfPile.add(deck.pullCardFromTop());
        centerPile.add(topCardOfPile);
    }
   
   public Player getCurrentPlayer()
   {
       return currentPlayer;
   }
   
   public static boolean checkValidityOfChoice(ArrayList<Card> chosenCards) 
   {
        boolean result;
        if (chosenCards.size() == 1)
        {
            result = true;
        }
        else
        {
            result = checkForSeries(chosenCards);
            if (result == false)
            {
                if(chosenCards.size() >= 3)
                {
                      result = checkForStraights(chosenCards);
                }
                else
                {
                        result = false;
                }
            }
        }
        
      return result;
    }
  
   private static boolean checkForSeries(ArrayList<Card> chosenCards) 
   {
    boolean result = true;
    Collections.sort(chosenCards);
    int i = 0;
    while (chosenCards.get(i).getRank().equals(Card.eCardRank.JOKER))
    {
        i++;
        if (i == chosenCards.size())
        {
            return true;
        }
    }

    Card.eCardRank seriesRank = chosenCards.get(i).getRank();
    for (i = i ; i < chosenCards.size() ; i++)
    {
        if (!chosenCards.get(i).getRank().equals(seriesRank))
        {
            result = false;
        }
    }
    return result;
}

   private static boolean checkForStraights(ArrayList<Card> chosenCards)
   {
        boolean result = true;
       int lastRankIndex;
       int jokerCounter=0;
        Card.eCardSuit straightSuit;
        int cardsCounter = 0;
        int i = 0;
        
        while (chosenCards.get(i).getRank().equals(Card.eCardRank.JOKER))
        {
            jokerCounter++;
            i++;
        }
        
        lastRankIndex = chosenCards.get(i).getRank().getStraightIndex();
        straightSuit = chosenCards.get(i).getSuit();
        cardsCounter++;
        
        Card currentCard;
        for (i =i+1; i < chosenCards.size() ; i++)
        {
            currentCard = chosenCards.get(i);
            if (!currentCard.getSuit().equals(straightSuit))
            {
                result = false;
            }
            else
            {
                    while (jokerCounter>0 && currentCard.getRank().getStraightIndex() - lastRankIndex!=1 )
                    {
                        jokerCounter--;
                        lastRankIndex++;
                    }
                    
                    if (currentCard.getRank().getStraightIndex() - lastRankIndex!=1)
                    {
                        result = false;
                    }
                    else
                    {
                        lastRankIndex++;
                        cardsCounter++;
                    }
            }
        }
        
         while (jokerCounter>0 )
         {
             cardsCounter++;
             jokerCounter--;
         }
                
        if (cardsCounter < 3)
        {
            result = false;
        }
        
        return result;
    }
   
   public void userCalledYaniv(Player calledPlayer) 
   {
       calledPlayer.setPlayerCondition(Player.ePlayerCondition.YANIV);
        ArrayList<Player>winners = new ArrayList<Player>();
        winners.add(calledPlayer);
        
        for (Player player : players)
        {
            if (player.TotalHandValue() < winners.get(0).TotalHandValue())
            {
                player.setPlayerCondition(Player.ePlayerCondition.ASSAF);
                winners.clear();
                winners.add(player);
            }
            else if (player.TotalHandValue() == winners.get(0).TotalHandValue() && !(player.equals(calledPlayer)))
            {
                winners.add(player);
            }
        }
        
        for (Player player : players)
        {
            if (winners.contains(player) == false)
            {
                player.setScores(player.getScores() + player.TotalHandValue());
            }
            player.setEndOfRoundHandValue(player.TotalHandValue());
        }
        
        if (winners.contains(calledPlayer) == false)
        {
            calledPlayer.setScores(calledPlayer.getScores() + ASSAF_PENALTY );
        }
        
        roundNumber++;
        gameStatus = eGameSatus.ENDROUND;
        roundWinners = winners;
        //modelListener.RoundEnded(winners);
        
        boolean gameEnded = checkForGameEnd();
        if (gameEnded == true)
        {
          gameWinners = calculateGameWinners();
          gameStatus = eGameSatus.ENDGAME;
            // modelListener.GameEnded(GameWinners);
        }
    }
   
   public void prepareANewRound() 
   {
       roundNumber++;
       gameStatus = eGameSatus.NORMAL;
        for (Player player : players)
        {
            player.setPlayerCondition(Player.ePlayerCondition.NORMAL);
            player.setCards(new ArrayList<Card>());
        }
        
        deck = new Deck();
        deck.InitializeDeck();
        centerPile = new Stack();
        
        dealCardsToPlayers();
    }
   
   private boolean checkForGameEnd() 
   {
        boolean result = false;
        switch (gameMode)
        {
            case GAME_LIMIT:
                for (Player player : players)
                {
                    if (player.getScores() > eGameModes.GAME_LIMIT.value)
                    {
                        return true;
                    }
                }
                break;
            case SINGLE_LIMIT:
                ArrayList<Player>toBeOutOfGame = new ArrayList<Player>();
                for (Player player : players)
                {
                    if (player.getScores() > eGameModes.SINGLE_LIMIT.value)
                    {
                        toBeOutOfGame.add(player);
                    }
                }
                for (Player player : toBeOutOfGame)
                {
                    players.remove(player);
                }
                    if (players.size() == 1)
                    {
                        return true;
                    }
                break;
            case TURNS:
                if (roundNumber >= eGameModes.TURNS.value)
                {
                    return true;
                }
                break;
        }
       
        return result;
    }
   
   private ArrayList<Player> calculateGameWinners()
   {
        ArrayList<Player>winners = new ArrayList<Player>();
        winners.add(players.get(0));
        
        for (Player player : players)
        {
            if (player.getScores() < winners.get(0).getScores())
            {
                winners.clear();
                winners.add(player);
            }
            else if (player.getScores() == winners.get(0).getScores() && !(player.equals(players.get(0))))
            {
                winners.add(player);
            }
        }
        return winners;
      }
   
   public void reset()
   {
        roundNumber = 1;
        deck =null;
        centerPile = new Stack();
    }

 public void UpdateHumanPlayerAfterTurn(ArrayList<Card> chosenCardsToDump, Card cardToDrawFromPile)
   {
            centerPile.push(chosenCardsToDump);
            for(Card card : chosenCardsToDump)
            {
                ArrayList<Card> tempCards =  currentPlayer.getCards();
                tempCards.remove(card);
            }
            
            if (cardToDrawFromPile == null) //user chose to draw from DECK
            {
                currentPlayer.getCards().add(deck.pullCardFromTop());
            }
            else //user chose to draw from PILE
            {
                currentPlayer.getCards().add(cardToDrawFromPile);
                ((ArrayList<Card>)centerPile.peek()).remove(cardToDrawFromPile);
            }
   }
        
 public void PlayerLeaveGame(Player leavingPlayer)
 {
       int leavingPlayerIndex = players.indexOf(leavingPlayer);
       int nextPlayerIndex = (leavingPlayerIndex + 1) % players.size();
       currentPlayer = players.get(nextPlayerIndex);
       players.remove(leavingPlayer);
       
       
       
       
//        int leavingPlayerIndex = players.indexOf(player);
//    int previousPlayerIndex;
//    if (leavingPlayerIndex == 0)
//    {
//        previousPlayerIndex = players.size() - 1;
//    }
//    else
//    {
//        previousPlayerIndex = leavingPlayerIndex - 1;
//    }
//
//    setCurrentPlayer(players.get(previousPlayerIndex));
//    players.remove(player);
    if (players.size() == 1)
    {
        gameStatus = eGameSatus.ENDGAME;
        allPlayersLeft = true;
    }
//    else
//    {
//        YanivFrame.viewListener.HumanPlayerFinishTurn();
//    }
}                          
        
                 
}                

  



