package game.table;

import card.CardInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;

/**
 * The table where the cards are placed.
 */
public final class Table {

    private final ArrayList<ArrayList<CardInterface>> table;

    /**
     * Constructor for the table.
     */
    public Table() {
        table = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            table.add(new ArrayList<>());
        }
    }

    /**
     * Getter for the table.
     *
     * @return the table
     */
    public ArrayList<CardInterface> getRow(final int row) {
        return table.get(row);
    }

    /**
     * Getter for a card at row and column location.
     *
     * @return the card
     */
    public CardInterface getCard(final int row, final int column) {
        // check if row and column are valid
        if (row < 0 || row > 3 || column < 0 || (table.get(row).size() - 1) < column) {
            return null;
        }
        // check if there is a card in the given position
        if (table.get(row).size() == 0 || table.get(row).get(column) == null) {
            return null;
        }
        return table.get(row).get(column);
    }

    /**
     * Adds a card to the table.
     *
     * @param row  the row where the card will be added
     * @param card the card to be added
     */
    public void addCard(final int row, final CardInterface card) {
        table.get(row).add(card);
    }

    /**
     * Removes a card from the table.
     *
     * @param row    the row where the card will be removed
     * @param column the column where the card will be removed
     */
    public CardInterface removeCard(final int row, final int column) {
        if (row < 0 || row > 3 || column < 0 || (table.get(row).size() - 1) < column) {
            return null;
        }
        // check if there is a card in the given position
        if (table.get(row).size() == 0 || table.get(row).get(column) == null) {
            return null;
        }
        return table.get(row).remove(column);
    }

    /**
     * Removes the card in the row.
     * @param row the row where the card will be removed
     * @param card the card to be removed
     */
    public void removeCard(final int row, final CardInterface card) {
        table.get(row).remove(card);
    }

    /**
     * Marks every card in the table as not attacked
     */
    public void markUnUsed() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < table.get(i).size(); j++) {
                table.get(i).get(j).setUsedAttack((short) (0));
            }
        }
    }

    /**
     * Unfreezes the cards that belong to the player.
     * @param player the player
     */
    public void unFreezePlayerCards(final int player) {
        if (player == 1) {
            for (int i = 2; i < 4; i++) {
                for (int j = 0; j < table.get(i).size(); j++) {
                    table.get(i).get(j).unFreeze();
                }
            }
        } else {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < table.get(i).size(); j++) {
                    table.get(i).get(j).unFreeze();
                }
            }
        }
    }

    /**
     * Returns the card with the highest health on the given row.
     * @param row the row where the card is
     * @return the card with the highest health
     */
    public int highestHealthOnRow(final int row) {
        CardInterface highest = null;
        int index = -1;
        for (int i = 0; i < table.get(row).size(); i++) {
            if (highest == null || highest.getHealth() < table.get(row).get(i).getHealth()) {
                highest = table.get(row).get(i);
                index = i;
            }
        }
        return index;
    }

    /**
     * Places a card on the table.
     * @param card the card to be placed
     * @param playerNo the player number
     * @return true if the card was placed, false otherwise
     */
    public boolean placeCard(final CardInterface card, final int playerNo) {
        int row = -1;
        switch (card.getName()) {
            case "The Ripper":
            case "Miraj":
            case "Goliath":
            case "Warden":
                if (playerNo == 1) {
                    row = 2;
                } else {
                    row = 1;
                }
                break;
            case "Sentinel":
            case "Berserker":
            case "The Cursed One":
            case "Disciple":
                if (playerNo == 1) {
                    row = 3;
                } else {
                    row = 0;
                }
                break;
            default:
                break;
        }
        if (row != -1) {
            if (table.get(row).size() < 5) {
                table.get(row).add(card);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Returns the json representation of the table.
     * @return the json representation of the table
     */
    public ArrayNode getJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode tableNode = objectMapper.createArrayNode();
        for (int i = 0; i < table.size(); i++) {
            ArrayNode col = objectMapper.createArrayNode();
            for (int j = 0; j < table.get(i).size(); j++) {
                col.add(table.get(i).get(j).getJson());
            }
            tableNode.add(col);
        }
        return tableNode;
    }
}
