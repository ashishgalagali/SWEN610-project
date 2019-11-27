package com.webcheckers.model;

import lombok.Data;

@Data
public class Move {

    Position start;
    Position end;

    public Move(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

}
