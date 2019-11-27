package com.webcheckers.appl;

import com.webcheckers.helper.Tuple;
import com.webcheckers.model.Game;
import com.webcheckers.model.Human;
import lombok.Data;

import java.util.*;

@Data
public class WebCheckersController {

    public static int numberOfGames;
    private Map<String, Game> userGame = new HashMap<>();
    private Queue<Human> usersWaiting = new LinkedList<>();
    private Map<Integer, Game> allgames = new HashMap();
    private List<Human> allUsers = new ArrayList<>();


    public Tuple getOpponent(String username){
        if(usersWaiting.isEmpty()) return null;
        if(usersWaiting.peek().getUserName().equals(username)) return null;
        Human h = usersWaiting.poll();
        return new Tuple(h, userGame.get(h.getUserName()));
    }

    public void addUserAndGame(Tuple t){
        allUsers.add(t.h);
        usersWaiting.add(t.h);
        userGame.put(t.h.getUserName(), t.g);
        allgames.put(t.g.getGameID(), t.g);
        numberOfGames++;
    }

    public void addUserToGame(Human human, int gameid){

    }



}
