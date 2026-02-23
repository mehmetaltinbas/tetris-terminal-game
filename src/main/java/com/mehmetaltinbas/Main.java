package com.mehmetaltinbas;

import com.mehmetaltinbas.core.TetrisGameManager;
import com.mehmetaltinbas.factory.TetrisFactory;
import com.mehmetaltinbas.models.TetrisAction;
import com.mehmetaltinbas.models.TetrominoShape;
import com.mehmetaltinbas.ui.TetrisTextInput;

import java.util.*;

public class Main {
    static void main() {
        try {
            String debugActions = "T T L T T T T T T T T O O O";
            String debugPieces = "J Z";

            List<TetrisAction> actions = TetrisTextInput.parseDebugActions(debugActions);
            List<TetrominoShape> nextTetrominoes = TetrisTextInput.parseDebugTetrominoes(debugPieces);

            TetrisGameManager manager = TetrisFactory.createTetrisGameManager(actions, nextTetrominoes);

            manager.simulate();

        } /*catch (IOException exception) {
            System.err.println("Input error: " + exception.getMessage());
        }*/ catch (Exception e) {
            System.err.println("Game error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
