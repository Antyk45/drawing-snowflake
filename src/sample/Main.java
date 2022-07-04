package sample;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static java.lang.Math.*;

public class Main extends Application {

    static final int WIDTH = 1200;
    static final int HEIGHT = 800;

    static Group group;
    static Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception{
        group = new Group();

        canvas = new Canvas(WIDTH, HEIGHT);

        ColorPicker colorPicker = new ColorPicker(Color.WHITE);
        colorPicker.setLayoutX(100);
        colorPicker.setLayoutY(50);

        Slider petals = new Slider(6, 12, 6);
        petals.setShowTickMarks(true);
        petals.setMinorTickCount(1);
        petals.setShowTickLabels(true);
        petals.setLayoutX(300);
        petals.setLayoutY(50);

        Spinner<Integer> brushSize = new Spinner<>(1, 10, 3, 1);
        brushSize.setLayoutX(500);
        brushSize.setLayoutY(50);

        Button save = new Button("Save");
        save.setLayoutX(900);
        save.setLayoutY(50);
        save.setOnMouseClicked(event -> {
            File screenshotFile = new File("screenshot" + LocalDateTime.now().toString().replace(":","-") + ".png");
            WritableImage image = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
            canvas.snapshot(null, image);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
            try {
                ImageIO.write(renderedImage, "png", screenshotFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button clear = new Button("Clear");
        clear.setLayoutX(700);
        clear.setLayoutY(50);
        clear.setOnAction(event -> {
            canvas.getGraphicsContext2D().clearRect(0, 0, WIDTH, HEIGHT);
        });

        group.getChildren().addAll(canvas, colorPicker, petals, brushSize, clear, save);

        Scene scene = new Scene(group, WIDTH, HEIGHT);
        scene.setFill(Color.BLACK);
        scene.setOnMouseDragged(event -> {
            draw(event.getSceneX(), event.getSceneY(), colorPicker.getValue(), brushSize.getValue(), (int) petals.getValue());
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Drawing snowflake");
        primaryStage.show();
    }

    private void draw(double x, double y, Color color, double brushSize, int petals) {
        double xx = x - WIDTH / 2;
        double yy = y - HEIGHT / 2;

        double angle = 2 * PI / petals;
        for (int p = 0; p < petals; p++) {
            double xxx = xx * cos(p*angle) - yy * sin(p*angle) + WIDTH / 2;
            double yyy = xx * sin(p*angle) + yy * cos(p*angle) + HEIGHT / 2;

            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(color);
            gc.fillOval(xxx, yyy, brushSize, brushSize);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
