/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yaniv.controller;

import generated.Rank;
import generated.Suit;
import generated.Yaniv;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.util.ParameterMap;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import yaniv.model.Card;
import yaniv.model.Deck;
import yaniv.model.GameModel;
import yaniv.model.Player;

/**
 *
 * @author osus
 */
@WebServlet(name = "XMLinitializer", urlPatterns = {"/XMLinitializer"})
public class XMLinitializer extends HttpServlet {

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
            throws ServletException, IOException, FileUploadException, Exception {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            
            boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
            
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
             FileItem fileItem = null;
            try 
            {
                    List<FileItem> fields = upload.parseRequest(request);
                    Iterator<FileItem> it = fields.iterator();
                    if (!it.hasNext()) {
                        out.print("hi");
                    }
                   
                    while (it.hasNext()) {
                            fileItem = it.next();
                    }
                    String xmlString = fileItem.getString();
                  
                    if (xmlString.length() == 0)
                    {
                        response.sendRedirect("InitGame.htm");
                    }
                    else
                    {
                    InputStream xmlStream = fileItem.getInputStream();
                    Yaniv yaniv =  unMarhsalXml(xmlStream);
                    
                    GameModel.eGameModes   gameMode = getGameModeFromXML(yaniv);
                    Deck deck = getDeckFromXML(yaniv);
                    ArrayList<Player> players = getPlayersFromXML(yaniv);
                    ArrayList<Player> humanPlayers = new ArrayList<Player>();
                    
                     for (Player player : players)
                     {
                            if (player.getPlayerType().equals(Player.ePlayerType.HUMAN))
                            {
                                humanPlayers.add(player);
                            }
                    }
                    
                    
                    ParameterMap<String, Object> XMLgameParams = new ParameterMap<String, Object>();
                    XMLgameParams.put("players", players);
                    XMLgameParams.put("humanPlayers", humanPlayers);
                    XMLgameParams.put("gameMode", gameMode);
                    XMLgameParams.put("deck", deck);
                    getServletContext().setAttribute("XMLgameParams", XMLgameParams);

                    
                    getServletContext().getRequestDispatcher("/ResultOfXMLParser.jsp").forward(request, response);
          
                    }
            } 
            
            catch (FileUploadException e) 
            {
                    e.printStackTrace();
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(XMLinitializer.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(XMLinitializer.class.getName()).log(Level.SEVERE, null, ex);
        }
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


private Yaniv unMarhsalXml(InputStream xmlStream) throws Exception
{
        generated.Yaniv yaniv = new Yaniv();
        
       try {
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(yaniv.getClass().getPackage().getName());
            javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
            yaniv = (Yaniv) unmarshaller.unmarshal(xmlStream); //NOI18N
            
       } catch (javax.xml.bind.JAXBException ex) {
          throw new Exception(ex);
        }
        
        return yaniv;
    }

    private GameModel.eGameModes getGameModeFromXML(Yaniv yaniv) 
    {
        GameModel.eGameModes gameMode = null;
        generated.GameEndType gameEndType = yaniv.getGameEndType();
        switch (gameEndType)
        {
            case FIRST_PLAYER_LIMIT:
                gameMode = GameModel.eGameModes.GAME_LIMIT;
                break;
            case SINGLE_PLAYER_LIMIT:
                gameMode = GameModel.eGameModes.SINGLE_LIMIT;
                break;
            case TURNS:
                gameMode = GameModel.eGameModes.TURNS;
                    break;
        }
        gameMode.setValue(yaniv.getGameEndValue());
       
        return gameMode;
    }

    private ArrayList<Player> getPlayersFromXML(Yaniv yaniv) 
    {
        Player.ePlayerType playerType = null;
        String playerName;
        ArrayList<Player> players = new ArrayList<Player>();
        List<generated.Player> filePlayers =  yaniv.getPlayer();
        for (generated.Player player : filePlayers)
        {
            playerName = player.getName();
            switch(player.getType())
            {
                case COMPUTER:
                    playerType = Player.ePlayerType.COMPUTER;
                    break;
                case HUMAN:
                    playerType = Player.ePlayerType.HUMAN;
                    break;
            }
            players.add(new Player(playerType,playerName));
            
        }
        return players;
    }

    private Deck getDeckFromXML(Yaniv genYaniv)
    {
       yaniv.model.Deck deck = new Deck();
       yaniv.model.Card.eCardRank cardRank = null;
       yaniv.model.Card.eCardSuit cardSuit = null;
       List<generated.Card> listOFCards = genYaniv.getDeck().getCard();
       for (generated.Card card : listOFCards)
       {
           if (card.isJoker() == true)
           {
               cardRank = yaniv.model.Card.eCardRank.JOKER;
               cardSuit = yaniv.model.Card.eCardSuit.JOKER;
           }
           else
           {
               cardRank = translateGenRank(card.getRank());
               cardSuit = translateGenSuit(card.getSuit());
           }
           
          deck.AddCardToDeck(new yaniv.model.Card(cardRank, cardSuit));
           
       }
       for (yaniv.model.Card card : deck.getDeck())
       {
           System.out.println(card.getRank() + "  " + card.getSuit());
       }
       
       return deck;
    }

    private Card.eCardRank translateGenRank(Rank rank) 
    {
        Card.eCardRank cardRank = null;
        switch(rank)
                {
                    case ACE:
                        cardRank = yaniv.model.Card.eCardRank.ACE;
                        break;
                    case DEUCE:
                        cardRank = yaniv.model.Card.eCardRank.DEUCE;
                        break;
                    case THREE:
                        cardRank = yaniv.model.Card.eCardRank.THREE;
                        break;
                    case FOUR:
                        cardRank = yaniv.model.Card.eCardRank.FOUR;
                        break;
                    case FIVE:
                        cardRank = yaniv.model.Card.eCardRank.FIVE;
                        break;
                    case SIX:
                        cardRank = yaniv.model.Card.eCardRank.SIX;
                        break;
                    case SEVEN:
                        cardRank = yaniv.model.Card.eCardRank.SEVEN;
                        break;
                    case EIGHT:
                        cardRank = yaniv.model.Card.eCardRank.EIGHT;
                        break;
                    case  NINE:
                        cardRank = yaniv.model.Card.eCardRank.NINE;
                        break;
                    case TEN:
                        cardRank = yaniv.model.Card.eCardRank.TEN;
                        break;
                    case JACK:
                        cardRank = yaniv.model.Card.eCardRank.JACK;
                        break;
                    case QUEEN:
                        cardRank = yaniv.model.Card.eCardRank.QUEEN;
                        break;
                    case KING:
                        cardRank = yaniv.model.Card.eCardRank.KING;
                        break;
                }
        return cardRank;
    }

    private Card.eCardSuit translateGenSuit(Suit suit)
    {
        Card.eCardSuit cardSuit = null;
        switch (suit)
                {
                    case CLUBS:
                        cardSuit = yaniv.model.Card.eCardSuit.CLUBS;
                        break;
                    case DIAMONDS:
                        cardSuit = yaniv.model.Card.eCardSuit.DIAMONDS;
                        break;
                    case HEARTS:
                        cardSuit = yaniv.model.Card.eCardSuit.HEARTS;
                        break;
                        case SPADES:
                            cardSuit = yaniv.model.Card.eCardSuit.SPADES;
                            break;
                }
        return cardSuit;
    }
    
}
