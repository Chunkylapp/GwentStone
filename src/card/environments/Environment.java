package card.environments;

import card.CardInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;
import game.table.Table;

import java.util.ArrayList;

public class Environment implements CardInterface {
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;

    public Environment() {
    }

    public Environment(int mana, String description, ArrayList<String> colors, String name) {
        this.mana = mana;
        this.description = description;
        this.colors = new ArrayList<String>(colors);
        this.name = name;
    }

    public int getMana() {
        return mana;
    }

    public int setMana(int mana) {
        this.mana = mana;
        return mana;
    }

    public int getAttackDamage() {
        return 0;
    }

    public int setAttackDamage(int attackDamage) {
        return 0;
    }

    public int getHealth() {
        return 0;
    }

    public int setHealth(int health) {
        return 0;
    }

    public String getDescription() {
        return description;
    }

    public String setDescription(String description) {
        this.description = description;
        return description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public ArrayList<String> setColors(ArrayList<String> colors) {
        this.colors = new ArrayList<String>(colors);
        return colors;
    }

    public String getName() {
        return name;
    }

    public String setName(String name) {
        this.name = name;
        return name;
    }

    public void attack(CardInterface attackedCard) {
    }

    public void freeze() {
    }

    public void unFreeze() {
    }

    public boolean isFrozen() {
        return false;
    }

    public boolean UsedAttack() {
        return false;
    }

    public void setUsedAttack(short usedAttack) {
    }


    public boolean isTank() {
        return false;
    }

    public boolean useEffect(Table table, int currentPlayer, int row) {
        ArrayList<CardInterface> tableRow = table.getRow(row);
        switch (name) {
            case "Firestorm":
                for (int i = tableRow.size()- 1; i >= 0; i--) {
                    CardInterface affCard = tableRow.get(i);
                    if (affCard != null) {
                        affCard.setHealth(affCard.getHealth() - 1);
                        if(affCard.getHealth() <= 0) {
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
            case "default":
                return false;
        }
        return false;
    }

    private boolean shalom(Table table, int currentPlayer, int row) {
        // find the card from the affected row with the highest health
        // steal the card
        if (currentPlayer == 1) {
            if (row == 1) {
                if (table.getRow(2).size() >= 5)
                    return false;
                //fuck
            }
            if (row == 0)
                if (table.getRow(3).size() >= 5)
                    return false;
            // fuck
        } else {
            if (row == 2) {
                if (table.getRow(1).size() >= 5)
                    return false;
                //fuck
            }
            if (row == 3)
                if (table.getRow(0).size() >= 5)
                    return false;
            // fuck
        }
        int column = table.highestHealthOnRow(row);
        if(column == -1)
            return false;
        CardInterface card = table.removeCard(row,column);
        if(row == 0){
            table.addCard(3, card);
        }
        if(row == 1){
            table.addCard(2, card);
        }
        if(row == 2){
            table.addCard(1, card);
        }
        if(row == 3){
            table.addCard(0, card);
        }
        return true;
    }

    public void spell(ArrayList<CardInterface> row) {
        // affects all the cards in the row
    }

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
}
