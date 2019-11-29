package com.webcheckers.model;

import lombok.Data;

import java.util.Objects;

@Data
public class Position {
    private int row;
    private int cell;

    public Position(int row, int cell) {
        this.row = row;
        this.cell = cell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return getRow() == position.getRow() &&
                getCell() == position.getCell();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getCell());
    }

    @Override
    public String toString() {
        return "Position{" +
                "row=" + row +
                ", cell=" + cell +
                '}';
    }
}
