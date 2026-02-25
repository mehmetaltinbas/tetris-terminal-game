package com.mehmetaltinbas.factory;

import com.mehmetaltinbas.core.TetrisGame;
import com.mehmetaltinbas.core.TetrisGameManager;
import com.mehmetaltinbas.core.TetrisMap;
import com.mehmetaltinbas.models.TetrisAction;
import com.mehmetaltinbas.models.TetrominoShape;
import com.mehmetaltinbas.ui.TetrisTextUI;

import java.util.*;

public class TetrisFactory {
    public static TetrisGameManager createTetrisGameManager(List<TetrisAction> debugActions, List<TetrominoShape> debugTetrominoes) {
        System.out.println("Allowed keyboard actions:");
        System.out.println("\t- move tetromino left: key 'q' or left arrow key");
        System.out.println("\t- move tetromino right: key 'e' or right arrow key");
        System.out.println("\t- rotate tetromino: key 'r'");
        System.out.println("\t- quit the game: key 'esc'");
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        int width;
        int height;

        do {
            System.out.print("Enter width of the map (8-99): ");
            width = scanner.nextInt();
        } while (width < 8 || width > 99);

        do {
            System.out.print("Enter height of the map (8-99): ");
            height = scanner.nextInt();
        } while (height < 8 || height > 99);


        TetrisGame.Builder tetrisGameBuilder = TetrisGame.Builder.newBuilder();
        tetrisGameBuilder
                .setMap(new TetrisMap(height, width))
                .setUI(new TetrisTextUI())
                .setFactory(new TetrominoFactory(debugTetrominoes));

        TetrisGameManager.Builder tetrisGameManagerBuilder = TetrisGameManager.Builder.newBuilder();
        tetrisGameManagerBuilder
                .setGame(tetrisGameBuilder.build())
                .setDebugActionList(debugActions);

        return tetrisGameManagerBuilder.build();
    }
}
