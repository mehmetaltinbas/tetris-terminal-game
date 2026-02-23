package com.mehmetaltinbas.core;

import com.mehmetaltinbas.models.Cell;

public class TetrisMap {
    private final Cell[][] cells;
    private final int height;
    private final int width;

    public TetrisMap(int height, int width) {
        this.height = height;
        this.width = width;
        this.cells = new Cell[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    public Cell getCell(int row, int column) {
        if (row < 0 || row >= height || column < 0 || column >= width) {
            return null;
        }
        return cells[row][column];
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
