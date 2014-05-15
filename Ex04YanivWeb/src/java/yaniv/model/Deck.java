/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yaniv.model;

import java.util.ArrayList;
import java.util.Collections;
import yaniv.model.Card.eCardRank;
import yaniv.model.Card.eCardSuit;

/** Deck represents a deck of cards.
 *
 * @author Elad Hossy
 */
public class Deck 
{
     private ArrayList<Card> cards = new ArrayList<Card>();

     public Deck()
     {}
     
    public void InitializeDeck() 
    {
       Card.eCardSuit[] suits = Card.eCardSuit.values();
       Card.eCardRank[] ranks = Card.eCardRank.values();
       for (Card.eCardSuit suit : suits)
       {
           if (!suit.equals(Card.eCardSuit.JOKER) )
           {
                for (Card.eCardRank rank : ranks)
                {
                    if (!rank.equals(Card.eCardRank.JOKER))
                    {
                            cards.add(new Card(rank,suit));
                    }
                }
           }
       }
       cards.add(new Card(eCardRank.JOKER,eCardSuit.JOKER));
       cards.add(new Card(eCardRank.JOKER,eCardSuit.JOKER));
       
      Collections.shuffle(cards);
    }

    public Card pullCardFromTop()
    {
        Card card = cards.get(cards.size()-1);
        cards.remove(cards.size()-1);
        return card;
    }
    
    public ArrayList<Card> getDeck() {
        return cards;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.cards = deck;
    }
     
     public void AddCardToDeck(Card card)
     {
         cards.add(card);
     }

    void Shuffle() 
    {
        Collections.shuffle(cards);
    }
    
    void addCardToDeckBottom(Card card)
    {
        cards.add(0, card);
    }
}
