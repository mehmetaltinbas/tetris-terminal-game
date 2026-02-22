package com.mehmetaltinbas.core;

import com.mehmetaltinbas.factory.TetrominoFactory;
import com.mehmetaltinbas.models.Cell;
import com.mehmetaltinbas.models.TetrisAction;
import com.mehmetaltinbas.models.Tetromino;
import com.mehmetaltinbas.ui.TetrisUI;

import java.util.HashSet;
import java.util.Set;

public class TetrisGame {
    protected TetrisMap map;
    protected TetrisUI ui;
    protected TetrominoFactory tetrominoFactory;
    protected Tetromino currentTetromino;
    protected int currentTetrominoStartRow;
    protected int currentTetrominoStartColumn;
    protected Set<Cell> currentTetrominoOccupiedCells = new HashSet<>();

    private TetrisGame() {
    }

    public void drawScreen() {
        ui.draw(map);
    }

    private void gameLost() {
        ui.gameLost();
    }

    protected Tetromino getNextTetromino() {
        return tetrominoFactory.getNext();
    }

    protected Cell getPlacementCell(Tetromino tetromino) {
        int halfTetrominoWidth = tetromino.getScale() / 2;
        int halfBoardWidth = map.getWidth() / 2;

        int startRow = map.getHeight() - tetromino.getScale();
        int startCol = halfBoardWidth - halfTetrominoWidth;

        return map.getCell(startRow, startCol);
    }

    protected boolean checkTetromino(Tetromino tetromino, int row, int column) {
        boolean[][] shape = tetromino.getTetrominoMap();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j]) {
                    int targetRow = row + i;
                    int targetCol = column + j;

                    Cell mapCell = map.getCell(targetRow, targetCol);
                    if (mapCell == null || mapCell.isOccupied()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void startGame() {
        Tetromino tetromino = getNextTetromino();
        Cell cell = getPlacementCell(tetromino);

        if (!placeTetromino(tetromino, cell.getRow(), cell.getColumn())) {
            gameLost();
        } else {
            drawScreen();
        }
    }

    public boolean resolveAction(TetrisAction action) {
        switch (action) {
            case Left -> left();
            case Right -> right();
            case Rotate -> rotate();
            case Tick -> {
                boolean success = tick();
                if (!success) {
                    gameLost();
                    return false;
                }
            }
        }

        drawScreen();

        return true;
    }

    protected boolean placeTetromino(Tetromino tetromino, int row, int column) {
        if (!tetromino.equals(currentTetromino)) {
            currentTetrominoOccupiedCells.clear();
        } else {
            for (Cell cell : currentTetrominoOccupiedCells) {
                cell.setOccupied(false);
            }
        }

        currentTetromino = tetromino;

        currentTetrominoStartRow = row;
        currentTetrominoStartColumn = column;

        boolean[][] tetrominoMap = currentTetromino.getTetrominoMap();

        for (boolean[] tetrominoMapRow : tetrominoMap) {
            for (boolean tetrominoMapCell : tetrominoMapRow) {
                if (tetrominoMapCell) {
                    Cell cell = map.getCell(row, column);

                    if (cell == null) return false;

                    if (cell.isOccupied()) {
                        if (!currentTetrominoOccupiedCells.contains(cell)) return false;
                    }

                    cell.setOccupied(true);
                    currentTetrominoOccupiedCells.add(cell);
                }

                column++;
            }

            column -= tetrominoMap.length;

            row++;
        }

        return true;
    }

    protected boolean tick() {
        int scale = currentTetromino.getScale();

        boolean[][] tetrominoMap = currentTetromino.getTetrominoMap();

        int row = currentTetrominoStartRow + scale - 1;

        for (int i = scale - 1; i >= 0; i --) {
            boolean[] tetrominoMapRow = tetrominoMap[i];

            int column = currentTetrominoStartColumn;
            row--;

            for (boolean tetrominoMapCell : tetrominoMapRow) {
                if (tetrominoMapCell) {
                    Cell cell = map.getCell(row, column);

                    if (cell == null) {
                        Tetromino newTetromino = getNextTetromino();

                        Cell placementCell = getPlacementCell(newTetromino);

                        return placeTetromino(newTetromino, placementCell.getRow(), placementCell.getColumn());
                    }

                    if (cell.isOccupied()) {
                        if (!currentTetrominoOccupiedCells.contains(cell)) {
                            Tetromino newTetromino = getNextTetromino();

                            Cell placementCell = getPlacementCell(newTetromino);

                            return placeTetromino(newTetromino, placementCell.getRow(), placementCell.getColumn());
                        }
                    }
                }

                column++;
            }
        }


        return placeTetromino(currentTetromino, currentTetrominoStartRow - 1, currentTetrominoStartColumn);
    }

    protected void rotate() {
        boolean[][] currentTetrominoMap = currentTetromino.getTetrominoMap();

        int scale = currentTetromino.getScale();

        // creating copy of tetromino map
        boolean[][] rotatedTetrominoMap = new boolean[scale][scale];

        // rotating the copy
        for (int i = 0; i < scale; i++) {
            boolean[] tetrominoMapRow = currentTetrominoMap[i];

            for (int j = 0; j < scale; j++) {
                boolean tetrominoMapCell = tetrominoMapRow[j];
                rotatedTetrominoMap[scale - 1 - j][i] = tetrominoMapCell;
            }
        }

        boolean isRotatable = true;

        // checking if any collisions, if there is set isRotatable to false
        int row = currentTetrominoStartRow;
        int column = currentTetrominoStartColumn;

        for (boolean[] tetrominoMapRow : rotatedTetrominoMap) {
            if (!isRotatable) break;

            for (boolean tetrominoMapCell : tetrominoMapRow) {
                if (tetrominoMapCell) {
                    Cell cell = map.getCell(row, column);

                    if (cell == null) {
                        isRotatable = false;
                        break;
                    }

                    if (cell.isOccupied()) {
                        if (!currentTetrominoOccupiedCells.contains(cell)) {
                            isRotatable = false;
                            break;
                        }
                    }
                }

                column++;
            }

            column -= scale;

            row++;
        }

        // if rotatable, actually rotating the tetromino map and placing tetromino
        if (isRotatable) {
            System.arraycopy(rotatedTetrominoMap, 0, currentTetrominoMap, 0, scale);

            placeTetromino(currentTetromino, currentTetrominoStartRow, currentTetrominoStartColumn);
        }
    }

    protected void right() {
        boolean[][] tetrominoMap = currentTetromino.getTetrominoMap();

        int row = currentTetrominoStartRow;

        boolean isMoveableToRight = true;

        for (boolean[] tetrominoMapRow : tetrominoMap) {
            int column = currentTetrominoStartColumn + 1;

            for (boolean tetrominoMapCell : tetrominoMapRow) {
                if (tetrominoMapCell) {
                    Cell cell = map.getCell(row, column);

                    if (cell == null) {
                        isMoveableToRight = false;
                        break;
                    }

                    if (cell.isOccupied()) {
                        if (!currentTetrominoOccupiedCells.contains(cell)) {
                            isMoveableToRight = false;
                            break;
                        }
                    }
                }

                column++;
            }
        }

        if (isMoveableToRight) {
            placeTetromino(currentTetromino, currentTetrominoStartRow, currentTetrominoStartColumn + 1);
        }
    }

    protected void left() {
        boolean[][] tetrominoMap = currentTetromino.getTetrominoMap();

        int row = currentTetrominoStartRow;

        boolean isMoveableToLeft = true;

        for (boolean[] tetrominoMapRow : tetrominoMap) {
            int column = currentTetrominoStartColumn + currentTetromino.getScale() - 2;

            for (int j = tetrominoMapRow.length - 1; j >= 0; j--) {
                boolean tetrominoMapCell = tetrominoMapRow[j];

                if (tetrominoMapCell) {
                    Cell cell = map.getCell(row, column);

                    if (cell == null) {
                        isMoveableToLeft = false;
                        break;
                    }

                    if (cell.isOccupied()) {
                        if (!currentTetrominoOccupiedCells.contains(cell)) {
                            isMoveableToLeft = false;
                            break;
                        }
                    }
                }

                column--;
            }
        }

        if (isMoveableToLeft) {
            placeTetromino(currentTetromino, currentTetrominoStartRow, currentTetrominoStartColumn - 1);
        }
    }

    public static class Builder {
        private final TetrisGame game;

        private Builder() {
            this.game = new TetrisGame();
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder setMap(TetrisMap map) {
            game.map = map;
            return this;
        }

        public Builder setUI(TetrisUI ui) {
            game.ui = ui;
            return this;
        }

        public Builder setFactory(TetrominoFactory factory) {
            game.tetrominoFactory = factory;
            return this;
        }

        public TetrisGame build() {
            return game;
        }
    }
}
