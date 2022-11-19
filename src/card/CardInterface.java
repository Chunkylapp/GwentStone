package card;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;
import game.table.Table;

import java.util.ArrayList;

public interface CardInterface {
    public int getMana();
    public int setMana(int mana);
    public int getAttackDamage();
    public int setAttackDamage(int attackDamage);
    public int getHealth();
    public int setHealth(int health);
    public String getDescription();
    public String setDescription(String description);
    public ArrayList<String> getColors();
    public ArrayList<String> setColors(ArrayList<String> colors);
    public String getName();
    public String setName(String name);
    public void attack(CardInterface attackedCard);
    public void freeze();
    public void unFreeze();
    public boolean isFrozen();
    public boolean UsedAttack();
    public void setUsedAttack(short usedAttack);
    public boolean isEnvironment();
    public boolean isTank();
    public boolean useEffect(Table table, int currentPlayer, int row);
    public ObjectNode getJson();
}
