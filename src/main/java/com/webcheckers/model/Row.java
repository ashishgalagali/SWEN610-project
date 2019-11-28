package com.webcheckers.model;

import lombok.Data;

import java.util.Iterator;
import java.util.List;

@Data
public class Row implements Iterable{
    private int index;
    private List<Square> squares;

    public Row(int index, List<Square> squares) {
        this.index = index;
        this.squares = squares;
    }

    @Override
    public Iterator iterator() {
        return this.squares.iterator();
    }
}
