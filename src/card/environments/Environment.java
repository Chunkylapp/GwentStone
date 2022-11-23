package card.environments;

import card.CardInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import game.table.Table;

import java.util.ArrayList;

/**
 * Class that represents the environment cards.
 */
public final class Environment implements CardInterface {
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;

    /**
     * Constructor for the Environment class.
     */
    public Environment() {
    }

    /**
     * Constructor for the Environment class.
     *
     * @param newMana        The mana cost of the card.
     * @param newDescription The description of the card.
     * @param newColors      The colors of the card.
     * @param newName        The name of the card.
     */
    public Environment(final int newMana, final String newDescription,
                       final ArrayList<String> newColors, final String newName) {
        mana = newMana;
        description = newDescription;
        colors = new ArrayList<>(newColors);
        name = newName;
    }

    /**
     * Getter for the mana cost of the card.
     *
     * @return The mana cost of the card.
     */
    public int getMana() {
        return mana;
    }

    /**
     * Setter for the mana cost of the card.
     *
     * @param newMana The mana cost of the card.
     * @return The mana cost of the card.
     */
    public int setMana(final int newMana) {
        mana = newMana;
        return mana;
    }

    /**
     * Getter for the attack damage of the card.
     *
     * @return The attack damage of the card.
     */
    public int getAttackDamage() {
        return 0;
    }

    /**
     * Setter for the attack damage of the card.
     *
     * @param newAttackDamage The attack damage of the card.
     * @return The attack damage of the card.
     */
    public int setAttackDamage(final int newAttackDamage) {
        return 0;
    }

    /**
     * Getter for the health of the card.
     *
     * @return The health of the card.
     */
    public int getHealth() {
        return 0;
    }

    /**
     * Setter for the health of the card.
     *
     * @param newHealth The health of the card.
     * @return The health of the card.
     */
    public int setHealth(final int newHealth) {
        return 0;
    }

    /**
     * Getter for the description of the card.
     *
     * @return The description of the card.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the description of the card.
     *
     * @param newDescription The description of the card.
     * @return The description of the card.
     */
    public String setDescription(final String newDescription) {
        this.description = newDescription;
        return description;
    }

    /**
     * Getter for the colors of the card.
     *
     * @return The colors of the card.
     */
    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     * Setter for the colors of the card.
     *
     * @param newColors The colors of the card.
     * @return The colors of the card.
     */
    public ArrayList<String> setColors(final ArrayList<String> newColors) {
        colors = new ArrayList<>(newColors);
        return colors;
    }

    /**
     * Getter for the name of the card.
     *
     * @return The name of the card.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name of the card.
     *
     * @param newName The name of the card.
     * @return The name of the card.
     */
    public String setName(final String newName) {
        name = newName;
        return name;
    }

    /**
     * Method that applies the attack action to a card.
     *
     * @param attackedCard card to be attacked.
     */
    public void attack(final CardInterface attackedCard) {
    }

    /**
     * Method marks the card as frozen.
     */
    public void freeze() {
    }

    /**
     * Method makrs the card as unfrozen.
     */
    public void unFreeze() {
    }

    /**
     * Method returns the frozen status of the card.
     *
     * @return The frozen status of the card.
     */
    public boolean isFrozen() {
        return false;
    }

    /**
     * Method returns the attack status of the card.
     *
     * @return The attack status of the card.
     */
    public boolean usedAttack() {
        return false;
    }

    /**
     * Methods sets the attack status of the card.
     *
     * @param newUsedAttack The attack status of the card.
     */
    public void setUsedAttack(final short newUsedAttack) {
    }

    /**
     * Method returns true if the card is a tank.
     *
     * @return True if the card is a tank.
     */
    public boolean isTank() {
        return false;
    }

    /**
     * Metthod applies the effect of the card.
     *
     * @param table         table of the game
     * @param currentPlayer current player
     * @param row           targeted row
     * @return True if the effect was applied.
     */
    public boolean useEffect(final Table table, final int currentPlayer, final int row) {
        ArrayList<CardInterface> tableRow = table.getRow(row);
        switch (name) {
            case "Firestorm":
                for (int i = tableRow.size() - 1; i >= 0; i--) {
                    CardInterface affCard = tableRow.get(i);
                    if (affCard != null) {
                        affCard.setHealth(affCard.getHealth() - 1);
                        if (affCard.getHealth() <= 0) {
                            table.removeCard(row, affCard);
                        }
                    }
                }
                return true;
            case "Winterfell":
                for (CardInterface affCard : tableRow) {
                    if (affCard != null) {
                        affCard.freeze();
                    }
                }
                return true;
            case "Heart Hound":
                return shalom(table, currentPlayer, row);
            default:
                return false;
        }
    }

    /**
     * Method implements the Hearth Hound effect.
     *
     * @param table         table of the game
     * @param currentPlayer current player
     * @param row           targeted row
     * @return True if the effect was applied.
     */
    private boolean shalom(final Table table, final int currentPlayer, final int row) {
        if (currentPlayer == 1) {
            if (row == 1) {
                if (table.getRow(2).size() >= 5) {
                    return false;
                }
            }
            if (row == 0) {
                if (table.getRow(3).size() >= 5) {
                    return false;
                }
            }
        } else {
            if (row == 2) {
                if (table.getRow(1).size() >= 5) {
                    return false;
                }
            }
            if (row == 3) {
                if (table.getRow(0).size() >= 5) {
                    return false;
                }
            }
        }
        int column = table.highestHealthOnRow(row);
        if (column == -1) {
            return false;
        }
        CardInterface card = table.removeCard(row, column);
        if (row == 0) {
            table.addCard(3, card);
        }
        if (row == 1) {
            table.addCard(2, card);
        }
        if (row == 2) {
            table.addCard(1, card);
        }
        if (row == 3) {
            table.addCard(0, card);
        }
        return true;
    }

    /**
     * Method returns true if the card is of type environment.
     *
     * @return True if the card is of type environment.
     */
    public boolean isEnvironment() {
        return true;
    }

    @Override
    public ObjectNode getJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode json = objectMapper.createObjectNode();
        json.put("mana", mana);
        json.put("description", description);
        ArrayNode strings = objectMapper.createArrayNode();
        for (String color : colors) {
            strings.add(color);
        }
        json.put("colors", strings);
        json.put("name", name);
        return json;
    }

    /**
     * Method applies the ability of the card.
     *
     * @param attacked      card to be attacked
     * @param table         table of the game
     * @param currentPlayer current player
     * @param row           targeted row
     * @return True if the ability was applied.
     */
    public boolean useAbility(final CardInterface attacked, final Table table,
                              final int currentPlayer, final int row) {
        return false;
    }
}
