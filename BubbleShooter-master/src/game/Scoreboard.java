package game;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Scoreboard {
    private SceneChanger sceneChanger = new SceneChanger();
    private Player player = new Player();
    private static BorderPane mainPane = new BorderPane();
    private static VBox centralPane = new VBox();
    private static Scene finishScene = new Scene(mainPane, 810, 810);
    private ArrayList<Double> scores = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();

    public void createScoreboard(String label) {
        //path to file
        String filePath = new File("").getAbsolutePath();

        Text scoreboardText = new Text(label);
        scoreboardText.setFont(Font.font("Verdana", 30));

        //top pane
        VBox topPane = new VBox();
        topPane.setAlignment(Pos.TOP_CENTER);
        topPane.getChildren().add(scoreboardText);


        //central pane
        centralPane.setAlignment(Pos.TOP_CENTER);
        centralPane.setSpacing(10);

        readFromFile(filePath);

        mainPane.setTop(topPane);
        mainPane.setCenter(centralPane);
        BorderPane.setMargin(topPane, new Insets(20, 0, 0, 0));
        BorderPane.setMargin(centralPane, new Insets(40, 0, 0, 0));
    }

    public void showScoreboard() {
        sceneChanger.setScene(finishScene);
    }

    public void showScoreboardWithMenuButton(Scene menuScene) {
        //menu button
        Button menuButton = new Button("Menu");
        menuButton.setOnAction(e -> sceneChanger.setScene(menuScene));

        //bottomPane
        VBox bottomPane = new VBox();
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setSpacing(10);
        bottomPane.getChildren().add(menuButton);

        mainPane.setBottom(bottomPane);
        sceneChanger.setScene(finishScene);
    }

    public void readFromFile(String filePath) {
        centralPane.getChildren().clear();
        scores.clear();
        names.clear();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath + "\\src\\Scoreboard\\scoreboard.txt"))) {
            int i = 1;
            for (String line; (line = bufferedReader.readLine()) != null; i++) {
                Scanner scanner = new Scanner(line);
                scanner.useLocale(Locale.US);
                Text text1 = new Text(i + ". " + line);
                text1.setFont(Font.font("Verdana", 15));
                centralPane.getChildren().add(text1);

                if (scanner.hasNextDouble()) {
                    scores.add(scanner.nextDouble());
                } else {
                    names.add(scanner.next());
                }
                names.add(line);
            }
        } catch (IOException exception) {
            System.out.println("Exception in method createScoreboard");
        }
        System.out.println(scores.size());
        System.out.println(names.size());

    }

    public void addScore() {
        //bottom pane
        VBox bottomPane = new VBox();
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setSpacing(10);
        mainPane.setBottom(bottomPane);

        //exit button
        Button exitButton = new Button("Exit");
        exitButton.setFont(Font.font("Verdana", 15));
        exitButton.setOnAction(event -> Platform.exit());

        Text userScore = new Text("Your score: " + player.getScore());
        userScore.setFont(Font.font("Verdana", 15));

        String filePath = new File("").getAbsolutePath();
        readFromFile(filePath);

        if (scores.get(scores.size() - 1) < player.getScore()) {
            int position = -1;
            for (int i = scores.size() - 1; i >= 0; i--) {
                if (player.getScore() > scores.get(i)) {
                    position = i;
                }
            }
            if (position <= 10) {

                TextField userName = new TextField();
                userName.setAlignment(Pos.CENTER);
                userName.setPrefColumnCount(20);
                userName.setPrefWidth(100);
                userName.setMaxWidth(200);
                userName.setPromptText("Name");
                userName.setFont(Font.font("Verdana", 15));

                bottomPane.getChildren().addAll(userScore, userName, exitButton);
                BorderPane.setMargin(bottomPane, new Insets(0, 0, 40, 0));
                final int fPosition = position;
                userName.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try (FileWriter fileWriter = new FileWriter(filePath + "\\src\\Scoreboard\\scoreboard.txt")) {
                            for (int i = 0; i < 10; i++) {
                                if (i < fPosition) {
                                    fileWriter.write(names.get(i) + "\n");
                                } else if (i == fPosition) {
                                    fileWriter.write(player.getScore() + " " + userName.getText() + "\n");
                                } else {
                                    fileWriter.write(names.get(i - 1) + "\n");
                                }
                            }
                        } catch (IOException exception) {
                            System.out.println("IOException in method addScore.");
                        }
                        readFromFile(filePath);
                        bottomPane.getChildren().removeAll(userScore, userName);
                        showScoreboard();
                    }
                });
            }
        }
    }
}
