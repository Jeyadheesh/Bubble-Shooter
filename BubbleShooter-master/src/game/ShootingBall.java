package game;

import com.sun.javafx.geom.Vec2d;
import javafx.scene.layout.Pane;

import java.util.HashSet;

public class ShootingBall extends Bubble {
    private double velocityX;
    private double velocityY;
    private Vec2d direction;
    private static Player player;
    private static EndGame endGame = new EndGame();


    public ShootingBall(Pane pane) {
        super(pane);
        this.velocityX = 0;
        this.velocityY = 0;
        this.positionX = 0;
        this.positionY = 0;
    }

    public static Player getPlayer() {
        return player;
    }

    public static void setPlayer(Player player) {
        ShootingBall.player = player;
    }

    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public Vec2d getDirection() {
        return direction;
    }

    public void setDirection(Vec2d direction) {
        this.direction = direction;
    }

    public boolean checkCollision() {
        double distance;
        for (Bubble bubble : bubbleList) {
            distance = Math.sqrt(Math.pow((this.positionX - bubble.positionX), 2) + Math.pow((this.positionY - bubble.positionY), 2));
            if (distance <= 2 * radius && bubble != this) {
                actionWhenHit(bubble);
                return true;
            }
        }
        return false;
    }

    public void actionWhenHit(Bubble bubble) {
        Bubble newBubble = new Bubble(this.pane);
        Vec2d newCoordinates = findClosestBubbleSpot(bubble);
        newBubble.setPosition(newCoordinates.x, newCoordinates.y);
        newBubble.setColor(this.color);
        newBubble.setImage();
        newBubble.draw();
        if (bubble.getColor().equals(this.color) && bubble.getSameColorChain().size() >= 2) {
            connectChains(newBubble);
            player.addScore(newBubble.getSameColorChain().size() + 1);
            this.remove();
            bubble.remove();
            newBubble.remove();
        } else {
            if (bubble.getColor().equals(newBubble.color)) {
                connectChains(newBubble);
                bubble.getSurroundedBubbles().add(newBubble);
                removeGroupOfBubbles(newBubble, 3);
                addToChain(bubble, newBubble);
            } else {
                newBubble.getSameColorChain().add(newBubble);
                System.out.println("NewBubble surrounding size = " + newBubble.getSameColorChain().size());
                bubble.getSurroundedBubbles().add(newBubble);
                connectChains(newBubble);
                removeGroupOfBubbles(newBubble, 3);
            }
        }
        endGame.finishGame(newBubble);
    }

    public Vec2d findClosestBubbleSpot(Bubble bubble) {
        double minDistance = 3 * Bubble.getRadius();
        Vec2d minCoordinates = new Vec2d(0, 0);
        Vec2d possiblePosition[];
        /*Vec2d possiblePosition[] = new Vec2d[]{
                new Vec2d(bubble.positionX - 2 * Bubble.getRadius(), bubble.positionY),
                new Vec2d(bubble.positionX + 2 * Bubble.getRadius(), bubble.positionY),
                new Vec2d(bubble.positionX - Bubble.getRadius(), bubble.positionY + 5 * Bubble.getRadius() / 3),
                new Vec2d(bubble.positionX + Bubble.getRadius(), bubble.positionY + 5 * Bubble.getRadius() / 3)};*/
        if (bubble.positionX == 0) {
            possiblePosition = new Vec2d[]{
                    new Vec2d(bubble.positionX + 2 * Bubble.getRadius(), bubble.positionY),
                    new Vec2d(bubble.positionX + Bubble.getRadius(), bubble.positionY + 5 * Bubble.getRadius() / 3)};
        } else if (bubble.positionX == 810 - Bubble.radius) {
            possiblePosition = new Vec2d[]{
                    new Vec2d(bubble.positionX - 2 * Bubble.getRadius(), bubble.positionY),
                    new Vec2d(bubble.positionX - Bubble.getRadius(), bubble.positionY + 5 * Bubble.getRadius() / 3)};
        } else {
            possiblePosition = new Vec2d[]{
                    new Vec2d(bubble.positionX - 2 * Bubble.getRadius(), bubble.positionY),
                    new Vec2d(bubble.positionX + 2 * Bubble.getRadius(), bubble.positionY),
                    new Vec2d(bubble.positionX - Bubble.getRadius(), bubble.positionY + 5 * Bubble.getRadius() / 3),
                    new Vec2d(bubble.positionX + Bubble.getRadius(), bubble.positionY + 5 * Bubble.getRadius() / 3)};
        }
        for (Vec2d point : possiblePosition) {
            double currentDistance = Math.sqrt(Math.pow((this.positionX - point.x), 2) + Math.pow((this.positionY - point.y), 2));
            if (currentDistance <= minDistance) {
                minCoordinates = point;
                minDistance = currentDistance;
            }
        }
        return minCoordinates;
    }

    public void actionWhenHitTopFrame() {
        Bubble newBubble = new Bubble(this.pane);
        newBubble.setPosition(findClosestLocationsX(), 0);
        newBubble.setColor(this.color);
        newBubble.setImage();
        newBubble.draw();
        newBubble.getSameColorChain().add(newBubble);
        connectChains(newBubble);
        if (newBubble.getSameColorChain().size() >= 3) {
            //player.addScore(newBubble.getSameColorChain().size());
            removeGroupOfBubbles(newBubble, 3);
        }
    }

    public double findClosestLocationsX() {
        int leftX = (int) positionX;
        int rightX = (int) positionX;
        double minX;
        while (leftX % (2 * radius) != 0) {
            leftX--;
        }
        while (rightX % (2 * radius) != 0) {
            rightX++;
        }
        if (positionX - leftX < rightX - positionX) {
            minX = leftX;
        } else {
            minX = rightX;
        }
        return minX;
    }

    public void addToChain(Bubble collidedBubble, Bubble newBubble) {
        System.out.println(collidedBubble.getSameColorChain().size());
        HashSet<Bubble> tempList = new HashSet<>(collidedBubble.getSameColorChain());
        for (Bubble bub : tempList) {
            bub.getSameColorChain().add(newBubble);
        }
        newBubble.getSameColorChain().addAll(collidedBubble.getSameColorChain());
        System.out.println(collidedBubble.getSameColorChain().size());
    }

    public void connectChains(Bubble newBubble) {
        double distance;
        for (Bubble bubble : bubbleList) {
            distance = Math.sqrt(Math.pow((newBubble.positionX - bubble.positionX), 2) + Math.pow((newBubble.positionY - bubble.positionY), 2));
            if (distance <= 2 * radius && bubble.getClass() == Bubble.class) {
                if (newBubble.getColor().equals(bubble.getColor())) {
                    newBubble.sameColorChain.addAll(bubble.sameColorChain);
                    bubble.sameColorChain.addAll(newBubble.sameColorChain);
                }
            }
        }
    }

    public void removeGroupOfBubbles(Bubble newBubble, int condition) {
        if (newBubble.getSameColorChain().size() >= condition) {
            player.addScore(newBubble.getSameColorChain().size());
            System.out.println("NewBubble before remove = " + newBubble.getSameColorChain().size());
            newBubble.remove();
        }
    }

    @Override
    public String toString() {
        return "ShootingBall{" +
                "velocityX=" + velocityX +
                ", velocityY=" + velocityY +
                ", direction=" + direction +
                ", positionX=" + positionX +
                ", positionY=" + positionY +
                '}';
    }
}
