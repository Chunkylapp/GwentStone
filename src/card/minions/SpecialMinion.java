package card.minions;
import card.CardInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;
import game.table.Table;

import java.util.ArrayList;

public class SpecialMinion implements CardInterface {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private short isFrozen;

    private short usedAttack;

    public SpecialMinion() {
    }

    public SpecialMinion(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        this.mana = mana;
        this.attackDamage = attackDamage;
        this.health = health;
        this.description = description;
        this.colors = colors;
        this.name = name;
        isFrozen = 0;
    }

    public int getMana() {
        return mana;
    }

    public int setMana(int mana) {
        this.mana = mana;
        return mana;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
        return attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public int setHealth(int health) {
        this.health = health;
        return health;
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
        this.colors = colors;
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
        attackedCard.setHealth(attackedCard.getHealth() - attackDamage);
        usedAttack = 1;
    }

    public void freeze() {
        isFrozen = 1;
    }

    public void unFreeze() {
        isFrozen = 0;
    }

    public boolean isFrozen() {
        return isFrozen == 1;
    }

    public boolean UsedAttack() {
        return usedAttack == 1;
    }

    public void setUsedAttack(short usedAttack){
        this.usedAttack = usedAttack;
    }

    public boolean isTank(){
        return false;
    }

    public boolean useEffect(Table table, int currentPlayer, int row) {
        return false;
    }

    public boolean isEnvironment() {
        return false;
    }
    public String toString() {
        return "specialMinion{"
                + "mana="
                + mana
                + ", attackDamage="
                + attackDamage
                + ", health="
                + health
                + ", description='"
                + description
                + '\''
                + ", colors='"
                + colors
                + '\''
                + ", name='"
                + name
                + '\''
                + '}';
    }

    public ObjectNode getJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode json = objectMapper.createObjectNode();
        json.put("mana", mana);
        json.put("attackDamage", attackDamage);
        json.put("health", health);
        json.put("description", description);
        ArrayNode strings = objectMapper.createArrayNode();
        for (String color : colors) {
            strings.add(color);
        }
        json.put("colors", strings);
        json.put("name", name);
        return json;
    }

    public boolean useAbility(CardInterface attacked, Table table, int currentPlayer, int row){
        usedAttack = 1;
        switch(name){
            case "The Ripper":
                attacked.setAttackDamage(attacked.getAttackDamage() - 2);
                if(attacked.getAttackDamage() < 0){
                    attacked.setAttackDamage(0);
                }
                return true;
            case "Miraj":
                attacked.setHealth(health + attacked.getHealth());
                health = attacked.getHealth() - health;
                attacked.setHealth(attacked.getHealth() - health);
                if(attacked.getHealth() <= 0){
                    table.removeCard(row, attacked);
                }
                return true;
            case "The Cursed One":
                int aux = attacked.getHealth();
                attacked.setHealth(attacked.getAttackDamage());
                attacked.setAttackDamage(aux);
                if(attacked.getHealth() <= 0){
                    table.removeCard(row, attacked);
                }
                return true;
            case "Disciple":
                attacked.setHealth(attacked.getHealth() + 2);
                return true;
            default:
                return false;
        }
    }

}
