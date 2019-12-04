package com.webcheckers.appl;

import com.webcheckers.helper.Tuple;
import com.webcheckers.model.Game;
import com.webcheckers.model.Human;
import lombok.Data;

import java.util.*;

@Data
public class WebCheckersController {

    //    Singleton class
    public static WebCheckersController webCheckersController;

    private WebCheckersController() {

    }

    //number of games being played
    public static int numberOfGames;
    //map of username and game
    private Map<String, Game> userGame = new HashMap<>();
    //users waiting for game {will not be more than one}
    private Queue<Human> hardUsersWaiting = new LinkedList<>();
    private Queue<Human> usersWaiting = new LinkedList<>();
    //map of game wrt gameid
    private Map<Integer, Game> allGames = new HashMap();
    //list of all users
    private List<Human> allUsers = new ArrayList<>();
    //check if user's game has started. this will be set to true when the opponent gets assigned
    private Map<String, Boolean> gameStarted = new HashMap<>();

    public static WebCheckersController getInstance() {
        if (webCheckersController == null) {
            webCheckersController = new WebCheckersController();
        }
        return webCheckersController;
    }

    public Tuple getOpponent(String username, boolean isEasy) {
        Human h;
        if (isEasy) {
            if (usersWaiting.isEmpty()) return null;
            if (usersWaiting.peek().getUserName().equals(username)) return null;
            h = usersWaiting.poll();
        } else {
            if (hardUsersWaiting.isEmpty()) return null;
            if (hardUsersWaiting.peek().getUserName().equals(username)) return null;
            h = hardUsersWaiting.poll();
        }
//        Human h = usersWaiting.poll();
        //set to true because their games have started
        gameStarted.put(h.getUserName(), true);
        gameStarted.put(username, true);
        return new Tuple(h, userGame.get(h.getUserName()));
    }

    public void addPlayerAndGame(Tuple t) {
        //add player to wait list
        allUsers.add(t.human);
        if (t.game.isEasy()) {
            usersWaiting.add(t.human);
        } else {
            hardUsersWaiting.add(t.human);
        }
        gameStarted.put(t.human.getUserName(), false);
        userGame.put(t.human.getUserName(), t.game);
        allGames.put(t.game.getGameID(), t.game);
        numberOfGames++;
    }

    public void addPlayerToGame(Human human, int gameid) {
        //add second player to the game
        Game game = allGames.get(gameid);
        game.setPlayerTwo(human);
        allGames.put(gameid, game);
        userGame.put(human.getUserName(), game);
    }

    public boolean playerHasGame(String username) {
        return gameStarted.get(username);
    }


}
