package com.mehmetaltinbas.ui;

import com.mehmetaltinbas.core.TetrisMap;

public class TetrisTextUI implements TetrisUI {
    @Override
    public void draw(TetrisMap tetrisMap, int score, int tickingIntervalMiliseconds) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("  *");
        for (int j = 0; j < tetrisMap.getWidth(); j++) {
            stringBuilder.append("-");
        }
        stringBuilder.append("*");
        stringBuilder.append(System.lineSeparator());

        for (int i = tetrisMap.getHeight() - 1; i >= 0; i--) {
            stringBuilder.append(i < 10 ? "0" + i : i);
            stringBuilder.append("|");
            for (int j = 0; j < tetrisMap.getWidth(); j++) {
                stringBuilder.append(tetrisMap.getCell(i, j).isOccupied() ? "X" : " ");
            }
            stringBuilder.append("|");
            stringBuilder.append(System.lineSeparator());
        }

        stringBuilder.append("  *");
        for (int j = 0; j < tetrisMap.getWidth(); j++) {
            stringBuilder.append("-");
        }
        stringBuilder.append("*");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("Score: ").append(score);
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("Time Interval: ").append(tickingIntervalMiliseconds).append("ms");
        stringBuilder.append(System.lineSeparator());

        System.out.println(stringBuilder);
    }

    @Override
    public void gameLost(int score) {
        System.out.println("Game Lost !!!");
        System.out.println("Final Score: " + score);
    }
}
