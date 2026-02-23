package com.mehmetaltinbas.ui;

import com.mehmetaltinbas.models.TetrisAction;
import com.mehmetaltinbas.models.TetrominoShape;

import java.util.*;

public class TetrisTextInput {
    public static List<TetrisAction> parseDebugActions(String actionsString) {
        ArrayList<TetrisAction> actions = new ArrayList<>();

        for (String actionString : actionsString.split(" ")) {
            switch (actionString) {
                case "T" -> actions.add(TetrisAction.Tick);
                case "R" -> actions.add(TetrisAction.Right);
                case "L" -> actions.add(TetrisAction.Left);
                case "O" -> actions.add(TetrisAction.Rotate);
            }
        }

        return actions;
    }

    public static List<TetrominoShape> parseDebugTetrominoes(String tetrominoesString) {
        ArrayList<TetrominoShape> tetrominoes = new ArrayList<>();

        for (String tetrominoString : tetrominoesString.split(" ")) {
            switch (tetrominoString) {
                case "T" -> tetrominoes.add(TetrominoShape.T);
                case "O" -> tetrominoes.add(TetrominoShape.O);
                case "S" -> tetrominoes.add(TetrominoShape.S);
                case "Z" -> tetrominoes.add(TetrominoShape.Z);
                case "I" -> tetrominoes.add(TetrominoShape.I);
                case "J" -> tetrominoes.add(TetrominoShape.J);
                case "L" -> tetrominoes.add(TetrominoShape.L);
            }
        }
        return tetrominoes;
    }
}
