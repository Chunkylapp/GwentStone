package card;

import java.util.ArrayList;

public interface cardInterface {
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
    public void attack(cardInterface attackedCard);
    public void freeze();
    public void unFreeze();
    public boolean isFrozen();
}
