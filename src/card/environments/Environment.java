package card.environments;

import card.CardInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
