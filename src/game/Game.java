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
    private int eOfRoundPlayer;
    private int rounds;
    private Player playerOne;
    private Player playerTwo;

    public Game(Input inputData) {
        CardFactory factory = new CardFactory();
        table = new Table();
        this.commands = new ArrayList<ActionsInput>(inputData.getGames().get(0).getActions());
        this.playerOneHero = (Hero) factory.getCard(inputData.getGames().get(0).getStartGame().getPlayerOneHero());
        this.playerTwoHero = (Hero) factory.getCard(inputData.getGames().get(0).getStartGame().getPlayerTwoHero());
        currentPlayer = inputData.getGames().get(0).getStartGame().getStartingPlayer();
        if (currentPlayer == 1) {
            eOfRoundPlayer = 2;
        } else {
            eOfRoundPlayer = 1;
        }
        rounds = 1;
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
                    if (eOfRoundPlayer == currentPlayer) {
                        rounds++;
                        if (playerOne.getMana() < 10)
                            playerOne.setMana(playerOne.getMana() + rounds);
                        if (playerTwo.getMana() < 10)
                            playerTwo.setMana(playerTwo.getMana() + rounds);
                    }
                    if (currentPlayer == 1) {
                        currentPlayer = 2;
                        playerOne.drawCard();
                    } else {
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
                    CardInterface card = getPlayer(currentPlayer).getHand().get(command.getHandIdx());
                    if (card != null) {
                        // output diferit pentru fiecare eroare ffs
                        // de bagat absolut toate comenzile intr-un command handler cu metode diferite
                        if (!card.isEnvironment()) {
                            if ((card.getMana() <= getPlayer(currentPlayer).getMana())) {
                                if (table.placeCard(card, currentPlayer)) {
                                    getPlayer(currentPlayer).setMana(getPlayer(currentPlayer).getMana() - card.getMana());
                                    getPlayer(currentPlayer).getHand().remove(command.getHandIdx());
                                } else {
                                    ObjectNode placeCard = objectMapper.createObjectNode();
                                    placeCard.put("command", command.getCommand());
                                    placeCard.put("handIdx", command.getHandIdx());
                                    placeCard.put("error", "Cannot place card on table since row is full.");
                                    output.add(placeCard);
                                    break;
                                }
                                //getPlayer(command.getPlayerIdx()).getHand().remove(command.getHandIdx());
                            } else {
                                ObjectNode placeCard = objectMapper.createObjectNode();
                                placeCard.put("command", command.getCommand());
                                placeCard.put("handIdx", command.getHandIdx());
                                placeCard.put("error", "Not enough mana to place card on table.");
                                output.add(placeCard);
                                break;
                            }
                        } else {
                            ObjectNode placeCard = objectMapper.createObjectNode();
                            placeCard.put("command", command.getCommand());
                            placeCard.put("handIdx", command.getHandIdx());
                            placeCard.put("error", "Cannot place environment card on table.");
                            output.add(placeCard);
                            break;
                        }
                    } else {
                        System.out.println("Card is null");
                    }
                    break;
                case "getCardsOnTable":
                    ObjectNode getCardsOnTable = objectMapper.createObjectNode();
                    getCardsOnTable.put("command", command.getCommand());
                    // put json array of cards on table
                    getCardsOnTable.put("output", table.getJson());
                    output.add(getCardsOnTable);
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
