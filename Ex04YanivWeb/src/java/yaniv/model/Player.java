/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yaniv.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import yaniv.model.GameModel.eCardSource;

/**This Class represent a player in the game
 *
 * @author Elad Hossy
 */
public class Player 
{
     public enum ePlayerType 
    {
        HUMAN, COMPUTER
    }
     
     public enum ePlayerCondition
     {
         YANIV, ASSAF, NORMAL
     }


    private ePlayerType playerType;
    private String  name;
    private int scores;
    private ArrayList<Card> cards;
    private ArrayList<Card> ChosenCards;
    boolean roundWinner = false;
    boolean gotAssafed = false;
    private boolean hasRetired = false;
    private int endOfRoundHandValue;
    private ePlayerCondition playerCondition = ePlayerCondition.NORMAL;

    public ePlayerCondition getPlayerCondition() {
        return playerCondition;
    }

    public void setPlayerCondition(ePlayerCondition playerCondition) {
        this.playerCondition = playerCondition;
    }

    public int getEndOfRoundHandValue() {
        return endOfRoundHandValue;
    }

    public void setEndOfRoundHandValue(int endOfRoundHandValue) {
        this.endOfRoundHandValue = endOfRoundHandValue;
    }

    public boolean isHasRetired() {
        return hasRetired;
    }

    public void setHasRetired(boolean hasRetired) {
        this.hasRetired = hasRetired;
    }
    ArrayList<Card> testGroupOfCards = new ArrayList<Card>();

    /** this nested class is used to wrap a decision of a player, regarding from where
     * to take a new card (cardSource) and whether he decided to call yaniv
     */
    public class PlayerDecision
    {
        eCardSource cardSource;
        boolean calledYaiv;
        public PlayerDecision(eCardSource cardSource, boolean calledYaiv)
        {
            this.cardSource = cardSource;
            this.calledYaiv = calledYaiv;
        }

        public boolean isCalledYaiv() {
            return calledYaiv;
        }

        public eCardSource getCardSource() {
            return cardSource;
        }
    }

    public boolean isGotAssafed() {
        return gotAssafed;
    }

    public void setGotAssafed(boolean gotAssafed) {
        this.gotAssafed = gotAssafed;
    }

    public boolean isRoundWinner() {
        return roundWinner;
    }

    public void setRoundWinner(boolean roundWinner) {
        this.roundWinner = roundWinner;
    }

    public ArrayList<Card> getChosenCards() {
        return ChosenCards;
    }
    
    /** calculate the sum of the values of all the cards the player is holding */
    public int TotalHandValue()
    {
        int sum = 0;
        for (Card card : cards)
        {
            sum = sum +  card.getRank().getValue();
        }
        
        return sum;
    }

    public void setChosenCards(ArrayList<Card> ChosenCards) {
        this.ChosenCards = ChosenCards;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
    
    public Player(ePlayerType playerType, String name) 
    {
        this.playerType = playerType;
        this.name = name;
        this.cards = new ArrayList<Card>();
         this.ChosenCards = new ArrayList<Card>();
    }

 public void sortCards()
 {
     Collections.sort(cards);
 }
    
 /** all the A.I of the computer player is implemented here */
    PlayerDecision  Play(Deck deck, Stack pile) 
    {
        if (TotalHandValue() <= 7 )
        {
            return new PlayerDecision(eCardSource.DECK, true);
        }
        ArrayList<Card> topCardsOfPile = (ArrayList<Card>)pile.peek();
        ArrayList<Card> CardsToDump;
        Card cardToTake;
        
        /** find series (cards of the same rank), to dump */
        CardsToDump = findSeries(cards);
        if (CardsToDump.size() == 0)
        {
            /** find straights (cards in an up-going order, and the same suit) */
              CardsToDump = findStraights(cards);
        }
        
        /** if no series or straight to be found, dump the highest value card */
        if (CardsToDump.size() == 0)
        {
            CardsToDump = findHighestValueCard();
        }
        
        /** after choosing which cards to dump, now choose from where to take a new card */
        eCardSource cardSource = chooseCardSource(topCardsOfPile);
        
     /** remove the cards from the hand */   
    for (Card card : CardsToDump)
    {
        cards.remove(card);
    }
        this.ChosenCards = CardsToDump;
        
        /** move the cards to the top of the pile, and take a new card */
        Player.DumpAndTakeCards(this, cardSource, deck, pile);
        return new PlayerDecision(cardSource,false);
    }

    /** algorithm to find series */
    private ArrayList<Card> findSeries(ArrayList<Card> groupOfCards) 
    {
        ArrayList<Card> foundSeries = new ArrayList<Card>();
        for (Card card : groupOfCards)
        {
            foundSeries.add(card);
            for (Card matchCard : groupOfCards)
            {
                if (card.getRank().equals(matchCard.getRank()) && matchCard != card)
                {
                    foundSeries.add(matchCard);
                }
            }
            if (foundSeries.size() == 1)
            {
                foundSeries.clear();
            }
            else
            {
                break;
            }
        }
        return foundSeries;
        
    
    }

    /** algorithm to find straigt */
    private ArrayList<Card> findStraights(ArrayList<Card> groupOfCards) 
    {
        int straightCounter;
        ArrayList<Card> straightFound = new ArrayList<Card>();
        for (int i = 0 ; i < groupOfCards.size() ; i++)
        {
            straightFound.add(groupOfCards.get(i));
            for (int j = i + 1 ; j < groupOfCards.size() ; j++)
            {
                if (groupOfCards.get(j).getSuit().equals(groupOfCards.get(i).getSuit()))
                {
                    if (groupOfCards.get(i).getRank().getStraightIndex() +j == groupOfCards.get(j).getRank().getStraightIndex())
                    {
                        straightFound.add(groupOfCards.get(j));
                    }
                }
            }
            if (straightFound.size() >=3)
            {
                return straightFound;
            }
            else
            {
                straightFound.clear();
            }
        }
        return straightFound;
    }
        
/** algorithm to find the higest value card */                
    private ArrayList<Card> findHighestValueCard() 
    {
        ArrayList<Card> highestCard = new ArrayList<Card>();
        int maxValue = 0;
        for (Card card : cards)
        {
            if (card.getRank().getValue() > maxValue)
            {
                maxValue = card.getRank().getValue();
                highestCard.clear();
                highestCard.add(card);
            }
        }
        
        return highestCard;
    
    }

    /** wisely choose the card to pick, according to what the player is holding */
    private eCardSource chooseCardSource(ArrayList<Card> topCardsOfPile) 
    {
        eCardSource cardSource = eCardSource.DECK;
        if (topCardsOfPile.get(0).getRank().equals(Card.eCardRank.JOKER))
        {
            cardSource = eCardSource.PILE_FIRST;
        }
        else if (topCardsOfPile.get(topCardsOfPile.size()-1).getRank().equals(Card.eCardRank.JOKER))
        {
            cardSource = eCardSource.PILE_LAST;
        }
        else
        {
                Card firstCardOnTopOfPile = topCardsOfPile.get(0);
                Card lastCardOnTopOfPile = topCardsOfPile.get(topCardsOfPile.size()-1);

                //Check for complition of a Series
                for (Card card : cards)
                {
                    if (firstCardOnTopOfPile.getRank().equals(card.getRank()))
                    {
                        cardSource = eCardSource.PILE_FIRST;
                        break;
                    }
                    else if (lastCardOnTopOfPile.getRank().equals(card.getRank()))
                    {
                        cardSource = eCardSource.PILE_LAST;
                        break;
                    }

                }

                /** If pile card not complete a series, check for straight */
                ArrayList<Card> tempStraightFound;
                for (Card pileCard : topCardsOfPile)
                {
                    testGroupOfCards.clear();
                    testGroupOfCards = (ArrayList<Card>)cards.clone();
                    testGroupOfCards.add(pileCard);
                    tempStraightFound = findStraights(testGroupOfCards);
                    if (tempStraightFound.size() > 0)
                    {
                        if (pileCard == topCardsOfPile.get(0))
                        {
                            cardSource = eCardSource.PILE_FIRST;
                        }
                        else
                        {
                            cardSource = eCardSource.PILE_LAST;
                        }
                    }
                }
        }
        return cardSource;

    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ePlayerType getPlayerType() 
    {
        return playerType;
    }

    public void setPlayerType(ePlayerType playerType) 
    {
        this.playerType = playerType;
    }

    public int getScores()
    {
        return scores;
    }

    public void setScores(int scores) 
    {
        this.scores = scores;
    }

    
public static void DumpAndTakeCards(Player player, eCardSource cardSource, Deck deck, Stack pile)
{

       ArrayList<Card> topCardsOfPile = (ArrayList<Card>)(pile.peek());
       switch (cardSource)
       {
           case DECK:
               player.getCards().add(deck.pullCardFromTop());
               break;
           case PILE_FIRST:
               player.getCards().add(topCardsOfPile.get(0));
               topCardsOfPile.remove(0);
               break;
           case PILE_LAST:
               player.getCards().add(topCardsOfPile.get(topCardsOfPile.size()-1));
               topCardsOfPile.remove(topCardsOfPile.size()-1);
               break;
       }
       pile.push(player.getChosenCards());
      player.setChosenCards(new ArrayList<Card>());
        
}

    public static void DumpCards(Player player, Stack pile)
    {
        pile.push(player.getChosenCards());
        player.setChosenCards( new ArrayList<Card>());

    }
}
