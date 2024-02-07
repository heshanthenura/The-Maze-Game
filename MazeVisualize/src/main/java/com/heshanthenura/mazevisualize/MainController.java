package com.heshanthenura.mazevisualize;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Stack;

public class MainController implements Initializable {
    @FXML
    Canvas canvas;
    GraphicsContext gc;
    int row = 20;
    int col = 20;

    double cellWidth;
    double cellHeight;
    double gap = 5; // Adjust gap size as needed
    Stack<Integer> visited = new Stack<>();
    Stack<Integer> position = new Stack<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {


            visited.add(0);
            position.add(0);
            gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.RED);
            gc.fillRect(0, 0, 600, 600);

            double totalGapWidth = (col - 1) * gap;
            double totalGapHeight = (row - 1) * gap;

            cellWidth = (canvas.getWidth() - totalGapWidth) / col;
            cellHeight = (canvas.getHeight() - totalGapHeight) / row;

            for (int r = 0; r < row; r++) {
                for (int c = 0; c < col; c++) {
                    double x = c * (cellWidth + gap);
                    double y = r * (cellHeight + gap);
                    gc.setFill(Color.WHITE);
                    gc.fillRect(x, y, cellWidth, cellHeight);
                }
            }

            new Thread(this::generateMaze).start();


        });

    }

    void generateMaze() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int currentPosition = position.peek();
            int n = getNeighbour(row, col, currentPosition, visited);

            if (n == -1) {
                Platform.runLater(() -> {
                    double drawX = (currentPosition % col) * (cellWidth + gap);
                    double drawY = (currentPosition / col) * (cellHeight + gap);
                    gc.setFill(Color.WHEAT);
//                    gc.fillRect(drawX, drawY, cellWidth, cellHeight);
                });
                position.pop();
            } else {
                int currentRow = currentPosition / col;
                int currentCol = currentPosition % col;
                int neighborRow = n / col;
                int neighborCol = n % col;

                double drawX1 = Math.min(currentCol, neighborCol) * (cellWidth + gap);
                double drawY1 = Math.min(currentRow, neighborRow) * (cellHeight + gap);
                double drawX2 = Math.abs(currentCol - neighborCol) * (cellWidth + gap) + cellWidth;
                double drawY2 = Math.abs(currentRow - neighborRow) * (cellHeight + gap) + cellHeight;

                Platform.runLater(() -> {
                    gc.setFill(Color.GREEN);
                    gc.fillRect(drawX1, drawY1, drawX2, drawY2);
                });

                visited.push(n);
                position.push(n);

                if (visited.size() == row * col) { // Check if all cells have been visited


                    Platform.runLater(() -> {
                        gc.setFill(Color.YELLOW);
                        gc.fillRect(0, 0, cellWidth, cellHeight);
                        gc.setFill(Color.YELLOW);
                        gc.fillRect((cellWidth+gap)*(col-1), (cellHeight+gap)*(row-1), cellWidth, cellHeight);
                    });

                    break;
                }
            }
        }
    }





    static int getNeighbour(int row, int col, int index, Stack<Integer> visited) {
        ArrayList<Integer> neighbours = new ArrayList<>();
        int rowStart = (index / col) * col;
        int rowEnd = rowStart + col - 1;

        int tn = index - col;
        int ln = index + 1;
        int bn = index + col;
        int rn = index - 1;

        if (tn >= 0 && !visited.contains(tn)) {
            neighbours.add(tn);
        }
        if (ln <= rowEnd && ln % col != 0 && !visited.contains(ln)) {
            neighbours.add(ln);
        }
        if (bn < row * col && !visited.contains(bn)) {
            neighbours.add(bn);
        }
        if (rn >= rowStart && rn % col != col - 1 && !visited.contains(rn)) {
            neighbours.add(rn);
        }

        if (neighbours.isEmpty()) {
            return -1;
        }

        int randomIndex = new Random().nextInt(neighbours.size());
        return neighbours.get(randomIndex);
    }
}
