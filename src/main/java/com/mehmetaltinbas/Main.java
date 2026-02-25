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
            //                    "T T T T T L L L , T T T T T , T T T T T, T T T T T";
            String debugActions = "T T T T T L L L T  T T T T T T L  T T R T T T T T  T T R R R T T T T T T  T T";
            String debugPieces = "O O O O Z T ";

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
