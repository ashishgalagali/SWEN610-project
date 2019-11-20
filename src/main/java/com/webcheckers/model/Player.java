package com.webcheckers.model;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import lombok.Data;

@Entity
@Data
public class Player {
    @Id
    private String playerId;
    private String playerName;
    private int points;
    private Color playerColor;

}
