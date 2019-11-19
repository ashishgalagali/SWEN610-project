package com.webcheckers.model;

import lombok.Data;

@Data
public class Player {
    private String playerId;
    private String playerName;
    private int points;
    private Color playerColor;

}
