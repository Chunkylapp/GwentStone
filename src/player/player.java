package player;
import card.cardInterface;

import java.util.ArrayList;

public class player {

    private int playerNo;
    private int mana;
    private cardInterface hero;
    private ArrayList<cardInterface> hand;
    private ArrayList<cardInterface> deck;

    public player() {
    }

    public player(int playerNo, int mana, cardInterface hero, ArrayList<cardInterface> hand, ArrayList<cardInterface> deck) {
        this.playerNo = playerNo;
        this.mana = mana;
        this.hero = hero;
        this.hand = new ArrayList<cardInterface>();
        this.deck = new ArrayList<cardInterface>(deck);
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

    public cardInterface getHero() {
        return hero;
    }

    public cardInterface setHero(cardInterface hero) {
        this.hero = hero;
        return hero;
    }

    public ArrayList<cardInterface> getHand() {
        return hand;
    }

    public ArrayList<cardInterface> setHand(ArrayList<cardInterface> hand) {
        //this.hand = hand;
        //return hand;
        return void;
    }

    public ArrayList<cardInterface> getDeck() {
        return deck;
    }

    public ArrayList<cardInterface> setDeck(ArrayList<cardInterface> deck) {
        //this.deck = deck;
        //return deck;
        return void;
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

}
