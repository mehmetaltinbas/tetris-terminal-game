package com.mehmetaltinbas.factory;

import com.mehmetaltinbas.core.TetrisGame;
import com.mehmetaltinbas.core.TetrisGameManager;
import com.mehmetaltinbas.core.TetrisMap;
import com.mehmetaltinbas.models.Cell;
import com.mehmetaltinbas.models.TetrisAction;
import com.mehmetaltinbas.models.Tetromino;
import com.mehmetaltinbas.models.TetrominoShape;
import com.mehmetaltinbas.ui.TetrisTextUI;

import java.util.*;

public class TetrisFactory {
    public static TetrisGameManager createTetrisGameManager(List<TetrisAction> actions, List<TetrominoShape> nextTetrominoes) {
        TetrisGame.Builder tetrisGameBuilder = TetrisGame.Builder.newBuilder();
        tetrisGameBuilder = tetrisGameBuilder.setMap(new TetrisMap(8, 8));
        tetrisGameBuilder = tetrisGameBuilder.setUI(new TetrisTextUI());
        tetrisGameBuilder = tetrisGameBuilder.setFactory(new TetrominoFactory(nextTetrominoes));

        TetrisGameManager.Builder tetrisGameManagerBuilder = TetrisGameManager.Builder.newBuilder();
        tetrisGameManagerBuilder = tetrisGameManagerBuilder.setGame(tetrisGameBuilder.build());
        tetrisGameManagerBuilder = tetrisGameManagerBuilder.setActionList(actions);

        return tetrisGameManagerBuilder.build();
    }
}
