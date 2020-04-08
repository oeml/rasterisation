package com.emelyanova.ui;

import com.emelyanova.line_utils.Dot;
import com.emelyanova.rasterisation.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.lang.Math;
import java.util.List;
import java.util.Map;

public class Main extends Application {

    final ToggleGroup group = new ToggleGroup();
    final VBox radioButtonsVBox = new VBox();

    private void addRadioButtonToToggleGroup(String label, boolean selected) {
        RadioButton rb = new RadioButton(label);
        rb.setToggleGroup(group);
        rb.setSelected(selected);
        radioButtonsVBox.getChildren().addAll(rb);
    }

    private void fillToggleGroup() {
        Label algorithmLabel = new Label("Choose algorithm:");
        radioButtonsVBox.setPadding(new Insets(10, 0, 10, 0));
        radioButtonsVBox.getChildren().addAll(algorithmLabel);

        addRadioButtonToToggleGroup("Step-by-step", true);
        addRadioButtonToToggleGroup("DDA", false);
        addRadioButtonToToggleGroup("Bresenham (line)", false);
        addRadioButtonToToggleGroup("Bresenham (circle)", false);
    }

    final VBox lineInputVBox = new VBox();
    final GridPane lineInputGrid = new GridPane();

    final VBox circleInputVBox = new VBox();
    final GridPane circleInputGrid = new GridPane();

    private void addCoordinateInput(String prompt, GridPane grid, int hPos, int vPos) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setPrefWidth(80);
        textField.setMaxWidth(80);

        GridPane.setConstraints(textField, hPos, vPos + 1);
        grid.getChildren().addAll(textField);
    }

    private void fillLineInput() {
        lineInputGrid.setPadding(new Insets(10, 0, 10, 0));
        lineInputGrid.setVgap(5);
        lineInputGrid.setHgap(5);

        Label label = new Label("Line: ");
        GridPane.setConstraints(label, 0, 0);
        lineInputGrid.getChildren().addAll(label);

        addCoordinateInput("x0", lineInputGrid, 0, 0);
        addCoordinateInput("x1", lineInputGrid, 0, 1);
        addCoordinateInput("y0", lineInputGrid, 1, 0);
        addCoordinateInput("y1", lineInputGrid, 1, 1);
        lineInputVBox.getChildren().addAll(lineInputGrid);
    }

    private void fillCircleInput() {
        circleInputGrid.setPadding(new Insets(10, 0, 10, 0));
        circleInputGrid.setVgap(5);
        circleInputGrid.setHgap(5);

        Label label = new Label("Circle: ");
        GridPane.setConstraints(label, 0, 0);
        circleInputGrid.getChildren().addAll(label);

        addCoordinateInput("center X", circleInputGrid, 0, 0);
        addCoordinateInput("center Y", circleInputGrid, 0, 1);
        addCoordinateInput("radius", circleInputGrid, 0, 2);
        circleInputVBox.getChildren().addAll(circleInputGrid);
    }

    final Canvas canvas = new Canvas(GraphContext.SIZE, GraphContext.SIZE);

    private GraphContext graphCtx = new GraphContext();

    private void drawDot(Dot dot) {
        int x = dot.getX(), y = dot.getY();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int xBottomLeft = graphCtx.getXBottomLeft();
        int yTopRight = graphCtx.getYTopRight();
        gc.fillRect((x - xBottomLeft) * graphCtx.scale(), (yTopRight - y - 1) * graphCtx.scale(),
                graphCtx.scale(), graphCtx.scale());
    }

    long calcTime = -1;

    private void drawFigure(Rasteriser rasterisator) {
        long startTime = System.currentTimeMillis();
        rasterisator.rasterise();
        long endTime = System.currentTimeMillis();
        calcTime = endTime - startTime;
        List<Dot> dots = rasterisator.getResult();
        for (Dot dot : dots) {
            drawDot(dot);
        }
    }

    private void drawCoordinates() {
        int xBottomLeft = graphCtx.getXBottomLeft();
        int xTopRight = graphCtx.getXTopRight();
        int yBottomLeft = graphCtx.getYBottomLeft();
        int yTopRight = graphCtx.getYTopRight();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        int x, y;
        int w = xTopRight - xBottomLeft;
        int h = yTopRight - yBottomLeft;

        gc.setStroke(Color.LIGHTGREY);
        for (x = 0; x <= w; ++x) {
            gc.strokeLine(x * graphCtx.scale(), 0, x * graphCtx.scale(), GraphContext.SIZE);
        }
        for (y = 0; y <= h; ++y) {
            gc.strokeLine(0, y * graphCtx.scale(), GraphContext.SIZE, y * graphCtx.scale());
        }

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        int yAxisOffset = -1 * xBottomLeft * graphCtx.scale() + graphCtx.scale() / 2;
        gc.strokeLine(yAxisOffset, 0, yAxisOffset, canvas.getHeight());
        gc.strokeLine(yAxisOffset, 0, yAxisOffset - 5, 10);
        gc.strokeLine(yAxisOffset, 0, yAxisOffset + 5, 10);
        gc.setLineWidth(1);
        for (y = 0; y <= h; ++y) {
            double shift = 0;
            if (graphCtx.scale() <= 1 && y % 50 != 0) continue;
            else if (graphCtx.scale() <= 3 && y % 20 != 0) continue;
            else if (graphCtx.scale() <= 10 && y % 10 != 0) continue;
            else if (graphCtx.scale() <= 20 && y % 5 != 0) continue;
            else {
                shift = 0.5;
            }
            gc.strokeText(String.valueOf(yBottomLeft + y),
                    yAxisOffset + graphCtx.scale() / 4,
                    (h - y - shift) * graphCtx.scale());
        }

        gc.setLineWidth(2);
        int xAxisOffset = yTopRight * graphCtx.scale() - graphCtx.scale() / 2;
        gc.strokeLine(0, xAxisOffset, GraphContext.SIZE, xAxisOffset);
        gc.strokeLine(canvas.getWidth() - 10, xAxisOffset - 5, canvas.getWidth(), xAxisOffset);
        gc.strokeLine(canvas.getWidth() - 10, xAxisOffset + 5, canvas.getWidth(), xAxisOffset);
        gc.setLineWidth(1);
        for (x = 0; x <= w; ++x) {
            double shift = 0;
            if (graphCtx.scale() <= 1 && x % 50 != 0) continue;
            else if (graphCtx.scale() <= 3 && x % 20 != 0) continue;
            else if (graphCtx.scale() <= 10 && x % 10 != 0) continue;
            else if (graphCtx.scale() <= 20 && x % 5 != 0) continue;
            else {
                shift = 0.5;
            }
            gc.strokeText(String.valueOf(xBottomLeft + x),
                    (x + shift) * graphCtx.scale(),
                    xAxisOffset - graphCtx.scale() / 4);
        }
    }

    private void alert(String message) {
        Alert a = new Alert(Alert.AlertType.WARNING, message);
        a.show();
    }

    private void enlargeAndDrawCoordinates(int xBottomLeft, int yBottomLeft, int xTopRight, int yTopRight) {
        int w = xTopRight + 1 - xBottomLeft;
        int h = yTopRight + 1 - yBottomLeft;
        if (w > GraphContext.SIZE) {
            graphCtx.reset(xBottomLeft, yBottomLeft,
                    xBottomLeft + GraphContext.SIZE, yBottomLeft + GraphContext.SIZE);
            alert("The specified coordinates were too large. Displaying only portion");
        } else {
            graphCtx.reset(xBottomLeft, yBottomLeft, xTopRight + 1, yTopRight + 1);
        }
        drawCoordinates();
    }

    private void toSquareAndDrawCoordinates(int xBottomLeft, int yBottomLeft, int xTopRight, int yTopRight) {
        int deltaX = Math.abs(xTopRight - xBottomLeft);
        int deltaY = Math.abs(yTopRight - yBottomLeft);
        int padding = (Math.max(deltaX, deltaY) - Math.min(deltaX, deltaY)) / 2;

        if (deltaX > deltaY) {
            yBottomLeft -= padding;
            yTopRight += padding;
        } else if (deltaX < deltaY) {
            xBottomLeft -= padding;
            xTopRight += padding;
        }

        enlargeAndDrawCoordinates(xBottomLeft, yBottomLeft, xTopRight, yTopRight);
    }

    private int getInput(GridPane grid, int xPos, int yPos) {
        ObservableList<Node> children = grid.getChildren();
        for (Node node : children) {
            if (GridPane.getRowIndex(node) == yPos && GridPane.getColumnIndex(node) == xPos) {
                return Integer.parseInt(((TextField) node).getText());
            }
        }
        return -1;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Rasterisator");
        Scene scene = new Scene(new HBox(), 900, 730);

        fillToggleGroup();
        fillLineInput();
        fillCircleInput();

        VBox infoVBox = new VBox();
        Label scaleLabel = new Label("Scale: ");
        Label timeLabel = new Label("Time: ");
        infoVBox.getChildren().addAll(scaleLabel, timeLabel);

        Button goButton = new Button("Go");
        goButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RadioButton rb = (RadioButton) group.getSelectedToggle();
                String selected = rb.getText();
                if (selected.equals("Bresenham (circle)")) {
                    int cx = getInput(circleInputGrid, 0, 1);
                    int cy = getInput(circleInputGrid, 0, 2);
                    int r = getInput(circleInputGrid, 0, 3);
                    enlargeAndDrawCoordinates(cx - r, cy - r, cx + r, cy + r);
                    drawFigure(new BresenhamCircleRasteriser(cx, cy, r));
                } else {
                    int x0 = getInput(lineInputGrid, 0, 1);
                    int y0 = getInput(lineInputGrid, 1, 1);
                    int x1 = getInput(lineInputGrid, 0, 2);
                    int y1 = getInput(lineInputGrid, 1, 2);
                    toSquareAndDrawCoordinates(Math.min(x0, x1), Math.min(y0, y1),
                            Math.max(x0, x1), Math.max(y0, y1));
                    if (selected.equals("Step-by-step")) {
                        drawFigure(new StepByStepLineRasteriser(x0, y0, x1, y1));
                    } else if (selected.equals("DDA")) {
                        drawFigure(new DDALineRasteriser(x0, y0, x1, y1));
                    } else if (selected.equals("Bresenham (line)")) {
                        drawFigure(new BresenhamLineRasteriser(x0, y0, x1, y1));
                    }
                }
                scaleLabel.setText("Scale: " + graphCtx.scale());
                timeLabel.setText("Time: " + calcTime + " ms");
            }
        });

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.getChildren().addAll(radioButtonsVBox, lineInputVBox, goButton, infoVBox);
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle old, Toggle newValue) {
                RadioButton rb = (RadioButton) group.getSelectedToggle();
                String changedTo = rb.getText();
                if (changedTo.equals("Bresenham (circle)")) {
                    vBox.getChildren().set(1, circleInputVBox);
                } else {
                    vBox.getChildren().set(1, lineInputVBox);
                }
            }
        });

        ((HBox) scene.getRoot()).getChildren().addAll(vBox, canvas);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
