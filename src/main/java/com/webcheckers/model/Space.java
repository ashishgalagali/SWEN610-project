package com.webcheckers.model;

import lombok.Data;

@Data
public class Space {
    private int cellIdx;

    public Space(int cellIdx, boolean isValid, Piece piece) {
        this.cellIdx = cellIdx;
        this.isValid = isValid;
        this.piece = piece;
    }

    private boolean isValid;
    private Piece piece;
}
