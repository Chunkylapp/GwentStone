package player;

import card.CardFactory;
import card.CardInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.CardInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * The class that represents a player.
 */
public final class Player {

    private int mana;
    private CardInterface hero;
    private ArrayList<CardInterface> hand;
    private ArrayList<CardInterface> deck;

    /**
     * Default constructor.
     */
    public Player() {
    }

    /**
     * Constructor with parameters.
     *
     * @param newMana  the mana of the player
     * @param newHero  the hero of the player
     * @param newRawDeck  the deck of the player
     * @param newSeed  the deck of the player
     */
    public Player(final int newMana, final CardInterface newHero,
                  final ArrayList<CardInput> newRawDeck, final long newSeed) {
        CardFactory factory = new CardFactory();
        mana = newMana;
        hero = newHero;
        hand = new ArrayList<>();
        // build the deck from CardInput to CardInterface
        deck = new ArrayList<>();
        for (CardInput rawCard : newRawDeck) {
            deck.add(deck.size(), factory.getCard(rawCard));
        }
        // shuffle the deck
        Collections.shuffle(deck, new Random(newSeed));
        // draw one card
        hand.add(deck.get(0));
        deck.remove(0);
    }

    /**
     * Getter for the mana of the player.
     *
     * @return the mana of the player
     */
    public int getMana() {
        return mana;
    }

    /**
     * Getter for the hero of the player.
     *
     * @return the hero of the player
     */
    public int setMana(final int newMana) {
        mana = newMana;
        return mana;
    }

    /**
     * Getter for the hero of the player.
     *
     * @return the hero of the player
     */
    public CardInterface getHero() {
        return hero;
    }

    /**
     * Getter for the hand of the player.
     *
     * @return the hand of the player
     */
    public ArrayList<CardInterface> getHand() {
        return hand;
    }

    /**
     * Draws a card from the player's deck.
     */
    public void drawCard() {
        if (deck.size() > 0) {
            hand.add(deck.get(0));
            deck.remove(0);
        }
    }

    /**
     * Returns the json representation of the player's deck.
     *
     * @return the json representation of the player's deck
     */
    public ArrayNode getJsonDeck() {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode json = objectMapper.createArrayNode();
        for (CardInterface card : deck) {
            json.add(card.getJson());
        }
        return json;
    }

    /**
     * Returns the json representation of the player's hand.
     *
     * @return the json representation of the player's hand
     */
    public ArrayNode getJsonHand() {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode json = objectMapper.createArrayNode();
        for (CardInterface card : hand) {
            json.add(card.getJson());
        }
        return json;
    }

}
