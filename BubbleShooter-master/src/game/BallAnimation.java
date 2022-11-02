package game;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;

public class BallAnimation {
    private TranslateTransition transition = new TranslateTransition();
    private Timeline timeline = new Timeline();
    private AnimationTimer animationTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            update();
        }
    };

    public void animate(Node node, double positionX, double positionY){
        transition.setDuration(Duration.millis(2000));
        transition.setToX(positionX);
        transition.setToY(positionY);
        transition.setNode(node);
        transition.play();
    }

    public  void animate2(Node node, double positionX, double positionY){
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                new KeyValue(node.translateXProperty(), 25)));
        timeline.play();
    }

    public void update(){
    }

}
