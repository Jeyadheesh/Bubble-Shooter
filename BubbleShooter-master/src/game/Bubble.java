package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

//fix radius in getBoundary()

public class Bubble {
    protected ImageView image;
    protected String color;
    protected Pane pane;
    protected double positionX;
    protected double positionY;
    private boolean attached;
    protected static ArrayList<Bubble> bubbleList = new ArrayList<>();
    private ArrayList<Bubble> surroundedBubbles = new ArrayList<>();
    protected HashSet<Bubble> sameColorChain = new HashSet<>();
    protected static double radius = 18;

    public Bubble(Pane pane) {
        this.positionX = 0;
        this.positionY = 0;
        attached = true;
        this.pane = pane;
        if (this.getClass() != ShootingBall.class) {
            bubbleList.add(this);
        }
    }

    public static void setRadius(double radius) {
        Bubble.radius = radius;
    }

    public static double getRadius() {
        return radius;
    }

    public static ArrayList<Bubble> getBubbleList() {
        return bubbleList;
    }

    public void setImage(String filename) {
        Image newImage = new Image(filename);
        this.image = new ImageView(newImage);
        image.setFitHeight(2 * radius);
        image.setFitWidth(2 * radius);
    }

    public void setImage() {
        setImage("images/" + color + "_bubble.png");
    }

    public ImageView getImage() {
        return image;
    }

    public void setPosition(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPosition() {
        image.setTranslateX(positionX);
        image.setTranslateY(positionY);
    }

    public void draw() {
        pane.getChildren().add(image);
        setPosition();
    }

    public void setRandomColor() {
        String[] availableColors = {"blue", "green", "pink", "red", "yellow"};
        Random random = new Random();
        color = availableColors[random.nextInt(availableColors.length)];
        setImage("images/" + color + "_bubble.png");
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public HashSet<Bubble> getSameColorChain() {
        return sameColorChain;
    }

    public void startChain() {
        double distance;
        for (Bubble bubble : bubbleList) {
            distance = Math.sqrt(Math.pow((this.positionX - bubble.positionX), 2) + Math.pow((this.positionY - bubble.positionY), 2));
            if (distance <= 2 * radius) {
                surroundedBubbles.add(bubble);
                if (this.color.equals(bubble.getColor())) {
                    sameColorChain.add(bubble);
                }
            }
        }
    }

    public void completeChain() {
        HashSet<Bubble> tempList = new HashSet<>(sameColorChain);
        for (Bubble bubble : tempList) {
            sameColorChain.addAll(bubble.getSameColorChain());
        }
    }

    public ArrayList<Bubble> getSurroundedBubbles() {
        return surroundedBubbles;
    }

    public void remove() {
        pane.getChildren().remove(this.image);
        bubbleList.remove(this);
        HashSet<Bubble> tempList = new HashSet<>(sameColorChain);
        for (Bubble bubble : tempList) {
            pane.getChildren().remove(bubble.image);
            bubbleList.remove(bubble);
            bubble.sameColorChain.clear();
            bubble.surroundedBubbles.clear();
        }
        sameColorChain.clear();
        surroundedBubbles.clear();
    }

    public void removeThis(){
        pane.getChildren().remove(this.image);
        bubbleList.remove(this);
        sameColorChain.clear();
        surroundedBubbles.clear();
    }

    public boolean isAttached() {
        return attached;
    }

    public void setAttached(boolean attached) {
        this.attached = attached;
    }

    public String toString() {
        return " Position: [" + positionX + "," + positionY + "]" +
                " Radius = " + radius + " Surrounding amount = " + sameColorChain.size();
    }
}
