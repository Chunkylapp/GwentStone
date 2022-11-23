package card.minions;

import card.CardInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import game.table.Table;

import java.util.ArrayList;

/**
 * The class that represents the special minion cards.
 */
public final class SpecialMinion implements CardInterface {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private short isFrozen;

    private short usedAttack;

    /**
     * Default constructor.
     */
    public SpecialMinion() {
    }

    /**
     * Constructor with parameters.
     * @param newMana the mana cost of the card
     * @param newAttackDamage the attack damage of the card
     * @param newHealth the health of the card
     * @param newDescription the description of the card
     * @param newColors the colors of the card
     * @param newName the name of the card
     */
    public SpecialMinion(final int newMana, final int newAttackDamage, final int newHealth,
                         final String newDescription, final ArrayList<String> newColors,
                         final String newName) {
        mana = newMana;
        attackDamage = newAttackDamage;
        health = newHealth;
        description = newDescription;
        colors = newColors;
        name = newName;
        isFrozen = 0;
    }

    /**
     * Getter for the mana cost of the card.
     * @return the mana cost of the card
     */
    public int getMana() {
        return mana;
    }

    /**
     * Setter for the mana cost of the card.
     * @param newMana the mana cost of the card
     */
    public int setMana(final int newMana) {
        mana = newMana;
        return mana;
    }

    /**
     * Getter for the attack damage of the card.
     * @return the attack damage of the card
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * Setter for the attack damage of the card.
     * @param newAttackDamage the attack damage of the card
     */
    public int setAttackDamage(final int newAttackDamage) {
        attackDamage = newAttackDamage;
        return attackDamage;
    }

    /**
     * Getter for the health of the card.
     * @return the health of the card
     */
    public int getHealth() {
        return health;
    }

    /**
     * Setter for the health of the card.
     * @param newHealth the health of the card
     */
    public int setHealth(final int newHealth) {
        health = newHealth;
        return health;
    }

    /**
     * Getter for the description of the card.
     * @return the description of the card
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the description of the card.
     * @param newDescription the description of the card
     */
    public String setDescription(final String newDescription) {
        description = newDescription;
        return description;
    }

    /**
     * Getter for the colors of the card.
     * @return the colors of the card
     */
    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     * Setter for the colors of the card.
     * @param newColors the colors of the card
     */
    public ArrayList<String> setColors(final ArrayList<String> newColors) {
        colors = newColors;
        return colors;
    }

    /**
     * Getter for the name of the card.
     * @return the name of the card
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name of the card.
     * @param newName the name of the card
     */
    public String setName(final String newName) {
        name = newName;
        return name;
    }

    /**
     * Method attacks another card.
     * @param attackedCard card to be attacked
     */
    public void attack(final CardInterface attackedCard) {
        attackedCard.setHealth(attackedCard.getHealth() - attackDamage);
        usedAttack = 1;
    }

    /**
     * Method marks the card as frozen.
     */
    public void freeze() {
        isFrozen = 1;
    }

    /**
     * Method marks the card as unfrozen.
     */
    public void unFreeze() {
        isFrozen = 0;
    }

    /**
     * Method checks if the card is frozen.
     * @return true if the card is frozen, false otherwise
     */
    public boolean isFrozen() {
        return isFrozen == 1;
    }

    /**
     * Method checks if the card has used its attack.
     * @return true if the card has used its attack, false otherwise
     */
    public boolean usedAttack() {
        return usedAttack == 1;
    }

    /**
     * Method sets the used attack.
     * @param newUsedAttack the new used attack
     */
    public void setUsedAttack(final short newUsedAttack) {
        usedAttack = newUsedAttack;
    }

    /**
     * Method checks if the card is a tank.
     * @return always false because special minions are not tanks
     */
    public boolean isTank() {
        return false;
    }

    /**
     * Method implied by the interface.
     * @param table table of the game
     * @param currentPlayer current player
     * @param row targeted row
     * @return always false because special minions dont have effects
     */
    public boolean useEffect(final Table table, final int currentPlayer, final int row) {
        return false;
    }

    /**
     * Method checks if the cards is of type environment.
     * @return always false because special minions are not environment cards
     */
    public boolean isEnvironment() {
        return false;
    }

    /**
     * Method returns the string representation of the card.
     * @return the string representation of the card
     */
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

    /**
     * Method returns the json representation of the card.
     * @return the json representation of the card
     */
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

    /**
     * Method applies the ability of the card.
     * @param attacked card to be attacked
     * @param table table of the game
     * @param currentPlayer current player
     * @param row targeted row
     * @return true if the ability was applied, false otherwise
     */
    public boolean useAbility(final CardInterface attacked, final Table table,
                              final int currentPlayer, final int row) {
        usedAttack = 1;
        switch (name) {
            case "The Ripper":
                attacked.setAttackDamage(attacked.getAttackDamage() - 2);
                if (attacked.getAttackDamage() < 0) {
                    attacked.setAttackDamage(0);
                }
                return true;
            case "Miraj":
                attacked.setHealth(health + attacked.getHealth());
                health = attacked.getHealth() - health;
                attacked.setHealth(attacked.getHealth() - health);
                if (attacked.getHealth() <= 0) {
                    table.removeCard(row, attacked);
                }
                return true;
            case "The Cursed One":
                int aux = attacked.getHealth();
                attacked.setHealth(attacked.getAttackDamage());
                attacked.setAttackDamage(aux);
                if (attacked.getHealth() <= 0) {
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
