package game;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class Main extends Application {

    Scene menuScene, gameScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {

        SceneChanger sceneChanger = new SceneChanger();
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.createScoreboard("Scoreboard");
        sceneChanger.setStage(window);

        Button gameButton = new Button("Play");
        gameButton.setOnAction(event -> sceneChanger.setScene(gameScene));

        Button scoreboardButton = new Button("Scoreboard");
        scoreboardButton.setOnAction(event -> scoreboard.showScoreboardWithMenuButton(menuScene));

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(event -> Platform.exit());

        //layout 1 - menu
        VBox paneScene1 = new VBox(80);
        paneScene1.setAlignment(Pos.CENTER);
        //paneScene1.setSpacing(80);
        paneScene1.getChildren().addAll(gameButton, scoreboardButton, exitButton);
        menuScene = new Scene(paneScene1, 800, 800);

        //menu button
        Button menuButton = new Button("Menu");
        menuButton.setOnAction(e -> window.setScene(menuScene));

        //Anchor pane
        Label label = new Label();
        Player player = new Player(label);
        ShootingBall.setPlayer(player);
        label.setText("Score: " + Double.toString(player.getScore()));
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().addAll(label, menuButton);
        AnchorPane.setLeftAnchor(label, 9.0);

        //layout 2 - game screen
        BorderPane borderPane = new BorderPane();
        Pane pane2 = new Pane();
        borderPane.setBottom(anchorPane);
        borderPane.setCenter(pane2);
        gameScene = new Scene(borderPane, 810, 700);
        AnchorPane.setRightAnchor(menuButton, (gameScene.getWidth() - 46) / 2);

        //finish line
        ImageView line = new ImageView("images/linia.png");
        pane2.getChildren().add(line);
        line.setTranslateX(0);
        line.setTranslateY(500);

        Cannon cannon = new Cannon(pane2, 810, 700);
        cannon.draw();
        AnimationController animationController = new AnimationController(gameScene, cannon);

        BubbleRows level1 = new BubbleRows(810, pane2);
        level1.draw();

        window.setTitle("Bubble Shooter");
        window.setScene(menuScene);
        window.show();
    }
}
