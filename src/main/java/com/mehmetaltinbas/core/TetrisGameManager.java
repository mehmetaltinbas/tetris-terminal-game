package com.mehmetaltinbas.core;

import com.mehmetaltinbas.models.TetrisAction;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TetrisGameManager {
    public static final boolean isDebugging = false;

    static volatile boolean isGameRunning = true;

    private TetrisGame game;
    private List<TetrisAction> debugActions;
    static final ConcurrentLinkedQueue<TetrisAction> actionQueue = new ConcurrentLinkedQueue<>();

    private TetrisGameManager() { }

    public void simulate() {
        if (game == null) {
            throw new IllegalStateException("Game must be initialized before simulation.");
        }

        game.startGame();

        if (!isDebugging) {
            startInputThread();

            while (isGameRunning) {

                if (!actionQueue.isEmpty()) {
                    TetrisAction action = actionQueue.poll();

                    if (!game.resolveAction(action)) isGameRunning = false;
                }
            }
        } else {
            for (TetrisAction action : debugActions) {
                System.out.println("currentAction: " + action);

                if (!game.resolveAction(action)) {
                    break;
                }
            }
        }
    }

    public void startInputThread() {
        Thread thread = new Thread(() -> {
            try (Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .jansi(true)
                    .build()
            ) {
                terminal.enterRawMode();

                NonBlockingReader nonBlockingReader = terminal.reader();

                boolean isRunning = true;

                while (isRunning) {
                    int keyCode = nonBlockingReader.read();

                    switch (keyCode) {
                        case 27: // Escape or start of Arrow Sequence
                            // Wait a very short time to see if more characters follow
                            int nextByte = nonBlockingReader.read(20L); // Wait 20ms for the next part of the sequence

                            if (nextByte <= 0) { // No following characters? It's the actual ESC key.
                                if (isDebugging) System.out.println("Esc pressed: Quitting...");
                                isGameRunning = false;
                                isRunning = false;
                            } else if (nextByte == 91) { // It's the start of an arrow sequence [
                                int arrowCode = nonBlockingReader.read();

                                if (arrowCode == 67) {
                                    actionQueue.add(TetrisAction.Right);
                                    if (isDebugging) System.out.println("Right Arrow");
                                }

                                if (arrowCode == 68) {
                                    actionQueue.add(TetrisAction.Left);
                                    if (isDebugging) System.out.println("Left Arrow");
                                }
                            }
                            break;
                        case 113: // 'q'
                            actionQueue.add(TetrisAction.Left);
                            if (isDebugging) System.out.println("Action: Left");
                            break;
                        case 101: // 'e'
                            actionQueue.add(TetrisAction.Right);
                            if (isDebugging) System.out.println("Action: Right");
                            break;
                        case 114: // 'r'
                            actionQueue.add(TetrisAction.Rotate);
                            if (isDebugging) System.out.println("Action: Rotate");
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.setDaemon(true); // Ensures thread closes when main ends

        thread.start();
    }

    public static class Builder {
        private final TetrisGameManager manager;

        private Builder() {
            manager = new TetrisGameManager();
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder setGame(TetrisGame game) {
            manager.game = game;
            return this;
        }

        public Builder setDebugActionList(List<TetrisAction> debugActions) {
            manager.debugActions = debugActions;
            return this;
        }

        public TetrisGameManager build() {
            if (manager.game == null || manager.debugActions == null) {
                throw new IllegalStateException("Manager requires both a Game and an Action List.");
            }
            return manager;
        }
    }
}
