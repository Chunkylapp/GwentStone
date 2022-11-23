package card;

import com.fasterxml.jackson.databind.node.ObjectNode;
import game.table.Table;

import java.util.ArrayList;

/**
 * Interface for all cards in order to make
 * them as generic as possible.
 */
public interface CardInterface {
    /**
     * @return mana cost of the card
     */
    int getMana();

    /**
     * @param newMana mana cost of the card
     * @return mana cost of the card
     */
    int setMana(int newMana);

    /**
     * @return attack damage of the card
     */
    int getAttackDamage();

    /**
     * @param newAttackDamage attack damage of the card
     * @return attack damage of the card
     */
    int setAttackDamage(int newAttackDamage);

    /**
     * @return health of the card
     */
    int getHealth();

    /**
     * @param newHealth health of the card
     * @return health of the card
     */
    int setHealth(int newHealth);

    /**
     * @return description of the card
     */
    String getDescription();

    /**
     * @param newDescription description of the card
     * @return description of the card
     */
    String setDescription(String newDescription);

    /**
     * @return colors of the card
     */
    ArrayList<String> getColors();

    /**
     * @param newColors colors of the card to be set
     * @return colors of the card
     */
    ArrayList<String> setColors(ArrayList<String> newColors);

    /**
     * @return name of the card
     */
    String getName();

    /**
     * @param newName name of the card
     * @return name of the card
     */
    String setName(String newName);

    /**
     * @param attackedCard card to be attacked
     */
    void attack(CardInterface attackedCard);

    /**
     * Method marks the card as frozen
     */
    void freeze();

    /**
     * Method marks the card as unfrozen
     */
    void unFreeze();

    /**
     * @return true if the card is frozen
     */
    boolean isFrozen();

    /**
     * @return true if the card has been used
     */
    boolean usedAttack();

    /**
     * Method marks the card as unused
     * @param newUsedAttack true if the card has been used
     */
    void setUsedAttack(short newUsedAttack);

    /**
     * @return true if the card is of type environment
     */
    boolean isEnvironment();

    /**
     * @return true if the card is of type tank
     */
    boolean isTank();

    /**
     * Method applies the effect of the card
     * @param table table of the game
     * @param currentPlayer current player
     * @param row targeted row
     * @return true if the effect has been applied
     */
    boolean useEffect(Table table, int currentPlayer, int row);

    /**
     * Method returns the card as a JSON object
     * @return card as a JSON object
     */
    ObjectNode getJson();

    /**
     * Method applies the ability of the card
     * @param attacked card to be attacked
     * @param table table of the game
     * @param currentPlayer current player
     * @param row targeted row
     * @return true if the ability was used
     */
    boolean useAbility(CardInterface attacked, Table table, int currentPlayer, int row);
}
