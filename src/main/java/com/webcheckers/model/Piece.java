package com.webcheckers.model;

import lombok.Data;

@Data
public class Piece {

    public Piece(PieceType type, Color color) {
        this.type = type;
        this.color = color;
    }

    private PieceType type;
    private Color color;
}
