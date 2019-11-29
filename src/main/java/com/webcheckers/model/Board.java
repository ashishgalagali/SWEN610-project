package com.webcheckers.model;

import lombok.Data;

import java.util.*;

@Data
public class Board implements Iterable {
    private int boardId;
    private Map<Position, Integer> squarePieceIdMap;
    private List<Row> rows = new ArrayList<>();
    private Set<Integer> redPieceIds = new HashSet<>();
    private Set<Integer> whitePieceIds = new HashSet<>();


    public Board() {
        this.rows.addAll(setInitialRows(Color.WHITE));
        this.rows.addAll(setInitialRows(Color.RED));
        this.squarePieceIdMap = initialiseMap();
        System.out.println(this.squarePieceIdMap);

    }

    private Map<Position, Integer> initialiseMap() {
        Map<Position, Integer> squarePieceIdMap = new HashMap<>();
        for (Row row : this.rows) {
            for (Square square : row.getSquares()) {
                if (square.getPiece() != null) {
                    squarePieceIdMap.put(new Position(row.getIndex(), square.getCellIdx()), square.getPiece().getPieceId());
                }
            }
        }
        return squarePieceIdMap;
    }

    private List<Row> setInitialRows(Color color) {
        List<Row> rows = new ArrayList<>();
        if (color == Color.WHITE) {
            rows.add(new Row(0, getEvenRowSquares(color, 0)));
            rows.add(new Row(1, getOddRowSquares(color, 4)));
            rows.add(new Row(2, getEvenRowSquares(color, 8)));
            rows.add(new Row(3, getEmptyOddSquares()));
        }
        if (color == Color.RED) {
            rows.add(new Row(4, getEmptyEvenSquares()));
            rows.add(new Row(5, getOddRowSquares(color, 12)));
            rows.add(new Row(6, getEvenRowSquares(color, 16)));
            rows.add(new Row(7, getOddRowSquares(color, 20)));
        }
        return rows;
    }

    private List<Square> getEmptyEvenSquares() {

        List<Square> emptyOddSquares = new ArrayList<>();
        emptyOddSquares.add(new Square(0, false, null));
        emptyOddSquares.add(new Square(1, true, null));
        emptyOddSquares.add(new Square(2, false, null));
        emptyOddSquares.add(new Square(3, true, null));
        emptyOddSquares.add(new Square(4, false, null));
        emptyOddSquares.add(new Square(5, true, null));
        emptyOddSquares.add(new Square(6, false, null));
        emptyOddSquares.add(new Square(7, true, null));

        return emptyOddSquares;
    }

    private List<Square> getEmptyOddSquares() {
        List<Square> emptyEvenSquares = new ArrayList<>();
        emptyEvenSquares.add(new Square(0, true, null));
        emptyEvenSquares.add(new Square(1, false, null));
        emptyEvenSquares.add(new Square(2, true, null));
        emptyEvenSquares.add(new Square(3, false, null));
        emptyEvenSquares.add(new Square(4, true, null));
        emptyEvenSquares.add(new Square(5, false, null));
        emptyEvenSquares.add(new Square(6, true, null));
        emptyEvenSquares.add(new Square(7, false, null));
        return emptyEvenSquares;
    }

    private List<Square> getOddRowSquares(Color color, int initialId) {
        List<Square> oddSquares = new ArrayList<>();
        oddSquares.add(new Square(0, true, new Piece(PieceType.SINGLE, color, initialId)));
        oddSquares.add(new Square(1, false, null));
        oddSquares.add(new Square(2, true, new Piece(PieceType.SINGLE, color, initialId + 1)));
        oddSquares.add(new Square(3, false, null));
        oddSquares.add(new Square(4, true, new Piece(PieceType.SINGLE, color, initialId + 2)));
        oddSquares.add(new Square(5, false, null));
        oddSquares.add(new Square(6, true, new Piece(PieceType.SINGLE, color, initialId + 3)));
        oddSquares.add(new Square(7, false, null));

        return oddSquares;
    }

    private List<Square> getEvenRowSquares(Color color, int initialId) {
        List<Square> evenSquares = new ArrayList<>();
        evenSquares.add(new Square(0, false, null));
        evenSquares.add(new Square(1, true, new Piece(PieceType.SINGLE, color, initialId)));
        evenSquares.add(new Square(2, false, null));
        evenSquares.add(new Square(3, true, new Piece(PieceType.SINGLE, color, initialId + 1)));
        evenSquares.add(new Square(4, false, null));
        evenSquares.add(new Square(5, true, new Piece(PieceType.SINGLE, color, initialId + 2)));
        evenSquares.add(new Square(6, false, null));
        evenSquares.add(new Square(7, true, new Piece(PieceType.SINGLE, color, initialId + 3)));
        return evenSquares;
    }

    @Override
    public Iterator iterator() {
        return this.rows.iterator();
    }


}
