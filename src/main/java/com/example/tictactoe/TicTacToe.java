package com.example.tictactoe;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.Random;

public class TicTacToe extends Application {

    private char currentPlayer = 'X';
    private Button[][] buttons = new Button[3][3];
    private Line winningLine = new Line();

    private boolean vsComputer = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        GridPane gridPane = createGridPane();
        root.setCenter(gridPane);
        root.setLeft(createButtonPane());

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = createButton();
                buttons[row][col] = button;
                gridPane.add(button, col, row);
            }
        }

        return gridPane;
    }

    private Button createButton() {
        Button button = new Button();
        button.setMinSize(100, 100);
        button.setOnAction(e -> handleButtonClick(button));
        return button;
    }

    private VBox createButtonPane() {
        VBox buttonPane = new VBox(10);
        buttonPane.setAlignment(Pos.CENTER);

        Button playerVsPlayerButton = new Button("1v1 Game");
        playerVsPlayerButton.setOnAction(e -> startPlayerVsPlayerGame());

        Button vsComputerButton = new Button("Vs Computer");
        vsComputerButton.setOnAction(e -> startVsComputerGame());

        Button startGameButton = new Button("Start Game");
        startGameButton.setOnAction(e -> resetGame());

        buttonPane.getChildren().addAll(playerVsPlayerButton, vsComputerButton, startGameButton);

        return buttonPane;
    }

    private void handleButtonClick(Button button) {
        if (button.getText().isEmpty()) {
            button.setText(String.valueOf(currentPlayer));
            if (checkForWinner()) {
                announceWinner();
            } else if (isBoardFull()) {
                announceDraw();
            } else {
                switchPlayer();
                if (vsComputer && currentPlayer == 'O') {
                    makeComputerMove();
                }
            }
        }
    }

    private void startPlayerVsPlayerGame() {
        resetGame();
        vsComputer = false;
        showMessage("1v1 Game Selected");
    }

    private void startVsComputerGame() {
        resetGame();
        vsComputer = true;
        showMessage("Vs Computer Selected");

        // makeComputerMove();
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    private boolean checkForWinner() {
        if (checkRows() || checkColumns() || checkDiagonals()) {
            return true;
        }
        return false;
    }

    private boolean checkRows() {
        for (int row = 0; row < 3; row++) {
            if (checkLine(buttons[row][0], buttons[row][1], buttons[row][2])) {
                drawWinningLine(buttons[row][0], buttons[row][2]);
                return true;
            }
        }
        return false;
    }

    private boolean checkColumns() {
        for (int col = 0; col < 3; col++) {
            if (checkLine(buttons[0][col], buttons[1][col], buttons[2][col])) {
                drawWinningLine(buttons[0][col], buttons[2][col]);
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonals() {
        if (checkLine(buttons[0][0], buttons[1][1], buttons[2][2])) {
            drawWinningLine(buttons[0][0], buttons[2][2]);
            return true;
        } else if (checkLine(buttons[0][2], buttons[1][1], buttons[2][0])) {
            drawWinningLine(buttons[0][2], buttons[2][0]);
            return true;
        }
        return false;
    }

    private boolean checkLine(Button b1, Button b2, Button b3) {
        return !b1.getText().isEmpty() && b1.getText().equals(b2.getText()) && b2.getText().equals(b3.getText());
    }

    private void drawWinningLine(Button startButton, Button endButton) {
        // Check if the line is already added
        if (!((Pane) startButton.getScene().getRoot()).getChildren().contains(winningLine)) {
            winningLine.setStroke(Color.RED);
            winningLine.setStrokeWidth(5);

            // Get the root pane
            Parent root = startButton.getScene().getRoot();

            // Set the line coordinates
            double startX = startButton.localToScene(startButton.getBoundsInLocal()).getMinX()
                    + startButton.localToScene(startButton.getBoundsInLocal()).getWidth() / 2.0;
            double startY = startButton.localToScene(startButton.getBoundsInLocal()).getMinY()
                    + startButton.localToScene(startButton.getBoundsInLocal()).getHeight() / 2.0;

            double endX = endButton.localToScene(endButton.getBoundsInLocal()).getMinX()
                    + endButton.localToScene(endButton.getBoundsInLocal()).getWidth() / 2.0;
            double endY = endButton.localToScene(endButton.getBoundsInLocal()).getMinY()
                    + endButton.localToScene(endButton.getBoundsInLocal()).getHeight() / 2.0;

            // Set the line coordinates
            winningLine.setStartX(startX);
            winningLine.setStartY(startY);
            winningLine.setEndX(endX);
            winningLine.setEndY(endY);

            // Add the line to the root pane
            ((Pane) root).getChildren().add(winningLine);
        }
    }


    private boolean isBoardFull() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                if (button.getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void announceWinner() {
        System.out.println("Player " + currentPlayer + " wins!");

        showGameOverAlert("Player " + currentPlayer + " wins!");
        resetGame();
    }


    private void announceDraw() {
        System.out.println("It's a draw!");
        showGameOverAlert("It's a draw!");
        resetGame();
    }

    private void showGameOverAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void resetGame() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setText("");
            }
        }

        // Remove the winning line from the root pane
        Parent root = buttons[0][0].getScene().getRoot();
        ((Pane) root).getChildren().remove(winningLine);

        winningLine.setStartX(0);
        winningLine.setStartY(0);
        winningLine.setEndX(0);
        winningLine.setEndY(0);

        currentPlayer = 'X';
    }

    private void showMessage(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Mode");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void makeComputerMove() {
        int[] bestMove = minimax(2, 'O');

        buttons[bestMove[0]][bestMove[1]].setText("O");

        if (checkForWinner()) {
            announceWinner();
        } else if (isBoardFull()) {
            announceDraw();
        } else {
            switchPlayer();
        }
    }

    private int[] minimax(int depth, char player) {
        int[] bestMove = new int[]{ -1, -1, (player == 'O') ? Integer.MIN_VALUE : Integer.MAX_VALUE };

        int bestScore = (player == 'O') ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().isEmpty()) {
                    buttons[i][j].setText(String.valueOf(player));

                    int score = (player == 'O') ? minimax(depth - 1, 'X')[2] : minimax(depth - 1, 'O')[2];

                    buttons[i][j].setText("");

                    if ((player == 'O' && score > bestScore) || (player == 'X' && score < bestScore)) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }

        return bestMove;
    }
}
