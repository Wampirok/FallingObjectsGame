package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class HelloApplication extends Application {

    private static final int screenSizeX =600;
    private static final int screenSizeY =800;
    private static final int circleRadius = 40;
    private static final int playerPosition =screenSizeY - circleRadius;
    private static final int fruitSpeed = 4;
    private static final int spawnTime = 1000;
    private Timeline fruitTimeLine =null;
    private Circle player;
    public int score = 0;
    private boolean isCounted;

    @Override
    public void start(Stage stage) throws IOException {

        Pane pane = new Pane();
        Group root = new Group();
        player = new Circle(screenSizeX/2,playerPosition,circleRadius, Color.ORCHID);
        root.getChildren().add(player);
        Scene scene = new Scene(pane, screenSizeX,screenSizeY);
        Image backgroundImage = new Image( getClass().getResource("/images/background.jpg").toExternalForm());
        BackgroundImage myBI = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(screenSizeX, screenSizeY, false, false, false, false)
        );

        pane.setBackground(new Background(myBI));

        Background background = new Background(myBI);
        pane.setBackground(background);
        pane.getChildren().add(root);


        stage.setScene(scene);
        stage.show();
        scene.setOnKeyPressed(this::handleKeys);

        Text scoreDisplay = new Text("Your score: " + score);
        scoreDisplay.setX(5);
        scoreDisplay.setY(50);
        scoreDisplay.setFont(Font.font("Helvetica", FontWeight.BOLD,30));
        scoreDisplay.setFill(Color.LIGHTPINK);

        root.getChildren().add(scoreDisplay);


        fruitTimeLine = new Timeline(new KeyFrame(Duration.millis(spawnTime), event -> {
            Circle fruit = new Circle(new Random().nextInt(screenSizeX - circleRadius), 0,20, Color.DARKSEAGREEN);
            root.getChildren().add(fruit);
            Timeline fallTimeLine = new Timeline(new KeyFrame(Duration.millis(15),e -> {
                fruit.setCenterY(fruit.getCenterY()+fruitSpeed);
                if (fruit.getCenterY() == playerPosition){

                    if (isOverlap(player,fruit)){

                        root.getChildren().remove(fruit);
                        //score++;
                        //getScore();
                        isCounted = false;
                        scoreDisplay.setText("Your score: " + getScore());
                    } else {
                        fruitTimeLine.play();
                    }
                }
            }));
            fallTimeLine.setCycleCount(Timeline.INDEFINITE);
            fallTimeLine.play();
        }));
        scoreDisplay.setText("Your score: " + score);
        fruitTimeLine.setCycleCount(Timeline.INDEFINITE);
        fruitTimeLine.play();
    }

    public static void main(String[] args) {
        launch();
    }
    public void handleKeys(KeyEvent event){
        if (event.getCode()== KeyCode.LEFT && player.getCenterX() >circleRadius){
            player.setCenterX(player.getCenterX()-15);
        } else if (event.getCode() == KeyCode.RIGHT&& player.getCenterX()<screenSizeX-circleRadius){
            player.setCenterX(player.getCenterX()+15);
        }
    }

    private boolean isOverlap(Shape shape1, Shape shape2){
        return Shape.intersect(shape1,shape2).getBoundsInLocal().getWidth() != -1;
    }
    public int getScore(){
        if (!isCounted){
            score = score+1;
            isCounted = true;
       }
        return score;
    }

}