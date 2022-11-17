package game;

import card.CardInterface;
import card.heros.Hero;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;
import fileio.DecksInput;
import fileio.Input;
import game.table.Table;

import java.util.ArrayList;
import card.CardFactory;
public class Game {

    private Table table;
    private ArrayList<ActionsInput> commands;
    private card.heros.Hero playerOneHero;
    private card.heros.Hero playerTwoHero;
    private int currentPlayer;
    private player.player playerOne;
    private player.player playerTwo;

    public Game(Input inputData) {
        CardFactory factory = new CardFactory();
        table = new Table();
        this.commands = new ArrayList<ActionsInput>(inputData.getGames().get(0).getActions());
        this.playerOneHero = (Hero) factory.getCard(inputData.getGames().get(0).getStartGame().getPlayerOneHero());
        this.playerTwoHero = (Hero) factory.getCard(inputData.getGames().get(0).getStartGame().getPlayerTwoHero());
        currentPlayer = inputData.getGames().get(0).getStartGame().getStartingPlayer();

        //playerOne = new player.player(1, 1, playerOneHero, inputData.getPlayerOneDecks().getDecks().get(inputData.getGames().get(0).getStartGame().getPlayerOneDeckIdx()));
    }

    private ArrayList<CardInterface> getPlayerDeck(int index){
        if(index == 1){
            return playerOne.getDeck();
        }
        return playerTwo.getDeck();
    }


    public void play(ArrayNode output) {
        for (ActionsInput command : commands) {
           switch(command.getCommand()){
               case "getPlayerDeck":
                   getPlayerDeck(command.getPlayerIdx());
               case "getPlayerTurn":

           }
        }
    }

}
