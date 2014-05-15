/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yaniv.model;

import java.util.ArrayList;
import java.util.Stack;
import yaniv.model.GameModel.eGameModes;


/**This class is used to wrap the game state data
 *
 * @author Elad Hossy
 */
public class GameState 
{
    private eGameModes gameMode;
    ArrayList<Player> players;
    int roundNumber;
    Player currentPlayer;
    Deck deck;
    private  Stack centerPile = new Stack();
    
    public GameState(eGameModes gameMode, ArrayList<Player>players, int roundNumber, Player currentPlayer, Deck deck, Stack CenterPile)
    {
        this.gameMode = gameMode;
        this.players = players;
        this.roundNumber = roundNumber;
        this.currentPlayer = currentPlayer;
        this.deck = deck;
        this.centerPile = centerPile;
    }

    public Stack getCenterPile() {
        return centerPile;
    }

    public void setCenterPile(Stack centerPile) {
        this.centerPile = centerPile;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public eGameModes getGameMode() {
        return gameMode;
    }

    public void setGameMode(eGameModes gameMode) {
        this.gameMode = gameMode;
    }




    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }
}
