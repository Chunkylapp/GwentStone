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

    private Player getPlayer(int index) {
        if (index == 1) {
            return playerOne;
        }
        return playerTwo;
    }


    public void play(ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        for (ActionsInput command : commands) {
            switch (command.getCommand()) {
                case "getPlayerDeck":
                    ObjectNode getPlayerDeck = objectMapper.createObjectNode();
                    getPlayerDeck.put("command", command.getCommand());
                    getPlayerDeck.put("playerIdx", command.getPlayerIdx());
                    getPlayerDeck.put("output", getPlayer(command.getPlayerIdx()).getJsonDeck());
                    output.add(getPlayerDeck);
                    break;
                case "getPlayerHero":
                    ObjectNode getPlayerHero = objectMapper.createObjectNode();
                    getPlayerHero.put("command", command.getCommand());
                    getPlayerHero.put("playerIdx", command.getPlayerIdx());
                    if (command.getPlayerIdx() == 1)
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
                case "endPlayerTurn":
                    if (currentPlayer == 1) {
                        currentPlayer = 2;
                        playerOne.drawCard();
                    }
                    else {
                        currentPlayer = 1;
                        playerTwo.drawCard();
                    }
                    break;
                case "getCardsInHand":
                    ObjectNode getCardsInHand = objectMapper.createObjectNode();
                    getCardsInHand.put("command", command.getCommand());
                    getCardsInHand.put("playerIdx", command.getPlayerIdx());
                    getCardsInHand.put("output", getPlayer(command.getPlayerIdx()).getJsonHand());
                    output.add(getCardsInHand);
                    break;
                case "placeCard":
                    table.placeCard(getPlayer(command.getPlayerIdx()).getHand().get(command.getHandIdx()), command.getX(), command.getY());
                    break;
                case "getCardsOnTable":
                    break;
                case "getPlayerMana":
                    ObjectNode getPlayerMana = objectMapper.createObjectNode();
                    getPlayerMana.put("command", command.getCommand());
                    getPlayerMana.put("playerIdx", command.getPlayerIdx());
                    getPlayerMana.put("output", getPlayer(command.getPlayerIdx()).getMana());
                    output.add(getPlayerMana);
                    break;


                default:
                    break;
            }
        }
    }

}
