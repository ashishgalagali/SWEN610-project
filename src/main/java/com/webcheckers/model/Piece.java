package com.webcheckers.model;

import lombok.Data;

@Data
public class Piece {

    private int pieceId;
    private PieceType type;
    private Color color;

    public Piece(PieceType type, Color color, int pieceId) {
        this.type = type;
        this.color = color;
        this.pieceId = pieceId;
    }
}
