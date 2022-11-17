package player;
import card.CardFactory;
import card.CardInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Player {

    private int playerNo;
    private int mana;
    private CardInterface hero;
    private ArrayList<CardInterface> hand;
    private ArrayList<CardInterface> deck;

    public Player() {
    }

    public Player(int playerNo, int mana, CardInterface hero, ArrayList<CardInput> rawDeck, long seed) {
        CardFactory factory = new CardFactory();
        this.playerNo = playerNo;
        this.mana = mana;
        this.hero = hero;
        this.hand = new ArrayList<CardInterface>();
        // build the deck from CardInput to CardInterface
        this.deck = new ArrayList<CardInterface>();
        for(CardInput rawCard : rawDeck) {
            deck.add(deck.size(), factory.getCard(rawCard));
        }
        // shuffle the deck
        Collections.shuffle(deck, new Random(seed));
        // draw one card
        hand.add(deck.get(0));
        deck.remove(0);
    }

    public int getPlayerNo() {
        return playerNo;
    }

    public int setPlayerNo(int playerNo) {
        this.playerNo = playerNo;
        return playerNo;
    }

    public int getMana() {
        return mana;
    }

    public int setMana(int mana) {
        this.mana = mana;
        return mana;
    }

    public CardInterface getHero() {
        return hero;
    }

    public CardInterface setHero(CardInterface hero) {
        this.hero = hero;
        return hero;
    }

    public ArrayList<CardInterface> getHand() {
        return hand;
    }

    public ArrayList<CardInterface> setHand(ArrayList<CardInterface> hand) {
        //this.hand = hand;
        //return hand;
        return null;
    }

    public ArrayList<CardInterface> getDeck() {
        return deck;
    }

    public ArrayList<CardInterface> setDeck(ArrayList<CardInterface> deck) {
        //this.deck = deck;
        //return deck;
        return null;
    }

    public void drawCard(){
        hand.add(deck.get(0));
        deck.remove(0);
    }

    /* might be useful later thanks github copilot :)
    public void drawCard() {
        if (deck.size() > 0) {
            hand.add(deck.get(0));
            deck.remove(0);
        }
    }

    public void playCard(int cardNo) {
        if (hand.size() > cardNo) {
            if (hand.get(cardNo).getMana() <= mana) {
                mana -= hand.get(cardNo).getMana();
                hand.remove(cardNo);
            }
        }
    }

    public void endTurn() {
        mana = 10;
    }

    public void startTurn() {
        drawCard();
    }*/

    public ArrayNode getJsonDeck(){
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode json = objectMapper.createArrayNode();
        for (CardInterface card : deck) {
            json.add(card.getJson());
        }
        return json;
    }

    public ArrayNode getJsonHand(){
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode json = objectMapper.createArrayNode();
        for (CardInterface card : hand) {
            json.add(card.getJson());
        }
        return json;
    }

}
