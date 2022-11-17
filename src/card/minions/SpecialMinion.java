package card.minions;
import card.CardInterface;

import java.util.ArrayList;

public class SpecialMinion implements CardInterface {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private short isFrozen;

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
        // to implement for each special minion
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
}
