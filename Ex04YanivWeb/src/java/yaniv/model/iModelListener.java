/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yaniv.model;

import java.util.ArrayList;

/** This interface is used by the Model to notify its observer (the View) of 
 *  changes in the game.
 *
 * @author Elad Hossy
 */
public interface iModelListener
{
    public void HumanPlayerTurn(Player humanPlayer);

    public void AssafOccured();

    public void RoundEnded(ArrayList<Player> winners);

    public void GameEnded(ArrayList<Player> gameWinners);
    
}
