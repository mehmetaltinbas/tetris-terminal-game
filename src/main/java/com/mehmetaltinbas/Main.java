package com.mehmetaltinbas;

import com.mehmetaltinbas.core.TetrisGameManager;
import com.mehmetaltinbas.factory.TetrisFactory;
import com.mehmetaltinbas.models.TetrisAction;
import com.mehmetaltinbas.models.TetrominoType;
import com.mehmetaltinbas.ui.TetrisTextInput;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    static void main() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            // System.out.println("Enter Actions (e.g., T R L O):");
            // List<TetrisAction> actions = TetrisTextInput.parseActions(bufferedReader.readLine());

            // System.out.println("Enter Tetrominoes (e.g., I J L O):");
            // List<Tetrominoes> nextTetrominoes = TetrisTextInput.parseTetrominoes(bufferedReader.readLine());

            // for quick testing
            // String actionsInput = "T T R T L T";
            // String piecesInput = "I J O";
            String actionsInput = "T T L T T T T T T T T O O O";
            String piecesInput = "J Z";
            List<TetrisAction> actions = TetrisTextInput.parseActions(actionsInput);
            List<TetrominoType> nextTetrominoes = TetrisTextInput.parseTetrominoes(piecesInput);

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
