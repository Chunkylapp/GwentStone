package card;

import card.environments.Environment;
import card.heros.Hero;
import card.minions.NormalMinion;
import card.minions.SpecialMinion;
import fileio.CardInput;
import java.util.ArrayList;

public class CardFactory {

    public CardFactory() {
    }

    public CardInterface getCard(CardInput rawCard) {
        return switch (rawCard.getName()) {
            case "Sentinel", "Warden", "Goliath", "Berserker" ->
                    new NormalMinion(rawCard.getMana(), rawCard.getAttackDamage(),
                            rawCard.getHealth(), rawCard.getDescription(),
                            rawCard.getColors(), rawCard.getName());
            case "The Ripper", "Miraj", "The Cursed One", "Disciple" ->
                    new SpecialMinion(rawCard.getMana(), rawCard.getAttackDamage(),
                            rawCard.getHealth(), rawCard.getDescription(),
                            rawCard.getColors(), rawCard.getName());
            case "Lord Royce", "Empress Thorina", "King Mudface", "General Kocioraw" ->
                    new Hero(rawCard.getMana(), rawCard.getAttackDamage(),
                            rawCard.getDescription(),
                            rawCard.getColors(), rawCard.getName());
            case "Firestorm", "Winterfell", "Heart Hound" ->
                    new Environment(rawCard.getMana(), rawCard.getDescription(),
                            new ArrayList<String>(rawCard.getColors()), rawCard.getName());
            default -> null;
        };
    }
}
