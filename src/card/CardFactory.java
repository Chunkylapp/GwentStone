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
        switch(rawCard.getName()) {
            case "Sentinel":
            case "Warden":
            case "Goliath":
            case "Berserker":
                return new NormalMinion(rawCard.getMana(), rawCard.getAttackDamage(),
                        rawCard.getHealth(), rawCard.getDescription(),
                        rawCard.getColors(), rawCard.getName());
            case "The Ripper":
            case "Miraj":
            case "The Cursed One":
            case "Disciple":
                return new SpecialMinion(rawCard.getMana(), rawCard.getAttackDamage(),
                        rawCard.getHealth(), rawCard.getDescription(),
                        rawCard.getColors(), rawCard.getName());
            case "Lord Royce":
            case "Empress Thorina":
            case "King Mudface":
            case "General Kocioraw":
                return new Hero(rawCard.getMana(), rawCard.getAttackDamage(),
                        rawCard.getDescription(),
                        rawCard.getColors(), rawCard.getName());
            case "Firestorm":
            case "Winterfell":
            case "Heart Hound":
                return new Environment(rawCard.getMana(), rawCard.getDescription(),
                        new ArrayList<String>(rawCard.getColors()), rawCard.getName());
            default:
                return null;
        }
    }
}
