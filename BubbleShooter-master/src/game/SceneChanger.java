package game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SceneChanger {
    private static Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        SceneChanger.stage = stage;
    }

    public void setScene(Scene scene) {
        SceneChanger.stage.setScene(scene);
    }

    public void showScoreboard(String label) {
        //path to file
        String filePath = new File("").getAbsolutePath();
        Text scoreboardText = new Text(label);
        scoreboardText.setFont(Font.font("Verdana", 30));
        BorderPane mainPane = new BorderPane();
        //top pane
        VBox topPane = new VBox();
        topPane.setAlignment(Pos.TOP_CENTER);
        topPane.getChildren().add(scoreboardText);

        VBox centralPane = new VBox();
        centralPane.setAlignment(Pos.TOP_CENTER);
        centralPane.setSpacing(10);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath + "\\src\\Scoreboard\\scoreboard.txt"))) {
            int i = 1;
            for (String line; (line = bufferedReader.readLine()) != null; i++) {
                Text text1 = new Text(i + ". " + line);
                text1.setFont(Font.font("Verdana", 15));
                centralPane.getChildren().add(text1);
            }
        } catch (IOException exception) {
            System.out.println("Exception in method createScoreboard");
        }
        Text userScore = new Text("Your score: ");
        userScore.setFont(Font.font("Verdana", 15));

        TextField userName = new TextField("Name");
        userName.setAlignment(Pos.CENTER);
        userName.setPrefColumnCount(20);
        userName.setPrefWidth(100);
        userName.setMaxWidth(200);
        userName.setPromptText("Name");
        userName.setFont(Font.font("Verdana", 15));
        centralPane.getChildren().add(userName);
        userName.setOnAction(event -> System.out.println("Napisane"));

        mainPane.setTop(topPane);
        mainPane.setCenter(centralPane);
        BorderPane.setMargin(topPane, new Insets(20, 0, 0, 0));
        BorderPane.setMargin(centralPane, new Insets(40, 0, 0, 0));

        Scene finishScene = new Scene(mainPane, 810, 810);
        setScene(finishScene);
    }
}
