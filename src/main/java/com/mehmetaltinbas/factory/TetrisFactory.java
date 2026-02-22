package com.mehmetaltinbas.factory;

import com.mehmetaltinbas.core.TetrisGame;
import com.mehmetaltinbas.core.TetrisGameManager;
import com.mehmetaltinbas.core.TetrisMap;
import com.mehmetaltinbas.models.Cell;
import com.mehmetaltinbas.models.TetrisAction;
import com.mehmetaltinbas.models.Tetromino;
import com.mehmetaltinbas.models.TetrominoType;
import com.mehmetaltinbas.ui.TetrisTextUI;

import java.util.*;

public class TetrisFactory {
    static Tetromino currentTetromino;
    static int currentTetrominoStartRow;
    static int currentTetrominoStartColumn;
    static Set<Cell> currentTetrominoOccupiedCells = new HashSet<>();

    public static TetrisGameManager createTetrisGameManager(List<TetrisAction> actions, List<TetrominoType> nextTetrominoes) {
        TetrisGame.Builder tetrisGameBuilder = TetrisGame.Builder.newBuilder(new CompleteTetrisGame());
        tetrisGameBuilder = tetrisGameBuilder.setMap(new TetrisMap(8, 8));
        tetrisGameBuilder = tetrisGameBuilder.setUI(new TetrisTextUI());
        tetrisGameBuilder = tetrisGameBuilder.setFactory(new TetrominoFactory(nextTetrominoes));

        TetrisGameManager.Builder tetrisGameManagerBuilder = TetrisGameManager.Builder.newBuilder();
        tetrisGameManagerBuilder = tetrisGameManagerBuilder.setGame(tetrisGameBuilder.build());
        tetrisGameManagerBuilder = tetrisGameManagerBuilder.setActionList(actions);

        return tetrisGameManagerBuilder.build();
    }

    static private class CompleteTetrisGame extends TetrisGame {
        public CompleteTetrisGame() {
            super();
        }

        @Override
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

        @Override
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

        // clockwise rotate
        @Override
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

        @Override
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

        @Override
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
    }
}
