package game;

import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class BubbleRows {
    private int rowNumber = 8;
    private static int columnNumber;
    private double windowWidth;
    private Pane pane;
    private ArrayList<Bubble> bubbles = new ArrayList<>();

    public BubbleRows(double windowWidth, Pane pane) {
        this.windowWidth = windowWidth;
        this.pane = pane;
    }

    private void setColumnNumber() {
        columnNumber = (int) ((windowWidth - Bubble.getRadius() / 2) / (2 * Bubble.getRadius()));
    }

    public void draw() {
        this.setColumnNumber();
        double positionX = 0;
        double positionY = 0;
        for (int i = 0; i < rowNumber; i++) {
            if (i % 2 == 1) {
                positionX += Bubble.getRadius();
            }
            for (int j = 0; j < columnNumber; j++) {
                Bubble tempBubble = new Bubble(pane);
                tempBubble.setPosition(positionX, positionY);
                tempBubble.setRandomColor();
                tempBubble.draw();
                bubbles.add(tempBubble);
                positionX += 2 * Bubble.getRadius();
            }
            positionY += 2 * Bubble.getRadius() * 2.5 / 3;
            positionX = 0;
        }
        for (Bubble bubble : bubbles) {
            bubble.startChain();
            //System.out.println(bubble.toString());
        }
        for (int i = bubbles.size() - 1; i >= 0; i--) {
            bubbles.get(i).completeChain();
            //System.out.println(bubbles.get(i).toString());
        }
        for (Bubble bubble : bubbles) {
            bubble.completeChain();
            //System.out.println(bubble.toString());
        }
        for (Bubble bubble : bubbles) {
            System.out.println(bubble.toString());
        }
    }
}
