package com.webcheckers.helper;

import com.webcheckers.model.Game;
import com.webcheckers.model.Human;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class Tuple {
    public Human human;
    public Game game;
}
