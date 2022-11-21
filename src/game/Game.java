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
                default -> {
                }
            }
        }
        if (output.has("gameEnded")) {
            return;
        }
    }

    private void getFrozenCardsOnTable(ArrayNode output, ObjectMapper objectMapper, ActionsInput command) {
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
        for (CardInterface RENAME_CARD_FROZEN : frozenCards) {
            frozenCardsArray.add(RENAME_CARD_FROZEN.getJson());
        }
        frozenCardsOnTable.put("output", frozenCardsArray);
        output.add(frozenCardsOnTable);
    }

    private void getEnvironmentCardsInHand(ArrayNode output, ObjectMapper objectMapper, ActionsInput command) {
        ArrayList<CardInterface> envCards = new ArrayList<>();
        for (CardInterface RENAME_CARD_ENV : getPlayer(currentPlayer).getHand()) {
            if (RENAME_CARD_ENV.isEnvironment()) {
                envCards.add(RENAME_CARD_ENV);
            }
        }
        ObjectNode envCardsInHand = objectMapper.createObjectNode();
        envCardsInHand.put("command", command.getCommand());
        ArrayNode envCardsArray = objectMapper.createArrayNode();
        for (CardInterface RENAME_CARD_ENV : envCards) {
            envCardsArray.add(RENAME_CARD_ENV.getJson());
        }
        envCardsInHand.put("output", envCardsArray);
        envCardsInHand.put("playerIdx", command.getPlayerIdx());
        output.add(envCardsInHand);
    }

    private void getCardAtPosition(ArrayNode output, ObjectMapper objectMapper, ActionsInput command) {
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

    private void useEnvironmentCard(ArrayNode output, ObjectMapper objectMapper, ActionsInput command) {
        // check if the card is on the table
        if (getPlayer(currentPlayer).getHand().size() < 1)
            return;
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
                if ((currentPlayer == 1 && (command.getAffectedRow() == 2 || command.getAffectedRow() == 3)) ||
                        (currentPlayer == 2 && (command.getAffectedRow() == 0 || command.getAffectedRow() == 1))) {
                    ObjectNode cardUsesAttack = objectMapper.createObjectNode();
                    cardUsesAttack.put("affectedRow", command.getAffectedRow());
                    cardUsesAttack.put("command", command.getCommand());
                    cardUsesAttack.put("error", "Chosen row does not belong to the enemy.");
                    cardUsesAttack.put("handIdx", command.getHandIdx());
                    output.add(cardUsesAttack);
                } else {
                    if (envCard.useEffect(table, currentPlayer, command.getAffectedRow())) {
                        getPlayer(currentPlayer).setMana(getPlayer(currentPlayer).getMana() - envCard.getMana());
                        getPlayer(currentPlayer).getHand().remove(command.getHandIdx());
                    } else {
                        ObjectNode cardUsesAttack = objectMapper.createObjectNode();
                        cardUsesAttack.put("affectedRow", command.getAffectedRow());
                        cardUsesAttack.put("command", command.getCommand());
                        cardUsesAttack.put("error", "Cannot steal enemy card since the player's row is full.");
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

    private void cardUsesAttack(ArrayNode output, ObjectMapper objectMapper, ActionsInput command) {
        // check if card is on table
        // attack firstly the tanks but only if they want to attack the front row
        // the hero can be attacked only if tanks are dead
        // must check if the current card is frozen or not
        if ((currentPlayer == 1 && (command.getCardAttacked().getX() == 2 || command.getCardAttacked().getX() == 3)) ||
                (currentPlayer == 2 && (command.getCardAttacked().getX() == 0 || command.getCardAttacked().getX() == 1))) {
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
        CardInterface cardAttacker = table.getCard(command.getCardAttacker().getX(), command.getCardAttacker().getY());
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
        if (cardAttacker.UsedAttack()) {
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
        CardInterface cardToAttack = table.getCard(command.getCardAttacked().getX(), command.getCardAttacked().getY());
        // check if the card to attack is on the 1st row
        if (cardToAttack != null) {
            if (currentPlayer == 1) {
                // check if the card to attack is a tank
                if (cardToAttack.isTank()) {
                    cardAttacker.attack(cardToAttack);
                    if (cardToAttack.getHealth() <= 0)
                        table.removeCard(command.getCardAttacked().getX(), command.getCardAttacked().getY());
                    cardAttacker.UsedAttack();
                } else {
                    // lets try to find a tank
                    CardInterface tank = null;
                    for (int i = 0; i < table.getRow(1).size(); i++) {
                        tank = table.getCard(1, i);
                        if (tank.isTank()) {
                            // error
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
                    // we can attack the desired card
                    cardAttacker.attack(cardToAttack);
                    if (cardToAttack.getHealth() <= 0)
                        table.removeCard(command.getCardAttacked().getX(), command.getCardAttacked().getY());
                    cardAttacker.UsedAttack();
                    return;
                }
            } else {
                // check if the card to attack is a tank
                if (cardToAttack.isTank()) {
                    cardAttacker.attack(cardToAttack);
                    if (cardToAttack.getHealth() <= 0)
                        table.removeCard(command.getCardAttacked().getX(), command.getCardAttacked().getY());
                    cardAttacker.UsedAttack();
                } else {
                    // lets try to find a tank
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
                    // we can attack the desired card
                    cardAttacker.attack(cardToAttack);
                    if (cardToAttack.getHealth() <= 0) {
                        table.removeCard(command.getCardAttacked().getX(), command.getCardAttacked().getY());

                    }
                    cardAttacker.UsedAttack();
                    return;
                }
            }
        }
    }

    private void getPlayerMana(ArrayNode output, ObjectMapper objectMapper, ActionsInput command) {
        ObjectNode getPlayerMana = objectMapper.createObjectNode();
        getPlayerMana.put("command", command.getCommand());
        getPlayerMana.put("playerIdx", command.getPlayerIdx());
        getPlayerMana.put("output", getPlayer(command.getPlayerIdx()).getMana());
        output.add(getPlayerMana);
    }

    private void getPlayerDeck(ArrayNode output, ObjectMapper objectMapper, ActionsInput command) {
        ObjectNode getPlayerDeck = objectMapper.createObjectNode();
        getPlayerDeck.put("command", command.getCommand());
        getPlayerDeck.put("playerIdx", command.getPlayerIdx());
        getPlayerDeck.put("output", getPlayer(command.getPlayerIdx()).getJsonDeck());
        output.add(getPlayerDeck);
    }

    private void getPlayerHero(ArrayNode output, ObjectMapper objectMapper, ActionsInput command) {
        ObjectNode getPlayerHero = objectMapper.createObjectNode();
        getPlayerHero.put("command", command.getCommand());
        getPlayerHero.put("playerIdx", command.getPlayerIdx());
        if (command.getPlayerIdx() == 1)
            getPlayerHero.put("output", playerOne.getHero().getJson());
        else
            getPlayerHero.put("output", playerTwo.getHero().getJson());
        output.add(getPlayerHero);
    }

    private void getPlayerTurn(ArrayNode output, ObjectMapper objectMapper, ActionsInput command) {
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
        }
        if (currentPlayer == 1) {
            currentPlayer = 2;
            table.unFreezePlayerCards(1);
        } else {
            currentPlayer = 1;
            table.unFreezePlayerCards(2);
        }
    }

    private void getCardsInHand(ArrayNode output, ObjectMapper objectMapper, ActionsInput command) {
        ObjectNode getCardsInHand = objectMapper.createObjectNode();
        getCardsInHand.put("command", command.getCommand());
        getCardsInHand.put("playerIdx", command.getPlayerIdx());
        getCardsInHand.put("output", getPlayer(command.getPlayerIdx()).getJsonHand());
        output.add(getCardsInHand);
    }

    private void getCardsOnTable(ArrayNode output, ObjectMapper objectMapper, ActionsInput command) {
        ObjectNode getCardsOnTable = objectMapper.createObjectNode();
        getCardsOnTable.put("command", command.getCommand());
        // put json array of cards on table
        getCardsOnTable.put("output", table.getJson());
        output.add(getCardsOnTable);
    }

    private void placeCard(ArrayNode output, ObjectMapper objectMapper, ActionsInput command) {
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

    private void cardUsesAbility(ArrayNode output, ObjectMapper objectMapper, ActionsInput command) {
        CardInterface cardAttacker = table.getCard(command.getCardAttacker().getX(), command.getCardAttacker().getY());
        CardInterface cardToAttack = table.getCard(command.getCardAttacked().getX(), command.getCardAttacked().getY());
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
        if (cardAttacker.UsedAttack()) {
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
            if ((currentPlayer == 1 && (command.getCardAttacked().getX() == 2 || command.getCardAttacked().getX() == 3)) ||
                    (currentPlayer == 2 && (command.getCardAttacked().getX() == 0 || command.getCardAttacked().getX() == 1))) {
                cardAttacker.useAbility(cardToAttack, table, currentPlayer, command.getCardAttacked().getX());
                return;
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
                return;
            }
        } else {
            if ((currentPlayer == 1 && (command.getCardAttacked().getX() == 2 || command.getCardAttacked().getX() == 3)) ||
                    (currentPlayer == 2 && (command.getCardAttacked().getX() == 0 || command.getCardAttacked().getX() == 1))) {
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
            } else {
                if (currentPlayer == 1) {
                    // check if the card to attack is a tank
                    if (cardToAttack.isTank()) {
                        cardAttacker.useAbility(cardToAttack, table, currentPlayer, command.getCardAttacked().getX());
                    } else {
                        // lets try to find a tank
                        CardInterface tank = null;
                        for (int i = 0; i < table.getRow(1).size(); i++) {
                            tank = table.getCard(1, i);
                            if (tank.isTank()) {
                                // error
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
                        // we can attack the desired card
                        cardAttacker.useAbility(cardToAttack, table, currentPlayer, command.getCardAttacked().getX());
                        return;
                    }
                } else {
                    // check if the card to attack is a tank
                    if (cardToAttack.isTank()) {
                        cardAttacker.useAbility(cardToAttack, table, currentPlayer, command.getCardAttacked().getX());
                    } else {
                        // lets try to find a tank
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
                        // we can attack the desired card
                        cardAttacker.useAbility(cardToAttack, table, currentPlayer, command.getCardAttacked().getX());
                        return;
                    }
                }
            }
        }

    }

    private void useAttackHero(ArrayNode output, ObjectMapper objectMapper, ActionsInput command) {
        CardInterface cardAttacker = table.getCard(command.getCardAttacker().getX(), command.getCardAttacker().getY());
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
        if (cardAttacker.UsedAttack()) {
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
            // check if the card to attack is a tank
            // lets try to find a tank
            CardInterface tank = null;
            for (int i = 0; i < table.getRow(1).size(); i++) {
                tank = table.getCard(1, i);
                if (tank.isTank()) {
                    // error
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
            // we can attack the desired card
            cardAttacker.attack(playerTwoHero);
            if(playerTwoHero.getHealth() <= 0) {
                ObjectNode gameOver = objectMapper.createObjectNode();
                gameOver.put("gameEnded", "Player one killed the enemy hero.");
                output.add(gameOver);
            }
            return;
        } else {
            // check if the card to attack is a tank
            // lets try to find a tank
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
            // we can attack the desired card
            cardAttacker.attack(playerOneHero);
            if(playerOneHero.getHealth() <= 0) {
                ObjectNode gameOver = objectMapper.createObjectNode();
                gameOver.put("gameEnded", "Player two killed the enemy hero.");
                output.add(gameOver);
            }
            return;
        }
    }
}
