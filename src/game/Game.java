package game;

import card.CardInterface;
import card.heros.Hero;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;
import game.table.Table;

import java.util.ArrayList;

import card.CardFactory;
import player.Player;

/**
 * This class is the main class of the game. It contains the main method and
 * the game logic.
 */
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

    private static int playerOneWins = 0;
    private static int playerTwoWins = 0;

    /**
     * Constructor for the game class.
     *
     * @param rawGame   the raw input for the game
     * @param inputData all raw input data
     */
    public Game(final GameInput rawGame, final Input inputData) {
        CardFactory factory = new CardFactory();
        table = new Table();
        this.commands = new ArrayList<ActionsInput>(rawGame.getActions());
        this.playerOneHero = (Hero) factory.getCard(rawGame.getStartGame().getPlayerOneHero());
        this.playerTwoHero = (Hero) factory.getCard(rawGame.getStartGame().getPlayerTwoHero());
        currentPlayer = rawGame.getStartGame().getStartingPlayer();
        if (currentPlayer == 1) {
            eOfRoundPlayer = 2;
        } else {
            eOfRoundPlayer = 1;
        }
        rounds = 1;
        playerOne = new Player(1, playerOneHero,
                inputData.getPlayerOneDecks().
                        getDecks().get(rawGame.getStartGame().getPlayerOneDeckIdx()),
                rawGame.getStartGame().getShuffleSeed());
        playerTwo = new Player(1, playerTwoHero,
                inputData.getPlayerTwoDecks().
                        getDecks().get(rawGame.getStartGame().getPlayerTwoDeckIdx()),
                rawGame.getStartGame().getShuffleSeed());
    }

    /**
     * @param index index of the player
     * @return Current player of the game by id
     */
    private Player getPlayer(final int index) {
        if (index == 1) {
            return playerOne;
        }
        return playerTwo;
    }


    /**
     * Method runs an instance of a game held in the Game object
     *
     * @param output the output of the game as a JSON object
     */
    public void play(final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        for (ActionsInput command : commands) {
            switch (command.getCommand()) {
                case "getPlayerDeck" -> {
                    getPlayerDeck(output, objectMapper, command);
                }
                case "getPlayerHero" -> {
                    getPlayerHero(output, objectMapper, command);
                }
                case "getPlayerTurn" -> {
                    getPlayerTurn(output, objectMapper, command);
                }
                case "endPlayerTurn" -> {
                    endPlayerTurn();
                }
                case "getCardsInHand" -> {
                    getCardsInHand(output, objectMapper, command);
                }
                case "placeCard" -> {
                    placeCard(output, objectMapper, command);
                }
                case "getCardsOnTable" -> {
                    getCardsOnTable(output, objectMapper, command);
                }
                case "getPlayerMana" -> {
                    getPlayerMana(output, objectMapper, command);
                }
                case "cardUsesAttack" -> {
                    cardUsesAttack(output, objectMapper, command);
                }
                case "useEnvironmentCard" -> {
                    useEnvironmentCard(output, objectMapper, command);
                }
                case "getCardAtPosition" -> {
                    getCardAtPosition(output, objectMapper, command);

                }
                case "getEnvironmentCardsInHand" -> {
                    getEnvironmentCardsInHand(output, objectMapper, command);
                }
                case "getFrozenCardsOnTable" -> {
                    getFrozenCardsOnTable(output, objectMapper, command);
                }
                case "cardUsesAbility" -> {
                    cardUsesAbility(output, objectMapper, command);
                }
                case "useAttackHero" -> {
                    useAttackHero(output, objectMapper, command);
                }
                case "useHeroAbility" -> {
                    useHeroAbility(output, objectMapper, command);
                }
                case "getPlayerOneWins" -> {
                    ObjectNode getPlayerOneWins = objectMapper.createObjectNode();
                    getPlayerOneWins.put("command", command.getCommand());
                    getPlayerOneWins.put("output", playerOneWins);
                    output.add(getPlayerOneWins);
                }
                case "getPlayerTwoWins" -> {
                    ObjectNode getPlayerTwoWins = objectMapper.createObjectNode();
                    getPlayerTwoWins.put("command", command.getCommand());
                    getPlayerTwoWins.put("output", playerTwoWins);
                    output.add(getPlayerTwoWins);
                }
                case "getTotalGamesPlayed" -> {
                    ObjectNode getTotalGamesPlayed = objectMapper.createObjectNode();
                    getTotalGamesPlayed.put("command", command.getCommand());
                    getTotalGamesPlayed.put("output", playerOneWins + playerTwoWins);
                    output.add(getTotalGamesPlayed);
                }
                default -> {
                }
            }
        }
    }

    /**
     * @param output       Output object used by the Jackson library to create the JSON output
     *                     for the client.
     * @param objectMapper Object mapper used by the Jackson library to create the JSON output
     *                     for the client.
     * @param command      Command object used to parse the data.
     * @return the playerOne
     */
    private void getFrozenCardsOnTable(final ArrayNode output, final ObjectMapper objectMapper,
                                       final ActionsInput command) {
        ArrayList<CardInterface> frozenCards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                CardInterface card = table.getCard(i, j);
                if (card != null) {
                    if (card.isFrozen()) {
                        frozenCards.add(card);
                    }
                }
            }
        }
        ObjectNode frozenCardsOnTable = objectMapper.createObjectNode();
        frozenCardsOnTable.put("command", command.getCommand());
        ArrayNode frozenCardsArray = objectMapper.createArrayNode();
        for (CardInterface renameCardFrozen : frozenCards) {
            frozenCardsArray.add(renameCardFrozen.getJson());
        }
        frozenCardsOnTable.put("output", frozenCardsArray);
        output.add(frozenCardsOnTable);
    }

    /**
     * @param output       Output object used by the Jackson library to create the JSON output
     *                     for the client.
     * @param objectMapper Object mapper used by the Jackson library to create the JSON output
     *                     for the client.
     * @param command      Command object used to parse the data.
     * @return the playerOne
     */
    private void getEnvironmentCardsInHand(final ArrayNode output, final ObjectMapper objectMapper,
                                           final ActionsInput command) {
        ArrayList<CardInterface> envCards = new ArrayList<>();
        for (CardInterface renameCardEnv : getPlayer(currentPlayer).getHand()) {
            if (renameCardEnv.isEnvironment()) {
                envCards.add(renameCardEnv);
            }
        }
        ObjectNode envCardsInHand = objectMapper.createObjectNode();
        envCardsInHand.put("command", command.getCommand());
        ArrayNode envCardsArray = objectMapper.createArrayNode();
        for (CardInterface renameCardEnv : envCards) {
            envCardsArray.add(renameCardEnv.getJson());
        }
        envCardsInHand.put("output", envCardsArray);
        envCardsInHand.put("playerIdx", command.getPlayerIdx());
        output.add(envCardsInHand);
    }

    /**
     * @param output       Output object used by the Jackson library to create the JSON output
     *                     for the client.
     * @param objectMapper Object mapper used by the Jackson library to create the JSON output
     *                     for the client.
     * @param command      Command object used to parse the data.
     * @return the playerOne
     */
    private void getCardAtPosition(final ArrayNode output, final ObjectMapper objectMapper,
                                   final ActionsInput command) {
        CardInterface cardAtPos = table.getCard(command.getX(), command.getY());
        if (cardAtPos == null) {
            ObjectNode cardAtPosition = objectMapper.createObjectNode();
            cardAtPosition.put("command", command.getCommand());
            cardAtPosition.put("output", "No card available at that position.");
            cardAtPosition.put("x", command.getX());
            cardAtPosition.put("y", command.getY());
            output.add(cardAtPosition);
        } else {
            ObjectNode cardAtPosition = objectMapper.createObjectNode();
            cardAtPosition.put("command", command.getCommand());
            cardAtPosition.put("output", cardAtPos.getJson());
            cardAtPosition.put("x", command.getX());
            cardAtPosition.put("y", command.getY());
            output.add(cardAtPosition);
        }
    }

    /**
     * @param output       Output object used by the Jackson library to create the JSON output
     *                     for the client.
     * @param objectMapper Object mapper used by the Jackson library to create the JSON output
     *                     for the client.
     * @param command      Command object used to parse the data.
     * @return the playerOne
     */
    private void useEnvironmentCard(final ArrayNode output, final ObjectMapper objectMapper,
                                    final ActionsInput command) {
        // check if the card is on the table
        if (getPlayer(currentPlayer).getHand().size() < 1) {
            return;
        }
        CardInterface envCard = getPlayer(currentPlayer).getHand().get(command.getHandIdx());
        if (!envCard.isEnvironment()) {
            ObjectNode cardUsesAttack = objectMapper.createObjectNode();
            cardUsesAttack.put("affectedRow", command.getAffectedRow());
            cardUsesAttack.put("command", command.getCommand());
            cardUsesAttack.put("error", "Chosen card is not of type environment.");
            cardUsesAttack.put("handIdx", command.getHandIdx());
            output.add(cardUsesAttack);
        } else {
            if (envCard.getMana() <= getPlayer(currentPlayer).getMana()) {
                if ((currentPlayer == 1 && (command.getAffectedRow() == 2
                        || command.getAffectedRow() == 3))
                        || (currentPlayer == 2 && (command.getAffectedRow() == 0
                                || command.getAffectedRow() == 1))) {
                    ObjectNode cardUsesAttack = objectMapper.createObjectNode();
                    cardUsesAttack.put("affectedRow", command.getAffectedRow());
                    cardUsesAttack.put("command", command.getCommand());
                    cardUsesAttack.put("error", "Chosen row does not belong to the enemy.");
                    cardUsesAttack.put("handIdx", command.getHandIdx());
                    output.add(cardUsesAttack);
                } else {
                    if (envCard.useEffect(table, currentPlayer, command.getAffectedRow())) {
                        getPlayer(currentPlayer).setMana(getPlayer(currentPlayer).getMana()
                                - envCard.getMana());
                        getPlayer(currentPlayer).getHand().remove(command.getHandIdx());
                    } else {
                        ObjectNode cardUsesAttack = objectMapper.createObjectNode();
                        cardUsesAttack.put("affectedRow", command.getAffectedRow());
                        cardUsesAttack.put("command", command.getCommand());
                        cardUsesAttack.put("error",
                                "Cannot steal enemy card since the player's row is full.");
                        cardUsesAttack.put("handIdx", command.getHandIdx());
                        output.add(cardUsesAttack);
                    }
                }
            } else {
                ObjectNode cardUsesAttack = objectMapper.createObjectNode();
                cardUsesAttack.put("affectedRow", command.getAffectedRow());
                cardUsesAttack.put("command", command.getCommand());
                cardUsesAttack.put("error", "Not enough mana to use environment card.");
                cardUsesAttack.put("handIdx", command.getHandIdx());
                output.add(cardUsesAttack);
            }
        }
    }

    /**
     * @param output       Output object used by the Jackson library to create the JSON output
     *                     for the client.
     * @param objectMapper Object mapper used by the Jackson library to create the JSON output
     *                     for the client.
     * @param command      Command object used to parse the data.
     * @return the playerOne
     */
    private void cardUsesAttack(final ArrayNode output, final ObjectMapper objectMapper,
                                final ActionsInput command) {
        if ((currentPlayer == 1 && (command.getCardAttacked().getX() == 2
                || command.getCardAttacked().getX() == 3))
                || (currentPlayer == 2 && (command.getCardAttacked().getX() == 0
                        || command.getCardAttacked().getX() == 1))) {
            ObjectNode cardUsesAttack = objectMapper.createObjectNode();
            cardUsesAttack.put("command", command.getCommand());
            ObjectNode cardAttackerxy = objectMapper.createObjectNode();
            cardAttackerxy.put("x", command.getCardAttacker().getX());
            cardAttackerxy.put("y", command.getCardAttacker().getY());
            cardUsesAttack.put("cardAttacker", cardAttackerxy);
            ObjectNode cardAttackedxy = objectMapper.createObjectNode();
            cardAttackedxy.put("x", command.getCardAttacked().getX());
            cardAttackedxy.put("y", command.getCardAttacked().getY());
            cardUsesAttack.put("cardAttacked", cardAttackedxy);
            cardUsesAttack.put("error", "Attacked card does not belong to the enemy.");
            output.add(cardUsesAttack);
            return;
        }
        CardInterface cardAttacker = table.getCard(command.getCardAttacker().getX(),
                command.getCardAttacker().getY());
        if (cardAttacker == null) {
            ObjectNode cardUsesAttack = objectMapper.createObjectNode();
            cardUsesAttack.put("command", command.getCommand());
            ObjectNode cardAttackerxy = objectMapper.createObjectNode();
            cardAttackerxy.put("x", command.getCardAttacker().getX());
            cardAttackerxy.put("y", command.getCardAttacker().getY());
            cardUsesAttack.put("cardAttacker", cardAttackerxy);
            ObjectNode cardAttackedxy = objectMapper.createObjectNode();
            cardAttackedxy.put("x", command.getCardAttacked().getX());
            cardAttackedxy.put("y", command.getCardAttacked().getY());
            cardUsesAttack.put("cardAttacked", cardAttackedxy);
            cardUsesAttack.put("error", "Attacker card does not exist.");
            output.add(cardUsesAttack);
            return;
        }
        if (cardAttacker.usedAttack()) {
            ObjectNode cardUsesAttack = objectMapper.createObjectNode();
            cardUsesAttack.put("command", command.getCommand());
            ObjectNode cardAttackerxy = objectMapper.createObjectNode();
            cardAttackerxy.put("x", command.getCardAttacker().getX());
            cardAttackerxy.put("y", command.getCardAttacker().getY());
            cardUsesAttack.put("cardAttacker", cardAttackerxy);
            ObjectNode cardAttackedxy = objectMapper.createObjectNode();
            cardAttackedxy.put("x", command.getCardAttacked().getX());
            cardAttackedxy.put("y", command.getCardAttacked().getY());
            cardUsesAttack.put("cardAttacked", cardAttackedxy);
            cardUsesAttack.put("error", "Attacker card has already attacked this turn.");
            output.add(cardUsesAttack);
            return;
        }
        if (cardAttacker.isFrozen()) {
            ObjectNode cardUsesAttack = objectMapper.createObjectNode();
            ObjectNode cardAttackerxy = objectMapper.createObjectNode();
            cardAttackerxy.put("x", command.getCardAttacker().getX());
            cardAttackerxy.put("y", command.getCardAttacker().getY());
            cardUsesAttack.put("cardAttacker", cardAttackerxy);
            ObjectNode cardAttackedxy = objectMapper.createObjectNode();
            cardAttackedxy.put("x", command.getCardAttacked().getX());
            cardAttackedxy.put("y", command.getCardAttacked().getY());
            cardUsesAttack.put("cardAttacked", cardAttackedxy);
            cardUsesAttack.put("command", command.getCommand());
            cardUsesAttack.put("error", "Attacker card is frozen.");
            output.add(cardUsesAttack);
            return;
        }
        CardInterface cardToAttack = table.getCard(command.getCardAttacked().getX(),
                command.getCardAttacked().getY());
        if (cardToAttack != null) {
            if (currentPlayer == 1) {
                if (cardToAttack.isTank()) {
                    cardAttacker.attack(cardToAttack);
                    if (cardToAttack.getHealth() <= 0) {
                        table.removeCard(command.getCardAttacked().getX(),
                                command.getCardAttacked().getY());
                    }
                    cardAttacker.usedAttack();
                } else {
                    CardInterface tank = null;
                    for (int i = 0; i < table.getRow(1).size(); i++) {
                        tank = table.getCard(1, i);
                        if (tank.isTank()) {
                            ObjectNode cardUsesAttack = objectMapper.createObjectNode();
                            cardUsesAttack.put("command", command.getCommand());
                            ObjectNode cardAttackerxy = objectMapper.createObjectNode();
                            cardAttackerxy.put("x", command.getCardAttacker().getX());
                            cardAttackerxy.put("y", command.getCardAttacker().getY());
                            cardUsesAttack.put("cardAttacker", cardAttackerxy);
                            ObjectNode cardAttackedxy = objectMapper.createObjectNode();
                            cardAttackedxy.put("x", command.getCardAttacked().getX());
                            cardAttackedxy.put("y", command.getCardAttacked().getY());
                            cardUsesAttack.put("cardAttacked", cardAttackedxy);
                            cardUsesAttack.put("error", "Attacked card is not of type 'Tank'.");
                            output.add(cardUsesAttack);
                            return;
                        }
                    }
                    cardAttacker.attack(cardToAttack);
                    if (cardToAttack.getHealth() <= 0) {
                        table.removeCard(command.getCardAttacked().getX(),
                                command.getCardAttacked().getY());
                    }
                    cardAttacker.usedAttack();
                }
            } else {
                if (cardToAttack.isTank()) {
                    cardAttacker.attack(cardToAttack);
                    if (cardToAttack.getHealth() <= 0) {
                        table.removeCard(command.getCardAttacked().getX(),
                                command.getCardAttacked().getY());
                    }
                    cardAttacker.usedAttack();
                } else {
                    CardInterface tank = null;
                    for (int i = 0; i < table.getRow(2).size(); i++) {
                        tank = table.getCard(2, i);
                        if (tank.isTank()) {
                            ObjectNode cardUsesAttack = objectMapper.createObjectNode();
                            cardUsesAttack.put("command", command.getCommand());
                            ObjectNode cardAttackerxy = objectMapper.createObjectNode();
                            cardAttackerxy.put("x", command.getCardAttacker().getX());
                            cardAttackerxy.put("y", command.getCardAttacker().getY());
                            cardUsesAttack.put("cardAttacker", cardAttackerxy);
                            ObjectNode cardAttackedxy = objectMapper.createObjectNode();
                            cardAttackedxy.put("x", command.getCardAttacked().getX());
                            cardAttackedxy.put("y", command.getCardAttacked().getY());
                            cardUsesAttack.put("cardAttacked", cardAttackedxy);
                            cardUsesAttack.put("error", "Attacked card is not of type 'Tank'.");
                            output.add(cardUsesAttack);
                            return;
                        }
                    }
                    cardAttacker.attack(cardToAttack);
                    if (cardToAttack.getHealth() <= 0) {
                        table.removeCard(command.getCardAttacked().getX(),
                                command.getCardAttacked().getY());
                    }
                    cardAttacker.usedAttack();
                }
            }
        }
    }

    /**
     * @param output       Output object used by the Jackson library to create the JSON output
     *                     for the client.
     * @param objectMapper Object mapper used by the Jackson library to create the JSON output
     *                     for the client.
     * @param command      Command object used to parse the data.
     * @return the playerOne
     */
    private void getPlayerMana(final ArrayNode output, final ObjectMapper objectMapper,
                               final ActionsInput command) {
        ObjectNode getPlayerMana = objectMapper.createObjectNode();
        getPlayerMana.put("command", command.getCommand());
        getPlayerMana.put("playerIdx", command.getPlayerIdx());
        getPlayerMana.put("output", getPlayer(command.getPlayerIdx()).getMana());
        output.add(getPlayerMana);
    }

    /**
     * @param output       Output object used by the Jackson library to create the JSON output
     *                     for the client.
     * @param objectMapper Object mapper used by the Jackson library to create the JSON output
     *                     for the client.
     * @param command      Command object used to parse the data.
     * @return the playerOne
     */
    private void getPlayerDeck(final ArrayNode output, final ObjectMapper objectMapper,
                               final ActionsInput command) {
        ObjectNode getPlayerDeck = objectMapper.createObjectNode();
        getPlayerDeck.put("command", command.getCommand());
        getPlayerDeck.put("playerIdx", command.getPlayerIdx());
        getPlayerDeck.put("output", getPlayer(command.getPlayerIdx()).getJsonDeck());
        output.add(getPlayerDeck);
    }

    /**
     * @param output       Output object used by the Jackson library to create the JSON output
     *                     for the client.
     * @param objectMapper Object mapper used by the Jackson library to create the JSON output
     *                     for the client.
     * @param command      Command object used to parse the data.
     * @return the playerOne
     */
    private void getPlayerHero(final ArrayNode output, final ObjectMapper objectMapper,
                               final ActionsInput command) {
        ObjectNode getPlayerHero = objectMapper.createObjectNode();
        getPlayerHero.put("command", command.getCommand());
        getPlayerHero.put("playerIdx", command.getPlayerIdx());
        if (command.getPlayerIdx() == 1) {
            getPlayerHero.put("output", playerOne.getHero().getJson());
        } else {
            getPlayerHero.put("output", playerTwo.getHero().getJson());
        }
        output.add(getPlayerHero);
    }

    /**
     * @param output       Output object used by the Jackson library to create the JSON output
     *                     for the client.
     * @param objectMapper Object mapper used by the Jackson library to create the JSON output
     *                     for the client.
     * @param command      Command object used to parse the data.
     * @return the playerOne
     */
    private void getPlayerTurn(final ArrayNode output, final ObjectMapper objectMapper,
                               final ActionsInput command) {
        ObjectNode getPlayerTurn = objectMapper.createObjectNode();
        getPlayerTurn.put("command", command.getCommand());
        getPlayerTurn.put("output", currentPlayer);
        output.add(getPlayerTurn);
    }

    private void endPlayerTurn() {
        if (eOfRoundPlayer == currentPlayer) {
            rounds++;
            playerOne.setMana(playerOne.getMana() + Math.min(rounds, 10));
            playerTwo.setMana(playerTwo.getMana() + Math.min(rounds, 10));
            playerTwo.drawCard();
            playerOne.drawCard();
            table.markUnUsed();
            playerOneHero.setUsedAttack((short) 0);
            playerTwoHero.setUsedAttack((short) 0);
        }
        if (currentPlayer == 1) {
            currentPlayer = 2;
            table.unFreezePlayerCards(1);
        } else {
            currentPlayer = 1;
            table.unFreezePlayerCards(2);
        }
    }

    /**
     * @param output       Output object used by the Jackson library to create the JSON output
     *                     for the client.
     * @param objectMapper Object mapper used by the Jackson library to create the JSON output
     *                     for the client.
     * @param command      Command object used to parse the data.
     * @return the playerOne
     */
    private void getCardsInHand(final ArrayNode output, final ObjectMapper objectMapper,
                                final ActionsInput command) {
        ObjectNode getCardsInHand = objectMapper.createObjectNode();
        getCardsInHand.put("command", command.getCommand());
        getCardsInHand.put("playerIdx", command.getPlayerIdx());
        getCardsInHand.put("output", getPlayer(command.getPlayerIdx()).getJsonHand());
        output.add(getCardsInHand);
    }

    /**
     * @param output       Output object used by the Jackson library to create the JSON output
     *                     for the client.
     * @param objectMapper Object mapper used by the Jackson library to create the JSON output
     *                     for the client.
     * @param command      Command object used to parse the data.
     * @return the playerOne
     */
    private void getCardsOnTable(final ArrayNode output, final ObjectMapper objectMapper,
                                 final ActionsInput command) {
        ObjectNode getCardsOnTable = objectMapper.createObjectNode();
        getCardsOnTable.put("command", command.getCommand());
        // put json array of cards on table
        getCardsOnTable.put("output", table.getJson());
        output.add(getCardsOnTable);
    }

    /**
     * @param output       Output object used by the Jackson library to create the JSON output
     *                     for the client.
     * @param objectMapper Object mapper used by the Jackson library to create the JSON output
     *                     for the client.
     * @param command      Command object used to parse the data.
     * @return the playerOne
     */
    private void placeCard(final ArrayNode output, final ObjectMapper objectMapper,
                           final ActionsInput command) {
        CardInterface card = getPlayer(currentPlayer).getHand().get(command.getHandIdx());
        if (card != null) {
            // output diferit pentru fiecare eroare ffs
            // de bagat absolut toate comenzile intr-un command handler cu metode diferite
            if (!card.isEnvironment()) {
                if ((card.getMana() <= getPlayer(currentPlayer).getMana())) {
                    if (table.placeCard(card, currentPlayer)) {
                        getPlayer(currentPlayer).setMana(getPlayer(currentPlayer).getMana()
                                - card.getMana());
                        getPlayer(currentPlayer).getHand().remove(command.getHandIdx());
                    } else {
                        ObjectNode placeCard = objectMapper.createObjectNode();
                        placeCard.put("command", command.getCommand());
                        placeCard.put("handIdx", command.getHandIdx());
                        placeCard.put("error", "Cannot place card on table since row is full.");
                        output.add(placeCard);
                        return;
                    }
                    //getPlayer(command.getPlayerIdx()).getHand().remove(command.getHandIdx());
                } else {
                    ObjectNode placeCard = objectMapper.createObjectNode();
                    placeCard.put("command", command.getCommand());
                    placeCard.put("handIdx", command.getHandIdx());
                    placeCard.put("error", "Not enough mana to place card on table.");
                    output.add(placeCard);
                    return;
                }
            } else {
                ObjectNode placeCard = objectMapper.createObjectNode();
                placeCard.put("command", command.getCommand());
                placeCard.put("handIdx", command.getHandIdx());
                placeCard.put("error", "Cannot place environment card on table.");
                output.add(placeCard);
                return;
            }
        } else {
            System.out.println("Card is null");
            return;
        }
    }

    /**
     * @param output       Output object used by the Jackson library to create the JSON output
     *                     for the client.
     * @param objectMapper Object mapper used by the Jackson library to create the JSON output
     *                     for the client.
     * @param command      Command object used to parse the data.
     * @return the playerOne
     */
    private void cardUsesAbility(final ArrayNode output, final ObjectMapper objectMapper,
                                 final ActionsInput command) {
        CardInterface cardAttacker = table.getCard(command.getCardAttacker().getX(),
                command.getCardAttacker().getY());
        CardInterface cardToAttack = table.getCard(command.getCardAttacked().getX(),
                command.getCardAttacked().getY());
        if (cardAttacker == null) {
            ObjectNode cardUsesAttack = objectMapper.createObjectNode();
            cardUsesAttack.put("command", command.getCommand());
            ObjectNode cardAttackerxy = objectMapper.createObjectNode();
            cardAttackerxy.put("x", command.getCardAttacker().getX());
            cardAttackerxy.put("y", command.getCardAttacker().getY());
            cardUsesAttack.put("cardAttacker", cardAttackerxy);
            ObjectNode cardAttackedxy = objectMapper.createObjectNode();
            cardAttackedxy.put("x", command.getCardAttacked().getX());
            cardAttackedxy.put("y", command.getCardAttacked().getY());
            cardUsesAttack.put("cardAttacked", cardAttackedxy);
            cardUsesAttack.put("error", "Attacker card does not exist.");
            output.add(cardUsesAttack);
            return;
        }
        if (cardAttacker.usedAttack()) {
            ObjectNode cardUsesAttack = objectMapper.createObjectNode();
            cardUsesAttack.put("command", command.getCommand());
            ObjectNode cardAttackerxy = objectMapper.createObjectNode();
            cardAttackerxy.put("x", command.getCardAttacker().getX());
            cardAttackerxy.put("y", command.getCardAttacker().getY());
            cardUsesAttack.put("cardAttacker", cardAttackerxy);
            ObjectNode cardAttackedxy = objectMapper.createObjectNode();
            cardAttackedxy.put("x", command.getCardAttacked().getX());
            cardAttackedxy.put("y", command.getCardAttacked().getY());
            cardUsesAttack.put("cardAttacked", cardAttackedxy);
            cardUsesAttack.put("error", "Attacker card has already attacked this turn.");
            output.add(cardUsesAttack);
            return;
        }
        if (cardAttacker.isFrozen()) {
            ObjectNode cardUsesAttack = objectMapper.createObjectNode();
            ObjectNode cardAttackerxy = objectMapper.createObjectNode();
            cardAttackerxy.put("x", command.getCardAttacker().getX());
            cardAttackerxy.put("y", command.getCardAttacker().getY());
            cardUsesAttack.put("cardAttacker", cardAttackerxy);
            ObjectNode cardAttackedxy = objectMapper.createObjectNode();
            cardAttackedxy.put("x", command.getCardAttacked().getX());
            cardAttackedxy.put("y", command.getCardAttacked().getY());
            cardUsesAttack.put("cardAttacked", cardAttackedxy);
            cardUsesAttack.put("command", command.getCommand());
            cardUsesAttack.put("error", "Attacker card is frozen.");
            output.add(cardUsesAttack);
            return;
        }
        if (cardAttacker.getName().equals("Disciple")) {
            if ((currentPlayer == 1 && (command.getCardAttacked().getX() == 2
                    || command.getCardAttacked().getX() == 3))
                    || (currentPlayer == 2 && (command.getCardAttacked().getX() == 0
                            || command.getCardAttacked().getX() == 1))) {
                cardAttacker.useAbility(cardToAttack, table, currentPlayer,
                        command.getCardAttacked().getX());
            } else {
                ObjectNode cardUsesAttack = objectMapper.createObjectNode();
                cardUsesAttack.put("command", command.getCommand());
                ObjectNode cardAttackerxy = objectMapper.createObjectNode();
                cardAttackerxy.put("x", command.getCardAttacker().getX());
                cardAttackerxy.put("y", command.getCardAttacker().getY());
                cardUsesAttack.put("cardAttacker", cardAttackerxy);
                ObjectNode cardAttackedxy = objectMapper.createObjectNode();
                cardAttackedxy.put("x", command.getCardAttacked().getX());
                cardAttackedxy.put("y", command.getCardAttacked().getY());
                cardUsesAttack.put("cardAttacked", cardAttackedxy);
                cardUsesAttack.put("error", "Attacked card does not belong to the current player.");
                output.add(cardUsesAttack);
            }
        } else {
            if ((currentPlayer == 1 && (command.getCardAttacked().getX() == 2
                    || command.getCardAttacked().getX() == 3))
                    || (currentPlayer == 2 && (command.getCardAttacked().getX() == 0
                            || command.getCardAttacked().getX() == 1))) {
                ObjectNode cardUsesAttack = objectMapper.createObjectNode();
                cardUsesAttack.put("command", command.getCommand());
                ObjectNode cardAttackerxy = objectMapper.createObjectNode();
                cardAttackerxy.put("x", command.getCardAttacker().getX());
                cardAttackerxy.put("y", command.getCardAttacker().getY());
                cardUsesAttack.put("cardAttacker", cardAttackerxy);
                ObjectNode cardAttackedxy = objectMapper.createObjectNode();
                cardAttackedxy.put("x", command.getCardAttacked().getX());
                cardAttackedxy.put("y", command.getCardAttacked().getY());
                cardUsesAttack.put("cardAttacked", cardAttackedxy);
                cardUsesAttack.put("error", "Attacked card does not belong to the enemy.");
                output.add(cardUsesAttack);
            } else {
                if (currentPlayer == 1) {
                    if (cardToAttack.isTank()) {
                        cardAttacker.useAbility(cardToAttack, table, currentPlayer,
                                command.getCardAttacked().getX());
                    } else {
                        CardInterface tank = null;
                        for (int i = 0; i < table.getRow(1).size(); i++) {
                            tank = table.getCard(1, i);
                            if (tank.isTank()) {
                                ObjectNode cardUsesAttack = objectMapper.createObjectNode();
                                cardUsesAttack.put("command", command.getCommand());

                                ObjectNode cardAttackerxy = objectMapper.createObjectNode();
                                cardAttackerxy.put("x", command.getCardAttacker().getX());
                                cardAttackerxy.put("y", command.getCardAttacker().getY());
                                cardUsesAttack.put("cardAttacker", cardAttackerxy);

                                ObjectNode cardAttackedxy = objectMapper.createObjectNode();
                                cardAttackedxy.put("x", command.getCardAttacked().getX());
                                cardAttackedxy.put("y", command.getCardAttacked().getY());
                                cardUsesAttack.put("cardAttacked", cardAttackedxy);

                                cardUsesAttack.put("error",
                                        "Attacked card is not of type 'Tank'.");
                                output.add(cardUsesAttack);
                                return;
                            }
                        }
                        cardAttacker.useAbility(cardToAttack, table, currentPlayer,
                                command.getCardAttacked().getX());
                    }
                } else {
                    if (cardToAttack.isTank()) {
                        cardAttacker.useAbility(cardToAttack, table, currentPlayer,
                                command.getCardAttacked().getX());
                    } else {
                        CardInterface tank = null;
                        for (int i = 0; i < table.getRow(2).size(); i++) {
                            tank = table.getCard(2, i);
                            if (tank.isTank()) {
                                ObjectNode cardUsesAttack = objectMapper.createObjectNode();
                                cardUsesAttack.put("command", command.getCommand());
                                ObjectNode cardAttackerxy = objectMapper.createObjectNode();
                                cardAttackerxy.put("x", command.getCardAttacker().getX());
                                cardAttackerxy.put("y", command.getCardAttacker().getY());
                                cardUsesAttack.put("cardAttacker", cardAttackerxy);
                                ObjectNode cardAttackedxy = objectMapper.createObjectNode();
                                cardAttackedxy.put("x", command.getCardAttacked().getX());
                                cardAttackedxy.put("y", command.getCardAttacked().getY());
                                cardUsesAttack.put("cardAttacked", cardAttackedxy);
                                cardUsesAttack.put("error",
                                        "Attacked card is not of type 'Tank'.");
                                output.add(cardUsesAttack);
                                return;
                            }
                        }
                        cardAttacker.useAbility(cardToAttack, table, currentPlayer,
                                command.getCardAttacked().getX());
                    }
                }
            }
        }
    }

    /**
     * @param output       Output object used by the Jackson library to create the JSON output
     *                     for the client.
     * @param objectMapper Object mapper used by the Jackson library to create the JSON output
     *                     for the client.
     * @param command      Command object used to parse the data.
     * @return the playerOne
     */
    private void useAttackHero(final ArrayNode output, final ObjectMapper objectMapper,
                               final ActionsInput command) {
        CardInterface cardAttacker = table.getCard(command.getCardAttacker().getX(),
                command.getCardAttacker().getY());
        if (cardAttacker == null) {
            ObjectNode cardUsesAttack = objectMapper.createObjectNode();
            cardUsesAttack.put("command", command.getCommand());
            ObjectNode cardAttackerxy = objectMapper.createObjectNode();
            cardAttackerxy.put("x", command.getCardAttacker().getX());
            cardAttackerxy.put("y", command.getCardAttacker().getY());
            cardUsesAttack.put("cardAttacker", cardAttackerxy);
            cardUsesAttack.put("error", "Attacker card does not exist.");
            output.add(cardUsesAttack);
            return;
        }
        if (cardAttacker.usedAttack()) {
            ObjectNode cardUsesAttack = objectMapper.createObjectNode();
            cardUsesAttack.put("command", command.getCommand());
            ObjectNode cardAttackerxy = objectMapper.createObjectNode();
            cardAttackerxy.put("x", command.getCardAttacker().getX());
            cardAttackerxy.put("y", command.getCardAttacker().getY());
            cardUsesAttack.put("cardAttacker", cardAttackerxy);
            cardUsesAttack.put("error", "Attacker card has already attacked this turn.");
            output.add(cardUsesAttack);
            return;
        }
        if (cardAttacker.isFrozen()) {
            ObjectNode cardUsesAttack = objectMapper.createObjectNode();
            ObjectNode cardAttackerxy = objectMapper.createObjectNode();
            cardAttackerxy.put("x", command.getCardAttacker().getX());
            cardAttackerxy.put("y", command.getCardAttacker().getY());
            cardUsesAttack.put("cardAttacker", cardAttackerxy);
            cardUsesAttack.put("command", command.getCommand());
            cardUsesAttack.put("error", "Attacker card is frozen.");
            output.add(cardUsesAttack);
            return;
        }
        if (currentPlayer == 1) {
            CardInterface tank = null;
            for (int i = 0; i < table.getRow(1).size(); i++) {
                tank = table.getCard(1, i);
                if (tank.isTank()) {
                    ObjectNode cardUsesAttack = objectMapper.createObjectNode();
                    cardUsesAttack.put("command", command.getCommand());
                    ObjectNode cardAttackerxy = objectMapper.createObjectNode();
                    cardAttackerxy.put("x", command.getCardAttacker().getX());
                    cardAttackerxy.put("y", command.getCardAttacker().getY());
                    cardUsesAttack.put("cardAttacker", cardAttackerxy);
                    cardUsesAttack.put("error", "Attacked card is not of type 'Tank'.");
                    output.add(cardUsesAttack);
                    return;
                }
            }
            cardAttacker.attack(playerTwoHero);
            if (playerTwoHero.getHealth() <= 0 && playerTwoHero.isDead()) {
                playerTwoHero.setDead(true);
                ObjectNode gameOver = objectMapper.createObjectNode();
                gameOver.put("gameEnded", "Player one killed the enemy hero.");
                output.add(gameOver);
                playerOneWins++;
            }
        } else {
            CardInterface tank = null;
            for (int i = 0; i < table.getRow(2).size(); i++) {
                tank = table.getCard(2, i);
                if (tank.isTank()) {
                    ObjectNode cardUsesAttack = objectMapper.createObjectNode();
                    cardUsesAttack.put("command", command.getCommand());
                    ObjectNode cardAttackerxy = objectMapper.createObjectNode();
                    cardAttackerxy.put("x", command.getCardAttacker().getX());
                    cardAttackerxy.put("y", command.getCardAttacker().getY());
                    cardUsesAttack.put("cardAttacker", cardAttackerxy);
                    cardUsesAttack.put("error", "Attacked card is not of type 'Tank'.");
                    output.add(cardUsesAttack);
                    return;
                }
            }
            cardAttacker.attack(playerOneHero);
            if (playerOneHero.getHealth() <= 0 && playerOneHero.isDead()) {
                playerOneHero.setDead(true);
                ObjectNode gameOver = objectMapper.createObjectNode();
                gameOver.put("gameEnded", "Player two killed the enemy hero.");
                output.add(gameOver);
                playerTwoWins++;
            }
        }
    }

    /**
     * @param output       Output object used by the Jackson library to create the JSON output
     *                     for the client.
     * @param objectMapper Object mapper used by the Jackson library to create the JSON output
     *                     for the client.
     * @param command      Command object used to parse the data.
     * @return the playerOne
     */
    private void useHeroAbility(final ArrayNode output, final ObjectMapper objectMapper,
                                final ActionsInput command) {
        if (currentPlayer == 1) {
            if (getPlayer(currentPlayer).getMana() < playerOneHero.getMana()) {
                ObjectNode heroAbility = objectMapper.createObjectNode();
                heroAbility.put("command", command.getCommand());
                heroAbility.put("affectedRow", command.getAffectedRow());
                heroAbility.put("error", "Not enough mana to use hero's ability.");
                output.add(heroAbility);
                return;
            }
            if (playerOneHero.usedAttack()) {
                ObjectNode heroAbility = objectMapper.createObjectNode();
                heroAbility.put("command", command.getCommand());
                heroAbility.put("affectedRow", command.getAffectedRow());
                heroAbility.put("error", "Hero has already attacked this turn.");
                output.add(heroAbility);
                return;
            }
            if ((playerOneHero.getName().equals("Lord Royce")
                    || playerOneHero.getName().equals("Empress Thorina"))
                    && (command.getAffectedRow() == 2 || command.getAffectedRow() == 3)) {
                ObjectNode heroAbility = objectMapper.createObjectNode();
                heroAbility.put("command", command.getCommand());
                heroAbility.put("affectedRow", command.getAffectedRow());
                heroAbility.put("error", "Selected row does not belong to the enemy.");
                output.add(heroAbility);
                return;
            }
            if ((playerOneHero.getName().equals("General Kocioraw")
                    || playerOneHero.getName().equals("King Mudface"))
                    && (command.getAffectedRow() == 0
                    || command.getAffectedRow() == 1)) {
                ObjectNode heroAbility = objectMapper.createObjectNode();
                heroAbility.put("command", command.getCommand());
                heroAbility.put("affectedRow", command.getAffectedRow());
                heroAbility.put("error", "Selected row does not belong to the current player.");
                output.add(heroAbility);
                return;
            }
            playerOneHero.useAbility(table, currentPlayer, command.getAffectedRow());
            playerOne.setMana(playerOne.getMana() - playerOneHero.getMana());
        } else {
            if (getPlayer(currentPlayer).getMana() < playerTwoHero.getMana()) {
                ObjectNode heroAbility = objectMapper.createObjectNode();
                heroAbility.put("command", command.getCommand());
                heroAbility.put("affectedRow", command.getAffectedRow());
                heroAbility.put("error", "Not enough mana to use hero's ability.");
                output.add(heroAbility);
                return;
            }
            if (playerTwoHero.usedAttack()) {
                ObjectNode heroAbility = objectMapper.createObjectNode();
                heroAbility.put("command", command.getCommand());
                heroAbility.put("affectedRow", command.getAffectedRow());
                heroAbility.put("error", "Hero has already attacked this turn.");
                output.add(heroAbility);
                return;
            }
            if ((playerTwoHero.getName().equals("Lord Royce")
                    || playerTwoHero.getName().equals("Empress Thorina"))
                    && (command.getAffectedRow() == 0 || command.getAffectedRow() == 1)) {
                ObjectNode heroAbility = objectMapper.createObjectNode();
                heroAbility.put("command", command.getCommand());
                heroAbility.put("affectedRow", command.getAffectedRow());
                heroAbility.put("error", "Selected row does not belong to the enemy.");
                output.add(heroAbility);
                return;
            }
            if ((playerTwoHero.getName().equals("General Kocioraw")
                    || playerTwoHero.getName().equals("King Mudface"))
                    && (command.getAffectedRow() == 2 || command.getAffectedRow() == 3)) {
                ObjectNode heroAbility = objectMapper.createObjectNode();
                heroAbility.put("command", command.getCommand());
                heroAbility.put("affectedRow", command.getAffectedRow());
                heroAbility.put("error", "Selected row does not belong to the current player.");
                output.add(heroAbility);
                return;
            }
            playerTwoHero.useAbility(table, currentPlayer, command.getAffectedRow());
            playerTwo.setMana(playerTwo.getMana() - playerTwoHero.getMana());
        }
    }

    /**
     * Function sets the number of wins for player one.
     *
     * @param playerOneWins the number of wins to set
     */
    public void setPlayerOneWins(final int playerOneWins) {
        this.playerOneWins = playerOneWins;
    }

    /**
     * Function sets the number of wins for player two.
     *
     * @param playerTwoWins the number of wins to set
     */
    public void setPlayerTwoWins(final int playerTwoWins) {
        this.playerTwoWins = playerTwoWins;
    }
}
