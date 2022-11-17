package game;

import card.CardInterface;
import card.heros.Hero;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.Input;
import game.table.Table;

import java.util.ArrayList;
import card.CardFactory;
import player.Player;

public class Game {

    private Table table;
    private ArrayList<ActionsInput> commands;
    private card.heros.Hero playerOneHero;
    private card.heros.Hero playerTwoHero;
    private int currentPlayer;
    private Player playerOne;
    private Player playerTwo;

    public Game(Input inputData) {
        CardFactory factory = new CardFactory();
        table = new Table();
        this.commands = new ArrayList<ActionsInput>(inputData.getGames().get(0).getActions());
        this.playerOneHero = (Hero) factory.getCard(inputData.getGames().get(0).getStartGame().getPlayerOneHero());
        this.playerTwoHero = (Hero) factory.getCard(inputData.getGames().get(0).getStartGame().getPlayerTwoHero());
        currentPlayer = inputData.getGames().get(0).getStartGame().getStartingPlayer();

        playerOne = new Player(1, 1, playerOneHero, inputData.getPlayerOneDecks().getDecks().get(inputData.getGames().get(0).getStartGame().getPlayerOneDeckIdx()), inputData.getGames().get(0).getStartGame().getShuffleSeed());
        playerTwo = new Player(2, 1, playerTwoHero, inputData.getPlayerTwoDecks().getDecks().get(inputData.getGames().get(0).getStartGame().getPlayerTwoDeckIdx()), inputData.getGames().get(0).getStartGame().getShuffleSeed());
    }

    private Player getPlayerDeck(int index){
        if(index == 1){
            return playerOne;
        }
        return playerTwo;
    }


    public void play(ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        for (ActionsInput command : commands) {
           switch(command.getCommand()){
               case "getPlayerDeck":
                   ObjectNode getPlayerDeck = objectMapper.createObjectNode();
                   getPlayerDeck.put("command", command.getCommand());
                   getPlayerDeck.put("playerIdx", command.getPlayerIdx());
                   getPlayerDeck.put("output", getPlayerDeck(command.getPlayerIdx()).getJsonDeck());
                   output.add(getPlayerDeck);
                   break;
               case "getPlayerHero":
                     ObjectNode getPlayerHero = objectMapper.createObjectNode();
                     getPlayerHero.put("command", command.getCommand());
                     getPlayerHero.put("playerIdx", command.getPlayerIdx());
                     if(command.getPlayerIdx() == 1)
                         getPlayerHero.put("output", playerOne.getHero().getJson());
                     else
                         getPlayerHero.put("output", playerTwo.getHero().getJson());
                     output.add(getPlayerHero);
                     break;
               case "getPlayerTurn":
                   ObjectNode getPlayerTurn = objectMapper.createObjectNode();
                   getPlayerTurn.put("command", command.getCommand());
                   getPlayerTurn.put("output", currentPlayer);
                   output.add(getPlayerTurn);
                   break;
               default:
                   break;
           }
        }
    }

}
