package game.table;

import card.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class Table {

    private ArrayList<ArrayList<CardInterface>> table;

    public Table() {
        table = new ArrayList<ArrayList<CardInterface>>();
        for (int i = 0; i < 4; i++) {
            table.add(new ArrayList<CardInterface>());
        }
    }

    public ArrayList<ArrayList<CardInterface>> getTable() {
        return table;
    }

    public ArrayList<CardInterface> getRow(int row) {
        return table.get(row);
    }

    public CardInterface getCard(int row, int column) {
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

    public void setCard(int row, int column, CardInterface card) {
        table.get(row).set(column, card);
    }

    public void addCard(int row, CardInterface card) {
        table.get(row).add(card);
    }

    public CardInterface removeCard(int row, int column) {
        if (row < 0 || row > 3 || column < 0 || (table.get(row).size() - 1) < column) {
            return null;
        }
        // check if there is a card in the given position
        if (table.get(row).size() == 0 || table.get(row).get(column) == null) {
            return null;
        }
        return table.get(row).get(column);
    }

    public void removeCard(int row, CardInterface card) {
        table.get(row).remove(card);
    }

    public void removeRow(int row) {
        table.remove(row);
    }

    public void addRow() {
        table.add(new ArrayList<CardInterface>());
    }

    public void addRow(int row) {
        table.add(row, new ArrayList<CardInterface>());
    }

    public void addRow(int row, ArrayList<CardInterface> cards) {
        table.add(row, cards);
    }

    public void addRow(ArrayList<CardInterface> cards) {
        table.add(cards);
    }

    public void clear() {
        table.clear();
    }

    public void clearRow(int row) {
        table.get(row).clear();
    }

    public void markUnUsed(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < table.get(i).size(); j++) {
                table.get(i).get(j).setUsedAttack((short)(0));
                table.get(i).get(j).unFreeze();
            }
        }
    }

    public int highestHealthOnRow(int row){
        CardInterface highest = null;
        int index = -1;
        for (int i = 0; i < table.get(row).size(); i++) {
            if (highest == null || highest.getHealth() < table.get(row).get(i).getHealth()){
                highest = table.get(row).get(i);
                index = i;
            }
        }
        return index;
    }

    public boolean placeCard(CardInterface card, int playerNo) {
        int row = -1;
        switch (card.getName()) {
            case "The Ripper":
            case "Miraj":
            case "Goliath":
            case "Warden":
                if (playerNo == 1)
                    row = 2;
                else
                    row = 1;
                break;
            case "Sentinel":
            case "Berserker":
            case "The Cursed One":
            case "Disciple":
                if (playerNo == 1)
                    row = 3;
                else
                    row = 0;
                break;
            default:
                break;
        }
        if (row != -1)
            if (table.get(row).size() < 5) {
                table.get(row).add(card);
                return true;
            } else {
                return false;
            }
        else
            return false;
    }

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
