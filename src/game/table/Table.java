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
        return table.get(row).get(column);
    }

    public void setCard(int row, int column, CardInterface card) {
        table.get(row).set(column, card);
    }

    public void addCard(int row, CardInterface card) {
        table.get(row).add(card);
    }

    public void removeCard(int row, int column) {
        table.get(row).remove(column);
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

    public void placeCard(CardInterface card, int row, int column) {
        // check if there are no more than 5 cards in the row
        table.get(row).add(column, card);
    }

    public ObjectNode getJson(){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode tableNode = objectMapper.createObjectNode();
        ArrayNode rows = objectMapper.createArrayNode();
        for(int i = 0; i < table.size(); i++){
            ArrayNode col = objectMapper.createArrayNode();
            for(int j = 0; j < table.get(i).size(); j++){
                col.add(table.get(i).get(j).getJson());
            }
            rows.add(col);
        }
        tableNode.set("table", rows);
        return tableNode;
    }

}
