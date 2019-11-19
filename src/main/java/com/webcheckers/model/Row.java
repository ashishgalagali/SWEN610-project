package com.webcheckers.model;

import lombok.Data;

import java.util.Iterator;
import java.util.List;

@Data
public class Row implements Iterable{
    private int index;
    private List<Space> spaces;

    public Row(int index, List<Space> spaces) {
        this.index = index;
        this.spaces = spaces;
    }

    @Override
    public Iterator iterator() {
        return this.spaces.iterator();
    }
}
