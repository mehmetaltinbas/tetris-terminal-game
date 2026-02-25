package com.mehmetaltinbas.ui;

import com.mehmetaltinbas.core.TetrisMap;

public interface TetrisUI {
    void draw(TetrisMap tetrisMap, int score, int tickingIntervalMiliseconds);
    void gameLost(int score);
}
