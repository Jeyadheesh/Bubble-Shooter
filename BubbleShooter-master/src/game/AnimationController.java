package game;

import com.sun.javafx.geom.Vec2d;
import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class AnimationController {
    private Vec2d mouseLocation = new Vec2d(0, 0);
    private Scene scene;
    private Cannon cannon;
    private Vec2d finalLocation = new Vec2d(0, 0);
    private boolean clicked = false;
    private boolean bounce = true;
    private double aFactor;
    private double bFactor;
    private EventHandler<MouseEvent> eventHandler2 = event -> {
        if (!clicked) {
            mouseLocation.set(event.getX(), event.getY());
            System.out.println(mouseLocation.toString());
            System.out.println("First ball position = " + cannon.getFirstBallPosition());
            computeFinalLocation(cannon.getFirstBallPosition());
            clicked = true;
            startAnimationTimer();
        }
    };

    private AnimationTimer animationTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (clicked) {
                update();
                System.out.println(cannon.getFirstBallPosition());
            }
        }
    };

    public void startAnimationTimer() {
        animationTimer.start();
    }

    public AnimationController(Scene scene, Cannon cannon) {
        this.scene = scene;
        this.cannon = cannon;
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler2);
        animationTimer.start();
    }

    public void computeFinalLocation(Vec2d point) {
        aFactor = (mouseLocation.y - Bubble.getRadius() - point.y) / (mouseLocation.x - Bubble.getRadius() - point.x);
        bFactor = point.y - point.x * aFactor;
        finalLocation.set(-bFactor / aFactor, 0);
    }

    public double computeLocationX(double y) {
        return (y - bFactor) / aFactor;
    }

    public Vec2d getMouseLocation() {
        return mouseLocation;
    }

    public void update() {
        ShootingBall ball = cannon.getFirstBall();
        if (!ball.checkCollision()) {
            if (ball.getPositionY() > 0) {
                bounceFromFrame(ball);
                /*
                if (Math.abs(aFactor) < 0.5)
                    ball.setPosition(computeLocationX(ball.getPositionY() - 3), ball.getPositionY() - 3);
                else*/
                ball.setPosition(computeLocationX(ball.getPositionY() - 8), ball.getPositionY() - 8);
                ball.setPosition();
            } else {
                ball.setPosition(ball.findClosestLocationsX(), 0);
                ball.setPosition();
                ball.actionWhenHitTopFrame();
                animationTimer.stop();
                cannon.removeFirstBall();
                ball.remove();
                setAttachedAll();
                clicked = false;
                removeNotAttached();
            }
        } else {

            animationTimer.stop();
            cannon.removeFirstBall();
            ball.remove();
            setAttachedAll();
            clicked = false;
            System.out.println("Hit");
            removeNotAttached();
        }
    }

    public void bounceFromFrame(ShootingBall ball) {
        if (bounce && (ball.getPositionX() + 2 * Bubble.getRadius() >= scene.getWidth() || ball.getPositionX() <= 0)) {
            if (ball.getPositionX() + 2 * Bubble.getRadius() >= scene.getWidth()) {
                aFactor = (ball.getPositionY() + Bubble.getRadius() - Bubble.getRadius()) / (ball.getPositionX() + Bubble.getRadius() - (2 * scene.getWidth() - finalLocation.x));
            } else {
                aFactor = (ball.getPositionY() + Bubble.getRadius() - finalLocation.y) / (ball.getPositionX() + Bubble.getRadius() + finalLocation.x);
            }
            bFactor = ball.getPositionY() - ball.getPositionX() * aFactor;
            finalLocation.set(-bFactor / aFactor, 0);
            bounce = false;
        } else if (!bounce && ball.getPositionX() < scene.getWidth() && ball.getPositionX() > 0) {
            bounce = true;
        }
    }

    private void setAttachedAll() {
        for (Bubble bubble : Bubble.getBubbleList()) {
            bubble.setAttached(false);
        }
        for (Bubble bubble : Bubble.getBubbleList()) {
            if (bubble.getPositionY() == 0) {
                bubble.setAttached(true);
                setAttachedForSurrounded(bubble);
            }
        }
    }

    private void setAttachedForSurrounded(Bubble bubble) {
        for (Bubble surroundedBall : bubble.getSurroundedBubbles()) {
            if (!surroundedBall.isAttached()) {
                surroundedBall.setAttached(true);
                setAttachedForSurrounded(surroundedBall);
            }
        }
    }

    private void removeNotAttached() {
        ArrayList<Bubble> bubblesToRemove = new ArrayList<>();
        for (Bubble bubble : Bubble.getBubbleList()) {
            if (!bubble.isAttached()) {
                bubblesToRemove.add(bubble);
            }
        }
        ShootingBall.getPlayer().addScoreForNotAttahed(bubblesToRemove.size());
        for (Bubble bubble : bubblesToRemove) {
            bubble.removeThis();
        }
    }
}
