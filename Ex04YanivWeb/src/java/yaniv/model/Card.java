/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yaniv.model;

/** Card class is used to represent a game card of Yaniv.
 *  it implements Comparable in order to be compared according to index in a straigt.
 *
 * @author Elad Hossy
 */
public class Card implements Comparable
{
     public enum eCardRank
    {
        
        ACE  (1, "A", 1), DEUCE (2, "2", 2),THREE (3, "3", 3), FOUR (4, "4", 4), FIVE (5, "5", 5), SIX (6, "6", 6), SEVEN (7, "7", 7), EIGHT (8, "8", 8), NINE (9, "9", 9), TEN (10, "10", 10), JACK (10, "J", 11), QUEEN (10, "Q", 12), KING (10, "K", 13), JOKER (0, "", 0);
        
        /**  value is used to determine the score value of each rank */
        private final int value;
        
        /** the straightIndex is the location of the rank in a straight */
        private final int straightIndex;
        
        /** used for a short nick */
        private final String shortName;

        public int getStraightIndex() {
            return straightIndex;
        }

        public int getValue() {
            return value;
        }
        private eCardRank(int value, String shortName,int straightIndex)
        {
            this.value = value;
            this.shortName = shortName;        
            this.straightIndex = straightIndex;

        }

        public String getShortName() 
        {
            return shortName;
        }
    }
    
    public enum eCardSuit
    {
        CLUBS ("\u2663"), DIAMONDS ("\u2666"), HEARTS ("\u2665"), SPADES ("\u2660"), JOKER ("\u263B");
        
        private String uniCode;
        
        private eCardSuit(String uniCode)
        {
            this.uniCode = uniCode;
            
        }

        public String getUniCode() {
            return uniCode;
        }
        
        
    }
    
    private eCardRank rank;
    private eCardSuit suit;

    @Override
    public int compareTo(Object o) 
    {
        int myStraightIndex = rank.getStraightIndex();
        int otherCardStraightIndex = ((Card)o).getRank().getStraightIndex();
        return myStraightIndex - otherCardStraightIndex;
        
    }
    
    public Card(eCardRank rank, eCardSuit suit)
    {
        this.rank = rank;
        this.suit = suit;
    }

    public eCardRank getRank() {
        return rank;
    }

    public eCardSuit getSuit() {
        return suit;
    }
   
}
