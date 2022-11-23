package card.heros;

import card.CardInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import game.table.Table;

import java.util.ArrayList;

/**
 * Hero class
 */
public final class Hero implements CardInterface {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;

    private short isFrozen;

    private short usedAttack;

    private boolean dead;

    public Hero() {
    }

    /**
     * Constructor
     * @param newMana mana
     * @param newAttackDamage attack damage
     * @param newDescription description
     * @param newColors colors
     * @param newName name
     */
    public Hero(final int newMana, final int newAttackDamage, final String newDescription,
                final ArrayList<String> newColors, final String newName) {
        mana = newMana;
        attackDamage = newAttackDamage;
        health = 30;
        description = newDescription;
        colors = new ArrayList<>(newColors);
        name = newName;
        isFrozen = 0;
        dead = false;
    }

    /**
     * Method to get mana
     * @return mana
     */
    public int getMana() {
        return mana;
    }

    /**
     * Method to set mana
     * @param newMana mana
     * @return mana
     */
    public int setMana(final int newMana) {
        mana = newMana;
        return mana;
    }

    /**
     * Method to get attack damage
     * @return attack damage
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * Method to set attack damage
     * @param newAttackDamage attack damage
     * @return attack damage
     */
    public int setAttackDamage(final int newAttackDamage) {
        attackDamage = newAttackDamage;
        return attackDamage;
    }

    /**
     * Method to get health
     * @return health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Method to set health
     * @param newHealth health
     * @return health
     */
    public int setHealth(final int newHealth) {
        health = newHealth;
        return health;
    }

    /**
     * Method to get description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method to set description
     * @param newDescription description
     * @return description
     */
    public String setDescription(final String newDescription) {
        description = newDescription;
        return description;
    }

    /**
     * Method to get colors
     * @return colors
     */
    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     * Method to set colors
     * @param newColors colors
     * @return colors
     */
    public ArrayList<String> setColors(final ArrayList<String> newColors) {
        colors = newColors;
        return colors;
    }

    /**
     * Method to get name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set name
     * @param newName name
     * @return name
     */
    public String setName(final String newName) {
        name = newName;
        return name;
    }

    /**
     * Method to attack but hero can't attack
     */
    public void attack(final CardInterface attackedCard) {

    }

    /**
     * Method marks the hero as frozen
     */
    public void freeze() {
        isFrozen = 1;
    }

    /**
     * Method unfreezes the hero
     */
    public void unFreeze() {
        isFrozen = 0;
    }

    /**
     * Method checks if the hero is frozen
     * @return true if the hero is frozen, false otherwise
     */
    public boolean isFrozen() {
        return isFrozen == 1;
    }

    /**
     * Method to check if the hero attacked
     * @return true if the hero attacked, false otherwise
     */
    public boolean usedAttack() {
        return usedAttack == 1;
    }

    public void setUsedAttack(final short newUsedAttack) {
        usedAttack = newUsedAttack;
    }

    /**
     * Method returns true if the hero is a tank
     * but it's always false for the hero
     * @return false
     */
    public boolean isTank() {
        return false;
    }

    /**
     * Method applies the effect of the hero
     * but hero cards don't have effects
     * @param table table of the game
     * @param currentPlayer current player
     * @param row targeted row
     * @return false because hero cards don't have effects
     */
    @Override
    public boolean useEffect(final Table table, final int currentPlayer, final int row) {
        return false;
    }


    /**
     * Method returns false because the hero card
     * is not of type environment
     * @return false
     */
    public boolean isEnvironment() {
        return false;
    }

    /**
     * Method returns a json representation of the hero
     * @return  json representation of the hero
     */
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
        json.put("health", health);
        return json;
    }

    /**
     * Method returns the string representation of the hero
     * @return string representation of the hero
     */
    public String toString() {
        return "Hero{"
                + "mana=" + mana
                + ", attackDamage=" + attackDamage
                + ", health=" + health
                + ", description='"
                + description + '\''
                + ", colors=" + colors
                + ", name='" + name + '\''
                + ", isFrozen=" + isFrozen
                + '}';
    }

    /**
     * Method implied by the interface but it's not used
     * @param attacked card to be attacked
     * @param table table of the game
     * @param currentPlayer current player
     * @param row targeted row
     * @return false
     */
    public boolean useAbility(final CardInterface attacked, final Table table,
                              final int currentPlayer, final int row) {
        return false;
    }

    /**
     * Method applies the ability of the hero to the selected row
     * @param table table of the game
     * @param currentPlayer current player
     * @param row targeted row
     */
    public void useAbility(final Table table, final int currentPlayer, final int row) {
        switch (name) {
            case "Lord Royce" -> {
                ArrayList<CardInterface> cards = table.getRow(row);
                CardInterface highestAttack = null;
                for (CardInterface card : cards) {
                    if (highestAttack == null || card.getAttackDamage()
                            > highestAttack.getAttackDamage()) {
                        highestAttack = card;
                    }
                }
                if (highestAttack != null) {
                    highestAttack.freeze();
                }
                usedAttack = (short) 1;
            }
            case "Empress Thorina" -> {
                ArrayList<CardInterface> cards2 = table.getRow(row);
                CardInterface highestHealth = null;
                for (CardInterface card : cards2) {
                    if (highestHealth == null || card.getHealth()
                            > highestHealth.getHealth()) {
                        highestHealth = card;
                    }
                }
                if (highestHealth != null) {
                    cards2.remove(highestHealth);
                }
                usedAttack = (short) 1;
            }
            case "King Mudface" -> {
                ArrayList<CardInterface> cards3 = table.getRow(row);
                for (CardInterface card : cards3) {
                    card.setHealth(card.getHealth() + 1);
                }
                usedAttack = (short) 1;
            }
            case "General Kocioraw" -> {
                ArrayList<CardInterface> cards4 = table.getRow(row);
                for (CardInterface card : cards4) {
                    card.setAttackDamage(card.getAttackDamage() + 1);
                }
                usedAttack = (short) 1;
            }
            default -> {
            }
        }
    }

    /**
     * Method returns true if the hero is alive
     * @return true if the hero is alive, false otherwise
     */
    public boolean isDead() {
        return !dead;
    }

    /**
     * Method sets the hero as dead/alive
     * @param newDead true if the hero is dead, false otherwise
     */
    public void setDead(final boolean newDead) {
        dead = newDead;
    }

}
