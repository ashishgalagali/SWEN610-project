package com.webcheckers.model;

import lombok.Data;

import java.util.Iterator;
import java.util.List;

@Data
public class Board implements Iterable {
    private List<Row> rows;

    public Board(List<Row> rows) {
        this.rows = rows;
    }

    @Override
    public Iterator iterator() {
        return this.rows.iterator();
    }

}
