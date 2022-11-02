package game;

import com.sun.javafx.geom.Vec2d;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Cannon {
    private ArrayList<ShootingBall> bubbles = new ArrayList<>();
    private int bubbleNumber = 5;
    private Pane pane;
    private double cannonPositionX;
    private double cannonPositionY;
    private double windowWidth;
    private double windowHight;

    public Cannon(Pane pane, double windowWidth, double windowHight) {
        this.pane = pane;
        this.windowWidth = windowWidth;
        this.windowHight = windowHight;
    }

    public Vec2d getFirstBallPosition() {
        return new Vec2d(bubbles.get(0).positionX, bubbles.get(0).positionY);
    }

    public Vec2d getCannonPosition(){
        return new Vec2d(cannonPositionX, cannonPositionY);
    }

    public ShootingBall getFirstBall() {
        return bubbles.get(0);
    }

    public ImageView getImage() {
        return bubbles.get(0).getImage();
    }

    public void draw() {
        for (int i = 0; i < bubbleNumber; i++) {
            ShootingBall bubble = new ShootingBall(pane);
            if (i == 0) {
                bubble.setPosition(windowWidth / 2 - Bubble.getRadius(), windowHight - 4 * Bubble.getRadius());
            } else {
                bubble.setPosition(windowWidth - (bubbleNumber - i) * 2.5 * Bubble.getRadius(), windowHight - 4 * Bubble.getRadius());
            }
            bubble.setRandomColor();
            bubble.draw();
            bubbles.add(bubble);
        }
        cannonPositionX = bubbles.get(0).getPositionX();
        cannonPositionY = bubbles.get(0).getPositionY();
    }

    public void removeFirstBall() {
        bubbles.remove(0);
        if(bubbles.isEmpty()){
            draw();
        }
        else {
            bubbles.get(0).setPosition(cannonPositionX, cannonPositionY);
            bubbles.get(0).setPosition();
        }
    }

    @Override
    public String toString() {
        return "Cannon{" +
                "FirstBubble = [" + bubbles.get(0).getPositionX() + ", " + bubbles.get(0).getPositionY() + "]" +
                '}';
    }
}
