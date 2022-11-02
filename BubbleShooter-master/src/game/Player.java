package game;

import javafx.scene.control.Label;

public class Player {
    private static double score = 0;
    private String name;
    private Label label;

    public Player(Label label) {
        this.label = label;
    }

    public Player() {
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addScore(int shotBallsAmount) {
        score += shotBallsAmount * 100;
        if (shotBallsAmount > 3) {
            score += (double) shotBallsAmount / 2 * 100;
        }
        label.setText("Score: " + Double.toString(score));
    }

    public void addScoreForNotAttahed(int shotBallsAmount) {
        score += shotBallsAmount * 150;
        label.setText("Score: " + Double.toString(score));
    }
}
