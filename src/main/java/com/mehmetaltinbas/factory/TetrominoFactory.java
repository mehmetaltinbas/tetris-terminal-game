package com.mehmetaltinbas.factory;

import com.mehmetaltinbas.models.Tetromino;
import com.mehmetaltinbas.models.TetrominoShape;

import java.util.*;

public class TetrominoFactory {
    private final HashMap<TetrominoShape, Tetromino> tetrominoesMap;
    private final List<TetrominoShape> debugTetrominoes;
    private int currentTetrominoesIndex = -1;

    public TetrominoFactory(List<TetrominoShape> debugTetrominoes) {
        this.debugTetrominoes = debugTetrominoes;
        tetrominoesMap = createMap();
    }

    private HashMap<TetrominoShape, Tetromino> createMap() {
        final HashMap<TetrominoShape, Tetromino> tetrominoesMap;
        tetrominoesMap = new HashMap<>();
        tetrominoesMap.put(TetrominoShape.S, new Tetromino(new String[]{
            ".##",
            "##.",
            "...",
        }));
        tetrominoesMap.put(TetrominoShape.Z, new Tetromino(new String[]{
            "##.",
            ".##",
            "...",
        }));
        tetrominoesMap.put(TetrominoShape.O, new Tetromino(new String[]{
            "##",
            "##"
        }));
        tetrominoesMap.put(TetrominoShape.T, new Tetromino(new String[]{
            ".#.",
            "###",
            "..."
        }));
        tetrominoesMap.put(TetrominoShape.L, new Tetromino(new String[]{
            ".#.",
            ".#.",
            ".##",
        }));
        tetrominoesMap.put(TetrominoShape.J, new Tetromino(new String[]{
            ".#.",
            ".#.",
            "##.",
        }));
        tetrominoesMap.put(TetrominoShape.I, new Tetromino(new String[]{
            ".#..",
            ".#..",
            ".#..",
            ".#.."
        }));
        return tetrominoesMap;
    }

    public Tetromino createTetromino(TetrominoShape tetrominoShape) {
        return new Tetromino(tetrominoesMap.get(tetrominoShape));
    }

    public Tetromino getNext() {
        return createTetromino(debugTetrominoes.get(++currentTetrominoesIndex));
    }

    public Tetromino getRandom() {
        TetrominoShape[] shapes = tetrominoesMap.keySet().toArray(new TetrominoShape[0]);

        int randomIndex = new Random().nextInt(shapes.length);

        return createTetromino(shapes[randomIndex]);
    }
}
